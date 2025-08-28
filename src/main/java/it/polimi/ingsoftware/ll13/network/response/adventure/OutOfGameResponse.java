package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

/**
 * Represents a server response indicating a scenario where the player is
 * out of the game or has scores calculated outside the ongoing gameplay.
 * This response encapsulates a single property, `points`, reflecting the
 * points associated with this out-of-game event.
 *
 * This class extends the {@code Response} class and provides functionality
 * to process this specific type of response using the provided {@code Handler}.
 */
public class OutOfGameResponse extends Response {

    private float points;

    /**
     * Default constructor for the OutOfGameResponse class.
     *
     * Initializes the points for the player to */
    public OutOfGameResponse(){
        points = 0;
    }

    /**
     * Constructs an OutOfGameResponse object with the specified points.
     *
     * @param points A float representing the points associated with the out-of-game
     *               event. This value may indicate scores calculated outside the
     *               ongoing gameplay or similar scenarios.
     */
    public OutOfGameResponse(float points) {
        this.points = points;
    }

    /**
     * Executes the logic for processing an {@code OutOfGameResponse} by delegating
     * the operation to the provided {@code Handler} instance. Specifically, it
     * invokes the {@code handleOutOfGameResponse} method of the {@code Handler}.
     *
     * @param handler the {@code Handler} instance responsible for processing
     *                the {@code OutOfGameResponse}.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleOutOfGameResponse(this);
    }

    /**
     * Retrieves the points associated with this response.
     *
     * @return A float representing the points, which might indicate a score or
     *         value calculated outside of ongoing gameplay scenarios.
     */
    public float getPoints() {
        return points;
    }

}
