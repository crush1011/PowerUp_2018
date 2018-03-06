package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class FourCubeRightAuto implements Runnable{
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public FourCubeRightAuto(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		collector.moveArm(75);
		
		driveTrain.turnToOneSide(25, 0.8, 600, false);
		
		driveTrain.driveLine(110, 30, 140);
		
		driveTrain.turnToOneSide(0, 0.8, 600, true);
		
		collector.outtakeCube(0.5);
		
		driveTrain.driveLine(-80, 0, 140);
		
		collector.moveArm(130);
		
		driveTrain.turnTo(-45, 0.8, 600);
		
		collector.intakeCubeAuto(1.0, 200, 600);
		driveTrain.driveLine(57, -45, 140);
		
		driveTrain.driveLine(-67, -45, 140);
		
		collector.moveArm(75);
		
		driveTrain.turnTo(0, 0.8, 600);
		
	
		driveTrain.driveLine(80, 0, 140);
		collector.outtakeCube(0.5);
		
		driveTrain.driveLine(-60, 0, 140);
		
		collector.moveArm(130);
		
		driveTrain.turnTo(-45, 0.8, 600);
		
		collector.intakeCubeAuto(1.0, 200, 1800);
		driveTrain.driveLine(50, -45, 140);
		
		driveTrain.driveLine(-60, -45, 140);
		
		collector.moveArm(75);
		
		driveTrain.turnTo(0, 0.8, 600);
		
	
		driveTrain.driveLine(60, 0, 140);
		collector.outtakeCube(0.5);
		
		
		
	}
}
