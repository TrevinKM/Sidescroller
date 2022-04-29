package SideScroller;

import org.jsfml.graphics.RenderWindow;
/**
 * @author mabarana
 * Class to check if the user is in menu or in game
 * */
public class GameStateController {

    //Set variable to check game state and window
    private GameState currentState;
    private String sessionID;
    private RenderWindow window;

    /**
     * Constructor for the GameStateController class
     * @param currentState Checks what screen the player is on
     * @param sessionID Checks the session ID of the player
     * @param  window Check what window is currently operated on
     *
     * */
    public GameStateController(GameState currentState, String sessionID, RenderWindow window){
        super();
        this.sessionID = sessionID;
        this.currentState = currentState;
        this.window = window;

        //Defaults to main menu if a state is not chosen
        if(currentState == null){
            this.currentState = MainMenu.Instance();
        }
    }

    /**
     * initialise the setters and getters for attributes in the class
     * */
    public GameStateController getGameStateController() {
        return this;
    }

    public RenderWindow getWindow() {
        return window;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState){
        this.currentState = currentState;
    }

    public void setWindow(RenderWindow window){this.window=window;}

    /**
     * Update the current state to the value passed into this method
     * */
    public  void update(){
        currentState.updateState(this);
    }
}
