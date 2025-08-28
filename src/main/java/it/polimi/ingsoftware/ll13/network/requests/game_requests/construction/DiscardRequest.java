package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class DiscardRequest extends Request {
    private final Tile tile;
    public DiscardRequest(int senderId, Tile tile) {
        super(senderId);
        this.tile = tile;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleDiscardRequest(this);
        }
    }

    public Tile getTile() {
        return tile;
    }
}
