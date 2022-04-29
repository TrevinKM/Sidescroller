package SideScroller;

import org.jsfml.graphics.*;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

import java.io.IOException;
import java.nio.file.Paths;

public class EndGameScreen implements GameState {
    public static EndGameScreen instance = new EndGameScreen();

    private EndGameScreen(){}
    private static long score;
    public static EndGameScreen Instance(long endtime){
        score = endtime * 1000;
        return instance;
    }

    @Override
    public void updateState(GameStateController mnu) {
        //positions need to change
        Buttons playagain = new Buttons("playagain.png", 50, 150);
        Buttons gameover = new Buttons("gameover.png", 50, 50);
        Buttons exit = new Buttons("exit.png", 280, 150);
        Buttons mainmenu = new Buttons("mainmenu.png", 140, 310);
        RenderWindow window = mnu.getWindow();
        Font font = new Font();
        try{
            font.loadFromFile(Paths.get("./img/fonts/slkscr.ttf"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        Text scoreText = new Text("Score - " + score, font, 32);
        scoreText.setPosition(120, 225);
        while (window.isOpen()){
            window.clear(Color.BLACK);
            window.draw(playagain.getButtonSprite());
            window.draw(gameover.getButtonSprite());
            window.draw(exit.getButtonSprite());
            window.draw(mainmenu.getButtonSprite());
            window.draw(scoreText);
            window.display();

            for(Event event : window.pollEvents()){
                if (event.type == Event.Type.CLOSED){
                    window.close();
                } else if (event.type == Event.Type.MOUSE_BUTTON_PRESSED){
                    MouseEvent mouseEvent = event.asMouseEvent();
                    if (playagain.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y)){
                        playagain.animateOut(window);
                        mnu.setCurrentState(PlayerSelect.instance());
                        mnu.update();
                    } else if (mainmenu.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y)){
                        mainmenu.animateOut(window);
                        mnu.setCurrentState(MainMenu.Instance());
                        mnu.update();
                    } else if (exit.hasBeenClicked(mouseEvent.position.x, mouseEvent.position.y)){
                        exit.animateOut(window);
                        window.close();
                    }
                }
            }
        }
    }
}
