package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.tiles.decks.FlippedTileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class UpdatedFlippedDeck extends Response {
    private final List<Tile> flippedTiles;

    public UpdatedFlippedDeck(List<Tile> tiles) {
        this.flippedTiles = tiles;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleUpdateFlipped(this);
    }

    public List<Tile> getFlippedTiles() {
        return flippedTiles;
    }
}
