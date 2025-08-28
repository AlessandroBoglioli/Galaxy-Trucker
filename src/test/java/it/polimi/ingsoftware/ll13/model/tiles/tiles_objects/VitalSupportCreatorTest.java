package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VitalSupportCreatorTest {

    private final VitalSupportCreator creator = new VitalSupportCreator();

    @Test
    void createTile_shouldReturnVitalSupportTileWithValidData() throws Exception {
        JSONObject data = createValidVitalSupportData();

        Tile tile = creator.createTile(data);
        assertInstanceOf(VitalSupportTile.class, tile);
        VitalSupportTile vitalSupportTile = (VitalSupportTile) tile;
        assertAll(
                () -> assertEquals(ConnectorType.SMOOTH, vitalSupportTile.getTopConnector().getType()),
                () -> assertEquals(ConnectorType.DOUBLE, vitalSupportTile.getRightConnector().getType()),
                () -> assertEquals(ConnectorType.SINGLE, vitalSupportTile.getBottomConnector().getType()),
                () -> assertEquals(ConnectorType.UNIVERSAL, vitalSupportTile.getLeftConnector().getType()),
                () -> assertEquals(VitalSupportColor.PURPLE, vitalSupportTile.getVitalSupportColor())
        );
    }

    @ParameterizedTest
    @EnumSource(VitalSupportColor.class)
    void createTile_shouldAcceptAllVitalSupportColors(VitalSupportColor color) throws Exception {
        JSONObject data = createValidVitalSupportData();
        data.put("image", "");
        data.put("vital_support_color", color.name());

        VitalSupportTile vitalSupportTile = (VitalSupportTile) creator.createTile(data);
        assertEquals(color, vitalSupportTile.getVitalSupportColor());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectorCases")
    void createTile_shouldThrowForInvalidConnectors(JSONArray connectors) {
        JSONObject data = createValidVitalSupportData();
        data.put("connectors", connectors);
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid", "123"})
    void createTile_shouldThrowForInvalidConnectorTypes(String invalidType) {
        JSONObject data = createValidVitalSupportData();
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
    void createTile_shouldThrowForMissingVitalSupportColor() {
        JSONObject data = createValidVitalSupportData();
        data.remove("vital_support_color");
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForMissingConnectorsField() {
        JSONObject data = new JSONObject();
        data.put("vital_support_color", "PURPLE");
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALID", "123"})
    void createTile_shouldThrowForInvalidVitalSupportColor(String invalidColor) {
        JSONObject data = createValidVitalSupportData();
        data.put("vital_support_color", invalidColor);
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    // Helper methods
    private static JSONObject createValidVitalSupportData() {
        JSONObject data = new JSONObject();
        JSONArray connectors = new JSONArray()
                .put(createConnector("SMOOTH"))
                .put(createConnector("DOUBLE"))
                .put(createConnector("SINGLE"))
                .put(createConnector("UNIVERSAL"));
        data.put("image", "");
        data.put("connectors", connectors);
        data.put("vital_support_color", "PURPLE");
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