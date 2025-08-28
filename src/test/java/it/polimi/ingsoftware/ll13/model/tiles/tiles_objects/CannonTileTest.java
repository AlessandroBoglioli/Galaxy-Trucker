package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTileTest {

    private CannonTile cannonTile;

    @BeforeEach
    void setUp() {
        cannonTile = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
    }

    @Test
    void testConstructor() {
        assertNotNull(cannonTile);
        assertEquals(1.0F, cannonTile.getFirePower());
    }

    @Test
    void testSetFirePower() {
        cannonTile.setCannonDirection(Direction.BOTTOM);
        cannonTile.setFirePower();
        assertEquals(0.5F, cannonTile.getFirePower());

        cannonTile.setCannonDirection(Direction.TOP);
        cannonTile.setFirePower();
        assertEquals(1F, cannonTile.getFirePower());
    }

    @Test
    void testRotateClockwise() {
        cannonTile.rotateClockwise();
        assertEquals(Direction.RIGHT, cannonTile.getCannonDirection());
        assertEquals(0.5F, cannonTile.getFirePower());
    }

    @Test
    void testRotateCounterClockwise() {
        cannonTile.rotateCounterClockwise();
        assertEquals(Direction.LEFT, cannonTile.getCannonDirection());
        assertEquals(0.5F, cannonTile.getFirePower());
    }
}