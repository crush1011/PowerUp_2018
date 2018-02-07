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
		
	//	driveTrain.driveDistance(60, 0.5);
	//	driveTrain.turnTo(180, 0.5);
		driveTrain.circleTurn(40, 300, 0.8, true, false);
	}
	
	
}
