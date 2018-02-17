/*
 * Collector.java
 * Author: Nitesh Puri
 * Collaborator: Jeremiah Hanson
 * -----------------------------------------------------
 * This class controls the motors on the collector
 */


package systems.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Victor;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;

public class Collector implements Subsystem {
	
	private WPI_TalonSRX collectorArm1, collectorArm2; 
	private WPI_VictorSPX intakeLeft, intakeRight;
	private static Systems systems;
	private static Controls controls;
	private RobotEncoder armEncoder1;
	private RobotEncoder armEncoder2;
	private PIDManual armPID;
	
	private double averageArmEncoderPos;
	private double encoderRange;
	private double angleConstant;
	
	private int position;
	
	/*
	 * Constructor
	 * Author: Nitesh Puri
	 * ----------------------------------------
	 * constructor
	 */
	
	public Collector(WPI_TalonSRX collectorArm1, WPI_TalonSRX collectorArm2, 
			WPI_VictorSPX intakeLeft, 
			WPI_VictorSPX intakeRight,
			RobotEncoder armEncoder1,
			RobotEncoder armEncoder2) {
		this.collectorArm1 = collectorArm1;
		this.collectorArm2 = collectorArm2;
		this.intakeLeft = intakeLeft;
		this.intakeRight = intakeRight;
		this.armEncoder1 = armEncoder1;
		this.armEncoder2 = armEncoder2;
		this.armPID = new PIDManual(0.015, 0, 0);
		//intakeLeft.setInverted(true);
		collectorArm1.setNeutralMode(NeutralMode.Brake);
		collectorArm2.setNeutralMode(NeutralMode.Brake);
		averageArmEncoderPos = 0;
		
		position = 0;
		encoderRange = 0;
		angleConstant = 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see systems.Subsystem#update()
	 */
	@Override
	public void update() {
		
		
		if (systems == null) {
			systems = Systems.getInstance();
		}
		 if(!systems.inAuto) {
			//Controls for intake
			if(systems.getOperatorRtTrigger()>.2) {
				intakeLeft.set(systems.getOperatorRtTrigger());
				intakeRight.set(systems.getOperatorRtTrigger());
			}
			else if(systems.getOperatorLtTrigger()>.2) {
				intakeLeft.set(-systems.getOperatorLtTrigger());
				intakeRight.set(-systems.getOperatorLtTrigger());
			}
			else {
				intakeLeft.set(0.0);
				intakeRight.set(0.0);
			}
			
		//	averageArmEncoderPos = 0.5 * (armEncoder1.getValue() + armEncoder2.getValue());
			
			armPID.setCValue(averageArmEncoderPos);
			
			//Controls for arm
			if(controls.getButton(Controls.Button.LEFT_BUMPER, SysObj.Sensors.OPERATOR_STICK)){
				position = 1;
				armPID.setDValue(angleConstant * 135);
			}
			if(controls.getButton(Controls.Button.B, SysObj.Sensors.OPERATOR_STICK)){
				position = 2;
				armPID.setDValue(angleConstant * 60);
			}
			if(controls.getButton(Controls.Button.A, SysObj.Sensors.OPERATOR_STICK)){
				position = 3;
				armPID.setDValue(angleConstant * 10);
			}
			if(controls.getButton(Controls.Button.RIGHT_BUMPER, SysObj.Sensors.OPERATOR_STICK)){
				position = 4;
				armPID.setDValue(angleConstant * 0);

			}
			
			collectorArm1.set(armPID.getOutput());
			collectorArm2.set(armPID.getOutput());
			
			/*if(position == 1 && averageArmEncoderPos != 0){
				collectorArm1.set(0.5);
				collectorArm2.set(0.5);
			}
			else if(position == 2 && (averageArmEncoderPos > angleConstant * 10 + encoderRange)){
				collectorArm1.set(0.5);
				collectorArm2.set(0.5);
			}
			else if(position == 2 && averageArmEncoderPos < angleConstant * 10 - encoderRange){
				collectorArm1.set(-0.5);
				collectorArm2.set(-0.5);
			}
			else if(position == 3 && (averageArmEncoderPos > angleConstant * 60 + encoderRange)){
				collectorArm1.set(0.5);
				collectorArm2.set(0.5);
			}
			else if(position == 3 && averageArmEncoderPos < angleConstant * 60 - encoderRange){
				collectorArm1.set(-0.5);
				collectorArm2.set(-0.5);
			}
			else if(position == 4 && (averageArmEncoderPos > angleConstant * 135 + encoderRange)){
				collectorArm1.set(0.5);
				collectorArm2.set(0.5);
			}
			else if(position == 4 && averageArmEncoderPos < angleConstant * 135 - encoderRange){
				collectorArm1.set(-0.5);
				collectorArm2.set(-0.5);
			}
			else {
				collectorArm1.set(0.25);
				collectorArm2.set(0.25);
			}
			*/
}
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
