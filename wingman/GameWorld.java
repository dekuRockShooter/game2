package wingman;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.awt.Color;

import javax.swing.*;

import wingman.game.*;
import wingman.modifiers.*;
import wingman.modifiers.motions.MotionController;
import wingman.modifiers.weapons.*;
import wingman.ui.*;

// extending JPanel to hopefully integrate this into an applet
// but I want to separate out the Applet and Application implementations
public final class GameWorld extends JPanel implements Runnable, Observer {

    private Thread thread;
    
    // GameWorld is a singleton class!
    private static final GameWorld game = new GameWorld();
    public static final GameSounds sound = new GameSounds();
    public static final GameClock clock = new GameClock();
    public Level level;
    GameMenu menu;
   
    private BufferedImage bimg;
    int score = 0, life = 4;
    Point speed = new Point(0,0);
    Random generator = new Random();
    int sizeX, sizeY;
    private int[] leftWindowCoords;
    private int[] rightWindowCoords;
    
    /*Some ArrayLists to keep track of game things*/
    private ArrayList<BackgroundObject> background;
    private ArrayList<Ship> enemies;
    private ArrayList<Bullet> friendlyBullets, enemyBullets;
    private ArrayList<PlayerShip> players, playersInPlay;
    private ArrayList<InterfaceObject> ui;
    private ArrayList<Ship> powerups;
    private ArrayList<BackgroundObject> collidableBackgrounds;
    private ArrayList<BreakableWall> breakableWalls;
    private ArrayList<Integer> shakeLeft;
    private ArrayList<Integer> shakeRight;
    private Iterator<Integer> shakeLeftIter;
    /*
     * Stores the shake array of a player.  This is updated during a collision
     * and used to shake the screen of player x (shake[x] is the shake array of
     * player number x).
     */
    private HashMap<String, Iterator<Integer>> shake;
    
    public static HashMap<String, Image> sprites;
    public static HashMap<String, MotionController> motions = new HashMap<String, MotionController>();

    // is player still playing, did they win, and should we exit
    boolean gameOver, gameWon, gameFinished;
    ImageObserver observer;
        
    // constructors makes sure the game is focusable, then
    // initializes a bunch of ArrayLists
    private GameWorld(){
        this.setFocusable(true);
        background = new ArrayList<BackgroundObject>();
        enemies = new ArrayList<Ship>();
        friendlyBullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
        players = new ArrayList<PlayerShip>();
        playersInPlay = new ArrayList<PlayerShip>();
        ui = new ArrayList<InterfaceObject>();
        powerups = new ArrayList<Ship>();
        collidableBackgrounds = new ArrayList<BackgroundObject>();
        breakableWalls = new ArrayList<BreakableWall>();
        this.shakeLeft = new ArrayList<Integer>();
        this.shakeRight = new ArrayList<Integer>();
        this.shakeLeftIter = this.shakeLeft.iterator();
        leftWindowCoords = new int[2];
        leftWindowCoords[0] = 0;
        leftWindowCoords[1] = 0;
        rightWindowCoords = new int[2];
        shake = new HashMap<String, Iterator<Integer>>();
        
        sprites = new HashMap<String,Image>();
    }
    
    /* This returns a reference to the currently running game*/
    public static GameWorld getInstance(){
        return game;
    }

