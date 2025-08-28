package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VitalSupportTileTest {

    private VitalSupportTile vitalSupportTile;

    @BeforeEach
    void setUp() {
        vitalSupportTile = new VitalSupportTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                VitalSupportColor.PURPLE
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(vitalSupportTile);
        assertEquals(ConnectorType.UNIVERSAL, vitalSupportTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, vitalSupportTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, vitalSupportTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, vitalSupportTile.getConnectors()[3].getType());
        assertEquals(VitalSupportColor.PURPLE, vitalSupportTile.getVitalSupportColor());
    }
}