package wingman.modifiers.weapons;

import java.awt.Point;

import wingman.game.Bullet;
import wingman.game.BouncingBullet;
import wingman.game.Ship;
import wingman.GameWorld;
import wingman.modifiers.motions.SimpleMotion;


public class RotatableBouncer extends AbstractRotatableWeapon {
    int strength;

    public RotatableBouncer(){
        this(5,10);
    }

    public RotatableBouncer(int reload){
        this(5,reload);
    }

    public RotatableBouncer(int strength, int reload){
        super();
        this.reload = 20;
        this.strength = 10;
    }

    @Override
    public void fireWeapon(Ship theShip) {
        super.fireWeapon(theShip);
        Point location = theShip.getLocationPoint();
        Point offset = theShip.getGunLocation();
        location.x+=offset.x;
        location.y+=offset.y;
        Point speed = new Point((int)(8*Math.cos(angle)),
                                (int)(-8*Math.sin(angle)));

        Bullet bullet = new BouncingBullet(location, speed, strength, new SimpleMotion(), theShip, GameWorld.sprites.get("canon"));
        bullets = new Bullet[1];
        bullets[0] = bullet;

        this.setChanged();

        this.notifyObservers();
    }

}
