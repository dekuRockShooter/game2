package wingman.game.enemy;

import java.awt.Point;

import wingman.GameWorld;
import wingman.game.Ship;
import wingman.modifiers.motions.CentralHorizontalHover;
import wingman.modifiers.weapons.BossWeapon;

public class Boss extends Ship {

	
	public Boss(Point location, Point speed, int strength){
		super(location, speed, strength, GameWorld.sprites.get("boss"));
		this.weapon = new BossWeapon();
		this.gunLocation = new Point(41,54);
		
		motion = new CentralHorizontalHover(30, 50);
	}
	
	// end the game when we kill the boss
	public void die(){
		super.die();
		GameWorld.getInstance().endGame(true);
	}
}
