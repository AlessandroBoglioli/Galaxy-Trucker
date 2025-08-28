package it.polimi.ingsoftware.ll13.model.cards.decks;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck of cards for the trial level gameplay in Galaxy Trucker.
 * Contains only trial level cards and generates simplified stacks for new players.
 */
public class TryLevelDeck extends BaseDeck {

    private static final String TRY_LEVEL_PATH = "/json/cards_try_level.json";
    private static final int STACKS_COUNT = 4;
    private static final int CARDS_PER_STACK = 2;
    private static final int TRY_LEVEL = 0;

    /**
     * Constructs a new TryLevelDeck by loading cards from the trial level JSON file
     *
     * @throws JSONException if there's an error parsing the JSON file
     */
    public TryLevelDeck() throws JSONException {
        super();
        loadCardsFromJson(TRY_LEVEL_PATH);
    }

    int getCardsCountPerStack() {
        return CARDS_PER_STACK;
    }

    int getStacksCount() {
        return STACKS_COUNT;
    }

    /**
     * Generates the card stacks for trial level gameplay
     *
     * @return List of 4 CardStack objects, each containing 2 trial level cards
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
     * Generates a single stack for trial level gameplay
     *
     * @return A CardStack containing 2 cards from the trial level
     * @throws IllegalStateException if not enough trial level cards are available
     */
    private CardStack generateStack() {
        CardStack cardStack = new CardStack(CARDS_PER_STACK);
        for (int i = 0; i < CARDS_PER_STACK; i++) {
            cardStack.addCard(drawCardOfLevel(TRY_LEVEL));
        }
        return cardStack;
    }
}