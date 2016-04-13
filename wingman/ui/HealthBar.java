package wingman.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import wingman.GameWorld;
import wingman.game.*;


/**
 * Represent the game's information bar.  The bar holds information such as
 * health, score, and remaining lives.
 */
public class HealthBar extends InterfaceObject {
    PlayerShip player;
    String name;
    
    /**
     * Create a HealthBar that is associated with a player.
     */
    public HealthBar(PlayerShip player, String name){
        this.player = player;
        this.name = name;
    }
    
    /**
     * Update information.
     */
    public void draw(Graphics g2, int x, int y){
        if(player.getHealth()>80){
            g2.setColor(Color.GREEN);
        }
        else if(player.getHealth()>30){
            g2.setColor(Color.YELLOW);
        }
        else{
            g2.setColor(Color.RED);
        }
        g2.fillRect(player.getX(), player.getY(),
                    (int) Math.round(player.getHealth() / 2), 5);
    }

}
