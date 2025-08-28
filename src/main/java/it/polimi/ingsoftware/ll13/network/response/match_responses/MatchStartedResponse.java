package it.polimi.ingsoftware.ll13.network.response.match_responses;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.StartingCabinTile;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.io.Serial;
import java.util.List;

public class MatchStartedResponse extends Response {
    @Serial
    private static final long serialVersionUID=2L;
    private final List<TileCoordinates> initialShipTiles;
    private final boolean isHost;
    private final PlayerColors color;
    private final GameLevel level;

    public MatchStartedResponse(List<TileCoordinates> initialShipTiles, boolean isHost, GameLevel level) {
        this.initialShipTiles = initialShipTiles;
        this.isHost = isHost;
        this.color = ((StartingCabinTile)initialShipTiles.getFirst().getTile()).getColor();
        this.level = level;
    }

    @Override
    public void execute(Handler handler) {
       handler.handleMatchStartedResponse(this);
    }

    public List<TileCoordinates> getInitialShipTiles() {
        return initialShipTiles;
    }

    public boolean isHost() {
        return isHost;
    }

    public PlayerColors getColor() {
        return color;
    }

    public GameLevel getLevel() {
        return level;
    }
}
