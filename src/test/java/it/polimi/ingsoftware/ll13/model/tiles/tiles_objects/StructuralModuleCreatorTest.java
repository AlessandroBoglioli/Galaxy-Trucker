package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StructuralModuleCreatorTest {

    private final StructuralModuleCreator creator = new StructuralModuleCreator();

    @Test
    void createTile_shouldReturnStructuralModuleTileWithValidConnectors() throws Exception {
        JSONObject data = createValidStructuralModuleData();

        Tile tile = creator.createTile(data);
        assertInstanceOf(StructuralModuleTile.class, tile);
        StructuralModuleTile moduleTile = (StructuralModuleTile) tile;
        assertAll(
                () -> assertEquals(ConnectorType.SMOOTH, moduleTile.getTopConnector().getType()),
                () -> assertEquals(ConnectorType.DOUBLE, moduleTile.getRightConnector().getType()),
                () -> assertEquals(ConnectorType.SINGLE, moduleTile.getBottomConnector().getType()),
                () -> assertEquals(ConnectorType.UNIVERSAL, moduleTile.getLeftConnector().getType())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectorCases")
    void createTile_shouldThrowForInvalidConnectors(JSONArray connectors) {
        JSONObject data = new JSONObject();
        data.put("image", "");
        data.put("connectors", connectors);
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid", "123"})
    void createTile_shouldThrowForInvalidConnectorTypes(String invalidType) {
        JSONObject data = new JSONObject();
        JSONArray connectors = new JSONArray()
                .put(createConnector(invalidType))
                .put(createConnector("DOUBLE"))
                .put(createConnector("SINGLE"))
                .put(createConnector("UNIVERSAL"));
        data.put("image", "");
        data.put("connectors", connectors);
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForMissingConnectorsField() {
        JSONObject data = new JSONObject();
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(null));
    }

    // Helper methods
    private static JSONObject createValidStructuralModuleData() {
        JSONObject data = new JSONObject();
        JSONArray connectors = new JSONArray()
                .put(createConnector("SMOOTH"))
                .put(createConnector("DOUBLE"))
                .put(createConnector("SINGLE"))
                .put(createConnector("UNIVERSAL"));
        data.put("image", "");
        data.put("connectors", connectors);
        return data;
    }

    private static JSONObject createConnector(String type) {
        return new JSONObject().put("type", type);
    }

    private static Stream<JSONArray> provideInvalidConnectorCases() {
        return Stream.of(
                new JSONArray(), // empty array
                new JSONArray().put(createConnector("SMOOTH")), // only 1 connector
                new JSONArray() // missing 4th connector
                        .put(createConnector("SMOOTH"))
                        .put(createConnector("DOUBLE"))
                        .put(createConnector("SINGLE"))
        );
    }
}