/*
 * DriveTrain.java
 * Author: Jeremiah Hanson
 * ------------------------------------------------------------
 * This class controls all the speedcontrollers for the drive train
 */

package systems.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Resources;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;

public class DriveTrain implements Subsystem{
	
	private WPI_TalonSRX ltMain, ltSlave1, ltSlave2;
	private WPI_TalonSRX rtMain, rtSlave1, rtSlave2;
	private static Systems systems;
	private DifferentialDrive drive;
	private PIDManual driveStraightPID;
	public Resources resources;
	private double driveConstant, distancePerPulse;
	private final double ROBOT_WIDTH;	//Distance between wheels
	
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
		
		driveStraightPID = new PIDManual(0.05, 0 , 0);
		
		resources = new Resources();
		
		driveConstant = 1.0;
		drive = new DifferentialDrive(ltMain, rtMain);
		
		ROBOT_WIDTH=21;
		
		distancePerPulse = 1;
	}

	@Override
	public void update() {
		if (systems == null) {
			systems = Systems.getInstance();
			systems.setDistancePerPulse(Systems.getRobotEncoder(SysObj.Sensors.LEFT_ENCODER), distancePerPulse);
			systems.setDistancePerPulse(Systems.getRobotEncoder(SysObj.Sensors.RIGHT_ENCODER), distancePerPulse);
		}
		
		if (systems.inAuto){
			return;
		}
		
		if(systems.getButton(Controls.Button.LEFT_BUMPER, true)){
			driveConstant = 0.6;
		}
		else if(systems.getButton(Controls.Button.RIGHT_BUMPER, true)){
			driveConstant = 0.8;
		}
		else {
			driveConstant = 1;
		}
		
		drive.arcadeDrive(driveConstant*systems.getDriverAxisLeftY(), driveConstant*systems.getDriverAxisRightX(), true);
		//System.out.println("" + systems.getPulse());
	}
	@Override
	public void toSmartDashboard() {
		SmartDashboard.putString("DB/String 0", "Distance: " + systems.getEncoderDistance(SysObj.Sensors.LEFT_ENCODER));
	}
	
	
	/*
	 * getSystems
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Ethan Yes, Jeremiah Hanson
	 * -------------------------------------------------------
	 * Purpose: Gets an instance of Systems
	 * Returns: nothing
	 */
	public void getSystems(){
		systems = Systems.getInstance();
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
	
	/*
	 * tankDrive
	 * Author: Finlay Parsons
	 * -------------------------
	 * Sets each wheel speed individually. Used for circleTurn-ing in auto
	 * Parameters:
	 * 	double leftSpeed- left wheels speed
	 * 	double rightSpeed- right wheels speed
	 */
	public void tankDrive(double leftSpeed, double rightSpeed){
		drive.tankDrive(leftSpeed, rightSpeed);
	}
	
	/*
	 * align
	 * Author: Finlay Parsons
	 * ---------------------------
	 * Purpose: Aligns robot to a multiple of 90 degrees
	 */
	public void align(int direction) {
		driveStraightPID.setDValue(direction);
		
	}
	
	
	/*
	 * driveDistance
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Ethan Yes, Jeremiah Hanson
	 * --------------------------------------------------------
	 * Purpose: Drive a distance at a speed
	 * Parameters:
	 * 	distance: how far to drive (in inches)
	 * 	speed: how fast to drive
	 * Returns: nothing
	 */
	public void driveDistance(double distance, double speed){
		double desiredDriveAngle = systems.getNavXAngle();
		double rotateConstant = 0;
		
		Systems.getRobotEncoder(SysObj.Sensors.LEFT_ENCODER).setDistancePerPulse(distancePerPulse);
		Systems.getRobotEncoder(SysObj.Sensors.RIGHT_ENCODER).setDistancePerPulse(distancePerPulse);
		
		driveStraightPID.setDValue(desiredDriveAngle);
		driveStraightPID.setPID(SmartDashboard.getNumber("DB/Slider 0", 0), SmartDashboard.getNumber("DB/Slider 1", 0), SmartDashboard.getNumber("DB/Slider 2", 0));
		
		while(DriverStation.getInstance().isAutonomous() && (systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER)<distance)){
			systems.getNavX().update();
			systems.getRobotEncoder(SysObj.Sensors.RIGHT_ENCODER).update();
			systems.getRobotEncoder(SysObj.Sensors.LEFT_ENCODER).update();
			driveStraightPID.setCValue(resources.getAngleError(desiredDriveAngle, systems.getNavXAngle()));
			this.toSmartDashboard();
			driveStraightPID.toSmartDashboard();
			drive.arcadeDrive(SmartDashboard.getNumber("DB/Slider 3",  0), -driveStraightPID.getOutput());
		}
		SmartDashboard.putString("DB/String 1", "Left " + systems.getEncoderDistance(SysObj.Sensors.LEFT_ENCODER));
		SmartDashboard.putString("DB/String 2", "Right" + systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER));

		drive.arcadeDrive(0,0);
		systems.resetEncoders();
	}
	/*
	 * turnTo
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri
	 * --------------------------------
	 * Purpose: Turns the robot to a certain angle relative to its current angle.
	 */
	public void turnTo(double angle, double speed, boolean right) {
		if(!right){
			speed = -speed;
		}
		while(DriverStation.getInstance().isAutonomous() && (systems.getNavXAngle() >= angle+2 || systems.getNavXAngle() <= angle-2)) {
			systems.getNavX().update();
			drive.arcadeDrive(0, speed);
		}
		systems.resetAutoSystems();
	}
	
	/*
	 * circleTurn
	 * Author: Finlay Parsons
	 * ------------------------
	 * Robot moves along a circle with a specified radius to a specified angle
	 * FIN DO THE PARAMETERS
	 */
	public void circleTurn(double radius, double angle, double speed, boolean right, boolean forwards){
		double v1=1, v2;
		int counter = 0;
		double prevEncoder = 0;
		
		if(!forwards) speed *= -1;
		v2=v1/((ROBOT_WIDTH+radius)/radius);
		double initialAngle = systems.getNavXAngle();
		
		while(DriverStation.getInstance().isAutonomous() && (systems.getNavXAngle() >= angle+2 || systems.getNavXAngle() <= angle-2)) {
			systems.getNavX().update();
			systems.update();
			
			if((right && angle < systems.getNavXAngle()) || (!right && angle > systems.getNavXAngle())){
				if(360-Math.abs(angle - systems.getNavXAngle()) < 30){
					//speed = speed*(Math.abs(angle-systems.getNavXAngle())/30);
					speed=0.6;
				}
			}
			else{
				if(Math.abs(angle-systems.getNavXAngle()) < 30) {
				//	speed = speed*(Math.abs(angle-systems.getNavXAngle())/30);
					speed=0.6;
				}
			}
			if(right){
				drive.tankDrive(speed*v1, speed*v2);
			}
			else {
				drive.tankDrive(speed*v2, speed*v1);
			}
			
			
			if(systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER)==prevEncoder){
				counter++;
			}
			else{
				counter=0;
			}
			prevEncoder = systems.getEncoderDistance(SysObj.Sensors.RIGHT_ENCODER);
			
			if(counter>600) break;
			
		}
		//System.out.print("v1: " + v1 + "   ");
		//System.out.print("v2: " + v2);
		//System.out.println("    Angle:" + systems.getNavXAngle());
		systems.resetAutoSystems();
		drive.arcadeDrive(0, 0);
	}

}
