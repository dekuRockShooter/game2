package wingman.modifiers.weapons;

import wingman.GameWorld;
import wingman.game.Bullet;
import wingman.game.PlayerShip;
import wingman.game.Ship;
import wingman.modifiers.AbstractGameModifier;

/*Weapons are fired by motion controllers on behalf of players or ships
 * They observe motions and are observed by the Game World
 */
public abstract class AbstractWeapon extends AbstractGameModifier {
	Bullet[] bullets;
	boolean friendly;
	int lastFired=0, reloadTime;
	protected int direction;
	public int reload = 5;
	
	public AbstractWeapon(){
		super();
		this.addObserver(GameWorld.getInstance());
	}
	
	public void fireWeapon(Ship theShip){
		if(theShip instanceof PlayerShip){
			direction = 1;
		}
		else{
			direction = -1;
		}
	}
	
	/* read is called by Observers when they are notified of a change */
	public void read(Object theObject) {
		GameWorld world = (GameWorld) theObject;
		world.addBullet(bullets);	
	}
	
	public void remove(){
		this.deleteObserver(GameWorld.getInstance());
	}
}
