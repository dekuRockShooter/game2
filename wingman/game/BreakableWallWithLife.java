package wingman.game;

import java.awt.Image;
import java.awt.Point;

import wingman.GameWorld;
import wingman.modifiers.motions.SimpleMotion;
import wingman.modifiers.weapons.AbstractWeapon;

/** Ships are things that have weapons and health */
public class BreakableWallWithLife extends BreakableWall {
    
    public BreakableWallWithLife(Point location, int health, Image img){
        super(location, health, img);
    }

    @Override
    public void collide(GameObject otherObject){
        if (!(otherObject instanceof Bullet)) {
            super.collide(otherObject);
        }
        GameWorld.getInstance().getPlayers().next().incrementLife();
    }
}
