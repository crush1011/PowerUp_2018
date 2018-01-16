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

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import systems.subsystems.Controls;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class Systems {
	
	private static Systems systems;
	private HashMap<SysObj, Object> sysObjects;
	
	private Controls controls;
	
	
	/*
	 * Constructor
	 * Author: Jeremiah Hanson
	 * -------------------------------------------------
	 * Singlton constructor for the systems
	 */
	private Systems(){

		sysObjects = new HashMap<SysObj, Object>();
		
//=======================================================================================
// Motor Controllers
//=======================================================================================

		// Left Motor Controllers
		sysObjects.put(SysObj.MotorController.LEFT_1, new WPI_TalonSRX(2));
		sysObjects.put(SysObj.MotorController.LEFT_2, new WPI_TalonSRX(4));
		sysObjects.put(SysObj.MotorController.LEFT_3, new WPI_TalonSRX(6));
		
		// Right Motor Controllers
		sysObjects.put(SysObj.MotorController.RIGHT_1, new WPI_TalonSRX(1));
		sysObjects.put(SysObj.MotorController.RIGHT_2, new WPI_TalonSRX(3));
		sysObjects.put(SysObj.MotorController.RIGHT_3, new WPI_TalonSRX(5));
		
		// Arm Motor Controllers
		sysObjects.put(SysObj.MotorController.COLLECTOR_ARM, new WPI_TalonSRX(0));
		sysObjects.put(SysObj.MotorController.INTAKE_LEFT, new WPI_TalonSRX(7));
		sysObjects.put(SysObj.MotorController.INTAKE_RIGHT, new WPI_TalonSRX(8));
		
		// Climber Motor Controller(s)
		sysObjects.put(SysObj.MotorController.CLIMBER, new WPI_TalonSRX(9));

//=======================================================================================
// Sensors
//=======================================================================================

		// Encoders
		sysObjects.put(SysObj.Sensors.CLIMB_ENCODER, new Encoder(8,9));
		sysObjects.put(SysObj.Sensors.ARM_ENCODER, new Encoder(4,5));
		sysObjects.put(SysObj.Sensors.LEFT_ENCODER, new Encoder(2,3));
		sysObjects.put(SysObj.Sensors.RIGHT_ENCODER, new Encoder(1,0));
		
		// Joysticks
		Joystick drive = new Joystick(0), operator = new Joystick(1);
		sysObjects.put(SysObj.Sensors.DRIVER_STICK, drive);
		sysObjects.put(SysObj.Sensors.OPERATOR_STICK, operator);
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kLeftRumble, 0);
		((Joystick)sysObjects.get(SysObj.Sensors.OPERATOR_STICK)).setRumble(RumbleType.kRightRumble, 0);
		controls = new Controls(drive, operator);
		
		// Other Sensors
		sysObjects.put(SysObj.Sensors.PDP, new PowerDistributionPanel(0));
		
		
		
	}
	
	/*
	 * getInstance
	 * Author: Jeremiah Hanson
	 * --------------------------------------------------
	 * Purpose: gets the single instance of Systems. Creates
	 * 	Systems if one does not exist
	 * Returns: a Systems object
	 */
	public Systems getInstance() {
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
	}

}
