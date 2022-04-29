package SideScroller;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;

import java.io.IOException;
import java.nio.file.Paths;
import org.jsfml.window.VideoMode;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author mabarana
 * @version 0.1
 * Class to construct and run main menu
 * */
public class Main {


    public static void main(String[] args) throws IOException {
        //initialise buttons sprites and positions
        Texture buttontexture = new Texture();
        try{
            buttontexture.loadFromFile(Paths.get("img/menu/start.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Sprite buttonj = new Sprite(buttontexture);

        buttonj.setOrigin(0,0);
        buttonj.setPosition(50,50);


        //Create a window and draw pre rendered sprites
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(500,500), "Ship game Menu");
        /*        Buttons start = new Buttons("start.png", 100, 100);*/

        //Call game state controller to initialise a new instance of a game
        GameStateController mnu = new GameStateController(null, "test.v1", window);
        mnu.update();

    }
}

