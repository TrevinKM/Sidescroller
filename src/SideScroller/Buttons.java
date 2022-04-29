package SideScroller;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.Paths;
/** @author mabarana
 * Class to easily construct buttons
 * */
public class Buttons {

    //initialise attributes of the buttons
    private Texture buttonTexture;
    private Sprite buttonSprite;
    private String textureFile;
    private RenderWindow currentWindow;
    private float[] hitbox;
    private FloatRect boundingBox;

    /**
     * Constructor for Buttons class
     * @param textureFile takes the location of the texture file as a string to the class
     * @param xPos absolute x position of the button from origin
     * @param yPos absolute y position of the button from origin
     * */
    public Buttons(String textureFile, int xPos, int yPos) {

        //Initialise attributes
        this.textureFile = textureFile;
        buttonTexture = new Texture();

        //Try to load a texture
        try{
            buttonTexture.loadFromFile(Paths.get("img/menu/" + textureFile));
            //System.out.println(buttonTexture.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialise sprite from texture
        buttonSprite = new Sprite(buttonTexture);
        buttonSprite.setOrigin(0,0);
        buttonSprite.setPosition(xPos,yPos);

        //Retrieve the global bounding rectangle of the button
        this.boundingBox = buttonSprite.getGlobalBounds();

        //Hitbox will store the bounds of the button in an array of structure[0 LEFT, 2 RIGHT, 3 TOP, 4 BOTTOM]
        this.hitbox = new float[] {boundingBox.left, boundingBox.left + boundingBox.width,
                                    boundingBox.top, boundingBox.top + boundingBox.height};

        //System.out.println(hitbox[0] + " " + hitbox[1] + " " + hitbox[2] + " " + hitbox[3]);

    }

    /**
     * change the location of the button on the screen
     * @param xPos integer x coordinate the button should be
     * @param yPos integer y coordinate the button should be
     * */
    public void configureButtonPos(int xPos, int yPos){
        buttonSprite.setPosition(xPos,yPos);
    }

    // use a bounding box
    public int buttonBounds(){
        int a = 1;
        int b = 1;
        return a;
    }

    /**
     * getter for the button sprite
     * */
    public Sprite getButtonSprite(){
        return buttonSprite;
    }

    /**
     * Check the coordinates of the mouse click are within the bounds of the button
     * @param xClick x position of the mouse click
     * @param yClick y position of the mouse click
     * */
    public boolean hasBeenClicked(int xClick, int yClick){
        if (xClick > hitbox[0] && xClick < hitbox[1] && yClick > hitbox[2] && yClick < hitbox[3]){
            //System.out.println("clicked");
            return true;
        }
        else {
            return false;
        }

    }
    /**
     * set the new bounding box for a button once it has moved
     * */
    public void setHitbox(){
        this.boundingBox = buttonSprite.getGlobalBounds();
        //Hitbox will store the bounds of the button in an array of structure[0 LEFT, 2 RIGHT, 3 TOP, 4 BOTTOM]
        this.hitbox = new float[] {boundingBox.left, boundingBox.left + boundingBox.width,
                boundingBox.top, boundingBox.top + boundingBox.height};

    }
    /**
     * animate a button in using jsfml clock
     * */
    public void animateIn(){
        buttonSprite.move(50,0);
        setHitbox();
    }

    /**
     * animate a button out using jsfml clock
     * */
    public void animateOut(RenderWindow givenWindow){
        while (buttonSprite.getPosition().x < 500){
            givenWindow.clear();
            givenWindow.draw(buttonSprite);
            givenWindow.display();
            if (buttonSprite.getPosition().x < 500)
                buttonSprite.move(50,0);
        }
    }




}
