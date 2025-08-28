package it.polimi.ingsoftware.ll13.network.response.match_responses.validation;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.ArrayList;

public class CrewStatusResponse extends Response{
    private final boolean success;
    private final int humanCount;
    private final int purpleAlienCount;
    private final int yellowAlienCount;
    private final ArrayList<TileCoordinates> cabinTiles;

    public CrewStatusResponse(boolean success, int humanCount, int purpleAlienCount, int yellowAlienCount, ArrayList<TileCoordinates> cabinTiles) {
        this.success = success;
        this.humanCount = humanCount;
        this.purpleAlienCount = purpleAlienCount;
        this.yellowAlienCount = yellowAlienCount;
        this.cabinTiles = cabinTiles;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleCrewStatusResponse(this);
    }
    public boolean isSuccess() {
        return success;
    }

    public int getHumanCount() {
        return humanCount;
    }

    public int getPurpleAlienCount() {
        return purpleAlienCount;
    }

    public int getYellowAlienCount() {
        return yellowAlienCount;
    }

    public ArrayList<TileCoordinates> getCabinTiles() {
        return cabinTiles;
    }
}
