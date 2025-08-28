package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
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

class CargoHoldCreatorTest {

    private final CargoHoldCreator creator = new CargoHoldCreator();

    @Test
    void createTile_shouldReturnCargoHoldTileWithValidData() throws Exception {
        JSONObject data = createValidCargoHoldData();

        Tile tile = creator.createTile(data);
        assertInstanceOf(CargoHoldTile.class, tile);
        CargoHoldTile cargoHoldTile = (CargoHoldTile) tile;
        assertAll(
                () -> assertEquals(ConnectorType.SMOOTH, cargoHoldTile.getTopConnector().getType()),
                () -> assertEquals(ConnectorType.DOUBLE, cargoHoldTile.getRightConnector().getType()),
                () -> assertEquals(ConnectorType.SINGLE, cargoHoldTile.getBottomConnector().getType()),
                () -> assertEquals(ConnectorType.UNIVERSAL, cargoHoldTile.getLeftConnector().getType()),
                () -> assertEquals(CompartmentType.BLUE, cargoHoldTile.getCompartmentType()),
                () -> assertEquals(5, cargoHoldTile.getCompartmentCapacity())
        );
    }

    @ParameterizedTest
    @EnumSource(CompartmentType.class)
    void createTile_shouldAcceptAllCompartmentTypes(CompartmentType type) throws Exception {
        JSONObject data = createValidCargoHoldData();
        data.put("image", "");
        data.put("compartment_type", type.name());

        CargoHoldTile cargoHoldTile = (CargoHoldTile) creator.createTile(data);
        assertEquals(type, cargoHoldTile.getCompartmentType());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 100})
    void createTile_shouldAcceptValidCapacityValues(int capacity) throws Exception {
        JSONObject data = createValidCargoHoldData();
        data.put("image", "");
        data.put("compartment_capacity", capacity);

        CargoHoldTile cargoHoldTile = (CargoHoldTile) creator.createTile(data);
        assertEquals(capacity, cargoHoldTile.getCompartmentCapacity());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConnectorCases")
    void createTile_shouldThrowForInvalidConnectors(JSONArray connectors) {
        JSONObject data = createValidCargoHoldData();
        data.put("connectors", connectors);

        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid", "123"})
    void createTile_shouldThrowForInvalidConnectorTypes(String invalidType) {
        JSONObject data = createValidCargoHoldData();
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
    void createTile_shouldThrowForMissingCompartmentType() {
        JSONObject data = createValidCargoHoldData();
        data.remove("compartment_type");
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForMissingCapacity() {
        JSONObject data = createValidCargoHoldData();
        data.remove("compartment_capacity");
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void createTile_shouldThrowForNonPositiveCapacity(int invalidCapacity) {
        JSONObject data = createValidCargoHoldData();
        data.put("image", "");
        data.put("compartment_capacity", invalidCapacity);
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(null));
    }

    // Helper methods
    private static JSONObject createValidCargoHoldData() {
        JSONObject data = new JSONObject();
        JSONArray connectors = new JSONArray()
                .put(createConnector("SMOOTH"))
                .put(createConnector("DOUBLE"))
                .put(createConnector("SINGLE"))
                .put(createConnector("UNIVERSAL"));
        data.put("image", "");
        data.put("connectors", connectors);
        data.put("compartment_type", "BLUE");
        data.put("compartment_capacity", 5);
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