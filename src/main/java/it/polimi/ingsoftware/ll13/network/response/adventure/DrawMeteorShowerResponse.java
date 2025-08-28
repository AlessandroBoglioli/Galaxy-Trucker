package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 */
public class DrawMeteorShowerResponse extends Response {

    private final Card card;

    /**
     * Constructs a response for drawing a meteor shower card in the game.
     * This response is sent from the server to the client and contains
     * the card drawn during the meteor shower event.
     *
     * @param card The {@code Card} instance representing the meteor shower card.
     */
    public DrawMeteorShowerResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     * Retrieves the card associated with this response.
     *
     * @return The {@code Card} object that this response is carrying.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the appropriate handling logic for this response by invoking
     * the corresponding method on the provided {@code Handler} instance.
     *
     * @param handler The {@code Handler} instance responsible for processing
     *                this specific type of response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawMeteorShowerResponse(this);
    }
}
