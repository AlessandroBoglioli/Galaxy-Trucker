package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class DrawTileResponse extends Response {
    private final Tile drawnTile;
    private final boolean success;
    public DrawTileResponse(Tile drawnTile, boolean success){
        this.drawnTile = drawnTile;
        this.success = success;
    }


    @Override
    public void execute(Handler handler) {
        handler.handleDrawTile(this);
    }

    public Tile getDrawnTile() {
        return drawnTile;
    }

    public boolean isSuccess() {
        return success;
    }
}
