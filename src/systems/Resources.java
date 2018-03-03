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
	
	/*
	 * returnGreater
	 * Author: Finlay Parsons
	 * ------------------------
	 * Takes in two doubles and returns the greater
	 */
	public double returnGreater(double a, double b){
		if(a>b) return a;
		else return b;
	}
	
	/*
	 * returnLesser
	 * Author: Finlay Parsons
	 * -----------------------
	 * Takes in two doubles and returns the lesser
	 */
	public double returnLesser(double a, double b){
		if(a<b) return a;
		else return b;
	}
	
	/*
	 * roundDouble
	 * Author: Finlay Parsons
	 * ------------------------
	 * Rounds a given double to given power of 10
	 * Parameters:
	 * 	n: Double to round
	 * 	p: Rounds to the nearest 10^p
	 */
	public double roundDouble(double n, double p){
		if(n!=0) {
		return n - (Math.abs((n)/n) * (n % (Math.pow(10, p))));
		}
		else {
			return 0;
		}
	}
	
	/*
	 * limit
	 * Author: Finlay Parsons
	 * Collaborators: Ruben Castro, 520-461-0733
	 * --------------------------------------
	 * Limits a value between values
	 */
	public double limit(double numberIn, double lower, double upper){
		if(Math.abs(numberIn - lower) < Math.abs(numberIn - upper)){
			return lower;
		}
		if(Math.abs(numberIn - lower) > Math.abs(numberIn - upper)){
			return upper;
		}else{
			return numberIn;
		}
	}
}
