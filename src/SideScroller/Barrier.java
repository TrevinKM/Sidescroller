package SideScroller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Barrier in the game, extends Entity
 * @author Andrew (Fergus for colour system)
 */

public class Barrier extends Entities {
    String colour;
    private String trueColour;
    boolean open;

    /**
     * sets up coloured barriers that the player has to remove inorder to progress
     * @param colour a String to represent the Colour of the barrier, options are:
     *               blue
     *               green
     *               orange
     *               pink
     *               red
     *               yellow
     * @param bounds boundaries of plotting area
     */
    public Barrier(String colour, int[] bounds){
        super("barriers/" + colour + "1.png", bounds, 1);
        this.trueColour = colour;

        //sets random colour
        List<String> colours = new ArrayList<String> ();
        colours.add("blue"); colours.add("green"); colours.add("orange"); colours.add("pink"); colours.add("red"); colours.add("yellow");
        colours.remove(trueColour);
        int max = colours.size()-1;
        int min = 0;
        this.colour = colours.get((int)((Math.random()*((max-min)+1))+min));

        //updates sprite texture
        try {
            //loads texture from file
            shape.loadFromFile(Paths.get("img/barriers/" + this.colour +"1.png"));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }

        this.open = false;
    }

    /**
     * Returns the colour of the Barrier
     * @return the color of the Barrier as a String
     */
    String getColour(){
        return colour;
    }

    /**
     * Sets the barrier to open, so the player should now be able to pass through it
     */
    void open(){
        this.open = true;
        colour = trueColour;

        //updates sprite texture
        try {
            //loads texture from file
            shape.loadFromFile(Paths.get("img/barriers/" + colour +"1.png"));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }
        object.setTexture(shape);
    }
}
