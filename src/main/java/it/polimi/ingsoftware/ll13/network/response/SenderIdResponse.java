package it.polimi.ingsoftware.ll13.network.response;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;

import java.io.Serial;

/**
 * this class is a formal response used by the server to tag every client joining an unique id
 * base on its socket
 */
public class SenderIdResponse extends Response {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int senderId;
    public SenderIdResponse(int senderId){
        this.senderId=senderId;
    }
    public int getSenderId(){
        return this.senderId;
    }
    @Override
    public void execute(Handler handler) {
        handler.handleSenderIdResponse(this);
    }
}
