package wingman.modifiers.weapons;

import java.awt.Point;

import wingman.game.Bullet;
import wingman.game.Ship;
import wingman.GameWorld;
import wingman.modifiers.motions.SimpleMotion;


public class RotatableCanon extends AbstractRotatableWeapon {
    int strength;
    
    public RotatableCanon(){
        this(5,10);
    }
    
    public RotatableCanon(int reload){
        this(5,reload);
    }
    
    public RotatableCanon(int strength, int reload){
        super();
        this.reload = 30;
        this.strength = 50;
    }
    
    @Override
    public void fireWeapon(Ship theShip) {
        super.fireWeapon(theShip);
        Point location = theShip.getLocationPoint();
        Point offset = theShip.getGunLocation();
        location.x+=offset.x;
        location.y+=offset.y;
        Point speed = new Point((int)(25*Math.cos(angle)),
                                (int)(-25*Math.sin(angle)));
        
        Bullet bullet = new Bullet(location, speed, strength, new SimpleMotion(), theShip, GameWorld.sprites.get("canon"));
        bullets = new Bullet[1];
        bullets[0] = bullet;
                
        this.setChanged();
        
        this.notifyObservers();
    }

}
