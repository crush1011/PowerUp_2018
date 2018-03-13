package autonomous;

import systems.Systems;
import systems.subsystems.Collector;
import systems.subsystems.DriveTrain;

public class DriveShootRight implements Runnable{
	private Systems systems;
	private DriveTrain driveTrain;
	private Collector collector;
	
	public DriveShootRight(){
		
		systems = Systems.getInstance();
		driveTrain = Systems.getDriveTrain();
		collector = Systems.getCollector();
}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		

		driveTrain.driveLineDontStop(-100, 30, 140); //drive for 110 inches to switch
		
		collector.outtakeCube(0.5); //outtake	
		
	
	}
}
