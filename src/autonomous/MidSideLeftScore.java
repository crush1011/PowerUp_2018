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

public class MidSideLeftScore implements Runnable{
	
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public MidSideLeftScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
	}

	@Override
	public void run() {
		
		//driveTrain.turnTo(220, 0.7, false);
		
		driveTrain.driveDistance(103, -0.7); //120 - robot length for real
		
		collector.outtakeCube(0.5);
		
		driveTrain.turnTo(290, 0.7, false);
		
		//collector.moveArm(135);
		
		driveTrain.driveIntake(0.6, 0.8, 18);
		
		driveTrain.turnTo(0, 0.8, false);
		
		//collector.moveArm(0);
		
		systems.eject(0.8);
		
	}
	
	
}
