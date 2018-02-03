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

public class LeftSideLeftScore implements Auton{
	
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	private NavX navX;
	private RobotEncoder leftEncoder, rightEncoder, armEncoder;
	private double currentEncoder;
	private int counter=0;
	private double distance;
	private EthanDrive ethanDrive;
	
	public LeftSideLeftScore(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		//ethanDrive = Systems.getEthanDrive();
		collector = Systems.getCollector();
		navX =  Systems.getNavX();
		leftEncoder = Systems.getRobotEncoder(SysObj.Sensors.LEFT_ENCODER);
		rightEncoder = Systems.getRobotEncoder(SysObj.Sensors.RIGHT_ENCODER);
		armEncoder = Systems.getRobotEncoder(SysObj.Sensors.ARM_ENCODER);
		currentEncoder = 0;
		this.distance=60;
	}

	@Override
	public void update() {
		leftEncoder.update();
		rightEncoder.update();
		armEncoder.update();
		
		if(rightEncoder.getValue()!=currentEncoder) {
			currentEncoder=rightEncoder.getValue();
			counter++;
		}
		
		
		/*if(leftEncoder.getValue()>SmartDashboard.getNumber("DB/Slider 0", 0) && leftEncoder.getValue()<SmartDashboard.getNumber("DB/Slider 1", 10)){
			driveTrain.drive(0.5, 0);
		}*/
		SmartDashboard.putNumber("DB/Slider 3", 5);
		
		driveTrain.driveDistance(60, 0.5);
		
	}
	
	
}