    /*Game Initialization*/
    public void init() {
        int charIdx = 0;
        int xCoord = 0;
        int yCoord = 0;
        int imgWidth = 0;
        int imgHeight = 0;
        String line;
        Image img;
        Point point;
        Path file = Paths.get("wingman/Chapter11/map_layout");
        Charset charset = Charset.forName("US-ASCII");

        setBackground(Color.white);
        loadSprites();
        level = new TankLevel(this.sizeX,this.sizeY);
        clock.addObserver(level);
        level.addObserver(this);
        
        gameOver = false;
        observer = this;
        menu = new GameMenu();

        point = new Point(0,0);
        addBackground(new Background(sizeX,sizeY,speed, sprites.get("floor")));
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            while ((line = reader.readLine()) != null) {
                while (charIdx < line.length()) {
                    point = new Point(xCoord, yCoord);
                    char curTile = line.charAt(charIdx);
                    if (curTile == '1') {
                        img = this.sprites.get("wall1");
                        imgHeight = img.getHeight(this);
                        imgWidth = img.getWidth(this);
                        Island island = new Island(point,
                                                   speed,
                                                   img,
                                                   generator);
                        this.addBackground(island);
                        this.collidableBackgrounds.add(island);
                    }
                    else if (curTile == '2') {
                        img = this.sprites.get("wall2");
                        imgHeight = img.getHeight(this);
                        imgWidth = img.getWidth(this);
                        BreakableWall island = 
                            new BreakableWall(point, 10, img);
                        this.breakableWalls.add(island);
                        //collidableBackgrounds.add(island);
                    }
                    xCoord = xCoord + imgWidth;
                    ++charIdx;
                }
                yCoord = yCoord + imgHeight;
                xCoord = 0;
                charIdx = 0;
            }
        }
        catch (IOException x) {
        }
    }
    
    /*Functions for loading image resources*/
    private void loadSprites(){     
        sprites.put("wall1", getSprite("Chapter11/Blue_wall1.png"));
        sprites.put("wall2", getSprite("Chapter11/Blue_wall2.png"));
        sprites.put("floor", getSprite("Chapter11/Background.png"));
        
        sprites.put("bullet", getSprite("Resources/enemybullet1.png"));
        sprites.put("enemybullet1", getSprite("Resources/enemybullet1.png"));
        sprites.put("canon", getSprite("Chapter11/canonBullet.png"));
        
        sprites.put("player1", getSprite("Chapter11/Tank1.png"));
        sprites.put("player2", getSprite("Chapter11/Tank2.png"));
        sprites.put("player3", getSprite("Resources/player3.png"));
        sprites.put("enemy1", getSprite("Resources/enemy1.png"));
        sprites.put("enemy2", getSprite("Resources/enemy2.png"));
        sprites.put("enemy3", getSprite("Resources/enemy3.png"));
        sprites.put("bouncerPWUP", getSprite("Chapter11/BouncerPowerup.png"));
        sprites.put("machinegunPWUP", getSprite("Chapter11/MachineGunPowerup.png"));
        sprites.put("canonPWUP", getSprite("Chapter11/CanonPowerup.png"));
        sprites.put("minePWUP", getSprite("Chapter11/MinePowerup.png"));
        sprites.put("planePWUP", getSprite("Resources/life2.png"));
        
        sprites.put("explosion1_1", getSprite("Resources/explosion1_1.png"));
        sprites.put("explosion1_2", getSprite("Resources/explosion1_2.png"));
        sprites.put("explosion1_3", getSprite("Resources/explosion1_3.png"));
        sprites.put("explosion1_4", getSprite("Resources/explosion1_4.png"));
        sprites.put("explosion1_5", getSprite("Resources/explosion1_5.png"));
        sprites.put("explosion1_6", getSprite("Resources/explosion1_6.png"));
        sprites.put("explosion2_1", getSprite("Resources/explosion2_1.png"));
        sprites.put("explosion2_2", getSprite("Resources/explosion2_2.png"));
        sprites.put("explosion2_3", getSprite("Resources/explosion2_3.png"));
        sprites.put("explosion2_4", getSprite("Resources/explosion2_4.png"));
        sprites.put("explosion2_5", getSprite("Resources/explosion2_5.png"));
        sprites.put("explosion2_6", getSprite("Resources/explosion2_6.png"));
        sprites.put("explosion2_7", getSprite("Resources/explosion2_7.png"));
        
        sprites.put("gameover", getSprite("Resources/gameover.png"));
        sprites.put("powerup", getSprite("Resources/powerup.png"));
    }
    
    public Image getSprite(String name) {
        URL url = GameWorld.class.getResource(name);
        Image img = java.awt.Toolkit.getDefaultToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
    
    
    /********************************
     *  These functions GET things  *
     *      from the game world     *
     ********************************/
    
    public int getFrameNumber(){
        return clock.getFrame();
    }
    
    public int getTime(){
        return clock.getTime();
    }
    
    public void removeClockObserver(Observer theObject){
        clock.deleteObserver(theObject);
    }
    
    public ListIterator<BackgroundObject> getBackgroundObjects(){
        return background.listIterator();
    }
    
    public ListIterator<PlayerShip> getPlayers(){
        return playersInPlay.listIterator();
    }
    
    public ListIterator<Bullet> getFriendlyBullets(){
        return friendlyBullets.listIterator();
    }
    
    public ListIterator<Bullet> getEnemyBullets(){
        return enemyBullets.listIterator();
    }
    
    public ListIterator<Ship> getEnemies(){
        return enemies.listIterator();
    }
    
    public int countEnemies(){
        return enemies.size();
    }
    
    public int countPlayers(){
        return players.size();
    }
    
    public void setDimensions(int w, int h){
        this.sizeX = w;
        this.sizeY = h;
    }
    
    /********************************
     *  These functions ADD things  *
     *      to the game world       *
     ********************************/
    
    public void addBullet(Bullet...newObjects){
        for(Bullet bullet : newObjects){
            //if(bullet.isFriendly())
                //friendlyBullets.add(bullet);
            //else
                enemyBullets.add(bullet);
        }
    }
    
    public void addPlayer(PlayerShip...newObjects){
        for(PlayerShip player : newObjects){
            players.add(player);
            playersInPlay.add(player);
            ui.add(new HealthBar(player, Integer.toString(players.size())));
            shake.put(player.getName(), null);
        }
        ui.add(new ScoreBar(playersInPlay.get(0),
               new Point(0,0)));
        ui.add(new ScoreBar(playersInPlay.get(1),
               new Point((this.sizeX / 2) + 20, 20)));
    }

    /**
     * Update a player's shake array.
     */
    public void setShake(String playerName, Iterator<Integer> iter) {
        this.shake.put(playerName, iter);
    }
    
    // add background items (islands)
    public void addBackground(BackgroundObject...newObjects){
        for(BackgroundObject object : newObjects){
            background.add(object);
        }
    }
    
    // add power ups to the game world
    public void addPowerUp(Ship powerup){
        powerups.add(powerup);
    }
    
    public void addRandomPowerUp(){
        // rapid fire weapon or pulse weapon
        int randInt = generator.nextInt(20) % 5;
        if(randInt == 0) {
            powerups.add(new PowerUp(generator.nextInt(sizeX), 1,
                         new RotatableMachineGun(),
                         sprites.get("machinegunPWUP")));
        }
        else if (randInt == 1) {
            powerups.add(new PowerUp(generator.nextInt(sizeX), 1,
                                     new RotatableCanon(),
                                     sprites.get("canonPWUP")));
        }
        else if (randInt == 2) {
            powerups.add(new PowerUp(generator.nextInt(sizeX), 1,
                                     new RotatableBouncer(),
                                     sprites.get("bouncerPWUP")));
        }
        else if (randInt == 3) {
            powerups.add(new PowerUp(generator.nextInt(sizeX), 1,
                                     new Mine(),
                                     sprites.get("minePWUP")));
        }
        else if (randInt == 4) {
            powerups.add(new PowerUp(generator.nextInt(sizeX), 1,
                                     new RotatablePlaneCanon(),
                                     sprites.get("planePWUP")));
        }
    }
    
    // add enemies to the game world
    public void addEnemies(Ship...newObjects){
        for(Ship enemy : newObjects){
            enemies.add(enemy);
            enemy.start();
        }
    }
    
    public void addClockObserver(Observer theObject){
        clock.addObserver(theObject);
    }
    
    // this is the main function where game stuff happens!
    // each frame is also drawn here
    public void drawFrame(int w, int h, Graphics2D g2) {
        int winWidth = w;
        int winHeight = h;
        w = sizeX;
        h = sizeY;
        ListIterator<?> iterator = getBackgroundObjects();
        if (menu.isWaiting()){
            menu.draw(g2, w, h);
        }
        else if (!gameFinished) {                        

            // Draw some explosions.
            if ((this.getTime() % 100) == 0) {
                int x = generator.nextInt(100);
                int y = generator.nextInt(100);
                if ((x % 2) == 0) {
                    x = -x;
                }
                if ((y % 2) == 0) {
                    y = -y;
                }
                this.addBackground(new SmallExplosion(new Point(
                                this.playersInPlay.get(0).getX() + x, 
                                this.playersInPlay.get(0).getY() + y)));
                this.addBackground(new SmallExplosion(new Point(
                                this.playersInPlay.get(1).getX() - y, 
                                this.playersInPlay.get(1).getY() - x)));
            }

            while(iterator.hasNext()){
                BackgroundObject obj = (BackgroundObject) iterator.next();
                obj.update(w, h);
                if(obj.getY()>h || !obj.show){
                    iterator.remove();
                }
                obj.draw(g2, this);
            }
            for (BreakableWall breakable : this.breakableWalls) {
                breakable.draw(g2, this);
            }
            PlayerShip player1 = this.playersInPlay.get(0);
            PlayerShip player2 = this.playersInPlay.get(1);
            for (PlayerShip curPlayer : this.playersInPlay) {
                for (BackgroundObject bg : this.collidableBackgrounds) {
                    if(curPlayer.collision(bg)){
                        curPlayer.collide(bg);
                    }
                }
                for (BreakableWall breakable : this.breakableWalls) {
                    if(curPlayer.collision(breakable)){
                        curPlayer.collide(breakable);
                    }
                }
            }
            if (player1.collision(player2)) {
                player1.collide(player2);
                player2.collide(player1);
            }
            
            // remove stray enemy bullets and draw
            Iterator<Bullet> bulletIter = getEnemyBullets();
            while(bulletIter.hasNext()){
                Bullet bullet = (Bullet) bulletIter.next();
                ListIterator<PlayerShip> players = getPlayers();
                while(players.hasNext()){
                    PlayerShip player = players.next();
                    if(bullet.collision(player) && player.respawnCounter<=0 &&
                       bullet.getOwner() != player){
                        player.collide(bullet);
                        bullet.collide(player);
                        if (bullet.isDead())
                            bulletIter.remove();
                        if(player.isDead()){
                            bullet.getOwner().incrementScore(1);
                        }
                    }
                }
                for (BackgroundObject bg : this.collidableBackgrounds) {
                    if (bullet.collision(bg)) {
                        bullet.collide(bg);
                        if (bullet.isDead())
                            bulletIter.remove();
                    }
                }
                Iterator<BreakableWall> breakableIter;
                breakableIter = this.breakableWalls.iterator();
                BreakableWall breakable;
                while (breakableIter.hasNext()) {
                    breakable = breakableIter.next();
                    if(bullet.collision(breakable)){
                        bullet.collide(breakable);
                        if (bullet.isDead())
                            bulletIter.remove();
                        breakable.damage(bullet.getStrength());
                        if (breakable.isDead()) {
                            breakableIter.remove();
                        }
                    }
                }
                if(bullet.getY()>h+10 || bullet.getY()<-10){
                    bulletIter.remove();
                }
                bullet.draw(g2, this);
            }

            // remove stray friendly bullets and draw
            iterator = getFriendlyBullets();
            while(iterator.hasNext()){
                Bullet obj = (Bullet) iterator.next();
                //obj.update(w, h);
                if(obj.getY()>h+10 || obj.getY()<-10){
                    iterator.remove();
                }
                obj.draw(g2, this);
            }
            
            // update players and draw
            iterator = getPlayers();
            while(iterator.hasNext()){
                PlayerShip player = (PlayerShip) iterator.next();
                player.update(w, h);
                player.draw(g2, this);
            }
            for (Ship enemy : this.enemies) {
                enemy.draw(g2, this);
            }
            
            // powerups
            iterator = powerups.listIterator();
            while(iterator.hasNext()){
                Ship powerup = (Ship) iterator.next();
                ListIterator<PlayerShip> players =  getPlayers();
                while(players.hasNext()){
                    PlayerShip player = players.next();
                    if(powerup.collision(player)){
                        AbstractRotatableWeapon weapon = (AbstractRotatableWeapon)powerup.getWeapon();
                        player.setWeapon(weapon);
                        powerup.die();
                        iterator.remove();
                    }
                }
                powerup.draw(g2, this);
            }
            
            // interface stuff
            iterator = ui.listIterator();
            int offset = 0;
            while(iterator.hasNext()){
                InterfaceObject object = (InterfaceObject) iterator.next();
                object.draw(g2, offset, h);
                offset += 300;
            }
        }
        // end game stuff
        else{
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Calibri", Font.PLAIN, 24));
            if(!gameWon){
                g2.drawImage(sprites.get("gameover"), w/3-50, h/2, null);
            }
            else{
                g2.drawImage(sprites.get("youwon"), sizeX/3, 100, null);
            }
            g2.drawString("Score", sizeX/3, 400);
            int i = 1;
            for(PlayerShip player : this.playersInPlay){
                g2.drawString(player.getName() + ": " + Integer.toString(player.getScore()), sizeX/3, 375+50*i);
                i++;
            }
        }
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    private void paintWindow(int playerNum) {
        int[] windowCoords;
        if (playerNum == 0) {
            windowCoords = this.leftWindowCoords;
        }
        else {
            windowCoords = this.rightWindowCoords;
        }
        int H = getSize().height;
        int W = getSize().width / 2;
        int R = this.sizeX;
        int B = this.sizeY;
        int x = this.playersInPlay.get(playerNum).getX();
        int y = this.playersInPlay.get(playerNum).getY();
        Iterator<Integer> shakeIter = this.shake.get(
                                  this.playersInPlay.get(playerNum).getName());
        if (x <= (W / 2)) {
            x = 0;
        }
        else if (x >= (R - (W / 2))) {
            x = R - W;
        }
        else {
            x = x - (W / 2);
        }
        if (y >= (B - (H / 2))) {
            y = B - H;
        }
        else if (y <= (H / 2)) {
            y = 0;
        }
        else {
            y = y - (H / 2);
        }
        if ((shakeIter != null) && shakeIter.hasNext()) {
            int shake = shakeIter.next();
            x = x + shake;
            y = y + shake;
        }
        windowCoords[0] = x;
        windowCoords[1] = y;
    }

    /* paint each frame */
    public void paint(Graphics g) {
        if(playersInPlay.size()!=0)
            clock.tick();
        Dimension windowSize = getSize();
        Graphics2D g2 = createGraphics2D(this.sizeX, this.sizeY);
        drawFrame(windowSize.width, windowSize.height, g2);
        if (this.menu.isWaiting()) {
            g2.dispose();
            g.drawImage(bimg, 0, 0, this);
            return;
        }
        this.paintWindow(0);
        this.paintWindow(1);
        g.drawImage(bimg.getSubimage(this.leftWindowCoords[0], 
                                     this.leftWindowCoords[1],
                                     windowSize.width / 2, windowSize.height),
                                     0, 0, this);
        g.drawImage(bimg.getSubimage(this.rightWindowCoords[0], 
                                     this.rightWindowCoords[1],
                                     windowSize.width / 2, windowSize.height),
                                     windowSize.width / 2, 0,
                                     this);
        Image minimap = bimg.getScaledInstance(this.sizeY / 5,
                                               this.sizeX / 5,
                                               BufferedImage.SCALE_FAST);
        g.drawImage(minimap,
                    (windowSize.width / 2) - (minimap.getWidth(this) / 2),
                    windowSize.height - (minimap.getHeight(this)),
                    null);
        g2.dispose();
    }

    /* start the game thread*/
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    /* run the game */
    public void run() {
        
        Thread me = Thread.currentThread();
        while (thread == me) {
            this.requestFocusInWindow();
            repaint();
          
          try {
                thread.sleep(23); // pause a little to slow things down
            } catch (InterruptedException e) {
                break;
            }
            
        }
    }
    
    /* End the game, and signal either a win or loss */
    public void endGame(boolean win){
        this.gameOver = true;
        this.gameWon = win;
    }
    
    public boolean isGameOver(){
        return gameOver;
    }
    
    // signal that we can stop entering the game loop
    public void finishGame(){
        gameFinished = true;
    }
    

    /*I use the 'read' function to have observables act on their observers.
     */
    @Override
    public void update(Observable o, Object arg) {
        AbstractGameModifier modifier = (AbstractGameModifier) o;
        modifier.read(this);
    }
}
