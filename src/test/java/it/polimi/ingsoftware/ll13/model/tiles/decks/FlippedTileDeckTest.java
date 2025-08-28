package it.polimi.ingsoftware.ll13.model.tiles.decks;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.StartingCabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlippedTileDeckTest {

    private FlippedTileDeck flippedDeck;
    private StartingCabinTile testTile1;
    private StartingCabinTile testTile2;

    @BeforeEach
    public void setUp() {
        flippedDeck = new FlippedTileDeck();
        assertNotNull(flippedDeck);
        testTile1 = new StartingCabinTile("", PlayerColors.RED);
        testTile2 = new StartingCabinTile("", PlayerColors.BLUE);
    }

    @Test
    public void addTile_singleTile() {
        flippedDeck.addTile(testTile1);
        assertEquals(1, flippedDeck.getSize());
        assertEquals(testTile1, flippedDeck.getTilePosition(0));
    }

    @Test
    public void addTile_multipleTiles() {
        flippedDeck.addTile(testTile1);
        flippedDeck.addTile(testTile2);
        assertEquals(2, flippedDeck.getSize());
        assertTrue(flippedDeck.getTiles().contains(testTile1));
        assertTrue(flippedDeck.getTiles().contains(testTile2));
    }

    @Test
    public void addTile_exceedMaxSize() {
        for (int i = 0; i < flippedDeck.getMaxSize(); i++) {
            flippedDeck.addTile(new StartingCabinTile("", PlayerColors.RED));
        }
        assertThrows(IllegalStateException.class, () -> flippedDeck.addTile(testTile1));
        assertEquals(flippedDeck.getMaxSize(), flippedDeck.getSize());
    }

    @Test
    public void getTileObject_existingTile() {
        flippedDeck.addTile(testTile1);
        Tile retrievedTile = flippedDeck.getTileObject(testTile1);
        assertEquals(testTile1, retrievedTile);
        assertEquals(0, flippedDeck.getSize());
    }

    @Test
    public void getTileObject_nonExistingTile() {
        flippedDeck.addTile(testTile1);
        StartingCabinTile nonExistingTile = new StartingCabinTile("", PlayerColors.RED);
        assertThrows(IllegalStateException.class, () -> flippedDeck.getTileObject(nonExistingTile));
        assertEquals(1, flippedDeck.getSize()); // Ensure the original tile is still there
    }

    @Test
    public void getTileObject_emptyDeck() {
        assertThrows(IllegalStateException.class, () -> flippedDeck.getTileObject(testTile1));
    }

    @Test
    public void getTilePosition_validIndex() {
        flippedDeck.addTile(testTile1);
        flippedDeck.addTile(testTile2);
        Tile retrievedTile = flippedDeck.getTilePosition(0);
        assertEquals(testTile1, retrievedTile);
        assertEquals(1, flippedDeck.getSize());
        assertEquals(testTile2, flippedDeck.getTilePosition(0));
    }

    @Test
    public void getTilePosition_negativeIndex() {
        flippedDeck.addTile(testTile1);
        assertThrows(IndexOutOfBoundsException.class, () -> flippedDeck.getTilePosition(-1));
        assertEquals(1, flippedDeck.getSize());
    }

    @Test
    public void getTilePosition_outOfBoundsIndex() {
        flippedDeck.addTile(testTile1);
        assertThrows(IndexOutOfBoundsException.class, () -> flippedDeck.getTilePosition(1));
        assertEquals(1, flippedDeck.getSize());
    }



    @Test
    public void getSize_emptyDeck() {
        assertEquals(0, flippedDeck.getSize());
    }

    @Test
    public void getSize_nonEmptyDeck() {
        flippedDeck.addTile(testTile1);
        assertEquals(1, flippedDeck.getSize());
        flippedDeck.addTile(testTile2);
        assertEquals(2, flippedDeck.getSize());
    }

    @Test
    public void getTiles_emptyDeck() {
        assertTrue(flippedDeck.getTiles().isEmpty());
    }

    @Test
    public void getTiles_nonEmptyDeck() {
        flippedDeck.addTile(testTile1);
        flippedDeck.addTile(testTile2);
        List<Tile> tiles = flippedDeck.getTiles();
        assertEquals(2, tiles.size());
        assertTrue(tiles.contains(testTile1));
        assertTrue(tiles.contains(testTile2));
    }

    @Test
    public void getMaxSize() {
        assertEquals(156, flippedDeck.getMaxSize());
    }
}