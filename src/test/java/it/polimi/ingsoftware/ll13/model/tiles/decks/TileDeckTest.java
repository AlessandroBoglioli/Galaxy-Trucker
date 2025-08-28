package it.polimi.ingsoftware.ll13.model.tiles.decks;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileDeckTest {

    private TileDeck tileDeck;
    TileDecksManager manager;

    @BeforeEach
    void setUp() {
        manager = new TileDecksManager();
        assertNotNull(manager.getTileDeck());
        tileDeck = new TileDeck();
    }

    @Test
    public void constructor_TwoManagerSameDeck(){
        TileDecksManager manager1 = new TileDecksManager();
        assertEquals(manager1.getTileDeck(), manager.getTileDeck());
    }

    @Test
    void constructor_ShouldCreateNonEmptyDeck() {
        assertTrue(tileDeck.getSize() >  0);
        assertEquals(tileDeck.getSize(), 152);
    }

    @Test
    void drawTile_ShouldRemoveTileFromDeck() {
        int initialSize = tileDeck.getSize();
        Tile drawnTile = tileDeck.drawTile();

        assertNotNull(drawnTile);
        assertEquals(initialSize - 1, tileDeck.getSize());
    }



    @Test
    void tiles_ShouldBeUnique() {
        List<Tile> tiles = tileDeck.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                assertNotEquals(tiles.get(i), tiles.get(j));
            }
        }
    }

    @Test
    void deck_ShouldBeShuffled() {
        TileDeck secondDeck = new TileDeck();
        assertNotEquals(tileDeck.getTiles(), secondDeck.getTiles());
    }

    @Test
    void getSize_ShouldReturnCorrectSize() {
        int expectedSize = tileDeck.getTiles().size();
        assertEquals(expectedSize, tileDeck.getSize());
    }
}