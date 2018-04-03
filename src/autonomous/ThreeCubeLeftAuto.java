package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class ThreeCubeLeftAuto implements Runnable{
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public ThreeCubeLeftAuto(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		collector.moveArm(72); //move arm to switch angle

		driveTrain.driveLineDontStop(108, -28, 140, true); //drive for 110 inches to switch
		
		collector.outtakeCubeAuto(0.65, true); //outtake	
		
		driveTrain.driveLine(-65, 0, 140); //back up to grab second cube from PC Zone
		
		collector.moveArm(131); //move arm down to collecting position
		
		driveTrain.turnTo(60, 0.8, 600); //turn to the furthest forward cube
		
		collector.intakeCubeAuto(1.0, 200, 3500); //intake thread starts
		
		driveTrain.driveLine(50, 60, 140); //drive towards the second cube while intaking
		
		driveTrain.driveLine(-60, 60, 140); //back up 
		
		collector.moveArm(72); // move arm to switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards the fence
		
		driveTrain.driveLineDontStop(80, 0, 140, false); //drive into the fence
		
		collector.outtakeCubeAuto(0.9, true); //outtake
		
		driveTrain.driveLine(-40, 0, 140); //drive backwards for the third cube
		
		collector.moveArm(131); //collecting position
		
		driveTrain.turnTo(60, 0.8, 600); //turn towards  cube
		
		collector.intakeCubeAuto(1.0, 200, 3500); //start intake thread
	
		driveTrain.driveLine(50, 60, 140); //drive into the cube intaking
	
		driveTrain.driveLine(-50, 60, 140); //back up
		
		collector.moveArm(72); //switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards fence
		
		driveTrain.driveLineDontStop(50, 0, 140, false); //drive into fence
		
		collector.outtakeCubeAuto(0.9, true); //outtake
		
		
		
	}
}
