package SideScroller;

import org.jsfml.audio.Music;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Class to run game
 * @author Fergus (Matt for Movement)
 */
public class Game {
    //game constants
    private RenderWindow window;
    private Player players[];
    private Music music = new Music();
    private SaveGame sg = new SaveGame();
    private boolean ply2 = false;


    /**
     * Two player game constructor
     *
     * @param size Vector2i giving size of display window
     * @param title Title of Game window
     * @param player1 represents player 1 in the game
     * @param player2 represents player in the game
     */
    public Game(Vector2i size, String title, Player player1, Player player2) {
        //Creates the window
        window = new RenderWindow();
        window.create(new VideoMode(size.x, size.y), title);

        //builds player array
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        ply2 = true;
    }

    /**
     * Single player game constructor
     *
     * @param size Vector2i giving size of display window
     * @param title Title of Game window
     * @param player represents game player
     */
    public Game(Vector2i size, String title, Player player) {
        //Creates the window
        window = new RenderWindow();
        window.create(new VideoMode(size.x, size.y), title);

        //builds player array
        players = new Player[1];
        players[0] = player;
    }

    /**
     * Shows instructions to the user
     * @param windowSize dimensions of the window
     * @throws GameEnded Error to indicate user closed window
     */
    private void showInstructions(Vector2i windowSize) throws GameEnded{
        Texture instructions = new Texture();

        //gets file location
        String target = "";
        if (players.length == 1){
            target+="instructions1.png";
        }
        else{
            target+="instructions2.png";
        }

        //loads file
        try {
            //loads texture from file
            instructions.loadFromFile(Paths.get("img/background/" + target));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }

        //sets sizes/position
        RectangleShape displayIns = new RectangleShape(new Vector2f(instructions.getSize()));
        displayIns.setTexture(instructions);
        displayIns.setOrigin(Vector2f.div(new Vector2f(instructions.getSize()), 2));
        displayIns.setPosition(Vector2f.div(new Vector2f(windowSize),2));

        //if bigger than window scales down
        if (displayIns.getSize().x > windowSize.x){
            float scale = windowSize.x/displayIns.getSize().x;
            displayIns.scale(new Vector2f(scale,scale));
        }
        if (displayIns.getSize().y > windowSize.y){
            float scale = windowSize.y/displayIns.getSize().y;
            displayIns.scale(new Vector2f(scale,scale));
        }

        //displays instructions
        window.draw(displayIns);
        window.display();

        //keeps instructions on screen till user presses enter
        boolean enter = false;
        while (window.isOpen() && !enter){
            for (Event event: window.pollEvents()){
                if (event.type == Event.Type.KEY_PRESSED){
                    KeyEvent key = event.asKeyEvent();
                    if (key.key == Keyboard.Key.RETURN){
                        enter = true;
                    }
                }
                else if (event.type == Event.Type.CLOSED){
                    window.close();
                    throw new GameEnded();
                }
            }
        }
    }

    /**
     * Runs the game
     * @throws PlayerDied to indicate player ran out of lives
     */
    public void run() throws PlayerDied, GameEnded{
        try {
            //makes background
            Entities background = new Entities("background/bg1400x800.png", new int[]{0, window.getSize().y, window.getSize().x / 2}, 3); //doesn't work with non temp background?

            //adds center divider
            Vector2i windowSize = window.getSize();
            RectangleShape divider = new RectangleShape(new Vector2f(windowSize.x, 3));
            divider.setOrigin(0, 2);
            divider.setPosition(0, windowSize.y / 2);


            //Limit the framerate
            window.setFramerateLimit(30);

            //Start the music
            playMusic();

            //game variables
            float scrollSpeedX = 1f; //default scroll speed -> applied to actual scroll speed like graph
            float scrollSpeed = scrollSpeedX;

            showInstructions(windowSize);

            //starts player spawn timings
            for (int i = 0; i < players.length; i++) {
                players[i].start();
            }

            //game loop
            while (window.isOpen()) {
                //clears window and fills with black
                window.clear(Color.BLACK); //clears with black screen
                window.draw(background.object);

                if (players.length > 1) {
                    window.draw(divider);
                }


                for (int i = 0; i < players.length; i++) {
                    //spawns entities
                    int temp = players[i].checkInterval();
                    if (players.length > 1) {
                        if (temp == -2) {
                            players[1 - i].addKey(players[i].getLastBarrier());
                        }
                    }

                    //draws players and entities then removes entities if off screen
                    window.draw(players[i].object);
                    players[i].displayEntities(window, scrollSpeed);
                }

                //Display what was drawn
                window.display();

                //Handle events
                for (Event event : window.pollEvents()) {
                    switch (event.type) {
                        case KEY_PRESSED:
                            KeyEvent key = event.asKeyEvent();
                            for (Player player : players) {
                                player.keyPressed(key, players.length);
                            }
                            break;

                        case KEY_RELEASED:
                            KeyEvent keyRel = event.asKeyEvent();
                            for (Player player : players) {
                                player.keyReleased(keyRel);
                            }
                            break;

                        case CLOSED:
                            window.close();
                            throw new GameEnded();
                    }
                }

                //checks for collisions with other entities
                for (int i = 0; i < players.length; i++) {
                    int result = players[i].update(scrollSpeed);
                    if (result == 2) {
                        scrollSpeed = (float) (scrollSpeed * 0.7);
                    }
                }

                //updates scroll speed ->uses logarithmic curve
                scrollSpeedX += 1;
                scrollSpeed = (float) Math.sqrt(scrollSpeedX) / 3; //* scrollSpeedMultiplier;
            }
        } catch (PlayerDied | GameEnded e){ //catches errors and ends game cleanly
            sg.saveGame(1, players[0].getCoins());
            if(ply2){
                sg.saveGame(2, players[1].getCoins());
            }
            window.close();
            music.stop();
            throw e;
        }
    }

    /**
     * Method used to play an audio file called Music.wav, this will play the sound
     */
    public void playMusic(){

        Path filePath = Paths.get(("sounds/Music.wav"));
        try {
            music.openFromFile(filePath);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        music.setVolume(Settings.volumeLevel);  //sets a lower volume that the rest of the sound effects
        //System.out.println(Settings.volumeLevel);
        music.setLoop(true);  //loops the music continuously
        music.play();
    }
}

