/*
 * MidSideRightScore.java
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
import systems.subsystems.NavX;
import systems.subsystems.RobotEncoder;

public class MidSideRightScore implements Runnable{
	
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public MidSideRightScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
	}

	@Override
	public void run() {
		
		driveTrain.driveDistance(40, -.7);
		
		driveTrain.circleTurn(40, 90, .7, false, false);
		
		driveTrain.circleTurn(70, 270, .7, true, false);
		
		driveTrain.circleTurn(50, 270, 0.6, false, true); //Turns out of the switch fence finishing forwards
		
		//-collector.outtakeCube(0.7);
		
		driveTrain.circleTurn(40, 0, 0.6, false, true); //Turn to fence cubes
		
		collector.moveArm(135); //move collector down
		
		driveTrain.driveDistance(40, 0.6); //Drives towards the fence cubes
		
		driveTrain.driveDistance(10, 0.4); //Drive slowly towards cube
		
	//	collector.intakeCube(0.7); //intake
		
		driveTrain.driveDistance(10, -0.6); //back up a little
		
		collector.moveArm(75); //move collector to switch angle
		
		collector.outtakeCube(0.7); //outtake
		
	}
	
	
}
