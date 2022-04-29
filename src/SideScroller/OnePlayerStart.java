package SideScroller;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;

import java.time.Instant;

/**
 * @author mabarana
 * Class to start a one player game
 * */
public class OnePlayerStart implements GameState {
    //initialise class
    private static OnePlayerStart instance  = new OnePlayerStart();

    private OnePlayerStart(){}

    public static OnePlayerStart instance(){
        return instance;
    }

    @Override
    public void updateState(GameStateController mnu) {
        //Set screen size and pass a player object to Game
        Vector2i size = Settings.setSize;
        Player p1 = new Player("blue",2, new int[]{0, size.y, size.x}, false);
        Game onePlayerGame = new Game(size,"Shipgame", p1);
        long startTime = 0, endTime = 0;

        try{
            startTime = Instant.now().getEpochSecond();
            onePlayerGame.run();
        }
        catch (PlayerDied | GameEnded exception){
            endTime = Instant.now().getEpochSecond();

            //Reopens menu
            RenderWindow window = new RenderWindow();
            window.create(new VideoMode(500,500), "Ship game Menu");
            mnu.setWindow(window);
            mnu.setCurrentState(EndGameScreen.Instance((endTime - startTime)));
            mnu.update();
        }

    }
}