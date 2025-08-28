package it.polimi.ingsoftware.ll13.model.cards.decks;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TryLevelDeckTest {
    private TryLevelDeck deck;

    @BeforeEach
    void setUp() throws Exception {
        deck = new TryLevelDeck();
        deck.shuffle();
    }

    @Test
    void constructor_shouldLoadOnlyTrialLevelCards() {
        assertTrue(deck.remainingCards() > 0, "Deck should not be empty after construction");
        assertEquals(deck.remainingCards(), deck.remainingCardsOfLevel(0),
                "All cards should be trial level cards");
    }

    @Test
    void generateStacks_shouldReturnExactlyFourStacks() {
        List<CardStack> stacks = deck.generateStacks();
        assertEquals(deck.getStacksCount(), stacks.size(),
                "Should generate exactly " + deck.getStacksCount() + " stacks");
    }

    @Test
    void eachStack_shouldContainExactlyTwoCards() {
        List<CardStack> stacks = deck.generateStacks();
        for (CardStack stack : stacks) {
            assertEquals(deck.getCardsCountPerStack(), stack.getCards().size(),
                    "Each stack should contain exactly " + deck.getCardsCountPerStack() + " cards");
        }
    }

    @Test
    void allCardsInStacks_shouldBeTrialLevel() {
        List<CardStack> stacks = deck.generateStacks();
        for (CardStack stack : stacks) {
            for (Card card : stack.getCards()) {
                assertEquals(0, card.getLevel(),
                        "All cards in trial level stacks should be trial level");
            }
        }
    }

    @RepeatedTest(1000)
    void generateStacks_shouldUseAllCardsWhenExactlyEnough() {
        // Create a situation where we have exactly STACKS_COUNT * CARDS_PER_STACK cards
        int totalCardsNeeded = deck.getStacksCount() * deck.getCardsCountPerStack();
        while (deck.remainingCards() > totalCardsNeeded) {
            deck.drawCard();
        }

        deck.generateStacks();
        assertEquals(0, deck.remainingCards(), "Should use all cards when exactly enough for all stacks");
    }

    @Test
    void drawCardOfLevel_shouldOnlyReturnTrialLevelCards() {
        Card drawnCard = deck.drawCardOfLevel(0);
        assertEquals(0, drawnCard.getLevel(),
                "Should only return trial level cards");
    }
}