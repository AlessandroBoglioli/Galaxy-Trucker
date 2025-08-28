package it.polimi.ingsoftware.ll13.utils.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class JsonValidationUtilsTest {

    // ~~~~~~~~~~~~~~~~~~~~~~~~ validate_data_object ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void validateDataObject_shouldThrowWhenNull() {
        assertThrows(IllegalArgumentException.class,
                () -> JsonValidationUtils.validateDataObject(null),
                "Should throw IllegalArgumentException for null input");
    }

    @Test
    void validateDataObject_shouldNotThrowForValidObject() {
        assertDoesNotThrow(
                () -> JsonValidationUtils.validateDataObject(new JSONObject()),
                "Should not throw for valid JSONObject");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ extract_and_validate_positive_int ~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void extractAndValidatePositiveInt_shouldThrowWhenFieldMissing() {
        JSONObject data = new JSONObject();
        assertThrows(JSONException.class,
                () -> JsonValidationUtils.extractAndValidatePositiveInt(data, "missing_field"),
                "Should throw JSONException for missing field");
    }

    @Test
    void extractAndValidatePositiveInt_shouldThrowWhenNotInteger() {
        JSONObject data = new JSONObject().put("invalid", "not_a_number");
        assertThrows(JSONException.class,
                () -> JsonValidationUtils.extractAndValidatePositiveInt(data, "invalid"),
                "Should throw JSONException for non-integer value");
    }

    @Test
    void extractAndValidatePositiveInt_shouldThrowWhenNegative() {
        JSONObject data = new JSONObject().put("negative", -1);
        assertThrows(IllegalArgumentException.class,
                () -> JsonValidationUtils.extractAndValidatePositiveInt(data, "negative"),
                "Should throw IllegalArgumentException for negative value");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100})
    void extractAndValidatePositiveInt_shouldReturnValueForValidInput(int value) {
        JSONObject data = new JSONObject().put("valid", value);
        assertDoesNotThrow(
                () -> assertEquals(value,
                        JsonValidationUtils.extractAndValidatePositiveInt(data, "valid")),
                "Should return correct value for valid input");
    }
}