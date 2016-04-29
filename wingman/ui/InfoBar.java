package wingman.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import wingman.GameWorld;
import wingman.game.*;


public class InfoBar extends InterfaceObject {
    PlayerShip player;
    String name;
    
    public InfoBar(PlayerShip player, String name){
        this.player = player;
        this.name = name;
    }
    
    public void draw(Graphics g2, int x, int y){
        g2.setFont(new Font("Calibri", Font.PLAIN, 24));
        g2.setColor(Color.RED);
        g2.drawString("Lives: " + Integer.toString(player.getLives()), 50,
                      player.getLocation().y + 2*player.getLocation().height);
        //g2.drawString(Integer.toString(player.getScore()), x+250, y-8);
    }

}
