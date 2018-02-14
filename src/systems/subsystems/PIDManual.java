/*
 * PIDManual
 * Author: Finlay Parsons
 * ------------------------
 * This class is an attempt to manually control the PID outputs and inputs.
 * Delete it if it sucks.
 */
package systems.subsystems;

import systems.Resources;
import systems.Systems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class PIDManual {
	
	private Systems systems;
	private Resources resources;
	
	private double integral, derivative, prevError, error, dAngle;
	private double output;
	private final double refreshTime = (1/14500); //Cycle time for program
	
	private double p;
	private double i;
	private double d;
	
	int counter = 0;
	
	public PIDManual(){
		resources = new Resources();
		this.dAngle = 0;
		p = 1;
		i = 0;
		d = 0;
		
	}
	
	/*
	 * setDAngle
	 * Author: Finlay Parsons
	 * --------------------------
	 * Purpose: Sets the current angle we want the robot to be driving at
	 */
	public void setDAngle(double Angle){
		this.dAngle = Angle;
	}
	
	/*
	 * update
	 * Author: Finlay Parsons
	 * ------------------------
	 * Purpose: Updates all of the information needed to make the output calculations
	 * Note- Needs to be constantly updated while driving
	 */
	public void update(){
		
		if (systems == null){
			systems = Systems.getInstance();
		}
		
		p = 4;
		i = 0; //SmartDashboard.getNumber("DB/ Silder 1", 0);
		d = 0; //SmartDashboard.getNumber("DB/ Silder 2", 0);
		
		System.out.println(p +" - "+ i + " - " + d);
		
		error = resources.getAngleError(dAngle, systems.getNavXAngle());
		this.integral += error * refreshTime;
		derivative = (error - this.prevError) / refreshTime;
		this.output = p * error + i * this.integral + d * derivative;
		
		prevError = error;
	
	}
	
	/*
	 * getTurnOutput
	 * Author: Finlay Parsons
	 * ------------------------
	 * Purpose: Outputs the value of z in arcade drive
	 * Returns: A value between -1 and 1
	 */
	public double getTurnOutput(){		
		update();
		return(Math.sqrt(output/360));
	}
	
}
