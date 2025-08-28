package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class RotateTileRequest extends Request {
    private Tile tile;
    private final int rotationTimes;
    
    public RotateTileRequest(int senderId, Tile tile, int rotationTimes) {
        super(senderId);
        this.tile = tile;
        this.rotationTimes = rotationTimes;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handleTileRotateRequest(this);
        }
    }

    public Tile getTile() {
        return tile;
    }

    public int getRotationTimes() {
        return rotationTimes;
    }
}
