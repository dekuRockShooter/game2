package wingman.modifiers;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import wingman.GameWorld;
import wingman.game.*;
import wingman.game.enemy.Bomber;
import wingman.game.enemy.Boss;
import wingman.game.enemy.HoverEnemy;
import wingman.game.enemy.PowerupEnemy;
import wingman.game.enemy.PulseEnemy;
import wingman.game.enemy.SimpleEnemy;
import wingman.modifiers.weapons.SpreadWeapon;

/*This is where enemies are introduced into the game according to a timeline*/
/**
 * Creates sets of enemies that appear at a certain time.  This class defines
 * sets of enemies as well as a time sequence for their appearance.
 */
public class TankLevel extends Level {
    int lastPowerUp;
    int lastSpawn;
    int endgameDelay = 100; // don't immediately end on game end
    
    /*Constructor sets up arrays of enemies in a LinkedHashMap*/
    public TankLevel(int w, int h){
        super(w, h);
    }

    /**
     * Create waves of enemies and the time at which they appear.
     */
    public void load(){
        lastPowerUp = GameWorld.getInstance().getTime();
        lastSpawn = GameWorld.getInstance().getTime();
    }
    
    public void read(Object theObject){
    }

    /*Level observes GameClock and updates on every tick*/
    /**
     * Add enemies and powerups.  Enemies are added after a certain time
     * has elapsed, or if there is currently a low number of them.  Powerups
     * are added after some time has elapsed.
     */
    @Override
    public void update(Observable o, Object arg) {
        GameWorld world = GameWorld.getInstance();
        if(world.getTime() - lastPowerUp > 7000){
            Ship[] wave = {
                    new SimpleEnemy(lastPowerUp % 700, new Point(-1, 5), 1, 16),
                    new PowerupEnemy(lastPowerUp % 800, new Point(1, 4)),
            };
            world.addEnemies(wave);
            world.addRandomPowerUp();
            lastPowerUp=world.getTime();
        }
        if(world.getTime() - lastSpawn > 6000){
            Ship[] wave = {
                    new SimpleEnemy(lastSpawn % 800, new Point(1, 4), 1, 20),
                    new PowerupEnemy(lastSpawn % 700, new Point(-1, 3)),
            };
            world.addEnemies(wave);
            lastSpawn = world.getTime();
        }
        if(world.isGameOver()){
            if(endgameDelay<=0){
                GameWorld.getInstance().removeClockObserver(this);
                GameWorld.getInstance().finishGame();
            } else endgameDelay--;
        }
    }
}
