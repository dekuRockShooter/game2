package wingman.ui;

import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import wingman.GameWorld;
import wingman.game.*;


/**
 * Represent the game's information bar.  The bar holds information such as
 * health, score, and remaining lives.
 */
public class ScoreBar extends InterfaceObject {
    PlayerShip player;
    String name;
    
    /**
     * Create a ScoreBar that is associated with a player.
     */
    public ScoreBar(PlayerShip player, Point location){
        this.player = player;
        this.location = location;
    }
    
    /**
     * Update information.
     */
    public void draw(Graphics g2, int x, int y){
        g2.setFont(new Font("Calibri", Font.PLAIN, 24));
        g2.setColor(Color.WHITE);
        g2.drawString(Integer.toString(player.getScore()),
                      location.x, location.y);
    }

}
