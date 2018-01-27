/*
 * NavX.java
 * Author: Nitesh Puri
 * Collaborators: Finlay Parsons, Jeremiah Hanson
 * --------------------------------------------------
 * Controls for the NavX
 */
package systems.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import systems.Subsystem;
import systems.Systems;

public class NavX implements Subsystem {

	private AHRS navX;
	private double currentAngle;
	private double zeroAngle;
	private static Systems systems;
	
	/*
	 * Constructor
	 * Author: Nitesh Puri
	 * Collaborators: Finlay Parsons and Jeremiah Hanson
	 * ----------------------------------------------------
	 * This is the constructor
	 */
	public NavX(AHRS navX){
		this.navX = navX;
		zeroAngle = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see systems.Subsystem#update()
	 * Original Author: Ruben Castro
	 */
	@Override
	public void update() {
		if (systems == null) {
			systems = Systems.getInstance();
		}
		
		currentAngle = navX.getFusedHeading();
		currentAngle = ((currentAngle % 360) + 360) % 360;
		this.toSmartDashboard();
		
		
	}
	
	/*
	 * getCurrentAngle
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson
	 * --------------------------------------------
	 * Purpose: Returns the current angle of the robot
	 * Returns a double
	 */
	public double getCurrentAngle() {
		//update();
		return currentAngle-zeroAngle;
	}
	
	/*
	 * zeroAngler
	 * Author: Finlay Parsons
	 * Collaborators: Nitesh Puri, Jeremiah Hanson
	 * ---------------------------------------------
	 *Purpose: Sets the local angle to the zero angle
	 *Returns nothing
	 */
	public void zeroAngler() {
		//update();
		zeroAngle = currentAngle;
	}

	@Override
	public void toSmartDashboard() {
		SmartDashboard.putString("DB/String 0", "Angle: " + this.getCurrentAngle());
	}

}
