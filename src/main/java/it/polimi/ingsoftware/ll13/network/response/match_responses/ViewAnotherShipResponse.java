package it.polimi.ingsoftware.ll13.network.response.match_responses;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class ViewAnotherShipResponse extends Response {
    private final List<TileCoordinates> updatedTiles;
    private final PlayerColors color;
    private final boolean myShip;

    public ViewAnotherShipResponse(List<TileCoordinates> updatedTiles, boolean myShip, PlayerColors color) {
        this.updatedTiles = updatedTiles;
        this.myShip = myShip;
        this.color = color;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleViewAnotherShipResponse(this);
    }

    public List<TileCoordinates> getUpdatedTiles() {
        return updatedTiles;
    }
    public boolean isMyShip() {
        return myShip;
    }
    public PlayerColors getColor() {
        return color;
    }
}
