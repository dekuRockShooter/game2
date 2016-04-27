package wingman.game;

import java.awt.Image;
import java.awt.Point;

import wingman.GameWorld;
import wingman.modifiers.motions.SimpleMotion;
import wingman.modifiers.weapons.AbstractWeapon;

/** Ships are things that have weapons and health */
public class BreakableWallWithPop extends BreakableWall {
    
    public BreakableWallWithPop(Point location, int health, Image img){
        super(location, health, img);
    }

    @Override
    public void collide(GameObject otherObject){
        if (!(otherObject instanceof Bullet)) {
            super.collide(otherObject);
        }
        Point location = otherObject.getLocationPoint();
        location.y += 40;
        Point speed = new Point(1, 4);

        Bullet bullet = new BouncingBullet(location, speed, 10, 
                new SimpleMotion(), GameWorld.getInstance().getPlayers().next(),
                GameWorld.sprites.get("bullet"));
        GameWorld.getInstance().addBullet(bullet);
    }
}
