package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import org.json.JSONObject;

/**
 * The {@code EpidemicCreator} class represents an "Epidemic" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class EpidemicCreator extends CardCreator {
    /**
     * Creator of the card "Epidemic" card.
     *
     * @return the object Card, in particular, the object {@link EpidemicCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");

        return new EpidemicCard(level, image);
    }
}
