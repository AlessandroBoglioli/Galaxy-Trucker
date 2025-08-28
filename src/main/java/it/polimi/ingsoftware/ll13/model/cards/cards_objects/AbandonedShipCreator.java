package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * The {@code AbandonedShipCreator} is a subclass of {@link CardCreator} responsible
 * for creating instances of the {@link AbandonedShipCard} class. This class implements
 * the factory method pattern to handle the specific creation logic for the "Abandoned Ship" card.
 */
public class AbandonedShipCreator extends CardCreator {
    /**
     * Creates an instance of {@code AbandonedShipCard} using the provided JSON data.
     * The JSON data must include required fields such as "level", "image", "crew_sacrifice",
     * "credits_reward", and "flight_days_reduction", all of which are validated before card creation.
     *
     * @param data A {@code JSONObject} containing the necessary fields for creating an {@code AbandonedShipCard}.
     *             It must include:
     *             - {@code level} (positive integer): The level or tier of the card.
     *             - {@code image} (string): The image path or identifier for the card.
     *             - {@code crew_sacrifice} (positive integer): The number of crew members to be sacrificed.
     *             - {@code credits_reward} (positive integer): The amount of credits rewarded.
     *             - {@code flight_days_reduction} (positive integer): The number of flight days reduced.
     *
     * @return A newly created instance of {@code AbandonedShipCard} initialized with validated data.
     *
     * @throws JSONException If any of the required fields are missing, or if their types are invalid.
     * @throws IllegalArgumentException If any of the required fields have invalid values (e.g., negative numbers, null, or empty strings).
     */
    @Override
    public Card createCard(JSONObject data) throws JSONException, IllegalArgumentException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        int crewSacrifice = extractAndValidatePositiveInt(data, "crew_sacrifice");
        int creditsReward = extractAndValidatePositiveInt(data, "credits_reward");
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new AbandonedShipCard(
                level,
                image,
                crewSacrifice,
                creditsReward,
                flightDaysReduction
        );
    }
}
