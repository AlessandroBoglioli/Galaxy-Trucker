package it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class ValidationPhaseStarted extends Response {
    private final GamePhase phase;

    public ValidationPhaseStarted(GamePhase phase) {
        this.phase = phase;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleValidationPhase(this);
    }

    public GamePhase getPhase() {
        return phase;
    }
}
