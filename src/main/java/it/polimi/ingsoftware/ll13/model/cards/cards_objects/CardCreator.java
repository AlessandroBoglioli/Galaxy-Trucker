package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import org.json.JSONObject;

/**
 * The {@code CardCreator} is an abstract class representing a card creator.
 * This class defines a factory pattern interface for creating different types of {@link Card} objects.
 * Subclasses must implement the {@link #createCard(JSONObject)} method to provide specific card creation logic.
 */
public abstract class CardCreator {
    /**
     * Creates a Card object based on the provided JSON data.
     * Subclasses implementing this method should provide card creation logic specific to their requirements.
     *
     * @param data A {@code JSONObject} containing the data required to create a Card instance.
     * @return A {@code Card} object created using the provided data.
     */
    public abstract Card createCard(JSONObject data);
}
