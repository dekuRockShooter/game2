package wingman.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Observable;

import wingman.GameWorld;
import wingman.game.PlayerShip;
import wingman.game.Tank;
import wingman.game.Shell;
import wingman.modifiers.AbstractGameModifier;
import wingman.modifiers.motions.MenuController;

/**
 * Represents the main menu.  This is where the player starts the game or
 * exists from it.  This class observes AbstractGameModifier objects.
 */
public class GameMenu extends InterfaceObject {
    int selection;
    MenuController controller;
    boolean waiting;
    int yellowTitle;

    public GameMenu(){
        selection = 0;
        controller = new MenuController(this);
        waiting = true;
        this.yellowTitle = 0;
    }
    /**
     * Update the current selection.
     */
    public void draw(Graphics g2, int x, int y){
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, x, y);
        if (yellowTitle < 2) {
            g2.setColor(Color.YELLOW);
            ++this.yellowTitle;
        }
        else if (this.yellowTitle < 4) {
            g2.setColor(Color.ORANGE);
            ++this.yellowTitle;
        }
        else if (this.yellowTitle < 6) {
            g2.setColor(Color.RED);
            ++this.yellowTitle;
        }
        else {
            this.yellowTitle = 0;
        }
        g2.setFont(new Font("Calibri", Font.BOLD, 48));
        g2.drawString("TANK WARS!!!!", 200,150);
        g2.setFont(new Font("Calibri", Font.PLAIN, 24));
        if(selection==1)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        g2.drawString("Start", 200, 250);
        if(selection==2)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.WHITE);
        g2.drawString("Quit", 200, 350);
    }

    public void down(){
        if(selection<2)
            selection++;
    }

    public void up(){
        if(selection>0)
            selection--;
    }

    /**
     * Start the game according to the selection.  The game that starts
     * depends on if single or two player mode was selected.
     */
    public void applySelection(){
        /*
         * Initialize key controls, sprites, and the game world.
         */
        GameWorld world = GameWorld.getInstance();
        Dimension size = world.getSize();
        // One player.
        if(selection == 1){
            int[] controls = {KeyEvent.VK_LEFT,KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_SPACE};
            PlayerShip player = new Shell(new Point(240, size.height-80), new Point(6,6),GameWorld.sprites.get("player1"), controls, "Player 1");
            world.addPlayer(player);
        }
        // Quit.
        else{
            System.exit(0);
        }

        GameWorld.sound.play("Resources/strobe.mp3");

        controller.deleteObservers();
        world.removeKeyListener(controller);
        waiting=false;
    }

    public void update(Observable o, Object arg) {
        AbstractGameModifier modifier = (AbstractGameModifier) o;
        modifier.read(this);
    }

    public boolean isWaiting(){
        return this.waiting;
    }
}
