package it.polimi.ingsoftware.ll13.model.tiles.decks;

/**
 * This class implements the Singleton design pattern for the TileDeck and the FlippedTileDeck.
 */
public class TileDecksManager {

    private static TileDeck tileDeck = null;
    private static FlippedTileDeck flippedTileDeck = null;

    public TileDecksManager() {
        if (tileDeck == null) {
            tileDeck = new TileDeck();
        }
        if (flippedTileDeck == null) {
            flippedTileDeck = new FlippedTileDeck();
        }
    }

    /**
     * If the TileDeck was not already created it creates it and returns it.
     * @return Returns the istance of the TileDeck
     */
    public TileDeck getTileDeck() {
        return tileDeck;
    }

    /**
     * If the FlippedTileDeck was not already created it creates it and returns it.
     * @return Returns the istance of the FlippedTileDeck
     */
    public FlippedTileDeck getFlippedTileDeck() {
        return flippedTileDeck;
    }

}
