package SideScroller;

/**
 * Represents Key
 * @author Fergus
 */
public class Keys extends Entities{
    private Barrier target;

    /**
     * constructor for a key
     * @param target barrier to open
     * @param bounds area to spawn in
     */
    public Keys(Barrier target, int [] bounds){
        super("keys/" + target.colour + ".png", bounds, 2);
        this.target = target;
    }

    /**
     * opens barrier
     */
    public void open() {
        target.open();
    }
}
