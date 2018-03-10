/*
 * Collector.java
 * Author: Nitesh Puri
 * Collaborator: Jeremiah Hanson
 * -----------------------------------------------------
 * This class controls the motors on the collector
 */

package systems.subsystems;

import java.text.DecimalFormat;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import autonomous.Loop;
import autonomous.RPID;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Resources;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;

public class Collector implements Subsystem {

	private TalonSRX collectorArm1, collectorArm2;
	private WPI_VictorSPX intakeLeft, intakeRight;
	private static Systems systems;
	private RobotEncoder armEncoder1;
	private RobotEncoder armEncoder2;
	private PIDManual armPID;
	private RPID goodArmPID; // Just in case
	private Resources resources;

	private double averageArmEncoderPos;
	private double encoderRange;
	private double angleConstant, armConstant;
	private double idleTurnConstant;

	private boolean idleTurn, manualMode, collecting, fast;

	private int position, counter;

	DecimalFormat df;

	private static Thread cubeThrowThread;

	private Runnable updateRunnable;
	private Loop updateLoop;

	/*
	 * Constructor Author: Nitesh Puri ----------------------------------------
	 * constructor
	 */

	public Collector(TalonSRX talonSRX, TalonSRX talonSRX2, WPI_VictorSPX intakeLeft, WPI_VictorSPX intakeRight,
			RobotEncoder armEncoder1, RobotEncoder armEncoder2) {
		this.collectorArm1 = talonSRX;
		this.collectorArm2 = talonSRX2;
		this.intakeLeft = intakeLeft;
		this.intakeRight = intakeRight;
		this.armEncoder1 = armEncoder1;
		this.armEncoder2 = armEncoder2;
		this.armPID = new PIDManual(0.02, 0, 0.005, 0.02); // 0.015, 0, 0
		this.goodArmPID = new RPID(0.018, 0.0, 0.001, 0.02); // 0.015, 0, 0
		//0.03, 0.0, 0.00555														// 0.035, 0,
																// 0.005, 0.02
		resources = new Resources();

		df = new DecimalFormat("#.##");

		intakeRight.setInverted(true);

		talonSRX.setNeutralMode(NeutralMode.Brake);
		talonSRX2.setNeutralMode(NeutralMode.Brake);
		averageArmEncoderPos = 0;

		talonSRX2.setInverted(true);

		position = 0;
		encoderRange = 0;
		goodArmPID.setSetPoint(0);
		goodArmPID.setOutputRange(-1, 1);
		armConstant = 1;
		idleTurnConstant = 0;

		idleTurn = false;
		manualMode = false;
		collecting = false;
		fast = false;

		updateRunnable = () -> {
			if (DriverStation.getInstance().isEnabled())
				update();
		};

		updateLoop = new Loop(updateRunnable, 50);

		cubeThrowThread = new Thread(new CubeThrow());
	}

	/*
	 * CubeThrow Author: Nitesh Puri Collaborators: Jeremiah Hanson
	 * -------------------------------------- Runnable Class Purpose: Throw the
	 * cube.
	 */

	private class CubeThrow implements Runnable {

		@Override
		public void run() {

			goodArmPID.setSetPoint(35);

			while (true) {
				boolean stop = false;
				averageArmEncoderPos = 0.5 * (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1)
						+ systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));

				// goodArmPID.setCValue(averageArmEncoderPos);
				collectorArm1.set(ControlMode.PercentOutput, goodArmPID.crunch(averageArmEncoderPos));
				collectorArm2.set(ControlMode.PercentOutput, goodArmPID.crunch(averageArmEncoderPos));

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

		if (cubeThrowThread.isAlive()) {
			return;
		}

