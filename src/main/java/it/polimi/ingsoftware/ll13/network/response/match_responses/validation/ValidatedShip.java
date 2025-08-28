package it.polimi.ingsoftware.ll13.network.response.match_responses.validation;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class ValidatedShip extends Response{
    private final List<TileCoordinates> invalidTiles;
    private final boolean isValid;
    public ValidatedShip(List<TileCoordinates> invalidTiles, boolean isValid) {
        this.invalidTiles = invalidTiles;
        this.isValid = isValid;
    }
    @Override
    public void execute(Handler handler) {
        handler.handleValidateShip(this);
    }

    public List<TileCoordinates> getInvalidTiles() {
        return invalidTiles;
    }

    public boolean isValid() {
        return isValid;
    }
}
