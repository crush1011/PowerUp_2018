/*
 * LeftSideLeftScore.java
 * Author: Finlay Parsons
 * Collaborators: Jeremiah Hanson
 * --------------------------------
 * The autonomous program being run when in
 * starting position 1 and trying to score on
 * the left side of the switch.
 */
package autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.SysObj;
import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;
import systems.subsystems.EthanDrive;
import systems.subsystems.NavX;
import systems.subsystems.RobotEncoder;

public class LeftSideLeftScore implements Runnable{
	
	private Systems systems;
	private DriveTrain driveTrain;
	
	public LeftSideLeftScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
	}

	@Override
	public void run() {
		
		driveTrain.driveDistance(110, 0.7); //Drives forward to the switch fence
		
		driveTrain.circleTurn(45, 90, 0.7, true, true); //Turns into the switch fence
		
		//TODO: Eject the cube
		
		driveTrain.circleTurn(50, 180, 0.6, false, false); //Turns out of the switch fence finishing backwards
		
		driveTrain.driveDistance(30, -0.5);
		
		driveTrain.turnTo(90, 0.5, false); //Turns towards fence cubes
		
		driveTrain.driveDistance(100, 0.6); //Drives towards the fence cubes
		
		//TODO: Pick up cube
		
		driveTrain.circleTurn(20, 180, 0.6, false, false);
		
		systems.resetNavXAngle();
		
		driveTrain.driveDistance(40, 0.6);
		
		//TODO: Eject the cube
	}
	
	
}
