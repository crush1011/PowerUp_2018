package systems;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

//import javax.annotation.Resources;

/**
 * Created by raque on 9/7/2017.
 */
public class RDriveTrain {

    private boolean driveTrainEnabled;

    private RPID autoAnglePID;
    private double kV, kA, maxSpeed, maxAccel;
    private double leftVelocity, rightVelocity;
    
    private double actualLeftVelocityAvg = 0, actualRightVelocityAvg=0;
    private double referenceLeftVelocityAvg=0, referenceRightVelocityAvg;
    	
    private double actualLeftVelocity, actualRightVelocity;

    private double leftPIDVoltageAdjustment, rightPIDVoltageAdjustment;
    private Supplier<Double> angleSupplier, leftPositionSupplier, rightPositionSupplier;
    private BiConsumer<Double, Double> voltageConsumer;
    private boolean vPIDEnabled;

    private Runnable driveTrainLoop;

    //suppliers in inches
    public RDriveTrain(double vP, double vI, double vD, double aP, double aI, double aD, double maxSpeed,
                       double maxAccel, Supplier<Double> angleSupplier,
                       Supplier<Double> leftPositionSupplier, Supplier<Double> rightPositionSupplier,
                       BiConsumer<Double, Double> voltageConsumer){
    	double t = System.currentTimeMillis();

        autoAnglePID = new RPID(aP, aI, aD);
        autoAnglePID.setContinuous(true);
        autoAnglePID.setInputRange(0, 360);
        autoAnglePID.setOutputRange(-maxSpeed *0.5, maxSpeed*0.5);
       
        this.maxSpeed=maxSpeed;
        this.maxAccel=maxAccel;
        kV = 1/maxSpeed;
        kA = 1/maxAccel;
        kA=0.15*kA;
        leftVelocity=0;
        rightVelocity=0;
        this.angleSupplier=angleSupplier;
        this.leftPositionSupplier=leftPositionSupplier;
        this.rightPositionSupplier = rightPositionSupplier;
        this.voltageConsumer=voltageConsumer;
        vPIDEnabled=false;
        driveTrainEnabled=false;
    }

    public void completeStop(){
    	setVoltage(0,0);
    	leftVelocity=0;
    	rightVelocity=0;
    }
    
    public void setVelocity(double leftVelocity, double rightVelocity){
        leftVelocity = Resources.limit(leftVelocity, -maxSpeed, maxSpeed);
        rightVelocity = Resources.limit(rightVelocity, -maxSpeed, maxSpeed);
      //  System.out.println(leftVelocity);
        //setsetpoints of pid
 
        double leftVoltage = leftPIDVelocityToVoltage(leftVelocity, 0);
        double rightVoltage = rightPIDVelocityToVoltage(rightVelocity, 0);
        
        //setVoltage to drivetrain
        setVoltage(leftVoltage,rightVoltage);
    }

    //meant to be used only for AUTO in conjuction with Trajectories
    public void setVelocity(double leftVelocity, double rightVelocity, double angle){
        autoAnglePID.setSetPoint(angle);
        double anglePIDAdjustment = autoAnglePID.crunch(angleSupplier.get());
        System.out.println("PIDADJUST:" + anglePIDAdjustment);
        anglePIDAdjustment=0;
        setVelocity(leftVelocity+anglePIDAdjustment, rightVelocity -anglePIDAdjustment);
    }


    public double getLeftVelocity(){
        return actualLeftVelocityAvg;
    }

    public double getRightVelocity(){
        return actualRightVelocityAvg;
    }
    
    public double testPID(double setPoint){
    	 autoAnglePID.setSetPoint(setPoint);
         double anglePIDAdjustment = autoAnglePID.crunch(angleSupplier.get());
         System.out.println("PIDADJUST:" + anglePIDAdjustment);
         return anglePIDAdjustment;
   
    }

    public double rawVelocityToVoltage(double velocity, double accel){
        return kV * velocity + kA* accel;
    }

    private double leftPIDVelocityToVoltage(double velocity, double accel){
        double voltage = rawVelocityToVoltage(velocity,accel);
      //  voltage +=(leftPIDVoltageAdjustment * (velocity/maxSpeed));
        return voltage;
    }

    public double rightPIDVelocityToVoltage(double velocity, double accel){
        double voltage = rawVelocityToVoltage(velocity,accel);
     //   voltage +=(rightPIDVoltageAdjustment * (velocity/maxSpeed));
        
        return voltage;
        
    }

    public void setVoltage(double leftVoltage, double rightVoltage){
        //set Speed controllers to this voltage
    	voltageConsumer.accept(leftVoltage, rightVoltage);
    }
    
    public void arcadeDrivePID(double move, double rotate) {
		// rotate = 0.7*rotate;
		double leftMotorSpeed;
		double rightMotorSpeed;
		double moveValue, rotateValue;
		moveValue = Resources.limit(move, -1, 1);
		rotateValue = Resources.limit(rotate, -1, 1); 
		//System.out.println(moveValue);
		//squared inputs
		if (true) {
			// square the inputs (while preserving the sign) to increase fine
			// control
			// while permitting full power
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

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		leftMotorSpeed = leftMotorSpeed * maxSpeed;
		rightMotorSpeed = rightMotorSpeed * maxSpeed;
		//System.out.println(leftMotorSpeed);
		setVelocity(leftMotorSpeed, rightMotorSpeed);
	}
    
    public void arcadeDrive(double move, double rotate) {
		double leftMotorSpeed;
		double rightMotorSpeed;
		double moveValue, rotateValue;
		moveValue = Resources.limit(move, -1, 1);
		rotateValue = Resources.limit(rotate, -1, 1); // negative p positive

		//squared inputs
		if (true) {
			// square the inputs (while preserving the sign) to increase fine
			// control
			// while permitting full power
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

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
		setVoltage(leftMotorSpeed,rightMotorSpeed);
	}

    public void enablePID(){
        leftPIDVoltageAdjustment=0;
        rightPIDVoltageAdjustment=0;
        vPIDEnabled=true;
    }

    public void disablePID(){
        vPIDEnabled=false;
    }

    public synchronized void enable(){
        startLoop();
    }

    public synchronized void disable(){
        driveTrainEnabled=false;
    }

    private synchronized void startLoop(){
    	System.out.println("DD"+driveTrainEnabled);
        if(driveTrainEnabled==false){
        	driveTrainEnabled=true;
            new Thread(driveTrainLoop).start();
        }
    }

	public double getLeftPIDVoltageAdjustment() {
		return leftPIDVoltageAdjustment;
	}

	public double getRightPIDVoltageAdjustment() {
		return rightPIDVoltageAdjustment;
	}
    
	public double getLeftSetpoint(){
		return leftVelocity;
	}
	
	public double getRightSetpoint(){
		return rightVelocity;
	}
    

}
