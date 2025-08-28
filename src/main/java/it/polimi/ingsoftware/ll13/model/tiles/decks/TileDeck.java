package it.polimi.ingsoftware.ll13.model.tiles.decks;

import it.polimi.ingsoftware.ll13.model.json_parser.TileParser;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to represent the 156 tiles that are placed on the table
 * during the ship building phase. (all the tiles are faced down)
 */
public class TileDeck {

    private static final String PATH = "/json/tiles.json";
    private final List<Tile> tiles;

    /**
     * The builder of this class use the json parser to convert the file contained in the attribute PATH
     * into the 156 tiles that can be used to build the ship.
     */
    public TileDeck() {
        tiles = new ArrayList<Tile>();
        TileParser parser = new TileParser();
        InputStream is = getClass().getResourceAsStream(PATH);
        List<Tile> parsedTiles = parser.parseTiles(is);
        tiles.addAll(parsedTiles);
        Collections.shuffle(tiles);
    }

    /**
     * This method is the only action that you can perform on this deck, when you draw a pace down tile,
     * you can either:
     *  - Use it on your ship
     *  - Putting it back on the table face up (FlippedTileDeck)
     * Either way you eliminate that tile from the deck.
     * @return Returns a random tile from this deck, since it was shuffled on creation.
     */
    public Tile drawTile() {
        synchronized (tiles) {
            if (tiles.isEmpty()) {
                return null;
            }
            return tiles.removeLast();
        }
    }

    // ---> Getters <---
    public int getSize(){
        return tiles.size();
    }

    protected List<Tile> getTiles(){
        return tiles;
    }

}