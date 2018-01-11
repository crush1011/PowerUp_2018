package systems;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Joystick;

public class Controls{


	public HashMap<ControlButtons, Boolean> driverButtons;
	public HashMap<ControlButtons, Boolean> operatorButtons;
	public HashMap<ControlAxis, Double> driverAxis;
	public HashMap<ControlAxis, Double> operatorAxis;
	
	public enum ControlButtons{
		A(1),
		B(2),
		X(3),
		Y(4),
		RIGHT_BUMPER(6),
		LEFT_BUMPER(5),
		BACK(7),
		START(8),
		LEFTJOY(9),
		RIGHTJOY(10);
		
		int index;
		private ControlButtons(int index){
			this.index=index;
		}
		
		public int getIndex(){
			return index;
		}
	}
	
	public enum ControlAxis{
		LEFT_X(0),
		LEFT_Y(1),
		RIGHT_X(4),
		RIGHT_Y(5),
		LEFT_TRIGGER(2),
		RIGHT_TRIGGER(3);
		
		int index;
		private ControlAxis(int index){
			this.index=index;
		}
		
		public int getIndex(){
			return index;
		}
	}

	
	
	Joystick driverJoystick, operatorJoystick;
	public Controls(Joystick driverJoystick, Joystick operatorJoystick){
		this.driverJoystick=driverJoystick;
		this.operatorJoystick=operatorJoystick;
		
		driverButtons = new HashMap<>();
		driverAxis = new HashMap<>();
		operatorButtons = new HashMap<>();
		operatorAxis = new HashMap<>();
	}
	
	public void update(){
		updateButtonHash(driverButtons, driverJoystick);
		updateButtonHash(operatorButtons, operatorJoystick);
		updateAxisHash(driverAxis, driverJoystick);
		updateAxisHash(operatorAxis, operatorJoystick);	
	}
	
	private static void updateButtonHash(HashMap<ControlButtons, Boolean> hash, Joystick stick){
		hash.put(ControlButtons.A, stick.getRawButton(ControlButtons.A.getIndex()));
		hash.put(ControlButtons.B, stick.getRawButton(ControlButtons.B.getIndex()));
		hash.put(ControlButtons.X, stick.getRawButton(ControlButtons.X.getIndex()));
		hash.put(ControlButtons.Y, stick.getRawButton(ControlButtons.Y.getIndex()));
		hash.put(ControlButtons.BACK, stick.getRawButton(ControlButtons.BACK.getIndex()));
		hash.put(ControlButtons.START, stick.getRawButton(ControlButtons.START.getIndex()));
		hash.put(ControlButtons.LEFT_BUMPER, stick.getRawButton(ControlButtons.LEFT_BUMPER.getIndex()));
		hash.put(ControlButtons.RIGHT_BUMPER, stick.getRawButton(ControlButtons.RIGHT_BUMPER.getIndex()));
		hash.put(ControlButtons.LEFTJOY, stick.getRawButton(ControlButtons.LEFTJOY.getIndex()));
		hash.put(ControlButtons.RIGHTJOY, stick.getRawButton(ControlButtons.RIGHTJOY.getIndex()));
	}
	
	private static void updateAxisHash(HashMap<ControlAxis,Double> hash, Joystick stick){
		hash.put(ControlAxis.LEFT_TRIGGER, stick.getRawAxis(ControlAxis.LEFT_TRIGGER.getIndex()));
		hash.put(ControlAxis.RIGHT_TRIGGER, stick.getRawAxis(ControlAxis.RIGHT_TRIGGER.getIndex()));
		hash.put(ControlAxis.LEFT_X, stick.getRawAxis(ControlAxis.LEFT_X.getIndex()));
		hash.put(ControlAxis.LEFT_Y, stick.getRawAxis(ControlAxis.LEFT_Y.getIndex()));
		hash.put(ControlAxis.RIGHT_X, stick.getRawAxis(ControlAxis.RIGHT_X.getIndex()));
		hash.put(ControlAxis.RIGHT_Y, stick.getRawAxis(ControlAxis.RIGHT_Y.getIndex()));
	}
}
