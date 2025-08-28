package it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.tiles.decks.FlippedTileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.decks.TileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class ConstructionPhase extends Response {
    private final int numTiles;
    private final List<Tile> flippedTiles;
    private final List<CardStack> cardStacks;

    public ConstructionPhase(int numTiles, List<Tile> flippedTiles, List<CardStack> cardStacks) {
        this.numTiles = numTiles;
        this.flippedTiles = flippedTiles;
        this.cardStacks = cardStacks;
    }
    @Override
    public void execute(Handler handler) {
        handler.handleConstructionPhase(this);
    }
    public int getNumTiles() {
        return numTiles;
    }
    public List<Tile> getFlippedTiles() {
        return flippedTiles;
    }
    public List<CardStack> getCardStacks() {
        return cardStacks;
    }
}
