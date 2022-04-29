package SideScroller;

/**
 * Error message thrown when player ends game
 * @author Fergus
 */
public class GameEnded extends Exception {
    public GameEnded() {
        super("Game Ended");
    }
}
