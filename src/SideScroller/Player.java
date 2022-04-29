package SideScroller;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.KeyEvent;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Instance of the entities class with extra fields/methods to represent a player
 * @author Fergus
 */
public class Player extends Entities {
    private int lives;
    private List<Entities> gameEntities = new ArrayList<>();
    private int coins;
    private Vector2f startingCords;
    private int[] bounds;
    private Interval[] intervals;
    private Movement mv;
    SaveGame sg = new SaveGame();
    private Text tp;
    private long lastDeath;
    private final long invulnerabilityPeriod = 750;

    /**
     * Constructor for player entities
     *
     * @param colour colour of player sprite
     * @param lives  lives of player
     * @param bounds bounds of player area
     */
    public Player(String colour, int lives, int[] bounds, boolean ply2) {
        super("ships/ship_" + colour + ".png");
        this.colour = colour;
        this.lives = lives;
        coins = 0;
        this.bounds = bounds;

        intervals = new Interval[6];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(10000,4000, i+2);
        }

        //=== Create Movement ===//
        this.mv = new Movement(object, ply2);

        //calculates starting position
        startingCords = new Vector2f(60, bounds[0] + (bounds[1] - bounds[0]) / 2);
        object.setPosition(startingCords);

        //=== Load game for correct player ===//
        if(!ply2){
            sg.loadGame(1);
        }
        else{
            sg.loadGame(2);
        }
        this.coins = sg.getCoins();

        //=== Sets coins ===//
        this.coins = sg.getCoins();

