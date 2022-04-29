package SideScroller;

import org.jsfml.graphics.*;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

/**
 * @author mabarana
 * class to implement the main menu
 * */
public class MainMenu implements GameState {

    public static MainMenu instance = new MainMenu();

    private MainMenu(){}

    public static MainMenu Instance(){
        return instance;
    }



    @Override
    public void updateState(GameStateController mnu) {

        //Create main menu button objects
        Buttons start = new Buttons("start.png", -125, 100);
        Buttons settingswheel = new Buttons("settingswheel.png", -125, 250);
        Buttons credits = new Buttons("CreditsB.png", 345, -20);

        //Get current window
        RenderWindow window = mnu.getWindow();

        while (window.isOpen()){

            //Clear the screen with a black colour and draw the sprites on the window
            window.clear(Color.BLACK);
            window.draw(start.getButtonSprite());
            window.draw(settingswheel.getButtonSprite());
            window.draw(credits.getButtonSprite());
            window.display();

            //Animate the buttons in
            if (start.getButtonSprite().getPosition().x <125){
                start.animateIn();
                settingswheel.animateIn();
            }

            //Check for input and which button has been pressed
            for(Event event : window.pollEvents()){
                if (event.type == Event.Type.CLOSED){
                    window.close();
                } else if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
                    MouseEvent mouseEvent = event.asMouseEvent();
                    if (start.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        start.animateOut(window);
                        mnu.setCurrentState(PlayerSelect.instance());
                        mnu.update();
                    } else if (settingswheel.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        start.animateOut(window);
                        mnu.setCurrentState(Settings.Instance());
                        mnu.update();
                    } else if (credits.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        //display credits window
                        RenderWindow credwindow = new RenderWindow();
                        credwindow.create(new VideoMode(1113,468), "Credits");
                        Buttons creditsscreen = new Buttons("credits.png", 0, 0);

                        while (credwindow.isOpen()){
                            credwindow.draw(creditsscreen.getButtonSprite());
                            credwindow.display();
                            for(Event event2 : credwindow.pollEvents()){
                                if (event2.type == Event.Type.CLOSED){
                                    credwindow.close();
                                }
                            }
                        }
                    }

                    //start.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y);
                }
            }
        }
    }
}
