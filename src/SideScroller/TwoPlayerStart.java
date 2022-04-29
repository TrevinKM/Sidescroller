package SideScroller;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;

import java.time.Instant;

/**
 * @author mabarana
 * Class to run a two player instance of the game
 * */
public class TwoPlayerStart implements GameState {
    //initialise class and set a new instace of the class
    private static TwoPlayerStart instance  = new TwoPlayerStart();

    private TwoPlayerStart(){}

    public static TwoPlayerStart instance(){
        return instance;
    }

    @Override
    public void updateState(GameStateController mnu) {

        //Set the screen size
        Vector2i size = Settings.setSize;

        //Create two player objects and pass them into Game
        Player p1 = new Player("blue",2, new int[]{0, size.y/2, size.x}, false);
        Player p2 = new Player("red",2, new int[]{size.y/2, size.y, size.x}, true);
        Game twoPlayerGame = new Game(size,"Splitscreen Shipgame", p1, p2);
        long startTime = 0, endTime = 0;

        try{
            //Run game
            startTime = Instant.now().getEpochSecond();
            twoPlayerGame.run();
        }
        catch (PlayerDied | GameEnded exception){
            endTime = Instant.now().getEpochSecond();
        }

        //Create a new window to take player back to the main menu
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(500,500), "End Menu");
        mnu.setWindow(window);
        mnu.setCurrentState(EndGameScreen.Instance((endTime - startTime)));
        mnu.update();
    }
}
