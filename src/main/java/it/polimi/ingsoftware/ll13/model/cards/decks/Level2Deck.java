package it.polimi.ingsoftware.ll13.model.cards.decks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of cards for Level 2 gameplay in Galaxy Trucker.
 * Contains cards from trial level, level 1 and level 2.
 */
public class Level2Deck extends BaseDeck {
    private static final String[] PATHS = {
            "/json/cards_try_level.json",
            "/json/cards_level1.json",
            "/json/cards_level2.json"
    };

    private static final int STACKS_COUNT = 4;
    private static final int CARDS_PER_STACK = 3;
    private static final int TRY_LEVEL = 0;
    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;

    /**
     * Constructs a new Level2Deck by loading cards from JSON files
     *
     * @throws JSONException if there's an error parsing the JSON files
     */
    public Level2Deck() throws JSONException {
        super();
        loadCardsFromJson(PATHS);
    }

    int getCardsCountPerStack() {
        return CARDS_PER_STACK;
    }

    int getStacksCount() {
        return STACKS_COUNT;
    }

    /**
     * Generates the card stacks for level 2 gameplay
     *
     * @return List of CardStack objects configured for level 2
     * @see CardStack
     */
    @Override
    public List<CardStack> generateStacks() {
        List<CardStack> stacks = new ArrayList<>();
        for (int i = 0; i < STACKS_COUNT; i++) {
            stacks.add(generateStack());
        }
        return stacks;
    }

    /**
     * Generates a single stack for level 2 gameplay
     *
     * @return A CardStack containing:
     *         - 1 random card from either trial level or level 1 (50% chance each)
     *         - 2 cards from level 2
     * @throws IllegalStateException if no cards are available for the required levels
     */
    private CardStack generateStack() {
        CardStack cardStack = new CardStack(CARDS_PER_STACK);

        // Simple method to generate a card of TRY_LEVEL with 50% of chance and LEVEL_1 with 50% of chance
        int randomLevel = random.nextBoolean() ? TRY_LEVEL : LEVEL_1;
        cardStack.addCard(drawCardOfLevel(randomLevel));

        // Level 2 cards
        for (int i = 0; i < 2; i++) {
            cardStack.addCard(drawCardOfLevel(LEVEL_2));
        }

        return cardStack;
    }
}