/*
 * Controls.java
 * Author: Jeremiah Hanson
 * ------------------------------------------------------------------
 * This class is designed to more easily handle input from the controllers
 */

package systems.subsystems;

public class Controls {
	
	/*
	 * Button
	 * Author: Jeremiah Hanson
	 * -----------------------------------------------
	 * This enum is designed to make handling the button
	 * presses easier.
	 */
	public enum Button {
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
		
		// Constructor
		private Button(int index){
			this.index=index;
		}
		
		/*
		 * getIndex
		 * Author: Jeremiah Hanson
		 * --------------------------------------------
		 * Purpose: This is a getter for the associated 
		 * 	button number
		 * Returns: an int 
		 */
		public int getIndex(){
			return index;
		}
		
	}

}
