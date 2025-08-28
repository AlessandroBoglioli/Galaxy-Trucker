package it.polimi.ingsoftware.ll13.utils.json;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Meteor;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Planet;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.WarZonePenalty;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.*;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating and extracting data from JSON objects.
 * Provides common validation methods for card creation.
 */
public final class JsonValidationCardUtils {

    // Private constructor to prevent instantiation
    private JsonValidationCardUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Extracts and validates a list of CargoColors from JSON array
     * @param jsonArray JSON array containing color strings
     * @return List of validated CargoColor enums
     * @throws JSONException If array is empty or contains invalid data
     * @throws IllegalArgumentException If any color string is invalid
     */
    public static List<CargoColor> extractAndValidateCargoColors(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray.isEmpty()) {
            throw new JSONException("Missing required field: " + jsonArray.toString());
        }

        // Creation of the CargoColor List
        List<CargoColor> cargoColors = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            cargoColors.add(CargoColor.valueOf(jsonArray.getString(i)));
        }
        return cargoColors;
    }

    /**
     * Extracts and validates a list of Meteors from JSON array
     * @param jsonArray JSON array containing meteor objects
     * @return List of validated Meteor objects
     * @throws JSONException If array is empty or contains invalid data
     * @throws IllegalArgumentException If any meteor data is invalid
     */
    public static List<Meteor> extractAndValidateMeteors(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray.isEmpty()) {
            throw new JSONException("Missing required field: " + jsonArray.toString());
        }

        // Creation of the meteors ArrayList
        List<Meteor> meteors = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject meteorObject = jsonArray.getJSONObject(i);

            // Creation of the meteor
            meteors.add(parseMeteor(meteorObject));
        }
        return meteors;
    }

    private static Meteor parseMeteor(JSONObject jsonObject) throws IllegalArgumentException {
        // Check if the field exists
        if (!jsonObject.has("type") || !jsonObject.has("direction")) {
            throw new JSONException("Missing required field: " + jsonObject.toString());
        }

        try {
            return new Meteor(ProblemType.valueOf(jsonObject.getString("type")), Direction.valueOf(jsonObject.getString("direction")));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid compartment type: " + jsonObject.getString("type") + "and" + jsonObject.getString("direction"), e);
        }
    }

    /**
     * Extracts and validates a list of FireShot from JSON array
     * @param jsonArray JSON array containing fire shots objects
     * @return List of validated FireShot objects
     * @throws JSONException If array is empty or contains invalid data
     * @throws IllegalArgumentException If any fireShot data is invalid
     */
    public static List<FireShot> extractAndValidateFireShots(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray.isEmpty()) {
            throw new JSONException("Missing required field: " + jsonArray.toString());
        }

        // Creation of the fireShots ArrayList
        List<FireShot> fireShots = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject fireShotObject = jsonArray.getJSONObject(i);

            fireShots.add(parseFireShot(fireShotObject));
        }
        return fireShots;
    }

    private static FireShot parseFireShot(JSONObject jsonObject) throws IllegalArgumentException {
        // Check if the field exists
        if (jsonObject.isEmpty()) {
            throw new JSONException("Missing required field: " + jsonObject.toString());
        }

        return new FireShot(ProblemType.valueOf(jsonObject.getString("type")), Direction.valueOf(jsonObject.getString("direction")));
    }

    /**
     * Extracts and validates a list of Planets from JSON array
     * @param jsonArray JSON array containing planets objects
     * @return List of validated Planets objects
     * @throws JSONException If array is empty or contains invalid data
     * @throws IllegalArgumentException If any Planet data is invalid
     */
    public static List<Planet> extractAndValidatePlanets(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray == null || jsonArray.isEmpty()) {
            throw new JSONException("Connectors array cannot be null or empty");
        }

        // Creation of the Planets ArrayList
        List<Planet> planets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject planetJson = jsonArray.getJSONObject(i);
            planets.add(new Planet(extractAndValidateCargoColors(planetJson.getJSONArray("cargos"))));
        }

        return planets;
    }

    /**
     * Extracts and validates a list of WarZonePenalty from JSON array
     * @param jsonArray JSON array containing war zone objects
     * @return List of validated WarZonePenalty objects
     * @throws JSONException If array is empty or contains invalid data
     * @throws IllegalArgumentException If any WarZonePenalty data is invalid
     */
    public static List<WarZonePenalty> extractAndValidateWarZonePenalties(JSONArray jsonArray) throws JSONException, IllegalArgumentException {
        // Check if the field exists
        if (jsonArray == null || jsonArray.isEmpty()) {
            throw new JSONException("Connectors array cannot be null or empty");
        }

        // Creation of the list of penalties
        List<WarZonePenalty> penalties = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            WarZonePenalty penalty = parseWarZonePenalty(jsonArray.getJSONObject(i));
            penalties.add(penalty);
        }

        return penalties;
    }

    // Method that help cleaning the code and contains all the logic of the parsing for the WarZonePenalty
    private static WarZonePenalty parseWarZonePenalty(JSONObject jsonObject) throws IllegalArgumentException {
        WarZonePenaltyType warZonePenaltyType;
        WarZonePenaltyEffect warZonePenaltyEffect;
        try {
            warZonePenaltyType = WarZonePenaltyType.valueOf(jsonObject.getString("target"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid compartment type: " + jsonObject.getString("target"), e);
        }
        try {
            warZonePenaltyEffect = WarZonePenaltyEffect.valueOf(jsonObject.getString("effect"));
        } catch (IllegalArgumentException e) {
            throw new JSONException("Invalid compartment type: " + jsonObject.getString("target"), e);
        }

        if (jsonObject.has("fire_shots")) {
            // Case fire shots
            List<FireShot> fireShots = extractAndValidateFireShots(jsonObject.getJSONArray("fire_shots"));
            return new WarZonePenalty(warZonePenaltyType, warZonePenaltyEffect, fireShots);
        } else {
            // Numeric penalty type
            int value = extractAndValidatePositiveInt(jsonObject, "value");
            return new WarZonePenalty(warZonePenaltyType, warZonePenaltyEffect, value);
        }
    }
}
