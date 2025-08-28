package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a DTO (Data Transfer Object) for the WarZone card case
 */
public class WarZoneDTO extends GeneralDTO {
    private int penaltyNumber; // This will be updated during the first step of the communication from client to view
    private List<Coordinates> usingTilesWithBattery;
    private int fireShotNumber;
    private int fireShotDirectionNumber;

    /**
     * Constructor of the WarZoneDTO.
     */
    public WarZoneDTO() {
        super(); // senderId will be calculated in a particular way considering the penalty
        this.penaltyNumber = -1;
        this.usingTilesWithBattery = new ArrayList<>();
        fireShotNumber = 0;
    }

    /**
     * Constructor of the WarZoneDTO.
     * @param senderId the playerId that will be used in the DTO.
     */
    public WarZoneDTO(int senderId) {
        super(senderId); // senderId will be calculated in a particular way considering the penalty
        this.penaltyNumber = 0;
        this.usingTilesWithBattery = new ArrayList<>();
        fireShotNumber = 0;
    }

    // --> GETTERS <--
    public List<Coordinates> getUsingTilesWithBattery() {
        return usingTilesWithBattery;
    }

    public int getPenaltyNumber() {
        return penaltyNumber;
    }

    public int getFireShotNumber() {
        return fireShotNumber;
    }

    public int getFireShotDirectionNumber() {
        return fireShotDirectionNumber;
    }

    // --> SETTERS <--
    public void setUsingTilesWithBattery(List<Coordinates> usingTilesWithBattery) {
        this.usingTilesWithBattery = usingTilesWithBattery;
    }

    public void setPenaltyNumber(int penaltyNumber) {
        this.penaltyNumber = penaltyNumber;
    }

    public void setFireShotNumber(int fireShotNumber) {
        this.fireShotNumber = fireShotNumber;
    }

    public void setFireShotDirectionNumber(int fireShotDirectionNumber) {
        this.fireShotDirectionNumber = fireShotDirectionNumber;
    }

    // --> LIST ADDERS <--
    public void addCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.add(coordinates);
    }

    // --> LIST REMOVER <--
    public void removeCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.remove(coordinates);
    }

    // --> LIST EMPTIER <--
    public void emptyList() {
        this.usingTilesWithBattery.clear();
    }
}
