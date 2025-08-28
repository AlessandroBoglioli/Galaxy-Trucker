package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;

/**
 * This is a DTO (Data Transfer Object) for the MeteorShower card case
 */
public class MeteorShowerDTO extends GeneralDTO {
    private int meteorNumber;
    private int meteorDirectionNumber;
    private Coordinates usingTileWithBattery;

    /**
     * Constructor of the MeteorShowerDTO.
     */
    public MeteorShowerDTO() {
        super();
        this.meteorNumber = -1;
        this.meteorDirectionNumber = -1;
        this.usingTileWithBattery = new Coordinates(-1, -1);
    }

    /**
     * Constructor of the MeteorShowerDTO.
     * @param playerId the playerId that the DTO will use.
     */
    public MeteorShowerDTO(int playerId) {
        super(playerId);
        this.meteorNumber = -1;
        this.meteorDirectionNumber = -1;
        this.usingTileWithBattery = new Coordinates(-1, -1);
    }

    /**
     * Constructor of the MeteorShowerDTO.
     * @param playerId the playerId that the DTO will use.
     * @param row the row of the element that will prevent the destruction of the ship.
     * @param column the column of the element that will prevent the destruction of the ship.
     */
    public MeteorShowerDTO(int playerId, int row, int column) {
        super(playerId);
        this.meteorNumber = -1;
        this.meteorDirectionNumber = -1;
        this.usingTileWithBattery = new Coordinates(row, column);
    }

    /**
     * Constructor of the MeteorShowerDTO.
     * @param playerId the playerId that the DTO will use.
     * @param row the row of the element that will prevent the destruction of the ship.
     * @param column the column of the element that will prevent the destruction of the ship.
     * @param meteorNumber the number of the column/row of the meteor.
     */
    public MeteorShowerDTO(int playerId, int row, int column, int meteorNumber) {
        super(playerId);
        this.meteorNumber = meteorNumber;
        this.meteorDirectionNumber = -1;
        this.usingTileWithBattery = new Coordinates(row, column);
    }

    // --> GETTERS <--
    public Coordinates getUsingTileWithBattery() {
        return usingTileWithBattery;
    }

    public int getMeteorNumber() {
        return meteorNumber;
    }

    public int getMeteorDirectionNumber() {
        return meteorDirectionNumber;
    }

    // --> SETTERS <--
    public void setMeteorNumber(int meteorNumber) {
        this.meteorNumber = meteorNumber;
    }

    public void setMeteorDirectionNumber(int meteorDirectionNumber) {
        this.meteorDirectionNumber = meteorDirectionNumber;
    }

    public void setCoordinates(int row, int column) {
        this.usingTileWithBattery = new Coordinates(row, column);
    }

    // --> EXTRA METHODS <--
    public void increaseMeteorNumber() {
        this.meteorNumber++;
    }
}
