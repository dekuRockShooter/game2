package wingman.game.enemy;

import java.awt.Point;

import wingman.GameWorld;
import wingman.game.PowerUp;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;
import wingman.modifiers.weapons.AbstractWeapon;
import wingman.modifiers.weapons.SpreadWeapon;

public class PowerupEnemy extends Ship {
	public PowerupEnemy(int location){
		this(location, new Point(3,3));
	}
	
	public PowerupEnemy(int location, Point speed){
		this(location, speed, new SpreadWeapon());
	}
	
	public PowerupEnemy(int location, Point speed, AbstractWeapon weapon){
		super(location, speed, 20, GameWorld.sprites.get("enemy2"));
		this.weapon = weapon;
		
		motion = new SimpleMotion();
	}
	
	public void die(){
		GameWorld.getInstance().addPowerUp(new PowerUp(this));
		super.die();
	}
}