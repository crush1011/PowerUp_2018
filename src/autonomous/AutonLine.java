package autonomous;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Systems;
import systems.subsystems.DriveTrain;
import systems.subsystems.NavX;

public class AutonLine  implements Runnable{
	
	double angle, topSpeed, distance;
	double rotateOutput;
	double loopCount, pastCount;
	
	final double acceleration = 140;
	DriveTrain drive;
	
	NavX navx;
	
	private static final double P = 0.03;
	
	public AutonLine(DriveTrain driveTrain, NavX navx, double distance,double topSpeed,double angle){

		
		this.angle=angle;
		this.topSpeed=topSpeed;
		this.distance=distance;
		
		this.drive = driveTrain;
		this.navx = navx;
	}

	double delT = 50;
	public void run(){
		boolean stop = false;
		//300 is accel

		boolean deAccelerate = false;	
		double initialPos = Systems.getInstance().getAverageDriveEncoderDistance();
		
		double distanceTravelled =0;
		
		boolean backwards = distance<0;
		
		double currentVelocity = 0;
		double lastVelocity =0;
		while(!stop && DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()){
			long startTime = System.currentTimeMillis();
			
			double timeToStop = Math.abs(currentVelocity/acceleration);
			
			timeToStop = Math.abs(currentVelocity)/acceleration;
			double distanceNeededToStop = (Math.abs(currentVelocity )) * timeToStop;
			
			distanceTravelled=Math.abs(Systems.getInstance().getAverageDriveEncoderDistance());
			
			
			//remainingdistance
			if(Math.abs(distance) - distanceTravelled<=distanceNeededToStop){
				deAccelerate=true;
			}

			if(deAccelerate? !backwards:backwards){
				currentVelocity-= (delT/1000) * acceleration;
			}else{
				currentVelocity += (delT/1000) * acceleration;
			}
			currentVelocity = Math.max(Math.min(topSpeed, currentVelocity), -topSpeed);
			System.out.println("currentV:" + currentVelocity);
			System.out.println(deAccelerate);
			System.out.println(backwards);
			
			double currentError = angle - navx.getCurrentAngle();
            if(Math.abs(currentError) > (360 - 0)/2){
                currentError  = currentError>0? currentError-360+0 : currentError+360-0;
            }
			
			double rotateOutput  = currentError * P;

			
			pastCount=loopCount;
			double actualVelocity = currentVelocity / 140;
			drive.drive(actualVelocity, 0);
			if(backwards){
				if(currentVelocity>=0 && lastVelocity<0){
					stop=true;
				}
			}else{
				if(currentVelocity<=0 && lastVelocity>0){
					stop=true;
				}
			}
			lastVelocity = currentVelocity;
			try {
				Thread.sleep(((long)delT + (System.currentTimeMillis() - startTime)));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		drive.drive(0, 0);
		


	}

	
	
}
