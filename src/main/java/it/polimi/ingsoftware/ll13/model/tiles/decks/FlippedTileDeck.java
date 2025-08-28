package it.polimi.ingsoftware.ll13.model.tiles.decks;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the tiles that were seen by a player but not used on their ship.
 * All this tiles are placed face up on the table in the real life game.
 */
public class FlippedTileDeck {

    private final List<Tile> tiles;
    private static final int maxSize = 156;

    public FlippedTileDeck() {
        tiles = new ArrayList<Tile>();
    }

    /**
     * Whenever a player draws a card from the TileDeck and not uses it, that tile will be inserted here.
     * @param tile Represent the tile to add into the Deck.
     */
    public synchronized void addTile(@NotNull Tile tile) {
        if (tiles.size() >= maxSize){
            throw new IllegalStateException("Too many tiles");
        }
        tiles.add(tile);
    }

    /**
     * This method is used to get a specific tile from the deck, since you already know witch tile you want
     * it only gets eliminated from the deck.
     * @param tile Represent the tile I want to get
     * @return Returns the tile that I selected
     */
    public synchronized Tile getTileObject(@NotNull Tile tile) {
        if (tiles.isEmpty()){
            throw new IllegalStateException("Tiles is empty");
        }
        if (tiles.remove(tile)){
            return tile;
        }
        throw new IllegalStateException("Tile not found");
    }

    /**
     * This method is used to get a tile from the deck by their position.
     * @param index Represent the position of the tile I want to get
     * @return Returns the tile that I selected
     */
    public synchronized Tile getTilePosition(int index) {
        synchronized (this) {
            if (tiles.isEmpty()){
                return null;
            }
            if (index < 0 || index >= tiles.size()){
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
            return tiles.remove(index);
        }
    }


    // ---> Getters <---
    public int getSize(){
        return tiles.size();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    protected int getMaxSize() {
        return maxSize;
    }
}
