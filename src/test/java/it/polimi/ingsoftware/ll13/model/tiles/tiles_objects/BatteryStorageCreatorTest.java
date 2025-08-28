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

class BatteryStorageCreatorTest {

    private final BatteryStorageCreator creator = new BatteryStorageCreator();

    @Test
    void createTile_shouldReturnBatteryStorageTileWithValidData() throws Exception {
        JSONObject data = createValidBatteryStorageData();

        Tile tile = creator.createTile(data);

        assertInstanceOf(BatteryStorageTile.class, tile);
        BatteryStorageTile batteryTile = (BatteryStorageTile) tile;
        assertAll(
                () -> assertEquals(ConnectorType.SMOOTH, batteryTile.getTopConnector().getType()),
                () -> assertEquals(ConnectorType.DOUBLE, batteryTile.getRightConnector().getType()),
                () -> assertEquals(ConnectorType.SINGLE, batteryTile.getBottomConnector().getType()),
                () -> assertEquals(ConnectorType.UNIVERSAL, batteryTile.getLeftConnector().getType()),
                () -> assertEquals(100, batteryTile.getBatteryCapacity())
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectorCases")
    void createTile_shouldThrowForInvalidConnectors(JSONArray connectors) {
        JSONObject data = new JSONObject();
        data.put("image", "");
        data.put("connectors", connectors);
        data.put("battery_capacity", 100);

        assertThrows(JSONException.class, () -> creator.createTile(data));
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
        data.put("battery_capacity", 100);

        assertThrows(IllegalArgumentException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForMissingBatteryCapacity() {
        JSONObject data = createValidBatteryStorageData();
        data.remove("battery_capacity");

        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "true", "null"})
    void createTile_shouldThrowForNonNumericBatteryCapacity(String invalidValue) {
        JSONObject data = createValidBatteryStorageData();
        data.put("battery_capacity", invalidValue);

        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNegativeBatteryCapacity() {
        JSONObject data = createValidBatteryStorageData();
        data.put("battery_capacity", -10);

        assertThrows(IllegalArgumentException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(null));
    }

    // Helper methods
    private static JSONObject createValidBatteryStorageData() {
        JSONObject data = new JSONObject();
        JSONArray connectors = new JSONArray()
                .put(createConnector("SMOOTH"))
                .put(createConnector("DOUBLE"))
                .put(createConnector("SINGLE"))
                .put(createConnector("UNIVERSAL"));
        data.put("image", "");
        data.put("connectors", connectors);
        data.put("battery_capacity", 100);
        return data;
    }

    private static JSONObject createConnector(String type) {
        return new JSONObject().put("type", type);
    }
}