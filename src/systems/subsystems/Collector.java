/*
 * Collector.java
 * Author: Nitesh Puri
 * Collaborator: Jeremiah Hanson
 * -----------------------------------------------------
 * This class controls the motors on the collector
 */

package systems.subsystems;

import java.text.DecimalFormat;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Resources;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;

public class Collector implements Subsystem {

	private WPI_TalonSRX collectorArm1, collectorArm2;
	private WPI_VictorSPX intakeLeft, intakeRight;
	private static Systems systems;
	private RobotEncoder armEncoder1;
	private RobotEncoder armEncoder2;
	private PIDManual armPID;
	private Resources resources;

	private double averageArmEncoderPos;
	private double encoderRange;
	private double angleConstant, armConstant;
	private double idleTurnConstant;

	private boolean idleTurn, manualMode, collecting, fast;

	private int position, counter;

	DecimalFormat df;
	
	private static Thread cubeThrowThread;

	/*
	 * Constructor Author: Nitesh Puri ----------------------------------------
	 * constructor
	 */

	public Collector(WPI_TalonSRX collectorArm1, WPI_TalonSRX collectorArm2, WPI_VictorSPX intakeLeft,
			WPI_VictorSPX intakeRight, RobotEncoder armEncoder1, RobotEncoder armEncoder2) {
		this.collectorArm1 = collectorArm1;
		this.collectorArm2 = collectorArm2;
		this.intakeLeft = intakeLeft;
		this.intakeRight = intakeRight;
		this.armEncoder1 = armEncoder1;
		this.armEncoder2 = armEncoder2;
		this.armPID = new PIDManual(0.015, 0, 0); //0.015, 0, 0
		resources = new Resources();

		df = new DecimalFormat("#.##");

		intakeRight.setInverted(true);

		collectorArm1.setNeutralMode(NeutralMode.Brake);
		collectorArm2.setNeutralMode(NeutralMode.Brake);
		averageArmEncoderPos = 0;

		collectorArm1.setInverted(true);

		position = 0;
		encoderRange = 0;
		armPID.setDValue(0);
		armConstant = 1;
		idleTurnConstant = 0;

		idleTurn = false;
		manualMode = false;
		collecting = false;
		fast = false;
		
		cubeThrowThread = new Thread(new CubeThrow());
	}
	
	/*
	 * CubeThrow
	 * Author: Nitesh Puri
	 * Collaborators: Jeremiah Hanson
	 * --------------------------------------
	 * Runnable Class
	 * Purpose: Throw the cube.
	 */
	
	private class CubeThrow implements Runnable {

		@Override
		public void run() {
			
			armPID.setDValue(45);
			
			while(true) {
				boolean stop = false;
				averageArmEncoderPos = 0.5 * (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)
						+ systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
				collectorArm1.set(armPID.getOutput());
				collectorArm2.set(armPID.getOutput());
				
				if (averageArmEncoderPos <= 50) {
					outtakeCube(.6);
					stop = true;
				}
				if (stop) 
					break;
			}
			
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see systems.Subsystem#update()
	 */
	@Override
	public void update() {

		if (systems == null) {
			systems = Systems.getInstance();
			armEncoder1 = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_1);
			armEncoder2 = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_2);
			armEncoder1.setDistancePerPulse(0.42);
			armEncoder2.setDistancePerPulse(0.42);

			// System.out.println("Collector.update(): " +
			// systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
		}
		
		if (cubeThrowThread.isAlive()) {
			return;
		}
		