        //sets up coins/lives output
        Font font = new Font();
        try{
            font.loadFromFile(Paths.get("./img/fonts/slkscr.ttf"));
        }catch (IOException e){
            e.printStackTrace();
        }
        tp = new Text(("Coins: "+ this.coins + " - Lives: " + this.lives), font, 16);
        tp.setPosition(0, bounds[0]);
    }

    /**
     * Constructor for player entities when already has saved data
     *
     * @param colour colour of player sprite
     * @param lives  lives of player
     * @param bounds bounds of player area
     * @param coins  amount of money player has
     */
    public Player(String colour, int lives, int[] bounds, int coins, boolean ply2) {
        super("img/ships/ship_" + colour + ".png");
        this.colour = colour;
        this.lives = lives;
        this.coins = coins;
        this.bounds = bounds;

        intervals = new Interval[6];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(10000,4000, i+2);
        }

        //=== Create Movement ===//
        this.mv = new Movement(super.object, ply2);

        //calculates starting position
        startingCords = new Vector2f(60, bounds[0] + (bounds[1] - bounds[0]) / 2);
        object.setPosition(startingCords);

        //sets up coins/lives output
        Font font = new Font();
        try{
            font.loadFromFile(Paths.get("./img/fonts/slkscr.ttf"));
        }catch (IOException e){
            e.printStackTrace();
        }
        tp = new Text("Coins: 0 - Lives: 0", font, 16);
        tp.setPosition(0, bounds[0]);
    }

    /**
     * Starts time related methods for game systems
     */
    public void start(){
        //sets spawn time
        lastDeath = ZonedDateTime.now().toInstant().toEpochMilli();

        for (int i = 0; i < intervals.length; i++){
            intervals[i].start();
        }
    }

    /**
     * Overrides the update position method
     *
     * @param scrollSpeed speed that game should be scrolling sideways
     * @return always 1 as doesn't do anything
     */
    @Override
    public int updatePosition(float scrollSpeed) {
        return 1;
    }

    /**
     * decreases player lives
     *
     * @throws PlayerDied if player runs out of lives
     */
    public void die() throws PlayerDied {
        lastDeath = ZonedDateTime.now().toInstant().toEpochMilli();
        lives--;
        if (lives <= 0) {
            playSound("GameOver.wav");
            throw new PlayerDied(colour);
        } else {
            //playSound("LifeLost.wav");
            object.setPosition(startingCords);
        }

    }

    /**
     * changes player skin to next colour in single player
     */
    private void changeColour(int mode) {
        //list of colours
        String[] colours = new String[] {"blue", "green", "orange", "pink", "red", "yellow"};
        //finds current colour
        int i = 0;
        while(!colours[i].equals(colour)){
            i++;
        }

        //sets new colour
        if (mode == 1){
            if(i==colours.length-1){
                i=0;
            }
            colour = colours[i+1];
        }
        else if (mode == 2){
            if(i==0){
                i=colours.length;
            }
            colour = colours[i-1];
        }

        //applies new colour
        try {
            //loads texture from file
            shape.loadFromFile(Paths.get("img/ships/ship_" + colour +".png"));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }
        object.setTexture(shape);
    }

    /**
     * called by game logic to add entities to each player
     *
     * @param type of entity
     *             1 - Key
     *             2 - Slow down scrolling
     *             3 - decrease barrier frequency
     *             4 - coins
     *             5 - hearts
     *             6 - enemy
     *             7 - barrier
     * @return -1 if there's an error with the type number
     * -2 if needs to spawn key
     */
    public int addEntity(int type) {
        if (type <= 1) {
        }//if type too small
        else if (type <= 5) { //if is a powerup
            gameEntities.add(new Powerups(type, bounds));
            return 1;
        } else if (type == 6) { //enemy
            gameEntities.add(new Enemies(bounds));
            return 1;
        } else if (type == 7) { //barrier
            gameEntities.add(new Barrier(colour, bounds));
            //adds key to other player
            return -2;
        }
        return -1;
    }

    /**
     * adds a key to the player's entity list
     *
     * @param target barrier to unlock
     */
    public void addKey(Barrier target) {
        gameEntities.add(new Keys(target, bounds));
    }

    /**
     * get last Barrier in list
     *
     * @return last barrier or null if none
     */
    public Barrier getLastBarrier() {
        Barrier temp = null;
        for (int i = 0; i < gameEntities.size(); i++) {
            if (gameEntities.get(i) instanceof Barrier) {
                temp = (Barrier) gameEntities.get(i);
            }
        }
        return temp;
    }

    /**
     * Displays, moves and removes entities
     *
     * @param window      render window displaying game
     * @param scrollSpeed current scroll speed of game
     */
    public void displayEntities(RenderWindow window, float scrollSpeed) {
        //coins/lives
        tp.setString("Coins: " + coins + " - Lives: " + lives);
        window.draw(tp);

        //entities
        for (int i = 0; i < gameEntities.size(); i++) {
            window.draw(gameEntities.get(i).object);
            if (gameEntities.get(i).updatePosition(scrollSpeed) == -1) {
                gameEntities.remove(i);
            }
        }
    }

    /**
     * checks if player collides with one of the entities in it's play area
     *
     * @param scrollSpeed current scroll speed of game
     * @param barrier used as a flag for if bounced of a barrier
     * @return returns -1 if not collided or 1,2,3 if needs parent to do something
     * 1 - Key
     * 2 - Slow down scrolling
     * 3 - decrease barrier frequency
     * @throws PlayerDied to signify player ran out of lives
     */
    public int checkCollide(float scrollSpeed, Boolean barrier) throws PlayerDied {
        //temp variables used to detect collisions based of distance rather than hitboxes
        float tmpx = object.getPosition().x;
        float tmpy = object.getPosition().y;
        Vector2i spriteSize = object.getTexture().getSize();
        spriteSize = Vector2i.div(spriteSize, 4);

        //checks not in invulnerability stage
        boolean invulnerable = !(ZonedDateTime.now().toInstant().toEpochMilli() - lastDeath > invulnerabilityPeriod);

        try {
            for (Entities entity : gameEntities) {
                if (object.getGlobalBounds().intersection(entity.object.getGlobalBounds()) != null) {
                    if (entity instanceof Keys) { //if key
                        Keys k = (Keys) entity;
                        gameEntities.remove(entity);
                        playSound("BarrOpen.wav");
                        k.open();
                    }
                    else if (entity instanceof Powerups) { //if pickup
                        gameEntities.remove(entity);
                        Powerups p = (Powerups) entity;
                        int type = p.collectPowerup();
                        switch (type) {
                            case 2:
                                playSound("pickup.wav");
                                return 2;
                            case 3:
                                intervals[5].decreaseFrequency();
                                playSound("pickup.wav");
                            case 4:
                                this.coins++;
                                playSound("coin.wav");
                                break;
                            case 5:
                                lives++;
                                playSound("pickup.wav");
                                break;
                        }
                    }
                    else if (entity instanceof Enemies && !invulnerable) { //if enemy
                        playSound("enemy.wav");
                        die();
                    }
                    else if (entity instanceof Barrier) { //if barrier
                        Barrier b = (Barrier) entity;
                        if (!b.getColour().equals(colour) && !invulnerable) {
                            mv.setXSpeed(0);
                            mv.setYSpeed(0);
                            super.updatePosition(scrollSpeed);
                            checkCollide(scrollSpeed, true); //calls recursively to make objects solid
                        }
                    }
                    return -1;
                }
            }

            //if out of bounds
            boolean bound = false;
            if (tmpx - spriteSize.x <= 0) {
                object.move(-mv.getXSpeed(), 0);
                if (barrier){
                    die();
                    bound = false;
                }
                else{
                    bound = true;
                }
            } else if (tmpx + spriteSize.x >= bounds[2]) {
                object.move(-mv.getXSpeed(), 0);
                bound = true;
            }
            if (tmpy - spriteSize.y <= bounds[0]) {
                object.move(0, -mv.getYSpeed());
                bound = true;
            } else if (tmpy + spriteSize.y >= bounds[1]) {
                object.move(0, -mv.getYSpeed());
                bound = true;
            }

            //recursively calls to make boundaries solid
            if (bound){
                checkCollide(scrollSpeed, false);
            }
        } catch (java.lang.StackOverflowError e){ //occasionally recursive approach doesn't work too well
            if (tmpx - spriteSize.x <= 0) {
                if (barrier){
                    die();
                }
                else {
                    object.setPosition(1, tmpy);
                }
            }
            else if (tmpx + spriteSize.x >= bounds[2]) {
                object.setPosition(bounds[2] - 1, tmpy);
            }
            if (tmpy - spriteSize.y <= bounds[0]) {
                object.setPosition(tmpx, bounds[0] + 1);
            }
            else if (tmpy + spriteSize.y >= bounds[1]) {
                object.setPosition(tmpx, bounds[1] - 1);
            }
        }

        return -1;
    }

    /**
     * checks
     *
     * @return 1 if is fine, if -1 was error with spawning, if -2 needs to spawn key
     */
    public int checkInterval() {
        //loops through entity type intervals
        for (int i = 0; i < intervals.length; i++){
            if (intervals[i].checkInterval()) {
                //adds entity
                return addEntity(i+2);
            }
        }

        return 1;
    }

    /**
     * Sets flag for movment keys
     * @param key keyboard value that was pressed
     * @param playerNum number of players (controls wether q and e can be used)
     * @throws GameEnded Signifies ESC pressed and ends game
     */
    public void keyPressed(KeyEvent key, int playerNum) throws GameEnded{
        int tmp = mv.pressed(key);
        if (playerNum < 2) {
            if (tmp == 1) {
                changeColour(1);
            } else if (tmp == 2) {
                changeColour(2);
            }
        }
    }

    /**
     * Disables flag for key press
     * @param key key released
     */
    public void keyReleased(KeyEvent key) {
        mv.released(key);
    }

    /**
     * updates players position
     * @param scrollSpeed game scroll speed
     * @return id of entity colliding with
     * @throws PlayerDied signifies player died
     */
    public int update(float scrollSpeed) throws PlayerDied {
        int result = checkCollide(scrollSpeed, false);
        mv.update();
        return result;
    }

    public int getCoins(){
        return this.coins;
    }

    public void setCoin(int coins){
        this.coins = coins;
    }
    
}
