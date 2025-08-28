package it.polimi.ingsoftware.ll13.utils.json;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonValidationUtils {
    // Private constructor to prevent instantiation
    private JsonValidationUtils() {
        ;
    }

    /**
     * Validates that a JSONObject is not null.
     * @param data JSON object to validate.
     * @throws IllegalArgumentException if data is null.
     */
    public static void validateDataObject(JSONObject data) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("Data object cannot be null");
        }
    }

    /**
     * Extracts and validates a positive integer from JSON data.
     * @param data JSON object containing the field.
     * @param fieldName Name of the field to extract.
     * @return The validated positive integer value.
     * @throws JSONException If field is missing or not an integer.
     * @throws IllegalArgumentException If value is negative.
     */
    public static int extractAndValidatePositiveInt(JSONObject data, String fieldName) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (!data.has(fieldName)) {
            throw new JSONException("Missing required field: " + fieldName);
        }

        // Extract and validate the actual value
        int value;
        try {
            value = data.getInt(fieldName);
        } catch (JSONException e) {
            throw new JSONException("Field '" + fieldName + "' must be an integer", e);
        }
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive number");
        }

        return value;
    }

    public static String extractAndValidateString(JSONObject data, String fieldName) throws JSONException, IllegalArgumentException {
        if (!data.has(fieldName)) {
            throw new JSONException("Missing required field: " + fieldName);
        }

        String value;
        try {
            value = data.getString(fieldName);
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("Field '" + fieldName + "' cannot be null or empty");
            }
            return value.trim();
        } catch (JSONException e) {
            throw new JSONException("Field '" + fieldName + "' must be a string", e);
        }
    }
}
