/*
 * Class: Systems
 * Author: Jeremiah Hanson
 * ----------------------------------------------------------------------------
 * Purpose: The purpose of this class is to create a hashmap that stores all of
 * objects that represent the various systems and sysObjects on the robot, such
 * as solenoids and speed-controllers 
 * 
 * Responsibilities: Responsible for maintaining the systems so that no class 
 * should have to create their own system objects but instead ask this class 
 * for those objects.
 */

package systems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import systems.subsystems.Collector;
import systems.subsystems.Controls;
import systems.subsystems.Controls.Axis;
import systems.subsystems.DriveTrain;
import systems.subsystems.NavX;
import systems.subsystems.PIDManual;
import systems.subsystems.RobotEncoder;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class Systems{
	
	private static Systems systems;
	private HashMap<SysObj, Object> sysObjects;
	
	private static Controls controls;
	private static DriveTrain driveTrain;
	private static Collector collector;
	private static NavX navX;
	private static RobotEncoder lEncoder, rEncoder, armEncoder;
	private static PIDManual pidManual;
	
	public boolean inAuto;
	
	/*
	 * Constructor
	 * Author: Jeremiah Hanson
	 * -------------------------------------------------
	 * Singlton constructor for the systems
	 */
	private Systems(){

		sysObjects = new HashMap<SysObj, Object>();
		
		System.out.println("Start building bot");
		
//=======================================================================================
// Motor Controllers
//=======================================================================================

		System.out.println("Start with motors");
		
		// Left Motor Controllers
		sysObjects.put(SysObj.MotorController.LEFT_1, new WPI_TalonSRX(2));
		sysObjects.put(SysObj.MotorController.LEFT_2, new WPI_TalonSRX(4));
		sysObjects.put(SysObj.MotorController.LEFT_3, new WPI_TalonSRX(6));
		
		// Right Motor Controllers
		sysObjects.put(SysObj.MotorController.RIGHT_1, new WPI_TalonSRX(1));
		sysObjects.put(SysObj.MotorController.RIGHT_2, new WPI_TalonSRX(3));
		sysObjects.put(SysObj.MotorController.RIGHT_3, new WPI_TalonSRX(5));
		
		// Create the driveTrain
		driveTrain = new DriveTrain((WPI_TalonSRX) sysObjects.get(SysObj.MotorController.LEFT_1), 
									(WPI_TalonSRX) sysObjects.get(SysObj.MotorController.LEFT_2), 
									(WPI_TalonSRX) sysObjects.get(SysObj.MotorController.LEFT_3), 
									(WPI_TalonSRX) sysObjects.get(SysObj.MotorController.RIGHT_1), 
									(WPI_TalonSRX) sysObjects.get(SysObj.MotorController.RIGHT_2), 
									(WPI_TalonSRX) sysObjects.get(SysObj.MotorController.RIGHT_3));
		
		// Arm Motor Controllers
		sysObjects.put(SysObj.MotorController.COLLECTOR_ARM, new WPI_TalonSRX(0));
		sysObjects.put(SysObj.MotorController.INTAKE_LEFT, new Spark(6));
		sysObjects.put(SysObj.MotorController.INTAKE_RIGHT, new Spark(5));
		
		// Create the Collector
		collector = new Collector((WPI_TalonSRX) sysObjects.get(SysObj.MotorController.COLLECTOR_ARM),
								  (Spark) sysObjects.get(SysObj.MotorController.INTAKE_LEFT), 
								  (Spark) sysObjects.get(SysObj.MotorController.INTAKE_RIGHT));
		
		// Climber Motor Controller(s)
		sysObjects.put(SysObj.MotorController.CLIMBER, new WPI_TalonSRX(9));

//=======================================================================================
// Sensors
//=======================================================================================

		System.out.println("Next, encoders");
		
		// Encoders
		sysObjects.put(SysObj.Sensors.CLIMB_ENCODER, new Encoder(8,9));
		sysObjects.put(SysObj.Sensors.ARM_ENCODER, new Encoder(4,5));
		sysObjects.put(SysObj.Sensors.LEFT_ENCODER, new Encoder(2,3, true, Encoder.EncodingType.k4X));
		sysObjects.put(SysObj.Sensors.RIGHT_ENCODER, new Encoder(1,0, true, Encoder.EncodingType.k4X));
		//((Encoder) sysObjects.get(SysObj.Sensors.LEFT_ENCODER)).setDistancePerPulse(Math.PI);
		lEncoder = new RobotEncoder((Encoder) sysObjects.get(SysObj.Sensors.LEFT_ENCODER));
		rEncoder = new RobotEncoder((Encoder) sysObjects.get(SysObj.Sensors.RIGHT_ENCODER));
		armEncoder = new RobotEncoder((Encoder) sysObjects.get(SysObj.Sensors.ARM_ENCODER));
		
		System.out.println("Don't forget controls");
		
		// Joysticks
		Joystick drive = new Joystick(0), operator = new Joystick(1);
		sysObjects.put(SysObj.Sensors.DRIVER_STICK, drive);
		sysObjects.put(SysObj.Sensors.OPERATOR_STICK, operator);
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kLeftRumble, 0);
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kRightRumble, 0);
		controls = new Controls(drive, operator);
		
		System.out.println("Gotta have power");
		
		// Other Sensors
		sysObjects.put(SysObj.Sensors.PDP, new PowerDistributionPanel(0));
		sysObjects.put(SysObj.Sensors.NAVX, new AHRS(SPI.Port.kMXP));
		navX = new NavX((AHRS) sysObjects.get(SysObj.Sensors.NAVX));
		
		System.out.println("Other stuff too!");
		
		pidManual = new PIDManual();
		
	}
	
	/*
	 * getInstance
	 * Author: Jeremiah Hanson
	 * --------------------------------------------------
	 * Purpose: gets the single instance of Systems. Creates
	 * 	Systems if one does not exist
	 * Returns: a Systems object
	 */
	public static Systems getInstance() {
		if (systems == null) {
			systems = new Systems();
		}
		return systems;
	}
	
	/*
	 * update
	 * Author: Jeremiah Hanson
	 * ------------------------------------------------
	 * Purpose: updates all the systems
	 */
	public void update() {
		controls.update();
		driveTrain.update();
		collector.update();
		navX.update();
		lEncoder.update();
		rEncoder.update();
		armEncoder.update();
		if(controls.getButton(Controls.Button.BACK, SysObj.Sensors.DRIVER_STICK)){
			navX.zeroAngler();
		}
	}
	
	/*
	 * getDriverAxisLeftX
	 * Author: Jeremiah Hanson
	 * --------------------------------------------
	 * Purpose: gets the drivers current axis hashmap
	 * Returns: HashMap
	 */
	public double getDriverAxisLeftY() {
		return controls.getDriverAxis().get(Axis.LEFT_Y);
	}
	
	/*
	 * getDriverAxisRightY
	 * Author: Jeremiah Hanson
	 * --------------------------------------------
	 * Purpose: gets the drivers current axis hashmap
	 * Returns: HashMap
	 */
	public double getDriverAxisRightX() {
		return controls.getDriverAxis().get(Axis.RIGHT_X);
	}
	
	/*
	 * getOperatorRtTrigger
	 * Author: Nitesh Puri
	 * ---------------------------------------------
	 * Purpose: gets the operator's current trigger value
	 * Returns: Operator's Trigger Value As Double
	 */
	public double getOperatorRtTrigger() {
		return controls.getOperatorAxes().get(Axis.RIGHT_TRIGGER);
	}
	
	/*
	 * getOperatorLtTrigger
	 * Author: Nitesh Puri
	 * ---------------------------------------------
	 * Purpose: gets the operator's current trigger value
	 * Returns: Operator's Trigger Value As Double
	 */
	public double getOperatorLtTrigger() {
		return controls.getOperatorAxes().get(Axis.LEFT_TRIGGER);
	}
	
	/*
	 * getOperatorLJostick
	 * Author: Nitesh Puri
	 * ---------------------------------------------
	 * Purpose: gets the operator's current left joystick value
	 * Returns: Operator's Left Joystick Value As Double
	 */
	public double getOperatorLJoystick(){
		return controls.getOperatorAxes().get(Axis.LEFT_Y);
	}
	
	/*
	 * getDriveTrain
	 * Author: Finlay Parsons
	 * Collaborators: Jeremiah Hanson
	 * ---------------------------------
	 * Purpose: Returns Systems instance of DriveTrain
	 * Returns: DriveTrain
	 */
	public static DriveTrain getDriveTrain(){
		return driveTrain;
	}
	
	/*
	 * getCollector
	 * Author Finlay Parsons
	 * Collaborators: Jeremiah Hanson
	 * ----------------------------------
	 * Purpose: Returns Systems instance of Collector
	 * Returns: Collector
	 */
	public static Collector getCollector(){
		return collector;
	}
	
	/*
	 * getNavX
	 * Author Finlay Parsons
	 * Collaborators: Jeremiah Hanson
	 * ----------------------------------
	 * Purpose: Returns Systems instance of NavX
	 * Returns: NavX
	 */
	public static NavX getNavX(){
		return navX;
	}
	
	/*
	 * getRobotEncoder
	 * Author Finlay Parsons
	 * Collaborators: Jeremiah Hanson
	 * ----------------------------------
	 * Purpose: Returns Systems instance of RobotEncoder
	 * Returns: RobotEncoder
	 */
	public static RobotEncoder getRobotEncoder(SysObj.Sensors sensor){
		switch(sensor){
		case LEFT_ENCODER:
			return lEncoder;
		case RIGHT_ENCODER:
			return rEncoder;
		case ARM_ENCODER:
			return armEncoder;
		default:
			return null;
		
		}
	}
	
	/*
	 * resetEncoders
	 * Author: Finlay Parsons
	 * Collaborators: Jeremiah Hanson, Ethan Ngo, Nitesh Puri
	 * --------------------------------------------------------
	 * Purpose: Resets the value of the encoders before to make the starting
	 * position equal to 0
	 */
	public void resetEncoders(){
		lEncoder.reset();
		rEncoder.reset();
		armEncoder.reset();
	}
	
	/*
	 * getPulse
	 * Author: Nitesh Puri
	 * Contributors: Jeremiah Hanson, Finlay Parsons
	 * -----------------------------------------------
	 * Purpose: Gets the pulse
	 * Return: Returns a double
	 */
	public double getPulse(){
		return lEncoder.getPulse();
	}
	
	/*
	 * printEncoderInfo
	 * Author: Finlay Parsons
	 * Contrubitors: Jeremiah Hanson
	 * -------------------------------------
	 * Purpose: prints encoder info to system.out
	 * Parameters:
	 * 	a: print distance
	 * 	b: print rate
	 *  c: print pulse
	 * returns nothing
	 */
	public void printEncoderInfo(boolean a, boolean b, boolean c, SysObj.Sensors sensor){
		
		switch(sensor){
			case LEFT_ENCODER: 
				if(a) System.out.print("Left Distance: "+ lEncoder.getValue());
				if(b) System.out.print("    Left Rate: " + lEncoder.getRate());
				if(c) System.out.print("    Left Pulse: " + lEncoder.getPulse());
				break;
			case RIGHT_ENCODER:
				if(a) System.out.print("Right Distance: "+ rEncoder.getValue());
				if(b) System.out.print("    Right Rate: " + rEncoder.getRate());
				if(c) System.out.print("    Right Pulse: " + rEncoder.getPulse());
				break;
			case ARM_ENCODER:
				if(a) System.out.print("Arm Distance: "+ armEncoder.getValue());
				if(b) System.out.print("    Arm Rate: " + armEncoder.getRate());
				if(c) System.out.print("    Arm Pulse: " + armEncoder.getPulse());
				break;
			default:
				System.out.println("ERROR: no encoder selected!");
				break;
		}
		
		
		System.out.println();
	
	}
	
	/*
	 * getNavXAngle
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson
	 * ----------------------------------------------
	 * Purpose: Gets the current angle of the robot
	 * Returns: Double
	 */
	public double getNavXAngle(){
		return navX.getCurrentAngle();
	}
	
	/*
	 * resetNavXAngle
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson, Ethan Yes
	 * -------------------------------------------------------
	 * Purpose: Sets the current navX angle to 0
	 * Returns: nothing
	 */
	public void resetNavXAngle(){
		navX.zeroAngler();
	}
	
	/*
	 * getNavXDriveAngle
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson, Ethan Yes
	 * -------------------------------------------------------
	 * Purpose: Gets the current angle that the robot is pointing
	 * Returns: A double in between -1 and 1. 0 is forward, positive is right.
	 */
	public double getNavXDriveAngle(){
		return navX.getDriveAngle();
	}
	
	/*
	 * getEncoderDistance
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson, Ethan Yes
	 * -------------------------------------------------------
	 * Purpose: Gets the current distance travelled by the encoders.
	 * Parameters:
	 * 	Encoders
	 * Returns a double
	 */
	public double getEncoderDistance(SysObj.Sensors encoder){
		switch(encoder){
		case LEFT_ENCODER:
			return lEncoder.getValue();
		case RIGHT_ENCODER:
			return rEncoder.getValue();
		case ARM_ENCODER:
			return armEncoder.getValue();
		default:
			return 0;
		}
	}
	
	/*
	 * instantiate
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Ethan Yes, Jeremiah Hanson
	 * -------------------------------------------------------
	 * Purpose: Gives an instance of systems to every class in here
	 * Returns: nothing
	 */
	public void instantiate(){
		driveTrain.getSystems();
	}
	
	
	/*
	 * resetAutoSystems
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Argeo Leyva 
	 * ------------------------------------------------------
	 * Purpose: Reset all of the subsystems auto uses
	 * Returns nothing
	 */
	public void resetAutoSystems(){
		resetNavXAngle();
		resetEncoders();
	}
	
	/*
	 * getPIDOutput
	 * Author: Finlay Parsons
	 * Collaborator: Jason Hyunh
	 * -----------------------------------
	 * Purpose: Returns output of the PID
	 */
	public double getPIDOutput(){
		return pidManual.getTurnOutput();
	}
}
