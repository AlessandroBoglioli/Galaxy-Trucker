package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationCardUtils.*;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code MeteorShowerCreator} class represents an "Meteor shower" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * This card needs a list of great meteors' directions and a list of small meteors' direction.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class MeteorShowerCreator extends CardCreator {
    /**
     * Creator of the card "Meteor shower" card.
     *
     * @param data    An undefined number of parameters, which in this case are: list of great meteors' directions and a list of small meteors' direction.
     * @return          The object Card, in particular, the object {@link MeteorShowerCard}..
     */
    public Card createCard(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");
        List<Meteor> meteors = extractAndValidateMeteors(data.getJSONArray("meteors"));

        return new MeteorShowerCard(level, image, meteors);
    }
}
