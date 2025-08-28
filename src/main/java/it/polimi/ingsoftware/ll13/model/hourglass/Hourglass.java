package it.polimi.ingsoftware.ll13.model.hourglass;

import java.io.Serializable;

/**
 * This class represent the hourglass that is used during the ship building phase.
 * The basic functions that should implement are:
 *  - starting the timer with a thread;
 *  - stopping the timer in case of an error.
 */

public class Hourglass implements Runnable {

    int maxTime;
    int remainingTime;
    private boolean executing;
    private Thread hourglassThread;
    private Runnable onFinishCallback;

    /**
     * The builder of the class sets up the hourglass for starting the time.
     * It sets the attributes to the maxTime and executing to false.
      * @param maxTime indicates the time of the hourglass in seconds
     */

    public Hourglass(int maxTime){
        this.maxTime = maxTime * 1000;
        this.remainingTime = this.maxTime;
        this.executing = false;
    }

    public boolean isExecuting(){
        return executing;
    }

    public Runnable getOnFinishCallback() {
        return onFinishCallback;
    }

    /**
     * This method starts the timer of the hourglass.
     * It creates the thread and starts it.
     * If the hourglass is already running it throws an exception.
     */

    public synchronized boolean startTime(Runnable onFinish) {
        if (executing) {
            return false;
        }
        else {
            this.onFinishCallback = onFinish;
            this.executing = true;
            this.remainingTime = maxTime;
            hourglassThread = new Thread(this);
            hourglassThread.start();
            return true;
        }
    }


    // Useless?
    private synchronized void stopTime() {
        if (executing) {
            executing = false;
            if (hourglassThread != null && hourglassThread.isAlive()) {
                hourglassThread.interrupt();
            }
        }
    }

    /**
     * The overridden run method continues to decrement the timer every second.
     * When the timer ends it should notify it.
     */

    @Override
    public void run() {
        try {
            while (executing && remainingTime > 0) {
                Thread.sleep(1000); // every second
                remainingTime -= 1000;
            }
            if (executing) {
                executing = false;
                if(onFinishCallback != null){
                    onFinishCallback.run();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    // ---> Getters <---
    public int getMaxTime() {
        return maxTime;
    }
    public int getRemainingTime() {
        return remainingTime;
    }

    // ---> Setters <---
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
