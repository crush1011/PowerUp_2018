package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class ThreeCubeRightAuto implements Runnable{
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public ThreeCubeRightAuto(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		collector.moveArm(72); //move arm to switch angle

		driveTrain.driveLineDontStop(100, 25, 140, true); //drive for 110 inches to switch
		
		collector.outtakeCubeAuto(0.65, true); //outtake		
		
		driveTrain.driveLine(-68, 0, 140); //back up to grab second cube from PC Zone
		
		collector.moveArm(104); //move arm down to collecting position 131 if broke
		
		driveTrain.turnTo(-60, 0.8, 600); //turn to the furthest forward cube
		
		collector.intakeCubeAuto(1.0, 200, 3500); //intake thread starts
		
		driveTrain.driveLine(75, -60, 140); //drive towards the second cube while intaking
		
		driveTrain.driveLine(-75, -60, 140); //back up 
		
		collector.moveArm(72); // move arm to switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards the fence
		
		driveTrain.driveLineDontStop(62, 0, 140, false); //drive into the fence
		
		collector.outtakeCubeAuto(0.9, true); //outtake
		
		driveTrain.driveLine(-40, 0, 140); //drive backwards for the third cube
		
		collector.moveArm(104); //collecting position 131 if broke
		
		driveTrain.turnTo(-60, 0.8, 600); //turn towards third cube
		
		collector.intakeCubeAuto(1.0, 200, 3500); //start intake thread
	
		driveTrain.driveLine(53, -60, 140); //drive into the cube intaking
	
		driveTrain.driveLine(-55, -60, 140); //back up
		
		collector.moveArm(72); //switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards fence
		
		driveTrain.driveLineDontStop(50, 0, 140, false); //drive into fence
		
		collector.outtakeCubeAuto(0.9, true); //outtake
		
		
		
	}
}
