package systems.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;
import systems.Subsystem;

/**
 * LED.java<p>
 * controls LEDs by using an ultrasonic sensor to detect distance to 
 * a cube
 * @author Jeremiah Hanson
 */
public class LED implements Subsystem{

	private Ultrasonic ultra;
	private Solenoid leds;
	private double range;
	private int count;
	
	public LED(Ultrasonic ultra) {
		this.ultra = ultra;
		leds = new Solenoid(0);
		ultra.setAutomaticMode(true);
		count = 0;
	}
	
	/**
	 * called every cycle to control the LEDs
	 */
	@Override
	public void update() {
		range = ultra.getRangeInches();
		
		if (range < 4.0) {
			solidLEDs();
		} else {
			offLEDs();
		}
		
		/*if (count == 20) {
			System.out.println("LED.update(): range is " + range);
			count = 0;
		}
		count++;*/
	}
	
	/**
	 * turn the LEDs to on 
	 */
	private void solidLEDs() {
		leds.set(true);
	}
	
	/**
	 * turn the LEDs to on 
	 */
	private void offLEDs() {
		leds.set(false);
	}

	/**
	 * sends data to the smart dashboard if used
	 */
	@Override
	public void toSmartDashboard() {
		
	}

}
