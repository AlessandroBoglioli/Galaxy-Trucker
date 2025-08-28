package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import org.json.JSONObject;

/**
 * The {@code OpenSpaceCreator} class represents an "Open space" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class OpenSpaceCreator extends CardCreator {
    /**
     * Creator of the card "Open space" card.
     *
     * @return          The object Card, in particular, the object {@link OpenSpaceCard}.
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");

        return new OpenSpaceCard(level, image);
    }
}
