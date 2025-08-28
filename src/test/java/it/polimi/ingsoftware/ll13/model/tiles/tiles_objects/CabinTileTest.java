package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_levels.LevelTwoShip;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CabinTileTest {

    private CabinTile cabinTile;

    @BeforeEach
    void setUp() {
        // Initialize a CabinTile with valid parameters
        cabinTile = new CabinTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL
        );
    }

    @Test
    void testConstructor() {
        // Verify that the connectors are initialized correctly
        Connector[] connectors = cabinTile.getConnectors();
        assertNotNull(connectors, "Connectors array should not be null.");
        assertEquals(4, connectors.length, "There should be 4 connectors.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[0].getType(), "Top connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[1].getType(), "Right connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[2].getType(), "Bottom connector type should be UNIVERSAL.");
        assertEquals(ConnectorType.UNIVERSAL, connectors[3].getType(), "Left connector type should be UNIVERSAL.");
    }

    @Test
    void testInvalidConnectorTypes() {
        // Verify that an exception is thrown if any connector type is null
        assertThrows(IllegalArgumentException.class, () -> {
            new CabinTile(
                    "",
                    null, // invalid connector
                    ConnectorType.UNIVERSAL,
                    ConnectorType.UNIVERSAL,
                    ConnectorType.UNIVERSAL
            );
        }, "An exception should be thrown if any connector type is null.");
    }

    @Test
    void testAddingHumansToCabin(){
        CabinTile cabinTile = new CabinTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH);
        cabinTile.addCrewMember(new Human());
        assertEquals(2,cabinTile.getCrewCount());
        assertInstanceOf(Human.class, cabinTile.getCrewMembers()[0]);
        assertInstanceOf(Human.class, cabinTile.getCrewMembers()[1]);
    }
    @Test
    void testAlienInCabin(){
        CabinTile cabinTile = new CabinTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH);
        Alien alienToAdd = new Alien(AlienColor.PURPLE);
        boolean added = cabinTile.addCrewMember(alienToAdd);
        assertTrue(added);
        assertEquals(1,cabinTile.getCrewCount());
        boolean added2 = cabinTile.addCrewMember(new Alien(AlienColor.YELLOW));
        assertFalse(added2);
        assertEquals(1,cabinTile.getCrewCount());
        boolean added3 = cabinTile.addCrewMember(new Human());
        assertFalse(added3);
        assertEquals(1,cabinTile.getCrewCount());
        Alien alien = cabinTile.getAlien();
        assertEquals(alien.getColor(),alienToAdd.getColor());
    }
    @Test
    void testEliminateCrewMember() {
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        CabinTile cabin = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.SMOOTH);
        cabin.addCrewMember(new Human());
        assertEquals(2,cabin.getCrewCount());
        ship.getShipLayout().addTile(cabin, 2, 2);
        cabin.eliminateCrewMembers(1);
        assertEquals(1, cabin.getCrewCount());
        cabin.eliminateCrewMembers(1);
        assertEquals(0, cabin.getCrewCount());
    }
}