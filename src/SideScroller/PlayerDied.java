package SideScroller;

/**
 * Error message thrown when a player dies
 * @author Fergus
 */
public class PlayerDied extends Exception {
    public PlayerDied(String errorMessage) {
        super(errorMessage);
    }
}
