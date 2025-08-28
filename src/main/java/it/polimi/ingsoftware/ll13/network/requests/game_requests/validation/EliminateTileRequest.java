package it.polimi.ingsoftware.ll13.network.requests.game_requests.validation;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class EliminateTileRequest extends Request{
    private final int row;
    private final int col;
    public EliminateTileRequest(int senderId, int row, int col) {
        super(senderId);
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleEliminateTileRequest(this);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
