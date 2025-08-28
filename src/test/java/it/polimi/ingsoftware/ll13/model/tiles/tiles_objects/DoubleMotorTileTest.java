package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoubleMotorTileTest {

    private DoubleMotorTile doubleMotorTile;

    @BeforeEach
    void setUp() {
        doubleMotorTile = new DoubleMotorTile(
                "",
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            Direction.BOTTOM
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(doubleMotorTile);
        assertEquals(ConnectorType.UNIVERSAL, doubleMotorTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, doubleMotorTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, doubleMotorTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, doubleMotorTile.getConnectors()[3].getType());
        assertEquals(Direction.BOTTOM, doubleMotorTile.getMotorDirection());
        assertEquals(2, doubleMotorTile.getThrust());
    }

    @Test
    void testGetThrust() {
        assertEquals(2, doubleMotorTile.getThrust());

        doubleMotorTile = new DoubleMotorTile(
                "",
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            Direction.TOP
        );
        assertEquals(0, doubleMotorTile.getThrust());
    }

    @Test
    void testRotateClockwise() {
        doubleMotorTile.rotateClockwise();
        assertEquals(Direction.LEFT, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateClockwise();
        assertEquals(Direction.TOP, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateClockwise();
        assertEquals(Direction.RIGHT, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateClockwise();
        assertEquals(Direction.BOTTOM, doubleMotorTile.getMotorDirection());
        assertEquals(2, doubleMotorTile.getThrust());
    }

    @Test
    void testRotateCounterClockwise() {
        doubleMotorTile.rotateCounterClockwise();
        assertEquals(Direction.RIGHT, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateCounterClockwise();
        assertEquals(Direction.TOP, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateCounterClockwise();
        assertEquals(Direction.LEFT, doubleMotorTile.getMotorDirection());
        assertEquals(0, doubleMotorTile.getThrust());

        doubleMotorTile.rotateCounterClockwise();
        assertEquals(Direction.BOTTOM, doubleMotorTile.getMotorDirection());
        assertEquals(2, doubleMotorTile.getThrust());
    }
}