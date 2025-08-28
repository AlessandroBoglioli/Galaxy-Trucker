package it.polimi.ingsoftware.ll13.model.cards.decks;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.model.json_parser.CardParser;

import org.json.JSONException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Abstract base class implementing the Deck interface, providing core functionality for card decks in Galaxy Trucker.
 */
public abstract class BaseDeck implements Deck, Serializable {

    protected final List<Card> cards;
    protected final Random random;
    protected final Map<Integer, List<Card>> cardsByLevel;

    protected BaseDeck() {
        this.cards = new ArrayList<>();
        this.random = new Random();
        this.cardsByLevel = new HashMap<>();
    }

    /**
     * Loads cards from specified JSON file paths
     *
     * @param jsonPaths Variable number of paths to JSON card files
     * @throws JSONException if there's an error parsing the JSON files
     */
    protected void loadCardsFromJson(String... jsonPaths) throws JSONException {
        CardParser parser = new CardParser();
        for (String path : jsonPaths) {
            InputStream is = getClass().getResourceAsStream(path);
            List<Card> parsedCards = parser.parseCards(is);
            cards.addAll(parsedCards);

            parsedCards.forEach(card -> {
                cardsByLevel.computeIfAbsent(card.getLevel(), k -> new ArrayList<>()).add(card);
            });
        }
    }

    /**
     * Draws a random card from the deck
     *
     * @return The drawn card
     * @throws IllegalStateException if the deck is empty
     */
    @Override
    public synchronized Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty.");
        }
        return cards.remove(random.nextInt(cards.size()));
    }

    /**
     * Draws a random card of the specified level
     *
     * @param level The level of card to draw
     * @return The drawn card
     * @throws IllegalStateException if no cards of requested level are available
     */
    protected synchronized Card drawCardOfLevel(int level) {
        List<Card> levelCards = cardsByLevel.getOrDefault(level, Collections.emptyList());
        if (levelCards.isEmpty()) {
            throw new IllegalStateException("No cards of level " + level + " left.");
        }

        Card card = levelCards.remove(random.nextInt(levelCards.size()));
        cards.remove(card);
        return card;
    }

    /**
     * Shuffles all cards in the deck and within each level group
     */
    @Override
    public void shuffle() {
        Collections.shuffle(cards);
        cardsByLevel.values().forEach(Collections::shuffle);
    }

    /**
     * Gets the number of remaining cards in the deck
     *
     * @return Total number of cards left
     */
    @Override
    public synchronized int remainingCards() {
        return cards.size();
    }

    /**
     * Gets the number of remaining cards of a specific level
     *
     * @param level The card level to check
     * @return Number of cards remaining for the specified level
     */
    protected synchronized int remainingCardsOfLevel(int level) {
        return cardsByLevel.getOrDefault(level, Collections.emptyList()).size();
    }
}