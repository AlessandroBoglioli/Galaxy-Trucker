package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class DrawTileRequest extends Request {
    private final boolean drawFromFlippedDeck;
    private final int flippedTileIndex;
    public DrawTileRequest(int senderId, boolean drawFromFlippedDeck, int flippedTileIndex) {
        super(senderId);
        this.drawFromFlippedDeck = drawFromFlippedDeck;
        this.flippedTileIndex = flippedTileIndex;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleDrawTileRequest(this);
        }
    }

    public boolean isDrawFromFlippedDeck() { // TODO implement method in MatchController
        return drawFromFlippedDeck;
    }

    public int getFlippedTileIndex() {
        return flippedTileIndex;
    }
}
