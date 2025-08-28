package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.io.Serial;
import java.util.List;

public class DrawFromTempResponse extends Response {
    private final Tile tile;
    private final List<TileCoordinates> updatedTiles;
    private final boolean success;

    public DrawFromTempResponse(Tile tile, List<TileCoordinates> updatedTiles, boolean success) {
        this.tile = tile;
        this.updatedTiles = updatedTiles;
        this.success = success;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleDrawFromTemp(this);
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<TileCoordinates> getUpdatedTiles() {
        return updatedTiles;
    }
}
