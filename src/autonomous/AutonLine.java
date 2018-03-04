package autonomous;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Systems;
import systems.subsystems.DriveTrain;

public class AutonLine extends PIDSubsystem implements Runnable{
	
	double angle, topSpeed, distance;
	double rotateOutput;
	double loopCount, pastCount;
	
	final double acceleration = 1.7;
	DriveTrain drive;
	
	AHRS navx;
	
	public AutonLine(DriveTrain driveTrain, AHRS navx, double distance,double topSpeed,double angle){
		super(2.0,0,0);
		this.setAbsoluteTolerance(2);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-180, 180);
		this.setSetpoint(angle);
		this.enable();
		
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
			if(loopCount!=pastCount){
				if(rotateOutput>=0){
					rotateOutput+=0.02;
				}else{
					rotateOutput-=0.02;
				}
			}
			pastCount=loopCount;
			double actualVelocity = currentVelocity / 180;
			drive.drive(actualVelocity, rotateOutput);
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
	
	@Override
	protected double returnPIDInput() {
		SmartDashboard.putString("DB/String 1", "" +navx.getFusedHeading());
		return navx.getFusedHeading();
	}

	@Override
	protected void usePIDOutput(double output) {
		rotateOutput = output / 140; //180? p140
		loopCount++;
		//SmartDashboard.putString("DB/String 0",""+rotateOutput);;
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	
	
}
