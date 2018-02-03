/*
 * DriveTrain.java
 * Author: Jeremiah Hanson
 * ------------------------------------------------------------
 * This class controls all the speedcontrollers for the drive train
 */

package systems.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;

public class DriveTrain implements Subsystem{
	
	private WPI_TalonSRX ltMain, ltSlave1, ltSlave2;
	private WPI_TalonSRX rtMain, rtSlave1, rtSlave2;
	private static Systems systems;
	private DifferentialDrive drive;
	private double driveConstant;
	
	/*
	 * Constructor
	 * Author: Jeremiah Hanson
	 * ---------------------------------------
	 * constructor
	 */
	public DriveTrain(WPI_TalonSRX ltM, WPI_TalonSRX ltS1, WPI_TalonSRX ltS2, 
			WPI_TalonSRX rtM, WPI_TalonSRX rtS1, WPI_TalonSRX rtS2) {
		
		// assign motors
		ltMain = ltM;
		ltSlave1 = ltS1;
		ltSlave2 = ltS2;
		ltSlave1.follow(ltM);
		ltSlave2.follow(ltM);
		ltMain.setNeutralMode(NeutralMode.Brake);
		ltSlave1.setNeutralMode(NeutralMode.Brake);
		ltSlave2.setNeutralMode(NeutralMode.Brake);
		
		rtMain = rtM;
		rtSlave1 = rtS1;
		rtSlave2 = rtS2;
		rtSlave1.follow(rtM);
		rtSlave2.follow(rtM);
		rtMain.setNeutralMode(NeutralMode.Brake);
		rtSlave1.setNeutralMode(NeutralMode.Brake);
		rtSlave2.setNeutralMode(NeutralMode.Brake);
		
		driveConstant = 0.75;
		drive = new DifferentialDrive(ltMain, rtMain);
	}

	@Override
	public void update() {
		if (systems == null) {
			systems = Systems.getInstance();
		}
		
		if (systems.inAuto){
			return;
		}
		
		drive.arcadeDrive(driveConstant*systems.getDriverAxisLeftY(), driveConstant*systems.getDriverAxisRightX(), true);
		System.out.println("" + systems.getPulse());
	}
	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * drive
	 * Author: Finlay Parsons
	 * Collaborators: Jeremiah Hanson
	 * -------------------------------
	 * Purpose: Set velocity and rotation of robot.
	 * Parameters: 
	 * 	x: Speed of the motors (-1 to 1)
	 *  z: Rotation of the robot (-1 to 1 - Clockwise is positive)
	 * Return: None
	 */
	public void drive(double x, double z){
		drive.arcadeDrive(x, z, true);
	}
	
	public void driveDistance(double distance, double speed){
		if(systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER)<distance){
			drive.arcadeDrive(speed, -2*systems.getNavXDriveAngle());
		}
		else {
			drive.arcadeDrive(0,0);
			systems.resetEncoders();
		}
	
	}

}
