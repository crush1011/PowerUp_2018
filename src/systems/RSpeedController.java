package systems;

import edu.wpi.first.wpilibj.SpeedController;

public class RSpeedController {

	double voltage;
	boolean negative;
	
	SpeedController scs[];
	
	public RSpeedController(SpeedController scs[], boolean negative){
		this.scs=scs;
		this.negative=negative;
	}
	
	
	public void set(double voltage){
		this.voltage=voltage;
		update();
	}
	
	private void update(){
		for(SpeedController sc: scs){
			sc.set(negative? -voltage:voltage);
		}
	}
}
