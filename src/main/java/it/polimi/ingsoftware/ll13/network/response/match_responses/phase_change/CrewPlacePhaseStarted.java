package it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.ArrayList;

public class CrewPlacePhaseStarted extends Response {
    private final ArrayList<TileCoordinates> cabinTiles;

    public CrewPlacePhaseStarted(ArrayList<TileCoordinates> cabinTiles) {
        this.cabinTiles = cabinTiles;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleCrewPhase(this);
    }

    public ArrayList<TileCoordinates> getCabinTiles() {
        return cabinTiles;
    }
}
