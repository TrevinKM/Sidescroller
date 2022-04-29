package SideScroller;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Superclass for game entities
 * @author Fergus
 */
public class Entities {
    Texture shape; //entity texture ->may not be needed anymore
    String colour; //entity colour
    Sprite object; //sprite is representation of object on screen

    /**
     * generic Constructor for entities - sets up sprite
     * @param textureFile location of sprite texture
     * @param bounds boundaries of plotting area
     * @param mode positioning mode for entity
     *             0 - no positioning -> default mode
     *             1 - span bounds
     *             2 - spawn randomly in bounds
     */
    public Entities(String textureFile, int[] bounds, int mode){
        //initiates texture
        shape = new Texture();

        try {
            //loads texture from file
            shape.loadFromFile(Paths.get("img/" + textureFile));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }

        //creates sprite using texture
        object = new Sprite(shape);

        //Sets sprite origin to its center
        object.setOrigin(Vector2f.div(new Vector2f(shape.getSize()), 2));

        //positions sprite
        if (mode == 1){ //span y
            //scales barrier to fit section
            float vScale = (bounds[1] - bounds[0])/(float)shape.getSize().y;
            object.scale(1,vScale);
            object.setPosition(bounds[2], bounds[0] + (bounds[1] - bounds[0])/2);
        }
        if (mode == 3){ //span whole window
            //scales barrier to fit section
            float vScale = (bounds[1] - bounds[0])/(float)shape.getSize().y;
            float xScale = (bounds[2]*2)/(float)shape.getSize().x;
            if (xScale > vScale){
                object.scale(xScale,xScale);
            }
            else{
                object.scale(vScale,vScale);
            }
            object.setPosition(bounds[2], bounds[0] + (bounds[1] - bounds[0])/2);
        }
        else if (mode == 2){ //random
            int ySize = shape.getSize().y;
            int yQuord = (int)(Math.random()*(((bounds[1]-ySize)-(bounds[0]+ySize))+1))+(bounds[0]+ySize);
            object.setPosition(bounds[2], yQuord);
        }
    }

    /**
     * generic for entities with no positioning data - sets up sprite
     * @param textureFile location of sprite texture
     */
    public Entities(String textureFile){
        //initiates texture
        shape = new Texture();

        try {
            //loads texture from file
            shape.loadFromFile(Paths.get("img/" + textureFile));
        } catch(IOException ex) {
            //if error loading file
            ex.printStackTrace();
        }

        //creates sprite using texture
        object = new Sprite(shape);

        //Sets sprite origin to its center
        object.setOrigin(Vector2f.div(new Vector2f(shape.getSize()), 2));
    }

    /**
     * Method used to move each game element across the screen to make the scrolling effect
     *
     * @param scrollSpeed speed that game should be scrolling sideways
     * @return -1 if off screen
     */
    public int updatePosition(float scrollSpeed) {
        object.move(-scrollSpeed,0);
        //if off screen
        if (object.getPosition().x + shape.getSize().x/2 < 0){
            return -1;
        }
        else{
            return 1;
        }
    }

    /**
     * Method used to play an audio file, adding sounds to the game
     *
     * @param fileName The file name of the sound to be played as a String
     */
    public void playSound(String fileName){

        SoundBuffer buffer = new SoundBuffer();
        Path filePath = Paths.get(("sounds/" + fileName));  //loads the file from the file path
        try {
            buffer.loadFromFile(filePath);
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Sound sound = new Sound();
        sound.setBuffer(buffer);
        sound.setVolume(70f);
        sound.play();           //opens a new thread and plays the sound
    }
}
