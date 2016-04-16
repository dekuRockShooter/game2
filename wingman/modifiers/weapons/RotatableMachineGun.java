package wingman.modifiers.weapons;

import java.awt.Point;

import wingman.game.Bullet;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;


public class RotatableMachineGun extends AbstractRotatableWeapon {
    int strength;
    
    public RotatableMachineGun(){
        this(5,10);
    }
    
    public RotatableMachineGun(int reload){
        this(5,reload);
    }
    
    public RotatableMachineGun(int strength, int reload){
        super();
        this.reload = 3;
        this.strength = 4;
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
        
        Bullet bullet = new Bullet(location, speed, strength, new SimpleMotion(), theShip);
        bullets = new Bullet[1];
        bullets[0] = bullet;
                
        this.setChanged();
        
        this.notifyObservers();
    }

}
