package wingman.game;

import java.awt.Point;
import java.awt.Image;
import java.awt.Rectangle;

import wingman.GameWorld;
import wingman.modifiers.motions.MotionController;

/*Bullets fired by player and enemy weapons*/
public class BouncingBullet extends Bullet {
    PlayerShip owner;
    boolean friendly;
    boolean leftHit;
    boolean bottomHit;
    Point origin;
    
    public BouncingBullet(Point location, Point speed, int strength, MotionController motion, GameObject owner){
        super(location, speed, strength, motion, owner);
        this.strength=strength;
        if(owner instanceof PlayerShip){
            this.owner = (PlayerShip) owner;
            this.friendly=true;
            this.setImage(GameWorld.sprites.get("explosion1_3"));
        }
        this.motion = motion;
        motion.addObserver(this);
        this.origin = location;
    }

    public BouncingBullet(Point location, Point speed, int strength,
                  MotionController motion, GameObject owner, Image img){
        this(location, speed, strength, motion, owner);
        //this.setImage(img);
        this.leftHit = false;
        this.bottomHit = false;
    }
    
    public PlayerShip getOwner(){
        return owner;
    }
    
    public boolean isFriendly(){
        if(friendly){
            return true;
        }
        return false;
    }

    @Override
    public void update(int w, int h) {
        location.x += speed.x;
        location.y += speed.y;
        if(location.y<-100 || location.y==h+100){
            this.show=false;
        }
    }

    @Override
    public boolean collision(GameObject otherObject) {
        if(location.intersects(otherObject.getLocation())){
            return true;
        }
        return false;
    }

    @Override
    public void collide(BackgroundObject otherObject) {
        Rectangle wall = otherObject.getLocation();
        Rectangle intersection = wall.intersection(location);
        if (speed.x == 0) {
            speed.y = -speed.y;
            return;
        }
        if (speed.y == 0) {
            speed.x = -speed.x;
            return;
        }
        if (intersection.width > intersection.height) {
            speed.y = -speed.y;
        }
        else {
            speed.x = -speed.x;
        }
        while (location.intersects(wall)) {
            location.x += speed.x;
            location.y += speed.y;
        }
    }
}
