package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
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

class MotorCreatorTest {

    private final MotorCreator creator = new MotorCreator();

    @Test
    void createTile_shouldReturnMotorTileWithValidData() throws Exception {
        JSONObject data = createValidMotorData();

        Tile tile = creator.createTile(data);
        assertInstanceOf(MotorTile.class, tile);
        MotorTile motorTile = (MotorTile) tile;
        assertAll(
                () -> assertEquals(ConnectorType.SMOOTH, motorTile.getTopConnector().getType()),
                () -> assertEquals(ConnectorType.DOUBLE, motorTile.getRightConnector().getType()),
                () -> assertEquals(ConnectorType.SINGLE, motorTile.getBottomConnector().getType()),
                () -> assertEquals(ConnectorType.UNIVERSAL, motorTile.getLeftConnector().getType()),
                () -> assertEquals(Direction.BOTTOM, motorTile.getMotorDirection())
        );
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void createTile_shouldAlwaysUseBottomDirection(Direction direction) throws Exception {
        JSONObject data = createValidMotorData();
        data.put("image", "");
        data.put("direction", direction.name());

        MotorTile motorTile = (MotorTile) creator.createTile(data);
        assertEquals(Direction.BOTTOM, motorTile.getMotorDirection());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectorCases")
    void createTile_shouldThrowForInvalidConnectors(JSONArray connectors) {
        JSONObject data = new JSONObject();
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
    private static JSONObject createValidMotorData() {
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