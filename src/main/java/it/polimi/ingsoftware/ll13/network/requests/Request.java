package it.polimi.ingsoftware.ll13.network.requests;

import it.polimi.ingsoftware.ll13.server.controller.Controller;

import java.io.Serial;
import java.io.Serializable;

public abstract class Request implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    protected final int senderId;

    protected Request(int senderId) {
        this.senderId = senderId;
    }

    public abstract void execute(Controller controller);

    public int getSenderId() {
        return senderId;
    }


}
