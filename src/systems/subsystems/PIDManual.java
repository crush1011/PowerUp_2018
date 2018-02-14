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
	
	private double integral, derivative, prevError, error, dAngle;
	private double output;
	private final double REFRESH_TIME = (1/14500); //Cycle time for program
	
	private double p;
	private double i;
	private double d;
	
	int counter;
	
	public PIDManual(){
		resources = new Resources();
		this.dAngle = 0;
		p = 1;
		i = 0;
		d = 0;
		error = 0;
		
		counter = 0;
		
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
		
		p = SmartDashboard.getNumber("DB/Slider 0", 0);
		i = SmartDashboard.getNumber("DB/Slider 1", 0);
		d = SmartDashboard.getNumber("DB/Slider 2", 0);
		
		//System.out.println(p +" - "+ i + " - " + d);
		
		systems.update();
		
		error = resources.getAngleError(dAngle, systems.getNavXAngle());
		this.integral += error * REFRESH_TIME;
	//	derivative = (error - this.prevError) / REFRESH_TIME;
		this.output = p * error + i * this.integral + d;
		
		prevError = error;
	
		if (counter % 2000 == 0) {
			//System.out.println("Error: " + error);
			this.toSmartDashboard();
		}
		counter++;
		//this.toSmartDashboard();
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
		return(-(output/180));
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		SmartDashboard.putString("DB/String 1", "Error: " + error);
		SmartDashboard.putString("DB/String 0", "PIDOutput:" + output);
		SmartDashboard.putString("DB/String 2", "Derivative:" + derivative);
		SmartDashboard.putString("DB/String 3", "Integral:" + integral);
		SmartDashboard.putString("DB/String 4", "Angle:" + systems.getNavXAngle());
		SmartDashboard.putString("DB/String 5", "Counter:" + counter);
		SmartDashboard.putString("DB/String 6", "P:" + p);
		SmartDashboard.putString("DB/String 7", "I:" + i);
		SmartDashboard.putString("DB/String 8", "D:" + d);

		
	}
	
}
