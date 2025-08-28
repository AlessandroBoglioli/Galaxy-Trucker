package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoHoldTileTest {

    private CargoHoldTile cargoHoldTile;

    @BeforeEach
    void setUp() {
        cargoHoldTile = new CargoHoldTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                CompartmentType.RED,
                3
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(cargoHoldTile);
        assertEquals(ConnectorType.UNIVERSAL, cargoHoldTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, cargoHoldTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, cargoHoldTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, cargoHoldTile.getConnectors()[3].getType());
        assertEquals(CompartmentType.RED, cargoHoldTile.getCompartmentType());
        assertEquals(3, cargoHoldTile.getCompartmentCapacity());
    }

    @Test
    void testGetters() {
        assertEquals(CompartmentType.RED, cargoHoldTile.getCompartmentType());
        assertEquals(3, cargoHoldTile.getCompartmentCapacity());
    }

    @Test
    void testAddCargo() {
        cargoHoldTile.addCargo(CargoColor.RED);
        cargoHoldTile.addCargo(CargoColor.BLUE);
        assertEquals(CargoColor.RED, cargoHoldTile.getCargos().get(0));
        assertEquals(CargoColor.BLUE, cargoHoldTile.getCargos().get(1));
    }

    @Test
    void checkIfExtraCargosAreIgnored() {
        for(int i = 0; i < cargoHoldTile.getCompartmentCapacity(); i++) {
            cargoHoldTile.addCargo(CargoColor.RED);
        }
        // This cargos should not be ignored
        for(int i = 0; i < 5; i++) {
            cargoHoldTile.addCargo(CargoColor.BLUE);
        }

        for(int i = 0; i < cargoHoldTile.getCompartmentCapacity(); i++) {
            assertEquals(CargoColor.RED, cargoHoldTile.getCargos().get(i));
        }
    }
}