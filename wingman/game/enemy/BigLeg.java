package wingman.game.enemy;

import java.awt.Point;
import java.awt.Image;

import wingman.GameWorld;
import wingman.game.Ship;
import wingman.game.GameObject;
import wingman.modifiers.motions.CentralHorizontalHover;
import wingman.modifiers.weapons.SpreadWeapon;
import wingman.modifiers.weapons.NullWeapon;

public class BigLeg extends Ship {
    public BigLeg(Point location, Point speed, Image img){
        super(location, speed, 20, img);
        strength=10;
        health=10;
        weapon = new NullWeapon();
        motion = new CentralHorizontalHover(1000000, location.y);
    }

    @Override
    public void collide(GameObject otherObject) {
        speed.x = -speed.x;
        while (collision(otherObject)) {
            location.x = location.x + speed.x;
        }
    }
}
