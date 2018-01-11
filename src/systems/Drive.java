package systems;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * RDriveTrain terms
 * double vP, double vI, double vD, double aP, double aI, double aD, double maxSpeed,
double maxAccel, Supplier<Double> angleSupplier,
Supplier<Double> leftPositionSupplier, Supplier<Double> rightPositionSupplier,
BiConsumer<Double, Double> voltageConsumer*/

public class Drive implements SubSystem{
	
	RDriveTrain rDriveTrain;
	RNavX navX;
	REncoder leftEncoder, rightEncoder;
	RSpeedController leftSC, rightSC;
	private double leftV, rightV;
	
	private static Drive instance;
	
	public static void instantiate(){
		Drive drive = new Drive();
		instance=drive;
	}
	
	public static Drive getInstance(){
		if(instance!=null){
			return instance;	
		}else{
			instantiate();
			return instance;
		}
		
	}
	
	public RDriveTrain getRDriveTrain(){
		return rDriveTrain;
	}
	
	public Drive(){
		//velocityPID
		double vP = 0.005;
		double vI = 0;
		double vD = 0;
		//anglePID
		double aP = 0.0;
		double aI = 0.0;
		double aD = 0.0;
		double maxSpeed = 145; //12.5 ft/s
		double maxAccel = 150; //
		Function<Double, Double> conversion = (value)->{
			return value/Systems.TICK_TO_IN;
		};
		navX = (RNavX) Systems.getInstance().getHashMap().get(SysObj.Sensors.NAVX);
		
		leftEncoder = new REncoder((Encoder) Systems.getInstance().
				getHashMap().get(SysObj.Sensors.LEFT_ENCODER), conversion);
		rightEncoder = new REncoder((Encoder) Systems.getInstance().
				getHashMap().get(SysObj.Sensors.LEFT_ENCODER), conversion);

		Supplier<Double> angleSupplier = ()->{
			return navX.getValue();
		};
		
		//negative getvalues, as encoders are opposite
		Supplier<Double> leftPositionSupplier = ()->{
			return -leftEncoder.getValue();
		};
		Supplier<Double> rightPositionSupplier = ()->{
			return -rightEncoder.getValue();
		};
		SpeedController leftControllers[] = {
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.LEFT_1),
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.LEFT_2),
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.LEFT_3)};

		SpeedController rightControllers[] = {
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.RIGHT_1),
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.RIGHT_2),
				(SpeedController) Systems.getInstance().getHashMap().get(SysObj.MotorController.RIGHT_3)};
		
		leftSC = new RSpeedController(leftControllers, false);
		rightSC = new RSpeedController(rightControllers, true);
		
		BiConsumer<Double, Double> voltageConsumer = (left,right)->{
			leftV=left;
			rightV=right;
			leftSC.set(left);
			rightSC.set(right);
		};
		
		rDriveTrain = new RDriveTrain(vP,vI,vD, aP, aI, aD, maxSpeed, maxAccel, angleSupplier, leftPositionSupplier, rightPositionSupplier, voltageConsumer);
		rDriveTrain.enablePID();
		rDriveTrain.enable();
	}
	
	public void setMotorsVoltage(double leftV, double rightV){
		rDriveTrain.setVoltage(leftV, rightV);
	}
	
	public void setVelocity(double leftV, double rightV){
		rDriveTrain.setVelocity(leftV, rightV);
	}
	
	public void arcadeDrive(double forwards, double turn){
		rDriveTrain.arcadeDrive(forwards, turn);
	}
	
	public void arcadeDrivePID(double forwards, double turn){
		rDriveTrain.arcadeDrivePID(forwards, turn);
	}
	
	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToSmartDashboard() {
		if(rDriveTrain==null){
			//SmartDashboard.putString("DB/String 5","NULL" );
		}else{
			SmartDashboard.putString("DB/String 6", "L:" + leftV);
			SmartDashboard.putString("DB/String 7", "R:" + rightV);
			SmartDashboard.putString("DB/String 8","LV:" +  rDriveTrain.getLeftVelocity());
			SmartDashboard.putString("DB/String 9","RV:" +  rDriveTrain.getRightVelocity());
			SmartDashboard.putString("DB/String 1" , "LP" + leftEncoder.getValue());
			SmartDashboard.putString("DB/String 5", "RP:" + rightEncoder.getValue());
			SmartDashboard.putNumber("DB/Slider 1", rDriveTrain.getLeftPIDVoltageAdjustment());
			SmartDashboard.putNumber("DB/Slider 2", rDriveTrain.getLeftPIDVoltageAdjustment());
			SmartDashboard.putString("DB/String 3", "LSP:"+rDriveTrain.getLeftSetpoint());
			SmartDashboard.putString("DB/String 4", "RSP:"+rDriveTrain.getRightSetpoint());
			
		}	
		
	}

	@Override
	public void startLoops() {
		// TODO Auto-generated method stub
		
	}
	
}
