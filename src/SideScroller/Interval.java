package SideScroller;

import java.time.ZonedDateTime;

/**
 * Data-type for time interval when spawning entities
 */
public class Interval {
    private float interval;
    private long lastEntityT;
    private int max;
    private int min;
    private int id;

    /**
     * Sets up interval parameters
     * @param max time interval
     * @param min time interval
     * @param id represents entity type of thing this represents
     *           used to set weighting
     */
    public Interval(int max, int min, int id){
        this.max = max;
        this.min = min;
        this.id = id;

        //applies weightings
        if (id == 2) { //Slow scrolling
            min*=4;
            max*=2;
        } else if (id == 3) { //barrier frequency
           min*= 2.5;
           max*=5;
        } else if (id == 4) { //coins
            min*=1.5;
        } else if (id == 5) { //hearts
            min*=2;
        } else if (id == 6) { //enemies
            min*=4;
            max*=3;
        }

        lastEntityT = ZonedDateTime.now().toInstant().toEpochMilli();
        interval = (int) ((Math.random() * ((max - min) + 1)) + min);
    }

    /**
     * Starts time related methods for game systems
     */
    public void start(){
        //sets spawn time
        lastEntityT = ZonedDateTime.now().toInstant().toEpochMilli();
    }

    /**
     * Checks interval
     * @return true if spawning something
     */
    public boolean checkInterval(){
        long timeNow = ZonedDateTime.now().toInstant().toEpochMilli();
        if (timeNow - lastEntityT >= interval) {
            //saves time of last entity
            lastEntityT = timeNow;

            //sets next interval
            interval = (int) ((Math.random() * ((max - min) + 1)) + min);
            max-=10;
            min-=10;

            return true;
        }
        else{
            return false;
        }
    }

    /**
     * if entity is a barrier, reduces spawn frequency
     */
    public void decreaseFrequency(){
        if  (id == 7){
            min*=1.5;
        }
    }
}
