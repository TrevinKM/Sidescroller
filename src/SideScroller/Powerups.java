package SideScroller;

/**
 * Builds powerups
 * @author Fergus
 */
public class Powerups extends Entities{
    private int type;

    /**
     * sets up the powerups
     * @param type type of powerup
     *             1 - Key
     *             2 - Slow down scrolling
     *             3 - decrease barrier frequency
     *             4 - coins
     *             5 - hearts
     * @param bounds boundaries of plotting area
     */
    public Powerups(int type, int[] bounds) {
        super("pickups/" + type + ".png", bounds, 2);
        this.type = type;
        colour = null;
    }

    /**
     * Returns pickup type
     * @return id of type
     *             1 - Key
     *             2 - Slow down scrolling
     *             3 - decrease barrier frequency
     *             4 - coins
     *             5 - hearts
     */
    public int collectPowerup(){
        return type;
    }
}
