package SideScroller;

/**
 * Class represents the enemies in the game, extends Entity
 * @author Andrew (Fergus for motion)
 */
public class Enemies extends Entities{
    private int speed;
    private int[] bounds;

    /**
     * creates and sets up the enemies
     * @param bounds boundaries of plotting area
     */
    public Enemies(int[] bounds) {
        // TODO: 02/02/2021 need to speak to enemies creator to understand skins 
        super("enemies/1.png", bounds, 2);
        float max = 5, min = 1;
        this.speed = (int) ((Math.random() * ((max - min) + 1)) + min);
        this.bounds = new int[] {bounds[0], bounds[1]};
    }

    @Override
    public int updatePosition(float scrollSpeed) {
        //makes enemy oscillate across screen
        if (object.getPosition().y <= bounds[0] || object.getPosition().y >= bounds[1]){ //if out of bounds
            speed*=-1;
        }
        object.move(0,speed);
        return super.updatePosition(scrollSpeed);
    }
}


