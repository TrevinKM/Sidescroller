package SideScroller;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

public class PlayerSelect implements GameState{

    private static PlayerSelect instance = new PlayerSelect();

    private PlayerSelect(){}

    public static PlayerSelect instance(){
        return instance;
    }

    @Override
    public void updateState(GameStateController mnu) {
        Buttons player1 = new Buttons("1player.png", -125, 140);
        Buttons player2 = new Buttons("2player.png", -125, 285);
        Buttons goBack = new Buttons("escape.png", 0, 0);

        RenderWindow window = mnu.getWindow();
        while (window.isOpen()){
            window.clear(Color.BLACK);
            window.draw(player1.getButtonSprite());
            window.draw(player2.getButtonSprite());
            window.draw(goBack.getButtonSprite());
            window.display();
            if (player1.getButtonSprite().getPosition().x < 125){
                player1.animateIn();
                player2.animateIn();
            }

            for(Event event : window.pollEvents()){
                if (event.type == Event.Type.CLOSED){
                    window.close();
                } else if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
                    MouseEvent mouseEvent = event.asMouseEvent();

                    if (player1.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        player1.animateOut(window);
                        mnu.setCurrentState(OnePlayerStart.instance());
                        window.close();
                        mnu.update();

                    } else if (player2.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y) == true){
                        player2.animateOut(window);
                        mnu.setCurrentState(TwoPlayerStart.instance());
                        window.close();
                        mnu.update();
                    }

                } else if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)){
                    mnu.setCurrentState(MainMenu.Instance());
                    mnu.update();
                }
            }
        }

    }
}
