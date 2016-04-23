package wingman.modifiers.weapons;

import java.awt.Point;
import java.awt.Image;
import java.awt.image.BufferedImage;

import wingman.game.Bullet;
import wingman.game.Ship;
import wingman.modifiers.motions.SimpleMotion;
import wingman.GameWorld;


public class Mine extends AbstractRotatableWeapon {
    int strength;
    int mineCount;
    
    public Mine(){
        this(5,10);
    }
    
    public Mine(int reload){
        this(5,reload);
    }
    
    public Mine(int strength, int reload){
        super();
        this.reload = 10;
        this.strength = 40;
        this.mineCount = 3;
    }
    
    @Override
    public void fireWeapon(Ship theShip) {
        if (this.mineCount < 1) {
            return;
        }
        super.fireWeapon(theShip);
        --this.mineCount;
        Point location = theShip.getLocationPoint();
        Point speed = new Point(0, 0);
        Bullet bullet = new Bullet(location, speed, strength,
                                   new SimpleMotion(), theShip, 
                                  GameWorld.sprites.get("wall1"));
        bullets = new Bullet[1];
        bullets[0] = bullet;
                
        this.setChanged();
        
        this.notifyObservers();
    }

}
