/*
 * Collector.java
 * Author: Nitesh Puri
 * Collaborator: Jeremiah Hanson
 * -----------------------------------------------------
 * This class controls the motors on the collector
 */


package systems.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Spark;
import systems.Subsystem;
import systems.Systems;

public class Collector implements Subsystem {
	
	private WPI_TalonSRX collectorArm; 
	Spark intakeLeft, intakeRight;
	private static Systems systems;
	
	/*
	 * Constructor
	 * Author: Nitesh Puri
	 * ----------------------------------------
	 * constructor
	 */
	
	public Collector(WPI_TalonSRX collectorArm, 
			Spark intakeLeft, 
			Spark intakeRight) {
		this.collectorArm = collectorArm;
		this.intakeLeft = intakeLeft;
		this.intakeRight = intakeRight;
		//intakeLeft.setInverted(true);
		collectorArm.setNeutralMode(NeutralMode.Brake);
	}
	
	/*
	 * (non-Javadoc)
	 * @see systems.Subsystem#update()
	 */
	@Override
	public void update() {
		if (systems == null) {
			systems = Systems.getInstance();
		}
		 if(!systems.inAuto) {
			//Controls for intake
			if(systems.getOperatorRtTrigger()>.2) {
				intakeLeft.set(systems.getOperatorRtTrigger());
				intakeRight.set(systems.getOperatorRtTrigger());
			}
			else if(systems.getOperatorLtTrigger()>.2) {
				intakeLeft.set(-systems.getOperatorLtTrigger());
				intakeRight.set(-systems.getOperatorLtTrigger());
			}
			else {
				intakeLeft.set(0.0);
				intakeRight.set(0.0);
			}
			
			//Controls for arm
			collectorArm.set(systems.getOperatorLJoystick());
		}
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