		/*
		 * averageArmEncoderPos = 0.5 *
		 * (systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1) +
		 * systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
		 */
		averageArmEncoderPos = -systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_1);
		if (true) {
			// Controls for intake
			if (systems.getMotorCurrent(10) < 75 && systems.getMotorCurrent(11) < 75) {
				if(systems.getButton(Controls.Button.X, false)) {
					intakeLeft.set(0.35);
					intakeRight.set(0.35);
				} else if(systems.getOperatorRtTrigger() > .1) {
					intakeLeft.set(-0.70 * systems.getOperatorRtTrigger());
					intakeRight.set(-0.70 * systems.getOperatorRtTrigger());
					if (position == 3 && !collecting) {
						goodArmPID.setSetPoint(135);
						collecting = true;
					}
				} else if (systems.getOperatorLtTrigger() > .1) {
					intakeLeft.set(systems.getOperatorLtTrigger());
					intakeRight.set(systems.getOperatorLtTrigger());
				} else {
					if (!systems.inAuto) {
						intakeLeft.set(idleTurnConstant);
						intakeRight.set(idleTurnConstant);
					}
					if (collecting) {
						goodArmPID.setSetPoint(120);
						SmartDashboard.putString("DB/String 2", "Hellu");
						collecting = false;
					}
				}
			} else {

				intakeLeft.set(0);
				intakeRight.set(0);
			}

			// goodArmPID.setCValue(averageArmEncoderPos);

			// Controls for arm
			if (systems.getButton(Controls.Button.LEFT_BUMPER, false)) {
				position = 1;
				goodArmPID.setSetPoint(127);
			}
			if (systems.getButton(Controls.Button.RIGHT_BUMPER, false)) {
				position = 2;
				goodArmPID.setSetPoint(75);
			}
			if (systems.getButton(Controls.Button.B, false)) {
				position = 3;
				goodArmPID.setSetPoint(115);
			}
			if (systems.getButton(Controls.Button.A, false)) {
				position = 4;
				goodArmPID.setSetPoint(15);
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
			
			if(systems.getButton(Controls.Button.BACK, false)){
				//systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_1).reset();
				//systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_2).reset();
				if(manualMode) manualMode = false; 
			}
			
				

			/*
			 * if(systems.getDPadButton(Controls.POV.UP, false)){ double i =
			 * averageArmEncoderPos; goodArmPID.setSetPoint(i - 5); }
			 * 
			 * if(systems.getDPadButton(Controls.POV.DOWN, false)){ double i =
			 * averageArmEncoderPos; goodArmPID.setSetPoint(i + 5); }
			 */

			// Automatic operator controls
			if (!manualMode) {
				systems.setRumbleOperator(0);
				if (fast) {
					collectorArm1.set(ControlMode.PercentOutput, 0.75);
					collectorArm2.set(ControlMode.PercentOutput, 0.75);
				} else {
					double angleFromTop = averageArmEncoderPos - 45.0;
					double feedForward = Math.sin(Math.toRadians(angleFromTop)) * 0.1;
					double motorValue = goodArmPID.crunch(averageArmEncoderPos) + feedForward;

					//System.out.println("motorVAlue :" + motorValue + "    SETPOINT:" + goodArmPID.getSetPoint()
					//		+ "  CurrentValue:" + averageArmEncoderPos);
					if(!cubeThrowThread.isAlive()) {
						collectorArm1.set(ControlMode.PercentOutput, motorValue);
						collectorArm2.set(ControlMode.PercentOutput, motorValue);
					}
				}
			}

			// Manual operator controls
			if (manualMode) {
				systems.setRumbleOperator(0.1);
				collectorArm1.set(ControlMode.PercentOutput, armConstant * -systems.getOperatorLJoystick());
				collectorArm2.set(ControlMode.PercentOutput, armConstant * -systems.getOperatorLJoystick());
			
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

		goodArmPID.setSetPoint(angle);

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

	public void intakeCubeAuto(double speed, double delay, double time) {
		Runnable intake = () -> {
			try {
				Thread.sleep((long) delay);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long startTime = System.currentTimeMillis();
			while(System.currentTimeMillis() - startTime < time) {
				intakeLeft.set(-speed);
				intakeRight.set(-speed);
				try {
					Thread.sleep((long) 50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
			intakeLeft.set(0);
			intakeRight.set(0);
		};
		new Thread(intake).start();
	}

	/*
	 * outtakeCube Author: Nitesh Puri Collaborators: Ethan Ngo and Finlay
	 * Parsons -------------------------------------------------- Parameters:
	 * None Purpose: Outtakes the cube
	 */
	public void outtakeCube(double speed) {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 500) {
			intakeLeft.set(speed);
			intakeRight.set(speed);
			systems.getDriveTrain().drive(.5, 0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		systems.getDriveTrain().drive(0, 0);
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

	public void enable() {

		if (systems == null) {
			systems = Systems.getInstance();
			armEncoder1 = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_1);
			armEncoder2 = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER_2);
			armEncoder1.setDistancePerPulse(0.42);
			armEncoder2.setDistancePerPulse(0.42);

		//	 System.out.println("Collector.update(): " +
			// systems.getEncoderDistance(SysObj.Sensors.ARM_ENCODER_2));
		}

		updateLoop.start();
	}

}
