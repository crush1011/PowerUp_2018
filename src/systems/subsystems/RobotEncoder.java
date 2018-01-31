package systems.subsystems;
/*
 * Class Name: Encoder.java
 * Author: Ethan Ngp
 * -----------------------------------------------------------
 * Purpose: Class for getting and setting values for encoder
 */

import java.util.function.Function;

import edu.wpi.first.wpilibj.Encoder;
import systems.Subsystem;

public class RobotEncoder implements Subsystem{
	private Encoder e;
	private double startPos;
	private Function<Double, Double> conversion;
	private boolean negative;
	private double currentPos;
	
	/*
	 * Constructor
	 * Author: Ethan Ngo
	 * ------------------------------------------------
	 * Constructor
	 * Parameters: Encoder
	 */
	public RobotEncoder(Encoder e){
		this.e=e;
		conversion = (val) ->{
			return val;
		};
		negative=false;
		startPos = 0;
		currentPos = startPos;
	}
	
	/*
	 * getValue()
	 * Author: Ethan Ngo
	 * -------------------------------------
	 * Purpose: Return currentPos value
	 * returns type double
	 */
	
	public double getValue(){
		return conversion.apply(currentPos);
		
	}
	
	public void reset(){
		startPos = e.getDistance();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(e!=null){
			currentPos = e.getDistance();
			currentPos -= startPos;
			currentPos = negative? -currentPos:currentPos;
		}
	}

	@Override
	public void toSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
