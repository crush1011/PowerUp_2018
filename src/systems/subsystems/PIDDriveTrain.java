package systems.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import systems.Systems;

public class PIDDriveTrain extends PIDSubsystem{
	
	public PIDDriveTrain(double p, double i, double d) {
		super(p, i, d);
		// TODO Auto-generated constructor stub
	}

	private Systems systems;
	


	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
