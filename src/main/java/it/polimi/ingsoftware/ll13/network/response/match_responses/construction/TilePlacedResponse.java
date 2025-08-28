package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * This class represents a response that occurs when a tile is placed in the game.
 * It extends the {@code Response} class and provides information about the placed tile
 * and whether the placement was successful or not.
 */
//Used to send back the tile to the player if the placement went wrong, setting the success attribute to
//False, if tile placement has success in the player's ship, an updatedShip response is sent directly so that
//player can have its view updated
public class TilePlacedResponse extends Response {
    private final Tile tile;
    private final boolean success;

    public TilePlacedResponse(Tile tile, boolean success) {
        this.tile = tile;
        this.success = success;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleTilePlaced(this);
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isSuccess() {
        return success;
    }
}
