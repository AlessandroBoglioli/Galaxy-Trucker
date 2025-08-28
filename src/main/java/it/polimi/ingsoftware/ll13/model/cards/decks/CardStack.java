package it.polimi.ingsoftware.ll13.model.cards.decks;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stack of cards with a maximum capacity.
 * Used to manage groups of cards during gameplay in Galaxy Trucker.
 */
public class CardStack implements Serializable {

    private List<Card> cards;
    private final int maxSize;

    /**
     * Creates a new empty CardStack with specified maximum capacity
     *
     * @param maxSize The maximum number of cards this stack can hold
     */
    public CardStack(int maxSize) {
        this.maxSize = maxSize;
        this.cards = new ArrayList<>();
    }

    /**
     * Gets all cards currently in this stack
     *
     * @return An unmodifiable list of cards in this stack
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Gets the maximum capacity of this stack
     *
     * @return The maximum number of cards this stack can hold
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Adds a card to this stack if there is space available
     *
     * @param card The card to add to the stack
     * @throws IllegalStateException if the stack is already at maximum capacity
     */
    public void addCard(Card card) throws IllegalStateException {
        if(cards.size() >= maxSize) {
            throw new IllegalStateException("Stack is full");
        }
        cards.add(card);
    }
}