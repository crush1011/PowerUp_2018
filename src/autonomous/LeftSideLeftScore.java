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
import systems.subsystems.NavX;
import systems.subsystems.RobotEncoder;

public class LeftSideLeftScore implements Runnable{
	
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public LeftSideLeftScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
	}

	@Override
	public void run() {
		driveTrain.driveDistance(1000, 0.5);
		
		/*driveTrain.driveDistance(110, -0.7); //Drives backwards to the switch fence
		
		driveTrain.circleTurn(45, 90, 0.7, false, false); //Turns into the switch fence
		
		collector.outtakeCube(); //outtake
		
		driveTrain.circleTurn(40, 270, 0.6, true, true); //Turns out of the switch fence finishing forwards
		
		driveTrain.circleTurn(40, 0, 0.6, true, true);
		
		collector.moveArm(135);
		
		driveTrain.driveDistance(40, 0.6); //Drives towards the fence cubes
		
		driveTrain.driveDistance(10, 0.4);
		
		collector.intakeCube();
		
		driveTrain.driveDistance(10, -0.6);
		
		collector.moveArm(75);
		
		collector.outtakeCube();*/
	}
	
	
}
