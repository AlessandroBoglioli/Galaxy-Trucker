package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    private Tile tile;

    // Common initialization for all tests
    @BeforeEach
    void setUp() {
        tile = new Tile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SMOOTH);
    }

    @Test
    void testRotateClockwise() {
        tile.rotateClockwise();
        assertEquals(Direction.TOP, tile.getConnectors()[0].getDirection(), "The connector at position 0 (TOP)");
        assertEquals(Direction.RIGHT, tile.getConnectors()[1].getDirection(), "The connector at position 1 (RIGHT)");
        assertEquals(Direction.BOTTOM, tile.getConnectors()[2].getDirection(), "The connector at position 2 (BOTTOM)");
        assertEquals(Direction.LEFT, tile.getConnectors()[3].getDirection(), "The connector at position 3 (LEFT)");
        //-----------
        assertEquals(ConnectorType.SMOOTH, tile.getConnectors()[0].getType(), "After clockwise rotation, TOP should be SMOOTH (old LEFT)");
        assertEquals(ConnectorType.SINGLE, tile.getConnectors()[1].getType(), "After clockwise rotation, RIGHT should be SINGLE (old TOP)");
        assertEquals(ConnectorType.DOUBLE, tile.getConnectors()[2].getType(), "After clockwise rotation, BOTTOM should be DOUBLE (old RIGHT)");
        assertEquals(ConnectorType.UNIVERSAL,tile.getConnectors()[3].getType(), "After clockwise rotation, LEFT should be UNIVERSAL (old BOTTOM)");
    }

    @Test
    void testRotateCounterClockwise() {
        tile.rotateCounterClockwise();
        assertEquals(Direction.TOP, tile.getConnectors()[0].getDirection(), "The connector at position 0 (TOP) should become RIGHT after rotation.");
        assertEquals(Direction.RIGHT, tile.getConnectors()[1].getDirection(), "The connector at position 1 (RIGHT) should become BOTTOM after rotation.");
        assertEquals(Direction.BOTTOM, tile.getConnectors()[2].getDirection(), "The connector at position 2 (BOTTOM) should become LEFT after rotation.");
        assertEquals(Direction.LEFT, tile.getConnectors()[3].getDirection(), "The connector at position 3 (LEFT) should become TOP after rotation.");
        //------
        assertEquals(ConnectorType.DOUBLE,tile.getConnectors()[0].getType() , "After counter-clockwise rotation, TOP should be DOUBLE (old RIGHT)");
        assertEquals(ConnectorType.UNIVERSAL,tile.getConnectors()[1].getType() , "After counter-clockwise rotation, RIGHT should be UNIVERSAL (old BOTTOM)");
        assertEquals(ConnectorType.SMOOTH,tile.getConnectors()[2].getType() , "After counter-clockwise rotation, BOTTOM should be SMOOTH (old LEFT)");
        assertEquals(ConnectorType.SINGLE,tile.getConnectors()[3].getType() , "After counter-clockwise rotation, LEFT should be SINGLE (old TOP)");
    }

    @Test
    void testMultipleRotations() {
        tile.rotateMultiple(3);
        assertEquals(Direction.TOP, tile.getConnectors()[0].getDirection(), "Index 0 should be TOP");
        assertEquals(Direction.RIGHT, tile.getConnectors()[1].getDirection(), "Index 1 should be RIGHT");
        assertEquals(Direction.BOTTOM, tile.getConnectors()[2].getDirection(), "Index 2 should be BOTTOM");
        assertEquals(Direction.LEFT, tile.getConnectors()[3].getDirection(), "Index 3 should be LEFT");
        //------
        assertEquals(ConnectorType.DOUBLE,tile.getConnectors()[0].getType() , "After rotateMultiple(3), TOP should be DOUBLE (old index 1)");
        assertEquals(ConnectorType.UNIVERSAL,tile.getConnectors()[1].getType() , "After rotateMultiple(3), RIGHT should be UNIVERSAL (old index 2)");
        assertEquals(ConnectorType.SMOOTH,tile.getConnectors()[2].getType(), "After rotateMultiple(3), BOTTOM should be SMOOTH (old index 3)");
        assertEquals(ConnectorType.SINGLE,tile.getConnectors()[3].getType() , "After rotateMultiple(3), LEFT should be SINGLE (old index 0)");
    }

    @Test
    void testCreateWithNullConnectors() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Tile("",null, null, null, null);
        }, "An exception should be thrown if any connector type is null when creating a tile.");
    }
}