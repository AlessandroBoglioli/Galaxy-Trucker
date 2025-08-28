package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a DTO (Data Transfer Object) for the Smugglers card case
 */
public class SmugglersDTO extends GeneralDTO {
    private boolean choice;
    private List<Coordinates> usingTilesWithBattery;

    /**
     * Constructor of the SmugglersDTO.
     */
    public SmugglersDTO() {
        super();
        choice = false;
        this.usingTilesWithBattery = new ArrayList<>();
    }

    /**
     * Constructor of the SmugglersDTO.
     * @param playerId the playerId that will be used in the DTO.
     */
    public SmugglersDTO(int playerId) {
        super(playerId);
        choice = false;
        this.usingTilesWithBattery = new ArrayList<>();
    }

    // --> GETTERS <--
    public boolean getChoice() {
        return choice;
    }

    public List<Coordinates> getUsingTilesWithBattery() {
        return usingTilesWithBattery;
    }

    // --> SETTERS <--
    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public void setUsingTilesWithBattery(List<Coordinates> usingTilesWithBattery) {
        this.usingTilesWithBattery = usingTilesWithBattery;
    }

    // --> Other methods <--
    public void emptyUsingTilesWithBattery() {
        usingTilesWithBattery.clear();
    }

    // --> LIST ADDERS <--
    public void addCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.add(coordinates);
    }

    // --> LIST REMOVER <--
    public void removeCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.remove(coordinates);
    }
}
