package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The {@code AbandonedStationCreator} class represents an "Abandoned Station" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs an amount of required crew, a list of cargo rewards, and a reduction in flight days.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class AbandonedStationCreator extends CardCreator {
    /**
     * Creator of the card "Abandoned Station" card.
     *
     * @param data A JSONObject containing all the data of the current card, which in this case are: amount of required crew, a list of cargo rewards, and a reduction in flight days.
     * @return the object Card, in particular, the object {@link AbandonedStationCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data,"level");
        String image = extractAndValidateString(data,"image");
        int requiredCrew = extractAndValidatePositiveInt(data, "required_crew");
        List<CargoColor> cargoColors = extractAndValidateCargoColors(data.getJSONArray("rewards"));
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new AbandonedStationCard(
                level,
                image,
                requiredCrew,
                cargoColors,
                flightDaysReduction
        );
    }
}
