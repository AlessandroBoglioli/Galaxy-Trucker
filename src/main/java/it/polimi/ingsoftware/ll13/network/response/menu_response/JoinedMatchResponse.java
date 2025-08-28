package it.polimi.ingsoftware.ll13.network.response.menu_response;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class JoinedMatchResponse extends Response {
    private final boolean success;
    private final int loggedPlayers;
    private final boolean inMatch;

    public JoinedMatchResponse(boolean success, int loggedPlayers, boolean inMatch) {
        this.success = success;
        this.loggedPlayers = loggedPlayers;
        this.inMatch = inMatch;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleJoinedMatchResponse(this);
    }

    public boolean isSuccess() {
        return success;
    }

    public int getLoggedPlayers() {
        return loggedPlayers;
    }

    public boolean isInMatch() {
        return inMatch;
    }
}
