package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BatteryStorageTileTest {

    private BatteryStorageTile batteryStorageTile;

    @BeforeEach
    void setUp() {
        // Initialize a BatteryStorageTile with valid parameters
        batteryStorageTile = new BatteryStorageTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                10
        );
    }

    @Test
    void testConstructor() {
        // Verify that the battery capacity and number are initialized correctly
        assertEquals(10, batteryStorageTile.getBatteryCapacity(), "Battery capacity should be 10.");
        assertEquals(10, batteryStorageTile.getBatteryNumber(), "Battery number should be equal to capacity at initialization.");
    }

    @Test
    void testGetConnectors() {
        // Verify that the connectors are initialized correctly
        Connector[] connectors = batteryStorageTile.getConnectors();
        assertNotNull(connectors, "Connectors array should not be null.");
        assertEquals(4, connectors.length, "There should be 4 connectors.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[0].getType(), "Top connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[1].getType(), "Right connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[2].getType(), "Bottom connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[3].getType(), "Left connector type should be UNIVERSAL.");
    }

    @Test
    void testInvalidBatteryCapacity() {
        // Verify that an exception is thrown if battery capacity is negative
        assertThrows(IllegalArgumentException.class, () -> {
            new BatteryStorageTile(
                    "",
                    ConnectorType.UNIVERSAL,
                    ConnectorType.UNIVERSAL,
                    ConnectorType.UNIVERSAL,
                    ConnectorType.UNIVERSAL,
                    -5
            );
        }, "An exception should be thrown if battery capacity is negative.");
    }
}