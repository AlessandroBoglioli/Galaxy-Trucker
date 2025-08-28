package it.polimi.ingsoftware.ll13.network.response;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;

import java.io.Serial;
import java.io.Serializable;

public abstract class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public abstract void execute(Handler handler);
}
