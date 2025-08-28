package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import org.json.JSONObject;

/**
 * The {@code StellarSpaceDustCreator} class represents an "Stellar space dust" card creator, which task is to create the actual object card.
 * It is a factory concrete creator.
 * It extends the abstract class {@code CardCreator} and implements the specific properties of this card type.
 *
 * @see CardCreator
 */
public class StellarSpaceDustCreator extends CardCreator {
    /**
     * Creator of the card "Stellar space dust" card.
     *
     * @param data A JSONObject with all the elements of the card, which in this case are: [NOTHING].
     * @return the object Card, in particular, the object {@link StellarSpaceDustCard}..
     */
    @Override
    public Card createCard(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        int level = extractAndValidatePositiveInt(data, "level");
        String image = extractAndValidateString(data, "image");

        return new StellarSpaceDustCard(level, image);
    }
}
