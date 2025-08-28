package it.polimi.ingsoftware.ll13.model.hourglass;

/**
 * This class extends the basic functions of the hourglass adding the method that resets the timer
 * after a player chooses to do it.
 */

public class HourglassLevel2 extends Hourglass {

    private boolean flipped;

    public HourglassLevel2(int maxTime) {
        super(maxTime);
        flipped = false;
    }
    public boolean isFlipped(){
        return flipped;
    }

    /**
     * The method restarts the timer of the hourglass, it can be called only one time.
     */
    public synchronized boolean flip() {
        if (this.flipped || this.remainingTime>0){
            return false;
        }
        this.remainingTime = this.maxTime;
        this.flipped = true;
        return true;
    }
}
