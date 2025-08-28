package it.polimi.ingsoftware.ll13.model.ship_board;


import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_components.*;

import java.io.Serial;
import java.io.Serializable;

public abstract class Ship implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;
    private ShipLayout shipLayout;
    private ShipStats shipStats;
    private BatteryManager batteryManager;
    private CargosManager cargosManager;
    private ProblemHandler problemHandler;
    private CabinManager cabinManager;
    private ShipCrewPlacer shipCrewPlacer;

    protected void initializeComponents(PlayerColors playerColors) {
        this.shipLayout = new ShipLayout(playerColors);
        this.shipStats = new ShipStats();
        this.batteryManager = new BatteryManager();
        this.cargosManager = new CargosManager();
        this.problemHandler = new ProblemHandler();
        this.cabinManager = new CabinManager();
        this.shipCrewPlacer = new ShipCrewPlacer();
    }

    /**
     * Constructor of the ship.
     * @param playerColors the color of the player.
     */
    public Ship(PlayerColors playerColors) {
        initializeComponents(playerColors);
    }

    // --> Getters <--
    public ShipLayout getShipLayout() {
        return shipLayout;
    }

    public ShipStats getShipStats() {
        return shipStats;
    }

    public BatteryManager getBatteryManager() {
        return batteryManager;
    }

    public CargosManager getCargosManager() {
        return cargosManager;
    }

    public ProblemHandler getProblemHandler() {
        return problemHandler;
    }

    public CabinManager getCabinManager() {
        return cabinManager;
    }

    public ShipCrewPlacer getShipCrewPlacer() {
        return shipCrewPlacer;
    }

    // --> Setters <--
    protected void setShipLayout(ShipLayout shipLayout) {
        this.shipLayout = shipLayout;
    }

    protected void setShipStats(ShipStats shipStats) {
        this.shipStats = shipStats;
    }

    protected void setBatteryManager(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    protected void setCargosManager(CargosManager cargosManager) {
        this.cargosManager = cargosManager;
    }

    protected void setProblemHandler(ProblemHandler problemHandler) {
        this.problemHandler = problemHandler;
    }

    protected void setCabinManager(CabinManager cabinManager) {
        this.cabinManager = cabinManager;
    }

    protected void setShipCrewPlacer(ShipCrewPlacer shipCrewPlacer) {
        this.shipCrewPlacer = shipCrewPlacer;
    }

    // --> Other methods <--
    // ABSTRACT ONES
    protected abstract void initializeMatrix();
    public abstract void initializeCrew();
    public abstract void connectCells();


}