		averageArmEncoderPos = 0.5 * (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)
				+ systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
		if (!systems.inAuto) {
			// Controls for intake
			if (systems.getMotorCurrent(10) < 75 && systems.getMotorCurrent(11) < 75) {
				if (systems.getOperatorRtTrigger() > .1) {
					intakeLeft.set(-Math.pow(0.87 * systems.getOperatorRtTrigger(), 2));
					intakeRight.set(-Math.pow(0.87 * systems.getOperatorRtTrigger(), 2));
					if (position == 3 && !collecting) {
						armPID.setDValue(135);
						collecting = true;
					}
				} else if (systems.getOperatorLtTrigger() > .1) {
					intakeLeft.set(Math.pow(systems.getOperatorLtTrigger(), 2));
					intakeRight.set(Math.pow(systems.getOperatorLtTrigger(), 2));
				} else {
					intakeLeft.set(idleTurnConstant);
					intakeRight.set(idleTurnConstant);
					if (collecting) {
						armPID.setDValue(120);
						SmartDashboard.putString("DB/String 2", "Hellu");
						collecting = false;
					}
				}
			} else {

				intakeLeft.set(0);
				intakeRight.set(0);
			}


			armPID.setCValue(averageArmEncoderPos);

			// Controls for arm
			if (systems.getButton(Controls.Button.LEFT_BUMPER, false)) {
				position = 1;
				armPID.setDValue(135);
			}
			if (systems.getButton(Controls.Button.RIGHT_BUMPER, false)) {
				position = 2;
				armPID.setDValue(75);
			}
			if (systems.getButton(Controls.Button.B, false)) {
				position = 3;
				armPID.setDValue(125);
			}
			if (systems.getButton(Controls.Button.A, false)) {
				position = 4;
				armPID.setDValue(15);
			}
			if (systems.getButton(Controls.Button.X, false)){
				position = 5;
				cubeThrowThread.start();
			}

			if (systems.getButton(Controls.Button.Y, false)) {
				idleTurnConstant = -0.2; // might be different for real robot
			} else {
				idleTurnConstant = 0;
			}

			if (systems.getButton(Controls.Button.START, false)) {
				if (!manualMode)
					manualMode = true;
				else if (manualMode)
					manualMode = false;
			}

			/*
			 * if(systems.getDPadButton(Controls.POV.UP, false)){ double i =
			 * averageArmEncoderPos; armPID.setDValue(i - 5); }
			 * 
			 * if(systems.getDPadButton(Controls.POV.DOWN, false)){ double i =
			 * averageArmEncoderPos; armPID.setDValue(i + 5); }
			 */

			// Automatic operator controls
			if (!manualMode) {
				if (fast) {
					collectorArm1.set(0.75);
					collectorArm2.set(0.75);
				} else {
					collectorArm1.set(armPID.getOutput());
					collectorArm2.set(armPID.getOutput());
				}
			}

			// Manual operator controls
			if (manualMode) {
				collectorArm1.set(armConstant * systems.getOperatorLJoystick());
				collectorArm2.set(armConstant * systems.getOperatorLJoystick());
			}

		}

		if (counter % 2000 == 0) {
			// this.toSmartDashboard();
		}
		counter++;
	}

	/*
	 * moveArm Author: Finlay Parsons ------------------------- Purpose: Moves
	 * the arm to specified angle - all the way back is 0, all the way down is
	 * 135 Parameters: angle: Desired angle of arm
	 */
	public void moveArm(double angle) {
		armPID.setDValue(angle);
		while (Math.abs(resources.getAngleError(angle, averageArmEncoderPos)) < 5) {
			armEncoder1.update();
			armEncoder1.update();
			update();
			averageArmEncoderPos = 0.5 * (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)
					+ systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
			armPID.setCValue(averageArmEncoderPos);
			collectorArm1.set(armPID.getOutput());
			collectorArm2.set(armPID.getOutput());
		}
		collectorArm1.set(0);
		collectorArm2.set(0);
	}

	/*
	 * intakeCube Author: Finlay Parsons ------------------------ Purpose: Spins
	 * the intake motors until the the cube is gained
	 */
	public void intakeCube(double speed) {
		intakeLeft.set(speed);
		intakeRight.set(speed);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intakeLeft.set(0);
		intakeRight.set(0);
	}

	/*
	 * outtakeCube Author: Nitesh Puri Collaborators: Ethan Ngo and Finlay
	 * Parsons -------------------------------------------------- Parameters:
	 * None Purpose: Outtakes the cube
	 */
	public void outtakeCube(double speed) {
		intakeLeft.set(speed);
		intakeRight.set(speed);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intakeLeft.set(0);
		intakeRight.set(0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see systems.Subsystem#toSmartDashboard()
	 */
	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		System.out.println("Encoder1: " + (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)));
		System.out.println("Encoder2: " + (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2)));
		/*
		 * SmartDashboard.putString("DB/String 4", "Encoder1: " +
		 * df.format(systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)));
		 * SmartDashboard.putString("DB/String 3", "Encoder2: " +
		 * df.format(systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2)));
		 * SmartDashboard.putString("DB/String 5", "Distance: " +
		 * df.format(averageArmEncoderPos));
		 */
		// SmartDashboard.putString("DB/Slider 4", "I hate this.");

	}

}
