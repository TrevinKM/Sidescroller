package SideScroller;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;
/**
 * @author mabarana
 * Class to make the settings page*/
public class Settings implements GameState{
    //Initialise settings class
    private static Settings instance = new Settings();

    private Settings(){}

    public static Settings Instance() {
        return instance;
    }

    //Set default volume and screen size
    public static Vector2i setSize = new Vector2i(1920 ,1080);
    public static float volumeLevel = 30f;

    @Override
    public void updateState(GameStateController mnu) {
        //Create button objects for the menu
        Buttons voltoken = new Buttons("token.png", 220,30);
        Buttons volumecontrols = new Buttons("gamevolume.png", 100, 385);
        Buttons fullRes = new Buttons("1920x1080res.png", -125, 140);
        Buttons lowRes = new Buttons("700x400res.png",-125, 285);
        Buttons cursor = new Buttons("token.png", 220, 30);
        Buttons goBack = new Buttons("escape.png", 0 , 0);

        //Get the current window in use
        RenderWindow window = mnu.getWindow();

        while (window.isOpen()){

            //Clear and colour the window black
            window.clear(Color.BLACK);

            //Draw the created button objects to the screen
            window.draw(volumecontrols.getButtonSprite());
            window.draw(fullRes.getButtonSprite());
            window.draw(lowRes.getButtonSprite());
            window.draw(cursor.getButtonSprite());
            window.draw(goBack.getButtonSprite());
            window.draw(voltoken.getButtonSprite());
            window.display();

            //Animate buttons in
            if (fullRes.getButtonSprite().getPosition().x < 125) {
                fullRes.animateIn();
                lowRes.animateIn();
            }

            //Check for input
            for(Event event : window.pollEvents()){
                if (event.type == Event.Type.CLOSED){
                    window.close();
                } else if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
                    MouseEvent mouseEvent = event.asMouseEvent();
                    if (fullRes.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        cursor.configureButtonPos(125,140);
                        setSize = new Vector2i(1920,1080);
                        //TODO: pass these vals in to the constructor of the game
                    } else if (lowRes.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        cursor.configureButtonPos(125,285);
                        setSize = new Vector2i(700, 400);
                        //TODO: pass these vals in to the constructor of the game
                    } else if (volumecontrols.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y)){
                        if (mouseEvent.position.x < 150){
                            voltoken.configureButtonPos(112, 446);
                            volumeLevel = 10f;
                        } else if (mouseEvent.position.x < 280 ){
                            voltoken.configureButtonPos(221, 446);
                            volumeLevel = 30f;
                        } else if (mouseEvent.position.x < 365 ){
                            voltoken.configureButtonPos(325, 446);
                            volumeLevel = 60f;
                        }


                    }

                //Check if esc is pressed
                } else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)){
                    mnu.setCurrentState(MainMenu.Instance());
                    mnu.update();

                }
            }

        }
    
    }
}
