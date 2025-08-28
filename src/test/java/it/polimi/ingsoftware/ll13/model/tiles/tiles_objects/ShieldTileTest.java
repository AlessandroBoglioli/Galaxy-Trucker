package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTileTest {

    private ShieldTile shieldTile;

    @BeforeEach
    void setUp() {
        shieldTile = new ShieldTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                Direction.TOP,
                Direction.BOTTOM // We know this could not happen...
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(shieldTile);
        assertEquals(ConnectorType.UNIVERSAL, shieldTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, shieldTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, shieldTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, shieldTile.getConnectors()[3].getType());
        assertEquals(Direction.TOP, shieldTile.getDirection1());
        assertEquals(Direction.BOTTOM, shieldTile.getDirection2());
    }

    @Test
    void testConstructorWithNullDirection() {
        assertThrows(IllegalArgumentException.class, () ->
                new ShieldTile(
                        "",
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        null,
                        Direction.BOTTOM
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new ShieldTile(
                        "",
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        ConnectorType.UNIVERSAL,
                        Direction.TOP,
                        null
                )
        );
    }

    @Test
    void testGetDirection1() {
        assertEquals(Direction.TOP, shieldTile.getDirection1());
    }

    @Test
    void testGetDirection2() {
        assertEquals(Direction.BOTTOM, shieldTile.getDirection2());
    }

    @Test
    void testSetDirection1() {
        shieldTile.setDirection1(Direction.LEFT);
        assertEquals(Direction.LEFT, shieldTile.getDirection1());

        assertThrows(IllegalArgumentException.class, () -> shieldTile.setDirection1(null));
    }

    @Test
    void testSetDirection2() {
        shieldTile.setDirection2(Direction.RIGHT);
        assertEquals(Direction.RIGHT, shieldTile.getDirection2());

        assertThrows(IllegalArgumentException.class, () -> shieldTile.setDirection2(null));
    }

    @Test
    void testRotateShieldsClockwise() {
        shieldTile.rotateShieldsClockwise();
        assertEquals(Direction.RIGHT, shieldTile.getDirection1());
        assertEquals(Direction.LEFT, shieldTile.getDirection2());

        shieldTile.rotateShieldsClockwise();
        assertEquals(Direction.BOTTOM, shieldTile.getDirection1());
        assertEquals(Direction.TOP, shieldTile.getDirection2());

        shieldTile.rotateShieldsClockwise();
        assertEquals(Direction.LEFT, shieldTile.getDirection1());
        assertEquals(Direction.RIGHT, shieldTile.getDirection2());

        shieldTile.rotateShieldsClockwise();
        assertEquals(Direction.TOP, shieldTile.getDirection1());
        assertEquals(Direction.BOTTOM, shieldTile.getDirection2());
    }

    @Test
    void testRotateShieldsCounterClockwise() {
        shieldTile.rotateShieldsCounterClockwise();
        assertEquals(Direction.LEFT, shieldTile.getDirection1());
        assertEquals(Direction.RIGHT, shieldTile.getDirection2());

        shieldTile.rotateShieldsCounterClockwise();
        assertEquals(Direction.BOTTOM, shieldTile.getDirection1());
        assertEquals(Direction.TOP, shieldTile.getDirection2());

        shieldTile.rotateShieldsCounterClockwise();
        assertEquals(Direction.RIGHT, shieldTile.getDirection1());
        assertEquals(Direction.LEFT, shieldTile.getDirection2());

        shieldTile.rotateShieldsCounterClockwise();
        assertEquals(Direction.TOP, shieldTile.getDirection1());
        assertEquals(Direction.BOTTOM, shieldTile.getDirection2());
    }

    @Test
    void testRotateClockwise() {
        shieldTile.rotateClockwise();
        assertEquals(Direction.RIGHT, shieldTile.getDirection1());
        assertEquals(Direction.LEFT, shieldTile.getDirection2());
    }

    @Test
    void testRotateCounterClockwise() {
        shieldTile.rotateCounterClockwise();
        assertEquals(Direction.LEFT, shieldTile.getDirection1());
        assertEquals(Direction.RIGHT, shieldTile.getDirection2());
    }
    @Test
    public void testSerializationDeserialization() {
        // Step 1: Create an instance of ShieldTile with sample data
        Direction direction1 = Direction.TOP;
        Direction direction2 = Direction.LEFT;
        ShieldTile originalTile = new ShieldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, direction1, direction2);

        try {
            // Step 2: Serialize the object to a byte array
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(originalTile);
            out.flush();
            byte[] serializedData = byteOut.toByteArray();

            // Step 3: Deserialize the object from the byte array
            ByteArrayInputStream byteIn = new ByteArrayInputStream(serializedData);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            ShieldTile deserializedTile = (ShieldTile) in.readObject();

            // Step 4: Verify that the deserialized object has the same state as the original
            assertEquals(originalTile.getDirection1(), deserializedTile.getDirection1(), "The first shield direction should be preserved.");
            assertEquals(originalTile.getDirection2(), deserializedTile.getDirection2(), "The second shield direction should be preserved.");

            // Additional check to ensure the tile's connectors are the same as well
            assertNotNull(deserializedTile.getConnectors(), "Connectors should not be null after deserialization.");
            assertEquals(originalTile.getConnectors().length, deserializedTile.getConnectors().length, "The number of connectors should be the same.");

        } catch (IOException | ClassNotFoundException e) {
            fail("Exception during serialization/deserialization: " + e.getMessage());
        }
    }
}