package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Card;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * DrawPlanetsResponse is a concrete implementation of the abstract {@link Response} class.
 * It represents a response sent to a client when a "Planets" card is drawn during the game.
 *
 * This class encapsulates the specific "Planets" {@link Card} that was drawn, providing
 * the card details through the getter method and enabling the execution of the related
 * handler logic defined in the {@link Handler} interface.
 *
 * Key responsibilities:
 * - Encapsulate the "Planets" card that was drawn.
 * - Execute the appropriate handler method defined in the {@link Handler} interface.
 */
public class DrawPlanetsResponse extends Response {

    private final Card card;

    /**
     * Represents a response for drawing a planet card in the game. This response is
     * sent from the server side and provides the details of the planet card drawn.
     * The card details can be used to apply its effects or display its information
     * on the client side.
     *
     * @param card The card drawn from the deck, representing the planet.
     */
    public DrawPlanetsResponse(Card card) {
        this.card = card;
    }

    // --> Getters <--

    /**
     *
     * @return
     */
    public Card getCard() {
        return card;
    }

    /**
     * Executes the appropriate handler logic for this response.
     * This method delegates the processing of the "Planets" card draw response
     * to the corresponding method in the provided {@link Handler} instance.
     *
     * @param handler the handler instance responsible for processing this response.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleDrawPlanetsResponse(this);
    }
}
