package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code SlaversCreator} class represents an "Slavers" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a fire power needed, a crew sacrifice in case of loss, a number of credit rewarded in case of won and a number of flights days reduction in case of won.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class SlaversCreator extends CardCreator {
    /**
     * Creator of the card "Slavers" card.
     *
     * @param data A JSONObject , which in this case are: fire power needed, crew sacrifice, credit reward and a number of flight days lost in case of win.
     * @return the object Card, in particular, the object {@link SlaversCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        int firePowerNeeded = extractAndValidatePositiveInt(data, "fire_power_needed");
        int crew_sacrifice = extractAndValidatePositiveInt(data, "crew_sacrifice");
        int creditsReward = extractAndValidatePositiveInt(data, "credits_reward");
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new SlaversCard(
                level,
                image,
                firePowerNeeded,
                crew_sacrifice,
                creditsReward,
                flightDaysReduction
        );
    }
}
