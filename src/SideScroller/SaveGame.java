package SideScroller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SaveGame {
    private int player;
    private int coins;

    /**
     * Grabs the data off the save file and stores the amount of coins
     * @param player - 1 for P1, 2 for P2 data
     */
    public void loadGame(int player){
        try{

            File dir = new File("save");
            File f = new File("save"+ Integer.toString(player) + ".txt");
            //=== If directory and file doesn't exist ===//
            if(!dir.isDirectory() && !f.exists()){
                f.mkdirs();
                try{
                    f.createNewFile();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            else{
                //=== If file exists ===//
                if(f.exists()){
                    //=== Read data ===//
                    Scanner r = new Scanner(f);
                    String l = "";
                    while(r.hasNextLine()){
                        l = r.nextLine();
                    }
                    //=== Split by comma ===//
                    List<String> data = Arrays.asList(l.split(","));
                    //=== Set coins for p1 ===//
                    if(data.get(0) != null && data.get(0).equals("1")){
                        this.coins = Integer.parseInt(data.get(1));
                    }
                    //=== Set coins for p2 ===//
                    else{
                        this.coins = Integer.parseInt(data.get(1));
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     * Saves game is a CSV style format. Save file is player
     * Number based, i.e save1.txt for player 1.
     * @param player - 1 for P1, 2 for P2
     * @param coins - Current ammount of coins player has
     */
    public void saveGame(int player, int coins){
        try{
            //=== Save file ===//
            FileWriter f = new FileWriter("save" + Integer.toString(player) + ".txt");
            String save = Integer.toString(player) + "," + Integer.toString(coins);
            System.out.println(save);
            f.write(save);
            f.close();
        }
        catch(IOException e ){
            e.printStackTrace();
        }
    }

    public int getCoins(){
        return this.coins;
    }
}
