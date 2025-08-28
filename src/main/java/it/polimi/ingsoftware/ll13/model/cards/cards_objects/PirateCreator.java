package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * The {@code PirateCreator} class represents an "Pirate" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a fire power needed for the win against pirates, a list of direction of fire shot in case of lost against them, reward credits in case of win and the number of flight reduction in case of win.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class PirateCreator extends CardCreator {
    /**
     * Creator of the card "Pirate" card.
     *
     * @param data A JSONObject with the elements of the cards, which in this case are: fire power needed, list of bif fire shot directions, list of small fire shot directions, credits reward and number of days of flight reduction.
     * @return the object Card, in particular, the object {@link PirateCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        int firePowerNeeded = extractAndValidatePositiveInt(data, "fire_power_needed");
        List<FireShot> fireShots = extractAndValidateFireShots(data.getJSONArray("fire_shots"));
        int creditsReward = extractAndValidatePositiveInt(data, "credits_reward");
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new PirateCard(
                level,
                image,
                firePowerNeeded,
                fireShots,
                creditsReward,
                flightDaysReduction
        );
    }
}
