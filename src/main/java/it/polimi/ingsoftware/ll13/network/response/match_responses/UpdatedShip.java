package it.polimi.ingsoftware.ll13.network.response.match_responses;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.network.response.Response;

import java.util.List;

public class UpdatedShip extends Response {
    private final List<TileCoordinates> updatedTiles;
    private final int batteryCount;
    private final int crewCount;
    private final int wasteTilesCount;
    private final int cargoCount;
    private final int exposedConnectorsCount;
    private final int creditCount;
    private final boolean success;
    public UpdatedShip(List<TileCoordinates> tiles , boolean success, int batteryCount, int crewCount, int wasteTilesCount, int cargoCount, int exposedConnectorsCount, int creditCount) {
        this.updatedTiles = tiles;
        this.success = success;
        this.batteryCount = batteryCount;
        this.crewCount = crewCount;
        this.wasteTilesCount = wasteTilesCount;
        this.cargoCount = cargoCount;
        this.exposedConnectorsCount = exposedConnectorsCount;
        this.creditCount = creditCount;
    }

    @Override
    public void execute(Handler handler) {
        handler.handleUpdateShipResponse(this);
    }

    public List<TileCoordinates> getUpdatedTiles(){
        return updatedTiles;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getBatteryCount() {
        return batteryCount;
    }
    public int getCrewCount() {
        return crewCount;
    }
    public int getWasteTilesCount() {
        return wasteTilesCount;
    }

    public int getCargoCount() {
        return cargoCount;
    }

    public int getExposedConnectorsCount() {
        return exposedConnectorsCount;
    }

    public int getCreditCount() {
        return creditCount;
    }
}
