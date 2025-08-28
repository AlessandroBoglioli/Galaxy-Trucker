package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StructuralModuleTileTest {

    private StructuralModuleTile structuralModuleTile;

    @BeforeEach
    void setUp() {
        structuralModuleTile = new StructuralModuleTile(
                "",
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL,
                ConnectorType.UNIVERSAL
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(structuralModuleTile);
        assertEquals(ConnectorType.UNIVERSAL, structuralModuleTile.getConnectors()[0].getType());
        assertEquals(ConnectorType.UNIVERSAL, structuralModuleTile.getConnectors()[1].getType());
        assertEquals(ConnectorType.UNIVERSAL, structuralModuleTile.getConnectors()[2].getType());
        assertEquals(ConnectorType.UNIVERSAL, structuralModuleTile.getConnectors()[3].getType());
    }
}