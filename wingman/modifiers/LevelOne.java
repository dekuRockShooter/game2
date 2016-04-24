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
public class LevelOne extends Level {
    int lastPowerUp;
    int lastSpawn;
    int endgameDelay = 100; // don't immediately end on game end
    
    /*Constructor sets up arrays of enemies in a LinkedHashMap*/
    public LevelOne(int w, int h){
        super(w, h);
        mapFilename = "wingman/Chapter11/level_1_map";
    }

    /**
     * Create waves of enemies and the time at which they appear.
     */
    public void load(){
        GameWorld world = GameWorld.getInstance();
        lastPowerUp = GameWorld.getInstance().getTime();
        lastSpawn = GameWorld.getInstance().getTime();
        Ship[] wave = {
                new HoverEnemy(200, new Point(1, 1)),
                new HoverEnemy(200, new Point(1, 1))
        };
        world.addEnemies(wave);
        lastPowerUp=world.getTime();
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
        if(world.isGameOver()){
            if(endgameDelay<=0){
                GameWorld.getInstance().removeClockObserver(this);
                GameWorld.getInstance().finishGame();
            } else endgameDelay--;
        }
    }
}
