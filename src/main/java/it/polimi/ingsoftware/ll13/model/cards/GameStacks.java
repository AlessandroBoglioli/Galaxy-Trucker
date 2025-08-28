package it.polimi.ingsoftware.ll13.model.cards;

import it.polimi.ingsoftware.ll13.model.cards.decks.BaseDeck;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.cards.decks.Level2Deck;
import it.polimi.ingsoftware.ll13.model.cards.decks.TryLevelDeck;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;

import org.json.JSONException;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Manages the creation and storage of card stacks for a Galaxy Trucker game session.
 * Creates appropriate decks based on the selected game level and generates the required stacks.
 */
public class GameStacks implements Serializable {

    private final List<CardStack> stacks;
    private final GameLevel currentLevel;

    /**
     * Creates a new GameStacks instance for the specified game level
     *
     * @param level The game level to create stacks for
     * @throws JSONException if there's an error loading card data from JSON files
     * @throws IllegalArgumentException if an unknown game level is specified
     */
    public GameStacks(GameLevel level) throws JSONException {
        if (level == null) {
            throw new IllegalArgumentException("Game level cannot be null");
        }
        this.currentLevel = level;
        this.stacks = createStacksForLevel(level);
    }

    /**
     * Gets all card stacks for the current game
     *
     * @return List of CardStack objects for the current game level
     */
    public List<CardStack> getStacks() {
        return stacks;
    }

    /**
     * Gets the game level these stacks were created for
     *
     * @return The current GameLevel enum value
     */
    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Creates the appropriate card stacks for the specified game level
     *
     * @param level The game level to create stacks for
     * @return List of properly configured CardStack objects
     * @throws JSONException if there's an error loading card data
     * @see BaseDeck
     */
    private List<CardStack> createStacksForLevel(GameLevel level) throws JSONException {
        BaseDeck deck = createDeckForLevel(level);
        deck.shuffle();
        return deck.generateStacks();
    }

    /**
     * Factory method that creates the appropriate deck type for the game level
     *
     * @param level The game level to create a deck for
     * @return A BaseDeck implementation specific to the requested level
     * @throws JSONException if there's an error loading card data
     * @throws IllegalArgumentException if the game level is not supported
     */
    public BaseDeck createDeckForLevel(GameLevel level) throws JSONException {
        return switch (level) {
            case TRY_LEVEL -> new TryLevelDeck();
            case LEVEL_2 -> new Level2Deck();
            default -> throw new IllegalArgumentException("Unknown game level: " + level);
        };
    }
}