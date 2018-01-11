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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;

public class Systems {
	
	private static Systems systems;
	HashMap<SysObj, Object> sysObjects;
	
	public Controls controls;
	public Drive drive;
	
	public static final double COLLECTOR_P = 0.015;
	public static final double COLLECTOR_I = 0;
	public static final double COLLECTOR_D = 0;
	
	//3.9 is wheel width
	public static final double TICK_TO_IN = ((256) / (3.9 * Math.PI));
	// Private constructor that is used once to create all the subsystem objects
	
	// Private constructor that is used once to create all the subsystem objects
	private Systems(){

		sysObjects = new HashMap();
		
		//driveTrain Constants
		sysObjects.put(SysObj.Constants.DRIVETRAIN_TICKTOIN, new Double(-20));
		
		// Left Motor Controllers
		sysObjects.put(SysObj.MotorController.LEFT_1, new WPI_TalonSRX(2));
		sysObjects.put(SysObj.MotorController.LEFT_2, new WPI_TalonSRX(4));
		sysObjects.put(SysObj.MotorController.LEFT_3, new WPI_TalonSRX(6));
		
		// Right Motor Controllers
		sysObjects.put(SysObj.MotorController.RIGHT_1, new WPI_TalonSRX(1));
		sysObjects.put(SysObj.MotorController.RIGHT_2, new WPI_TalonSRX(3));
		sysObjects.put(SysObj.MotorController.RIGHT_3, new WPI_TalonSRX(5));
		
		// Sensors
		sysObjects.put(SysObj.Sensors.DRIVER_STICK, new Joystick(0));
		sysObjects.put(SysObj.Sensors.OPERATOR_STICK, new Joystick(1));
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kLeftRumble, 0);
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kRightRumble, 0);
		sysObjects.put(SysObj.Sensors.NAVX, new RNavX(new AHRS(SPI.Port.kMXP)));
		sysObjects.put(SysObj.Sensors.PDP, new PowerDistributionPanel(0));
		
		// Encoders
		sysObjects.put(SysObj.Sensors.LEFT_ENCODER, new Encoder(2,3));
		sysObjects.put(SysObj.Sensors.RIGHT_ENCODER, new Encoder(1,0));

	}
	
	public void initiateSubsystems(){
		
		controls = new Controls((Joystick)sysObjects.get(SysObj.Sensors.DRIVER_STICK),
				(Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK));
		
		drive = Drive.getInstance();
				
	}
	
	
	// This returns the instance of Systems, or creates the instnace if there is
	// none.
	public static Systems getInstance(){
		
		if (systems == null) {
			systems = new Systems();

		}
		return systems;
		
	}
	
	// returns the hashmap containing all the sysObjects
	public HashMap<SysObj, Object> getHashMap() {
		return sysObjects;
	}
	

}
