package it.polimi.ingsoftware.ll13.network.response.match_responses.construction;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class ViewStackResponse extends Response {
    private final List<Card> cards;
    private final boolean success;

    public ViewStackResponse(List<Card> cards, boolean success) {
        this.cards = cards;
        this.success = success;
    }

    @Override
    public void execute(Handler handler) {
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isSuccess() {
        return success;
    }
}
