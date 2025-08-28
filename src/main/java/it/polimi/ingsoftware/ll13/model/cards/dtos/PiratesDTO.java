package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a DTO (Data Transfer Object) for the Pirates card case
 */
public class PiratesDTO extends GeneralDTO {
    private boolean choice;
    private int fireShotNumber;
    private int fireShotDirectionNumber;
    private List<Coordinates> usingDoubleCannons;
    private Coordinates usingShield;

    /**
     * Constructor of the PiratesDTO.
     */
    public PiratesDTO() {
        super();
        choice = false;
        fireShotNumber = -1;
        this.fireShotDirectionNumber = -1;
        this.usingDoubleCannons = new ArrayList<Coordinates>();
        this.usingShield = null;
    }

    /**
     * Constructor of the PiratesDTO.
     * @param playerId the playerId that will be used in the DTO.
     */
    public PiratesDTO(int playerId) {
        super(playerId);
        choice = false;
        fireShotNumber = -1;
        this.fireShotDirectionNumber = -1;
        this.usingDoubleCannons = new ArrayList<Coordinates>();
        this.usingShield = new Coordinates(-1, -1);
    }

    // --> GETTERS <--
    public boolean getChoice() {
        return choice;
    }

    public List<Coordinates> getUsingDoubleCannons() {
        return usingDoubleCannons;
    }

    public int getFireShotNumber() {
        return fireShotNumber;
    }

    public int getFireShotDirectionNumber() {
        return fireShotDirectionNumber;
    }

    public Coordinates getUsingShield() {
        return usingShield;
    }

    // --> SETTERS <--
    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public void setFireShotNumber(int fireShotNumber) {
        this.fireShotNumber = fireShotNumber;
    }

    public void setFireShotDirectionNumber(int fireShotDirectionNumber) {
        this.fireShotDirectionNumber = fireShotDirectionNumber;
    }

    public void setUsingDoubleCannons(List<Coordinates> usingDoubleCannons) {
        this.usingDoubleCannons = usingDoubleCannons;
    }

    public void setUsingShield(int row, int column) {
        if(row == -2 && column == -2){
            this.usingShield = null;
            return;
        }
        this.usingShield = new Coordinates(row, column);
    }

    // --> Other methods <--
    public void emptyUsingDoubleCannons() {
        usingDoubleCannons.clear();
    }

    // --> LIST ADDERS <--
    public void addCoordinate(Coordinates coordinates) {
        this.usingDoubleCannons.add(coordinates);
    }

    // --> LIST REMOVER <--
    public void removeCoordinates(Coordinates coordinates) {
        this.usingDoubleCannons.remove(coordinates);
    }
}
