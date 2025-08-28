package it.polimi.ingsoftware.ll13.network.response.adventure;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a response indicating the end of a game.
 * It contains information about the players' names and their respective points
 * at the conclusion of the game. This response is typically sent by the
 * server to the clients to convey the final standings.
 *
 * This class extends the {@code Response} class and implements functionality
 * to process this specific type of response using the provided {@code Handler}.
 */
public class EndGameResponse extends Response {

    private List<String> playersName;
    private List<Float> playersPoints;

    /**
     * Constructs an EndGameResponse that contains the names of the players*/
    public EndGameResponse(List<String> playersName, List<Float> playersPoints) {
        this.playersName = playersName;
        this.playersPoints = playersPoints;
    }

    /**
     * Default constructor for the EndGameResponse class.
     *
     * Initializes the lists of player names and their corresponding scores
     * as empty lists. This constructor is useful when the player names and
     * points are added incrementally after the object is created.
     */
    public EndGameResponse() {
        playersName = new ArrayList<String>();
        playersPoints = new ArrayList<Float>();
    }

    /**
     * Executes the logic for processing an {@code EndGameResponse} by delegating
     * the operation to the specified {@code Handler} instance. Specifically, it
     * invokes the {@code handleEndGameResponse} method of the provided {@code Handler}.
     *
     * @param handler the {@code Handler} instance responsible for processing
     *                the {@code EndGameResponse}.
     */
    @Override
    public void execute(Handler handler) {
        handler.handleEndGameResponse(this);
    }

    /**
     * Retrieves the list of player names associated with this response.
     *
     * @return A list of player names as {@code List<String>}.
     */
    public List<String> getPlayersName() {
        return playersName;
    }

    /**
     * Retrieves the list of players' points associated with this response.
     *
     * @return A list of players' points as {@code List<Float>}, where each float
     *         represents the points of a player in the game.
     */
    public List<Float> getPlayersPoints() {
        return playersPoints;
    }
}
