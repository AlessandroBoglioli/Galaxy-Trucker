package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * The {@code WarZoneCreator} class represents an "War zone" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a list of penalties of the war zone.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class WarZoneCreator extends CardCreator {
    /**
     * Creator of the card "War zone" card.
     *
     * @param data A JSONObject that contains all the elements of the card, which in this case are: list of penalties.
     * @return the object Card, in particular, the object {@link WarZoneCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        List<WarZonePenalty> penalties = extractAndValidateWarZonePenalties(data.getJSONArray("penalties"));

        return new WarZoneCard(
                level,
                image,
                penalties
        );
    }
}
