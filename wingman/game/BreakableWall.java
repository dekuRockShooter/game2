package wingman.game;

import java.awt.Image;
import java.awt.Point;

import wingman.GameWorld;
import wingman.modifiers.motions.MotionController;
import wingman.modifiers.weapons.AbstractWeapon;

/** Ships are things that have weapons and health */
public class BreakableWall extends GameObject {
    protected int health;
    
    public BreakableWall(Point location, int health, Image img){
        super(location, img);
        this.health = health;
    }
    
    public void damage(int damageDone){
        this.health -= damageDone;
        if(health<=0){
            this.die();
        }
        return;
    }
    
    public void die(){
        this.show=false;
        SmallExplosion explosion = new SmallExplosion(new Point(location.x,location.y));
        GameWorld.getInstance().addBackground(explosion);
    }
    
    public void collide(GameObject otherObject){
    }
    
    /* some setters and getters!*/
    public void setHealth(int health){
        this.health = health;
    }
    
    public int getHealth(){
        return health;
    }

    public boolean isDead(){
        return this.health == 0;
    }
}
