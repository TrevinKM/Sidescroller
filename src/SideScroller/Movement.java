package SideScroller;
import org.jsfml.graphics.Sprite;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.KeyEvent;

public class Movement{
    private float xVelo, yVelo;
    private double angle;
    private boolean wPress, aPress, sPress, dPress;
    private Keyboard.Key up, down, left, right, colForw, colBack;
    private final Sprite player;
    private boolean p2;

    //=== Used to define Rotation Speed ===//
    private final float rotationSpeed = 10f;

    //=== Used to define speed ===//
    private final float speed = 5f;

    /**
     * Movement takes in a Sprite when first made to link the relevant values to
     * @param player - Input of the player / sprite
     * @param p2 is it player 2 - whether the given sprite is the second player
     */
    public Movement(Sprite player, boolean p2){
        this.player = player;
        this.p2 = p2;
        if(!p2){
            up = Keyboard.Key.W;
            down = Keyboard.Key.S;
            left = Keyboard.Key.A;
            right = Keyboard.Key.D;
            colBack = Keyboard.Key.Q;
            colForw = Keyboard.Key.E;
        }
        else{
            up = Keyboard.Key.UP;
            down = Keyboard.Key.DOWN;
            left = Keyboard.Key.LEFT;
            right = Keyboard.Key.RIGHT;
        }
    }

    /**
     * Pressed sets the relevant keys value to true if the key is pressed
     * @param key - The KeyEvent
     * @return 1 or two if need to change player colour, else -1
     */
    public int pressed(KeyEvent key) throws GameEnded{
        if(key.asKeyEvent().key == up){wPress = true;}
        if(key.asKeyEvent().key == left){aPress = true;}
        if(key.asKeyEvent().key == down){sPress = true;}
        if(key.asKeyEvent().key == right){dPress = true;}
        if(!p2){
            if (key.asKeyEvent().key == colForw){return 1;}
            if (key.asKeyEvent().key == colBack){return 2;}
        }
        if (key.asKeyEvent().key == Keyboard.Key.ESCAPE){throw new GameEnded();}
        return -1;
    }

    /**
     * Released sets the relevant keys value to false if the key is released.
     * @param key - The KeyEvent
     */
    public void released(KeyEvent key){
        if(key.asKeyEvent().key == up){wPress = false;}
        if(key.asKeyEvent().key == left){aPress = false;}
        if(key.asKeyEvent().key == down){sPress = false;}
        if(key.asKeyEvent().key == right){dPress = false;}
    }

    /**
     * Used to calculate the forward and backward X,Y so that we can move
     * in the direction that the player is facing. It will update the
     * xVelo and yVelo variables respectively
     * @param speed - Speed in which the player will move
     *
     */
    private void calculateXY(float speed){
        double radians = Math.toRadians(angle);
        float x = (float) Math.cos(radians);
        float y = (float) Math.sin(radians);
        xVelo = x * speed;
        yVelo = y * speed;
    }

    /**
     * Returns the X velocity of the player
     * @return Float - X Velocity
     */
    public float getXSpeed(){
        return this.xVelo;
    }

    /**
     * Returns the Y velocity of the player
     * @return Float - Y Velocity
     */
    public float getYSpeed(){
        return this.yVelo;
    }

    /**
     * Sets X Speed
     * @param speed - Float
     */
    public void setXSpeed(float speed){
        this.xVelo = speed;
    }

    /**
     * Sets Y speed
     * @param speed - Float
     */
    public void setYSpeed(float speed){
        this.yVelo = speed;
    }



    /**
     * Update will move the player every tick and needs
     * to be within the core game loop. This is what makes
     * the player move.
     */
    public void update(){
        //=== Make it stop ===//
        if(xVelo > 0 && 0 < xVelo && xVelo < 0.005) {
            xVelo = 0f;
        }
        if(xVelo > 0 && 0 < yVelo && yVelo < 0.005){
            yVelo = 0f;
        }
        if(wPress){calculateXY(speed); player.move(xVelo,yVelo);}
        if(sPress){calculateXY(-speed);player.move(xVelo, yVelo);}
        if(aPress){angle -= rotationSpeed; player.rotate(-rotationSpeed);}
        if(dPress){angle += rotationSpeed; player.rotate(rotationSpeed);}
        xVelo *= 0.99;
        yVelo *= 0.99;

        //=== Keep angle in 0 - 360 ===//
        if(angle == -rotationSpeed){angle = 360 - rotationSpeed;}
        else if(angle == (360 + rotationSpeed)){angle = rotationSpeed;}

        player.move(xVelo, yVelo);
    }

}
