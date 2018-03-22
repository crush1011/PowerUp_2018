package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class FourCubeRightCustom implements Runnable {
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;

	public FourCubeRightCustom() {

		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		collector.moveArm(75); // move arm to switch angle

		driveTrain.driveLineDontStop(110, 20, 140); // drive for 115 inches to switch

		collector.outtakeCube(0.5, true); // outtake

		//TODO FIX BECAUSE NOT GOING FAR ENOUGH AND OUT ENOUGH TO GET OUTER MOST CUBE
		
		driveTrain.driveCircle(60, 295, false, true, 3); //back up while turning toward cubes

		collector.moveArm(135); // move arm down to collecting position

		collector.intakeCubeAuto(1.0, 200, 2000); // intake thread starts

		driveTrain.driveLine(80, -65, 140); // drive towards the second cube while intaking

		collector.moveArm(10); //move arm up

		driveTrain.driveCircle(8, 180, false, true, 6); //back up while turning into fence

		collector.outtakeCube(0.5, false); //outtake

		collector.moveArm(135); //move arm down

		collector.intakeCubeAuto(1.0, 200, 2500); //intake thread starts

		driveTrain.driveCircle(5, 315, true, true, 6); //drive while turning into third cube

		collector.moveArm(10); // move arm up

		driveTrain.driveCircle(3, 180, false, true, 6); //back up while turning into fence
		
		driveTrain.driveAuton(-0.5, 300);

		collector.outtakeCube(0.5, false); //outtake

		collector.moveArm(135); //move arm down

		collector.intakeCubeAuto(1.0, 200, 3000); //intake thread starts

		driveTrain.driveCircle(1, 335, true, true, 6); //drive while turning into fourth cube
		
		driveTrain.driveAuton(0.4, 400); //drive into fourth cube
		
		collector.moveArm(10); // move arm up

		driveTrain.driveCircle(5, 180, false, true, 6); //back up while turning into fence

		collector.outtakeCube(0.5, false); // outtake

	}
}
