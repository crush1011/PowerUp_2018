package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class FourCubeLeftCustom implements Runnable {
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;

	public FourCubeLeftCustom() {

		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		collector.moveArm(75); // move arm to switch angle

		driveTrain.driveLineDontStop(130, -28, 140); // drive for 130 inches to switch

		collector.outtakeCube(0.5, true); // outtake

		driveTrain.driveCircle(60, 62, false, false, 1); //back up while turning towards cubes

		collector.moveArm(135); // move arm down to collecting position

		collector.intakeCubeAuto(1.0, 200, 2000); // intake thread starts

		driveTrain.driveLine(78, 60, 140); // drive towards the second cube while intaking

		collector.moveArm(10); //move arm up

		driveTrain.driveCircle(8, 180, false, false, 3); //back up while turning into fence
		
		driveTrain.driveAuton(-0.5, 300);

		collector.outtakeCube(0.5, false); //outtake

		collector.moveArm(135); //move arm down

		collector.intakeCubeAuto(1.0, 200, 2500); //intake thread starts

		driveTrain.driveCircle(4, 45, true, false, 6); //drive while turning into third cube

		collector.moveArm(10); //move arm up

		driveTrain.driveCircle(5, 180, false, false, 3); //back up while turning into fence
		
		driveTrain.driveAuton(-0.5, 300);

		collector.outtakeCube(0.5, false); //outtake

		collector.moveArm(135); //move arm down

		collector.intakeCubeAuto(1.0, 200, 3000); //intake thread starts

		driveTrain.driveCircle(1, 30, true, false, 4); //drive forward while turning toward fourth cube
		
		driveTrain.driveAuton(0.4, 600); //drive into fourth cube
		
		collector.moveArm(10); //move arm up

		driveTrain.driveCircle(5, 180, false, false, 3); //back up while turning 

		collector.outtakeCube(0.5, false); // intake thread starts

	}
}
