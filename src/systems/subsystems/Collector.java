/*
 * Collector.java
 * Author: Nitesh Puri
 * Collaborator: Jeremiah Hanson
 * -----------------------------------------------------
 * This class controls the motors on the collector
 */


package systems.subsystems;

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
		intakeLeft.setInverted(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see systems.Subsystem#update()
	 */
	@Override
	public void update() {
		intakeLeft.set(systems.getOperatorRtTrigger());
		intakeRight.set(systems.getOperatorRtTrigger());
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
