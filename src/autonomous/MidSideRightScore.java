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
		
		driveTrain.driveDistance(103, -0.7);
		
		collector.outtakeCube(0.5);
		
		//driveTrain.circleTurn(30, angle, speed, right, forwards);
		
	}
	
	
}
