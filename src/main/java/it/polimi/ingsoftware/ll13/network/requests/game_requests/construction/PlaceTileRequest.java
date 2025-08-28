package it.polimi.ingsoftware.ll13.network.requests.game_requests.construction;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.requests.Request;
import it.polimi.ingsoftware.ll13.server.controller.Controller;
import it.polimi.ingsoftware.ll13.server.controller.GameController;

public class PlaceTileRequest extends Request {
    private final Tile tile;
    private final int row;
    private final int col;

    public PlaceTileRequest(int senderId, int row, int col, Tile tile) {
        super(senderId);
        this.tile = tile;
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute(Controller controller) {
        if(controller instanceof GameController){
            GameController.getInstance().handlePlaceTileRequest(this);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Tile getTile() {
        return tile;
    }
}
