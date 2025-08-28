package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a response that indicates the completion of a card effect
 * during the gameplay. This could signify the end of processing for a specific
 * event or effect triggered by a card action within the game.
 *
 * This class extends the {@code Response} class and provides additional
 * contextual information regarding whether the triggering of the response
 * corresponds to the first occurrence of the effect.
 *
 * The {@code FinishedCardEffectResponse} is processed by invoking the
 * {@code handleFinishedEffectResponse} method on a provided {@code Handler}
 * instance, to manage the resulting actions or state updates.
 */
public class FinishedCardEffectResponse extends Response {
    private final boolean isFirst;

    /**
     * Constructs a FinishedCardEffectResponse object.
     *
     * @param isFirst A boolean value that indicates whether this is the first occurrence
     *                of finishing a*/
    public FinishedCardEffectResponse(boolean isFirst) {
        this.isFirst = isFirst;
    }

    /**
     * Determines whether the effect being finalized is the first occurrence.
     *
     * @return {@code true} if this response corresponds to the first occurrence of
     *         finishing a card effect; {@code false} otherwise.
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Executes the logic associated with processing a finished card effect response
     * by delegating the operation to the provided {@code Handler} instance.
     * Specifically, it invokes the {@code handleFinishedEffectResponse} method on
     * the given {@code Handler}.
     *
     * @param handler The {@code Handler} instance responsible for processing this
     *                response. The method {@code handleFinishedEffectResponse}
     *                of this handler will be called to handle the response logic.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleFinishedEffectResponse(this);
    }
}
