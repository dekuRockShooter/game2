package wingman.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

import wingman.GameWorld;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.InputController;
import wingman.modifiers.weapons.NullWeapon;

public class Shell extends PlayerShip implements Observer {
    public Shell(Point location, Point speed, Image img, int[] controls, String name) {
        super(location,speed,img, controls, name);
        weapon = new NullWeapon();
        lives = 3;
    }

    @Override
    public void update(int w, int h) {
        if((location.x>0 || right==1) && (location.x<w-width || left==1)){
            location.x+=(right-left)*speed.x;
        }
    }

    @Override
    public void die() {
        lives = lives - 1;
    }

    @Override
    public void reset(){
        super.reset();
        this.weapon = new NullWeapon();
    }

    @Override
    public boolean isDead(){
        return lives < 1;
    }
}
