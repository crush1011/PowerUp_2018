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

import systems.SysObj;
import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;
import systems.subsystems.NavX;
import systems.subsystems.RobotEncoder;

public class LeftSideLeftScore implements Auton{
	
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	private NavX navX;
	private RobotEncoder leftEncoder, rightEncoder, armEncoder;

	public LeftSideLeftScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
		navX =  Systems.getNavX();
		leftEncoder = Systems.getRobotEncoder(SysObj.Sensors.LEFT_ENCODER);
		rightEncoder = Systems.getRobotEncoder(SysObj.Sensors.RIGHT_ENCODER);
		armEncoder = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER);
		
	}

	@Override
	public void update() {
		leftEncoder.update();
		rightEncoder.update();
		armEncoder.update();
		
		if(leftEncoder.getValue()<5){
			driveTrain.drive(0.5, 0);
		}
		else {
			driveTrain.drive(0, 0);
		}
	}
	
	
}
