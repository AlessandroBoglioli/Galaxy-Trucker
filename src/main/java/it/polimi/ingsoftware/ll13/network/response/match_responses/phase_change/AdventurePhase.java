package it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change;

import it.polimi.ingsoftware.ll13.client.handlers.Handler;
import it.polimi.ingsoftware.ll13.network.response.Response;

public class AdventurePhase extends Response {
    private final boolean isFirst;
    private final int batteryCount;
    private final int crewCount;
    private final int wasteTilesCount;
    private final int cargoCount;
    private final int exposedConnectorsCount;
    private final int creditCount;

    /**
     * Constructs an AdventurePhase object, representing the state of the game during the adventure phase.
     *
     * @param isFirst indicates whether the player is the first in the ranking order for the adventure phase.
     * @param batteryCount the number of batteries available on the player's ship.
     * @param crewCount the number of crew members present on the player's ship.
     * @param wasteTilesCount the*/
    public AdventurePhase(boolean isFirst, int batteryCount, int crewCount, int wasteTilesCount, int cargoCount, int exposedConnectorsCount, int creditCount) {
        this.isFirst = isFirst;
        this.batteryCount = batteryCount;
        this.crewCount = crewCount;
        this.wasteTilesCount = wasteTilesCount;
        this.cargoCount = cargoCount;
        this.exposedConnectorsCount = exposedConnectorsCount;
        this.creditCount = creditCount;
    }

    /**
     * Executes the AdventurePhase by invoking the corresponding handler method.
     * This method delegates the processing of the adventure phase to the provided handler,
     * allowing for specific behavior to be implemented depending on the handler's context.
     *
     * @param handler the handler responsible for processing the AdventurePhase
     */
    @Override
    public void execute(Handler handler) {
        handler.handleAdventurePhase(this);
    }

    /**
     * Checks if the player is the first in the ranking order for the adventure phase.
     *
     * @return true if the player is first in the ranking order, false otherwise
     */
    public boolean isFirst() {
        return isFirst;
    }

    /**
     * Retrieves the number of batteries available on a player's ship during the adventure phase.
     *
     * @return the number of batteries available
     */
    public int getBatteryCount() {
        return batteryCount;
    }

    /**
     * Retrieves the number of crew members present on the player's ship during the adventure phase.
     *
     * @return the number of crew members
     */
    public int getCrewCount() {
        return crewCount;
    }

    /**
     * Retrieves the number of waste tiles present on the player's ship during the adventure phase.
     *
     * @return the number of waste tiles
     */
    public int getWasteTilesCount() {
        return wasteTilesCount;
    }

    /**
     * Retrieves the number of cargo items present on the player's ship during the adventure phase.
     *
     * @return the number of cargo items
     */
    public int getCargoCount() {
        return cargoCount;
    }

    /**
     * Retrieves the number of exposed connectors on the player's ship during the adventure phase.
     *
     * @return the number of exposed connectors
     */
    public int getExposedConnectorsCount() {
        return exposedConnectorsCount;
    }

    /**
     * Retrieves the number of credits available during the adventure phase.
     *
     * @return the number of available credits
     */
    public int getCreditCount() {
        return creditCount;
    }
}

