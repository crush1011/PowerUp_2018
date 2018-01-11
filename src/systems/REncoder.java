package systems;

import java.util.function.Function;

import edu.wpi.first.wpilibj.Encoder;

public class REncoder implements RSensor<Encoder, Double>{
	private Encoder e;
	private double startPos;
	private Function<Double, Double> conversion;
	private boolean negative;
	
	public REncoder(Encoder e, Function<Double, Double> conversion){
		this(e);
		this.conversion=conversion;
	}
	
	public REncoder(Encoder e){
		this.e=e;
		conversion = (val) ->{
			return val;
		};
		negative=false;
	}
	
	public void setNegative(boolean b){
		this.negative=b;
	}
	
	public Double getValue(){
		double value = 0;
		if(e!=null){
			value=e.getDistance();
			value-=startPos;
			value = negative? -value:value;
		}
		
		return conversion.apply(value);
		
	}
	
	public void reset(){
		startPos = e.getDistance();
	}
	

	@Override
	public Encoder getSensor() {
		// TODO Auto-generated method stub
		return e;
	}
	
	
}
