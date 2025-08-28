package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * The {@code Smugglers} class represents a "Smuggler" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a a fire power needed to win the battle against smugglers, a cargo penalty in case of loss, a list of cargo rewards and a number of flight days reduction in case of win.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class SmugglersCreator extends CardCreator {
    /**
     * Creator of the card "Smugglers" card.
     *
     * @param data A JSONObject for the values of the card, which in this case are: fire power needed, number of cargo penalty, list of cargo rewards and number of flight days reduction.
     * @return the object Card, in particular, the object {@link SmugglersCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        int firePowerNeeded = extractAndValidatePositiveInt(data, "fire_power_needed");
        int cargoPenalty = extractAndValidatePositiveInt(data, "cargo_penalty");
        List<CargoColor> rewards = extractAndValidateCargoColors(data.getJSONArray("rewards"));
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new SmugglersCard(
                level,
                image,
                firePowerNeeded,
                cargoPenalty,
                rewards,
                flightDaysReduction
        );
    }
}
