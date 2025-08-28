package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class RotatedTileResponse extends Response {
    private final Tile tile;
    private final boolean success;

    public RotatedTileResponse(Tile tile, boolean success) {
        this.tile = tile;
        this.success = success;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleRotateTile(this);
    }

    public Tile getTile() {
        return tile;
    }
    public boolean isSuccess(){return success;
    }
}
