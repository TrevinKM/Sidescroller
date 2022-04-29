package SideScroller;

import org.jsfml.system.Vector2i;

public class TestMenu {

    public static void main(String[] args) {
        Vector2i size = new Vector2i(700,400);
        Player p1 = new Player("blue",2, new int[]{0, size.y/2, size.x}, false);
        Player p2 = new Player("red",2, new int[]{size.y/2, size.y, size.x}, true);
        Game twoPlayerGame = new Game(size,"Splitscreen Shipgame", p1, p2);

        try{
            twoPlayerGame.run();
        }
        catch (PlayerDied | GameEnded exception){
            System.out.println(exception.getMessage());
        }

    }
}

