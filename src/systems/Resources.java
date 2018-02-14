/*
 * Resources
 * Author: Finlay Parsons
 * -------------
 * Class for commonly used functions 
 */
package systems;

public class Resources {
	
	/*
	 * getAngleError
	 * Author: Finlay Parsons
	 * -------------------------
	 * Purpose: Returns the difference between two angles
	 * Parameters:
	 * 	dAngle - The desired angle
	 * 	cAngle - The current angle
	 * Returns: A doubleS
	 */
	public double getAngleError(double dAngle, double cAngle){
		if(Math.abs(dAngle-cAngle) > 180){
			return (360 - returnGreater(dAngle, cAngle)) + returnLesser(dAngle, cAngle);
		}
		else {
			return cAngle - dAngle;
		}
	}
	
	public double returnGreater(double a, double b){
		if(a>b) return a;
		else return b;
	}
	
	public double returnLesser(double a, double b){
		if(a<b) return a;
		else return b;
	}
}
