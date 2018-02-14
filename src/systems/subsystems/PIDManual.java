/*
 * PIDManual
 * Author: Finlay Parsons
 * ------------------------
 * This class is an attempt to manually control the PID outputs and inputs.
 * Delete it if it sucks.
 */
package systems.subsystems;

import systems.Resources;
import systems.Subsystem;
import systems.SysObj;
import systems.Systems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class PIDManual implements Subsystem{
	
	private Systems systems;
	private Resources resources;
	
	private double integral, derivative, prevError, error, dValue;
	private double output;
	private final double REFRESH_TIME = (1/14500); //Cycle time for program
	
	private double p;
	private double i;
	private double d;
	
	
	int counter;
	
	public PIDManual(){
		resources = new Resources();
		this.dValue = 0;
		p = 0;
		i = 0;
		d = 0;
		error = 0;
		
		counter = 0;
		
	}
	
	/*
	 * setPID
	 * Author: Finlay Parsons
	 * -------------------------
	 * Purpose: Sets the values of P, I, and D for whatever you're doing.
	 * Parameters:
	 * 	dP: Desired P
	 * 	dI: Desired I
	 * 	dD: Desired D
	 * Returns void
	 */
	public void setPID(double dP, double dI, double dD){
		p = dP;
		i = dI;
		d = dD;
	}
	
	/*
	 * setdValue
	 * Author: Finlay Parsons
	 * --------------------------
	 * Purpose: Sets the value to approach
	 */
	public void setDValue(double Angle){
		this.dValue = Angle;
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
		
		
		//System.out.println(p +" - "+ i + " - " + d);
		
		systems.update();
		
		error = resources.getAngleError(dValue, systems.getNavXAngle()); 
		this.integral += error * REFRESH_TIME;
	//	derivative = (error - this.prevError) / REFRESH_TIME;
		this.output = p * error + i * this.integral + d;
		
		prevError = error;
	
		if (counter % 2000 == 0) {
			this.toSmartDashboard();
		}
		counter++;
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
		return output;
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		SmartDashboard.putString("DB/String 1", "Error: " + resources.roundDouble(error, -1));
		SmartDashboard.putString("DB/String 9", "PIDOutput:" + resources.roundDouble(output, -1));
		SmartDashboard.putString("DB/String 2", "Derivative:" + derivative);
		SmartDashboard.putString("DB/String 3", "Integral:" + integral);
		SmartDashboard.putString("DB/String 4", "Angle:" + resources.roundDouble(systems.getNavXAngle(), -1));
		SmartDashboard.putString("DB/String 5", "Counter:" + counter);
		SmartDashboard.putString("DB/String 6", "P:" + p);
		SmartDashboard.putString("DB/String 7", "I:" + i);
		SmartDashboard.putString("DB/String 8", "D:" + d);

		
	}
	
}
