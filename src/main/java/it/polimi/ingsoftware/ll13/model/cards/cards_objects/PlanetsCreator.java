package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * The {@code PlanetsCreator} class represents an "Planets" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a planet list with inside cargos and a number of flight days reduction.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class PlanetsCreator extends CardCreator {
    /**
     * Creator of the card "Planets" card.
     *
     * @param data A JSONObject with all the elements of the card, which in this case are: list of planets with cargos on them and a reduction in flight days.
     * @return the object Card, in particular, the object {@link PlanetsCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws JSONException, IllegalArgumentException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        List<Planet> planets = extractAndValidatePlanets(data.getJSONArray("planets"));
        int flightDaysReduction = extractAndValidatePositiveInt(data, "flight_days_reduction");

        return new PlanetsCard(
                level,
                image,
                planets,
                flightDaysReduction
        );
    }
}
