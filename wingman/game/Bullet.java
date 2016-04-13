package wingman.game;

import java.awt.Point;

import wingman.GameWorld;
import wingman.modifiers.motions.MotionController;

/*Bullets fired by player and enemy weapons*/
public class Bullet extends MoveableObject {
	PlayerShip owner;
	boolean friendly;
	
	public Bullet(Point location, Point speed, int strength, MotionController motion, GameObject owner){
		super(location, speed, GameWorld.sprites.get("enemybullet1"));
		this.strength=strength;
		if(owner instanceof PlayerShip){
			this.owner = (PlayerShip) owner;
			this.friendly=true;
			this.setImage(GameWorld.sprites.get("bullet"));
		}
		this.motion = motion;
		motion.addObserver(this);
	}
	
	public PlayerShip getOwner(){
		return owner;
	}
	
	public boolean isFriendly(){
		if(friendly){
			return true;
		}
		return false;
	}
}
