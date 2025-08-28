package it.polimi.ingsoftware.ll13.utils.json;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonValidationTileUtils {

    // Private constructor to prevent instantiation
    private JsonValidationTileUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Extracts and validates a list of Connector from JSON array
     *
     * @param jsonArray JSON array containing connector objects
     * @return List of validated Connector objects
     * @throws JSONException            If array is empty or contains invalid data
     * @throws IllegalArgumentException If any WarZonePenalty data is invalid
     */
    public static List<ConnectorType> extractAndValidateConnector(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray == null || jsonArray.isEmpty()) {
            throw new JSONException("Connectors array cannot be null or empty");
        }

        // Creation of the list of connectors
        List<ConnectorType> connectorTypes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            connectorTypes.add(parseConnector(jsonArray.getJSONObject(i)));
        }

        return connectorTypes;
    }

    private static ConnectorType parseConnector(JSONObject jsonObject) throws IllegalArgumentException {
        try {
            return ConnectorType.valueOf(jsonObject.getString("type"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid connector type: " + jsonObject.getString("type"), e);
        }
    }

    /**
     * Extracts and validates a CompartmentType from JSON object
     *
     * @param jsonObject JSON object containing compartment type
     * @return validated CompartmentType
     * @throws JSONException if field is missing or contains invalid value
     */
    public static CompartmentType extractAndValidateCompartmentType(JSONObject jsonObject) throws JSONException {
        // Check if the field exists
        if (!jsonObject.has("compartment_type")) {
            throw new JSONException("Missing required field: " + jsonObject.toString());
        }

        try {
            return CompartmentType.valueOf(jsonObject.getString("compartment_type"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid compartment type: " + jsonObject.getString("compartment_type"), e);
        }
    }

    /**
     * Extracts and validates a PlayerColors from JSON object
     *
     * @param jsonObject JSON object containing player color
     * @return validated PlayerColors
     * @throws JSONException if field is missing or contains invalid value
     */
    public static PlayerColors extractAndValidatePlayerColors(JSONObject jsonObject) throws JSONException {
        // Check if the field exists
        if (!jsonObject.has("color")) {
            throw new JSONException("Missing required field: color");
        }

        try {
            return PlayerColors.valueOf(jsonObject.getString("color"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid player color: " + jsonObject.getString("color"), e);
        }
    }

    /**
     * Extracts and validates a VitalSupportColor from JSON object
     *
     * @param jsonObject JSON object containing vital support color
     * @return validated VitalSupportColor
     * @throws JSONException if field is missing or contains invalid value
     */
    public static VitalSupportColor extractAndValidateVitalSupportColor(JSONObject jsonObject) throws JSONException {
        // Check if the field exists
        if (!jsonObject.has("vital_support_color")) {
            throw new JSONException("Missing required field: vital_support_color");
        }

        try {
            return VitalSupportColor.valueOf(jsonObject.getString("vital_support_color"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid vital support color: " + jsonObject.getString("vital_support_color"), e);
        }
    }
}
