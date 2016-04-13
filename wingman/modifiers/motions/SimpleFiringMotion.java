package wingman.modifiers.motions;

import wingman.GameWorld;
import wingman.game.Ship;

public class SimpleFiringMotion extends SimpleMotion {
	public SimpleFiringMotion(int interval){
		super();
		this.fireInterval = interval;
	}
	
	public void read(Object theObject){
		super.read(theObject);
		
		Ship ship = (Ship) theObject;
		
		if(GameWorld.getInstance().getFrameNumber()%fireInterval==0){
			ship.fire();
		}
	}

}
