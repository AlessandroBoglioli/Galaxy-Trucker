package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleCannonTileTest {

    private DoubleCannonTile doubleCannonTile;

    @BeforeEach
    void setUp() {
        doubleCannonTile = new DoubleCannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
    }

    @Test
    void testConstructor() {
        assertNotNull(doubleCannonTile);
        assertEquals(2.0F, doubleCannonTile.getFirePower());
    }

    @Test
    void testSetFirePower() {
        doubleCannonTile.setCannonDirection(Direction.BOTTOM);
        doubleCannonTile.setFirePower();
        assertEquals(1.0F, doubleCannonTile.getFirePower());

        doubleCannonTile.setCannonDirection(Direction.TOP);
        doubleCannonTile.setFirePower();
        assertEquals(2.0F, doubleCannonTile.getFirePower());
    }

    @Test
    void testRotateClockwise() {
        doubleCannonTile.rotateClockwise();
        assertEquals(Direction.RIGHT, doubleCannonTile.getCannonDirection());
        assertEquals(1.0F, doubleCannonTile.getFirePower());
    }

    @Test
    void testRotateCounterClockwise() {
        doubleCannonTile.rotateCounterClockwise();
        assertEquals(Direction.LEFT, doubleCannonTile.getCannonDirection());
        assertEquals(1.0F, doubleCannonTile.getFirePower());
    }
}