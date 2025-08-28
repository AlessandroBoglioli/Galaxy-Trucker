package it.polimi.ingsoftware.ll13.utils.json;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JsonValidationTileUtilsTest {

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extractAndValidateConnector ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidateConnector_shouldReturnValidConnectors() throws Exception {
        JSONArray connectors = new JSONArray()
                .put(new JSONObject().put("type", "SMOOTH"))
                .put(new JSONObject().put("type", "DOUBLE"));

        List<ConnectorType> result = JsonValidationTileUtils.extractAndValidateConnector(connectors);

        assertEquals(2, result.size());
        assertEquals(ConnectorType.SMOOTH, result.get(0));
        assertEquals(ConnectorType.DOUBLE, result.get(1));
    }

    @ParameterizedTest
    @MethodSource("provideNullAndEmptyJSONArrays")
    void extractAndValidateConnector_shouldThrowForNullOrEmptyArray(JSONArray input) {
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidateConnector(input));
    }

    private static Stream<Arguments> provideNullAndEmptyJSONArrays() {
        return Stream.of(
                Arguments.of((JSONArray) null),
                Arguments.of(new JSONArray())
        );
    }

    @Test
    void extractAndValidateConnector_shouldThrowForInvalidConnectorType() {
        JSONArray connectors = new JSONArray()
                .put(new JSONObject().put("type", "INVALID_TYPE"));

        assertThrows(IllegalArgumentException.class,
                () -> JsonValidationTileUtils.extractAndValidateConnector(connectors));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extractAndValidateCompartmentType ~~~~~~~~~~~~~~~~~~~~~~~~
    @ParameterizedTest
    @EnumSource(CompartmentType.class)
    void extractAndValidateCompartmentType_shouldReturnValidType(CompartmentType type) throws Exception {
        JSONObject input = new JSONObject().put("compartment_type", type.name());
        assertEquals(type, JsonValidationTileUtils.extractAndValidateCompartmentType(input));
    }

    @Test
    void extractAndValidateCompartmentType_shouldThrowForMissingField() {
        JSONObject input = new JSONObject();
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidateCompartmentType(input));
    }

    @Test
    void extractAndValidateCompartmentType_shouldThrowForInvalidType() {
        JSONObject input = new JSONObject().put("compartment_type", "INVALID");
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidateCompartmentType(input));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extractAndValidatePlayerColors ~~~~~~~~~~~~~~~~~~~~~~~~
    @ParameterizedTest
    @EnumSource(PlayerColors.class)
    void extractAndValidatePlayerColors_shouldReturnValidColor(PlayerColors color) throws Exception {
        JSONObject input = new JSONObject().put("color", color.name());
        assertEquals(color, JsonValidationTileUtils.extractAndValidatePlayerColors(input));
    }

    @Test
    void extractAndValidatePlayerColors_shouldThrowForMissingField() {
        JSONObject input = new JSONObject();
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidatePlayerColors(input));
    }

    @Test
    void extractAndValidatePlayerColors_shouldThrowForInvalidColor() {
        JSONObject input = new JSONObject().put("color", "INVALID");
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidatePlayerColors(input));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extractAndValidateVitalSupportColor ~~~~~~~~~~~~~~~~~~~~~~~~
    @ParameterizedTest
    @EnumSource(VitalSupportColor.class)
    void extractAndValidateVitalSupportColor_shouldReturnValidColor(VitalSupportColor color) throws Exception {
        JSONObject input = new JSONObject().put("vital_support_color", color.name());
        assertEquals(color, JsonValidationTileUtils.extractAndValidateVitalSupportColor(input));
    }

    @Test
    void extractAndValidateVitalSupportColor_shouldThrowForMissingField() {
        JSONObject input = new JSONObject();
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidateVitalSupportColor(input));
    }

    @Test
    void extractAndValidateVitalSupportColor_shouldThrowForInvalidColor() {
        JSONObject input = new JSONObject().put("vital_support_color", "INVALID");
        assertThrows(JSONException.class,
                () -> JsonValidationTileUtils.extractAndValidateVitalSupportColor(input));
    }
}