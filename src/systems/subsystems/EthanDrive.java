package systems.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import systems.Subsystem;

public class EthanDrive extends PIDSubsystem implements Subsystem{
	
	private static WPI_TalonSRX ltMain, ltSlave1, ltSlave2;
	private static WPI_TalonSRX rtMain, rtSlave1, rtSlave2;
	
	
	public EthanDrive(WPI_TalonSRX ltM, WPI_TalonSRX ltS1, WPI_TalonSRX ltS2, 
			WPI_TalonSRX rtM, WPI_TalonSRX rtS1, WPI_TalonSRX rtS2){
		super("ApeDrive", 0.01, 0.04, 0.02);
		this.setAbsoluteTolerance(1);
		this.getPIDController().setContinuous(true);
		this.setInputRange(0, 360);
		this.setOutputRange(-1.0, 1.0);
		this.enable();
		ltMain = ltM;
		ltSlave1 = ltS1;
		ltSlave2 = ltS2;
		ltSlave1.follow(ltM);
		ltSlave2.follow(ltM);
		
		rtMain = rtM;
		rtSlave1 = rtS1;
		rtSlave2 = rtS2;
		rtSlave1.follow(rtM);
		rtSlave2.follow(rtM);
		
		
	}
	
	public static double limit(double value, double min, double max) {
		   return Math.min(Math.max(value, min), max);
	}
	
	public void arcadeDrive(double move, double rotate) {
		double leftMotorSpeed;
		double rightMotorSpeed;
		double moveValue, rotateValue;
		moveValue = limit(move, -1, 1); //positive     pnegative
		rotateValue = -limit(rotate, -1, 1); //negative pnegative

			if (moveValue >= 0.0) {
				moveValue = (moveValue * moveValue);
			} else {
				moveValue = -(moveValue * moveValue);
			}
			if (rotateValue >= 0.0) {
				rotateValue = (rotateValue * rotateValue);
			} else {
				rotateValue = -(rotateValue * rotateValue);
			}

		}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
	public static void instatiate() {
		EthanDrive ed = new EthanDrive(ltMain, ltSlave1, ltSlave2, rtMain, rtSlave1, rtSlave2);
	}
}
