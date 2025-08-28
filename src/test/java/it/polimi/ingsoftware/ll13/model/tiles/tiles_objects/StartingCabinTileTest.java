package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StartingCabinTileTest {

    private StartingCabinTile startingCabinTile;

    @BeforeEach
    void setUp() {
        startingCabinTile = new StartingCabinTile("", PlayerColors.RED);
    }

    @Test
    void testConstructor() {
        assertNotNull(startingCabinTile);
        assertEquals(PlayerColors.RED, startingCabinTile.getColor());
        assertEquals(ConnectorType.UNIVERSAL, startingCabinTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, startingCabinTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, startingCabinTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, startingCabinTile.getConnectors()[3].getType());
    }

    @Test
    void testConstructorWithNullColor() {
        assertThrows(IllegalArgumentException.class, () -> new StartingCabinTile("", null));
    }
}