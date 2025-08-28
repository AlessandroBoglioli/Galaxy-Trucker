package it.polimi.ingsoftware.ll13.server.Comunication;

import it.polimi.ingsoftware.ll13.model.hourglass.Hourglass;

public class FakeHourglass extends Hourglass {

    public FakeHourglass() {
        super(1);
    }
    @Override
    public void run() {
        try {
            Thread.sleep(50); // very short sleep for realism
            if (isExecuting() && getOnFinishCallback() != null) {
                getOnFinishCallback().run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
