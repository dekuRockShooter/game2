package wingman.game;

import java.awt.Point;
import java.awt.Image;
import java.util.Observable;

import wingman.GameWorld;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.SimpleMotion;
import wingman.modifiers.weapons.AbstractWeapon;

/* PowerUp extends ship so that it can hold a weapon to give to player*/
public class PowerUp extends Ship {
    public PowerUp(Ship theShip){
        super(theShip.getLocationPoint(), theShip.getSpeed(), 1, GameWorld.sprites.get("powerup"));
        this.motion = new SimpleMotion();
        this.motion.addObserver(this);
        this.weapon = theShip.getWeapon();
    }
    
    public PowerUp(int location, int health, AbstractWeapon weapon){
        super(new Point(location, -100),new Point(0,2), health, GameWorld.sprites.get("powerup"));
        this.motion = new SimpleMotion();
        this.motion.addObserver(this);
        this.weapon = weapon;
    }

    public PowerUp(int location, int health, AbstractWeapon weapon, Image img){
        super(new Point(location, -100),new Point(0,2), health, img);
        this.motion = new SimpleMotion();
        this.motion.addObserver(this);
        this.weapon = weapon;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        AbstractGameModifier modifier = (AbstractGameModifier) o;
        modifier.read(this);
    }
    
    public void die(){
        this.show=false;
        weapon.deleteObserver(this);
        motion.deleteObserver(this);
        GameWorld.getInstance().removeClockObserver(motion);
    }

}
