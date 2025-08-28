package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotorTileTest {

    private MotorTile motorTile;

    @BeforeEach
    void setUp() {
        motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
    }

    @Test
    void testConstructor() {
        assertNotNull(motorTile);
        assertEquals(ConnectorType.UNIVERSAL, motorTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, motorTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, motorTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, motorTile.getConnectors()[3].getType());
        assertEquals(Direction.BOTTOM, motorTile.getMotorDirection());
        assertEquals(1, motorTile.getThrust());
    }

    @Test
    void testConstructorWithNullDirection() {
        assertThrows(IllegalArgumentException.class, () ->
            new MotorTile(
                    "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                null)
        );
    }

    @Test
    void testGetThrust() {
        assertEquals(1, motorTile.getThrust());

        motorTile = new MotorTile(
                "",
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            ConnectorType.UNIVERSAL,
            Direction.TOP
        );
        assertEquals(0, motorTile.getThrust());
    }

    @Test
    void testRotateClockwise() {
        motorTile.rotateClockwise();
        assertEquals(Direction.LEFT, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateClockwise();
        assertEquals(Direction.TOP, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateClockwise();
        assertEquals(Direction.RIGHT, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateClockwise();
        assertEquals(Direction.BOTTOM, motorTile.getMotorDirection());
        assertEquals(1, motorTile.getThrust());
    }

    @Test
    void testRotateCounterClockwise() {
        motorTile.rotateCounterClockwise();
        assertEquals(Direction.RIGHT, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateCounterClockwise();
        assertEquals(Direction.TOP, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateCounterClockwise();
        assertEquals(Direction.LEFT, motorTile.getMotorDirection());
        assertEquals(0, motorTile.getThrust());

        motorTile.rotateCounterClockwise();
        assertEquals(Direction.BOTTOM, motorTile.getMotorDirection());
        assertEquals(1, motorTile.getThrust());
    }
}