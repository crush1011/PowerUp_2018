package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class FourCubeLeftAuto implements Runnable{
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public FourCubeLeftAuto(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		collector.moveArm(70); //move arm to switch angle

		driveTrain.driveLine(135, -28, 140); //drive for 110 inches to switch
		
		collector.outtakeCube(0.5); //outtake	
		
		driveTrain.driveLine(-64, 0, 140); //back up to grab first cube from PC Zone
		
		collector.moveArm(130); //move arm down to collecting position
		
		driveTrain.turnTo(60, 0.8, 600); //turn to the furthest forward cube
		
		collector.intakeCubeAuto(1.0, 200, 2500); //intake thread starts
		
		driveTrain.driveLine(42, 60, 140); //drive towards the first cube while intaking
		
		driveTrain.driveLine(-62, 60, 140); //back up 
		
		collector.moveArm(70); // move arm to switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards the fence
		
		driveTrain.driveLine(76, 0, 140); //drive into the fence
		
		collector.outtakeCube(0.5); //outtake
		
		driveTrain.driveLine(-40, 0, 140); //drive backwards for the second cube
		
		collector.moveArm(130); //collecting position
		
		driveTrain.turnTo(60, 0.8, 600); //turn towards second cube
		
		collector.intakeCubeAuto(1.0, 200, 2500); //start intake thread
	
		driveTrain.driveLine(50, 60, 140); //drive into the cube intaking
	
		driveTrain.driveLine(-55, 60, 140); //back up
		
		collector.moveArm(70); //switch position
		
		driveTrain.turnTo(0, 0.8, 600); //turn towards fence
		
		driveTrain.driveLine(44, 0, 140); //drive into fence
		
		collector.outtakeCube(0.5); //outtake
		
		
		
	}
}