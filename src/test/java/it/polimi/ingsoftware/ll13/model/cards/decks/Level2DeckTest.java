package it.polimi.ingsoftware.ll13.model.cards.decks;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class Level2DeckTest {
    private Level2Deck deck;

    @BeforeEach
    void setUp() throws Exception {
        deck = new Level2Deck();
        deck.shuffle();
    }

    @Test
    void constructor_shouldLoadCardsFromAllLevels() {
        assertTrue(deck.remainingCards() > 0, "Deck should not be empty");

        // Verify we have cards from all levels
        assertTrue(deck.remainingCardsOfLevel(0) > 0,
                "Should have trial level cards");
        assertTrue(deck.remainingCardsOfLevel(1) > 0,
                "Should have level 1 cards");
        assertTrue(deck.remainingCardsOfLevel(2) > 0,
                "Should have level 2 cards");
    }

    @Test
    void generateStacks_shouldReturnCorrectNumberOfStacks() {
        List<CardStack> stacks = deck.generateStacks();
        assertEquals(deck.getStacksCount(), stacks.size(),
                "Should generate exactly " + deck.getStacksCount() + " stacks");
    }

    @Test
    void eachStack_shouldHaveCorrectNumberOfCards() {
        List<CardStack> stacks = deck.generateStacks();
        for (CardStack stack : stacks) {
            assertEquals(deck.getCardsCountPerStack(), stack.getCards().size(),
                    "Each stack should contain " + deck.getCardsCountPerStack() + " cards");
        }
    }

    @Test
    void eachStack_shouldHaveCorrectCardLevelDistribution() {
        List<CardStack> stacks = deck.generateStacks();

        for (CardStack stack : stacks) {
            List<Card> cards = stack.getCards();

            // First card should be either level 0 or 1
            int firstCardLevel = cards.get(0).getLevel();
            assertTrue(firstCardLevel == 0 || firstCardLevel == 1, "First card should be either level 0 or 1");

            // Next two cards should be level 2
            assertEquals(2, cards.get(1).getLevel(), "Second card should be level 2");
            assertEquals(2, cards.get(2).getLevel(), "Third card should be level 2");
        }
    }

    @Test
    void drawCardOfLevel_shouldOnlyReturnCardsOfRequestedLevel() {
        Card level2Card = deck.drawCardOfLevel(2);
        assertEquals(2, level2Card.getLevel(),
                "Should only return cards of the requested level");
    }
}