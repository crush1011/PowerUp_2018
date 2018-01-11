package systems;

import com.kauailabs.navx.frc.AHRS;

public class RNavX  implements RSensor<AHRS, Double>{
	
	double zeroAngle;
	
	AHRS navX;
	public RNavX(AHRS navX){
		this.navX=navX;
		zeroAngle=0;
	}
	
	public double getAngle(){
		return getRawAngle()-zeroAngle;
	}
	
	private double getRawAngle(){
		double c =navX.getFusedHeading();
		c= ((c%360) + 360)%360;
		return c;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		zeroAngle = getRawAngle();
	}

	@Override
	public Double getValue() {
		return getAngle();
	}

	@Override
	public AHRS getSensor() {
		return navX;
	}
}
