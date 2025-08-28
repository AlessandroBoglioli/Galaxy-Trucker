package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a DTO (Data Transfer Object) for the OpenSpace card case
 */
public class OpenSpaceDTO extends GeneralDTO {
    private List<Coordinates> usingTilesWithBattery;

    /**
     * Constructor of the OpenSpaceDTO.
     */
    public OpenSpaceDTO() {
        super();
        this.usingTilesWithBattery = new ArrayList<>();
    }

    /**
     * Constructor of the OpenSpaceDTO.
     * @param playerId the playerId that will be used in the DTO.
     */
    public OpenSpaceDTO(int playerId) {
        super(playerId);
        this.usingTilesWithBattery = new ArrayList<>();
    }

    // --> GETTERS <--
    public List<Coordinates> getUsingTilesWithBattery() {
        return usingTilesWithBattery;
    }

    // --> SETTERS <--
    public void setUsingTilesWithBattery(List<Coordinates> usingTilesWithBattery) {
        this.usingTilesWithBattery = usingTilesWithBattery;
    }

    // --> Other methods <--
    public void emptyUsingTilesWithBattery() {
        this.usingTilesWithBattery.clear();
    }

    // --> LIST ADDER <--
    public void addCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.add(coordinates);
    }

    // --> LIST REMOVER <--
    public void removeCoordinates(Coordinates coordinates) {
        this.usingTilesWithBattery.remove(coordinates);
    }
}
