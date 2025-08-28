package it.polimi.ingsoftware.ll13.model.cards.decks;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import java.util.List;

public interface Deck {
    Card drawCard();
    void shuffle();
    int remainingCards();
    List<CardStack> generateStacks();
}