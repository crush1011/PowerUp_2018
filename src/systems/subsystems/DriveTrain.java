/*
 * DriveTrain.java
 * Author: Jeremiah Hanson
 * ------------------------------------------------------------
 * This class controls all the speedcontrollers for the drive train
 */

package systems.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import systems.Subsystem;
import systems.Systems;

public class DriveTrain implements Subsystem{
	
	private WPI_TalonSRX ltMain, ltSlave1, ltSlave2;
	private WPI_TalonSRX rtMain, rtSlave1, rtSlave2;
	private static Systems systems;
	private DifferentialDrive drive;
	
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
		
		rtMain = rtM;
		rtSlave1 = rtS1;
		rtSlave2 = rtS2;
		rtSlave1.follow(rtM);
		rtSlave2.follow(rtM);
		
		
		drive = new DifferentialDrive(ltMain, rtMain);
	}

	@Override
	public void update() {
		if (systems == null) {
			systems = Systems.getInstance();
		}
		drive.arcadeDrive(systems.getDriverAxisLeftY(), systems.getDriverAxisRightX());
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
