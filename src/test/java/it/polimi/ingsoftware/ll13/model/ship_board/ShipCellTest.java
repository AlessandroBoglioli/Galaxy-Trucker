package it.polimi.ingsoftware.ll13.model.ship_board;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShipCellTest {
    @Test
    void testCellInitialization() {
        // Create a ShipCell with no tile and marked as valid.
        ShipCell cell = new ShipCell(null, true);
        assertTrue(cell.isValid(), "Cell should be valid");
        assertNull(cell.getTile(), "Cell should start with no tile");
        assertNull(cell.getTop(), "Initial top neighbor should be null");
        assertNull(cell.getBottom(), "Initial bottom neighbor should be null");
        assertNull(cell.getLeft(), "Initial left neighbor should be null");
        assertNull(cell.getRight(), "Initial right neighbor should be null");
    }
    @Test
    void testSetAndGetTile() {
        Tile tile = new Tile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SMOOTH);
        ShipCell cell = new ShipCell(null, true);
        assertFalse(cell.isOccupied(), "Cell should not be occupied initially");
        cell.setTile(tile);
        assertTrue(cell.isOccupied(), "Cell should be occupied after setting a tile");
        assertEquals(tile, cell.getTile(), "getTile() should return the tile that was set");
        cell.clearTile();
        assertFalse(cell.isOccupied(), "Cell should not be occupied after clearing the tile");
        assertNull(cell.getTile(), "Tile should be null after clearing");
    }
    @Test
    void testNeighborSettersAndGetters() {
        ShipCell cell = new ShipCell(null, true);
        ShipCell top = new ShipCell(null, true);
        ShipCell bottom = new ShipCell(null, true);
        ShipCell left = new ShipCell(null, true);
        ShipCell right = new ShipCell(null, true);
        cell.setTop(top);
        cell.setBottom(bottom);
        cell.setLeft(left);
        cell.setRight(right);
        assertEquals(top, cell.getTop(), "Top neighbor should be set correctly");
        assertEquals(bottom, cell.getBottom(), "Bottom neighbor should be set correctly");
        assertEquals(left, cell.getLeft(), "Left neighbor should be set correctly");
        assertEquals(right, cell.getRight(), "Right neighbor should be set correctly");
    }
}
