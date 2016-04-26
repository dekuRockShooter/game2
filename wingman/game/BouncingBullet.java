package wingman.game;

import java.awt.Point;
import java.awt.Image;
import java.awt.Rectangle;

import wingman.GameWorld;
import wingman.modifiers.motions.MotionController;

/*Bullets fired by player and enemy weapons*/
public class BouncingBullet extends Bullet {
    boolean friendly;
    Point origin;
    
    public BouncingBullet(Point location, Point speed, int strength, MotionController motion, GameObject owner){
        super(location, speed, strength, motion, owner);
        this.strength=strength;
        if(owner instanceof PlayerShip){
            this.owner = (PlayerShip) owner;
            this.friendly=true;
            this.setImage(GameWorld.sprites.get("bullet"));
        }
        this.motion = motion;
        motion.addObserver(this);
        this.origin = location;
    }

    public BouncingBullet(Point location, Point speed, int strength,
                  MotionController motion, GameObject owner, Image img){
        this(location, speed, strength, motion, owner);
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
        this.impact(otherObject);
    }

    @Override
    public void collide(BreakableWall otherObject) {
        this.impact(otherObject);
    }

    @Override
    public void collide(GameObject otherObject) {
        this.impact(otherObject);
    }

    @Override
    public void collide(PlayerShip otherObject) {
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
        double x = intersection.x - wall.x;
        speed.y = -speed.y;
        if (x < (wall.width / 6)) {
            speed.x = -(int)(5 * Math.cos(Math.PI / 6));
        }
        else if (x < (wall.width / 4)) {
            speed.x = -(int)(5 * Math.cos(Math.PI / 4));
        }
        else if (x < (wall.width / 2)) {
            speed.x = -(int)(5 * Math.cos(Math.PI / 3));
        }
        else if (x < (4*wall.width / 6)) {
            speed.x = (int)(5 * Math.cos(Math.PI / 3));
        }
        else if (x < (3*wall.width / 4)) {
            speed.x = (int)(5 * Math.cos(Math.PI / 4));
        }
        else {
            speed.x = (int)(5 * Math.cos(Math.PI / 6));
        }
        while (location.intersects(wall)) {
            location.x += speed.x;
            location.y += speed.y;
        }
    }

    private void impact(GameObject otherObject) {
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
