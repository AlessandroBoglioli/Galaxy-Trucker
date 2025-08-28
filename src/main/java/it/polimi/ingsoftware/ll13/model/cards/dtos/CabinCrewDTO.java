package it.polimi.ingsoftware.ll13.model.cards.dtos;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;

/**
 * This is a DTO (Data Transfer Object) for the ??? card case
 */
public class CabinCrewDTO extends GeneralDTO {
    private final Coordinates coordinates;
    private CrewMember crewMember;

    /**
     * Creator of the CabinCrewDTO.
     */
    public CabinCrewDTO() {
        super();
        this.coordinates = new Coordinates(-1, -1);
        this.crewMember = null;
    }

    /**
     * Creator of the CabinCrewDTO.
     * @param playerId the playerId that the DTO will use.
     * @param row the row of the cabin.
     * @param column the column of the cabin.
     */
    public CabinCrewDTO(int playerId, int row, int column) {
        super(playerId);
        this.coordinates = new Coordinates(row, column);
        this.crewMember = null;
    }

    // --> GETTER <--
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }

    // --> SETTER <--
    public void setCoordinates(int row, int column) {
        this.coordinates.setRow(row);
        this.coordinates.setCol(column);
    }

    public void setCrewMember(CrewMember crewMember) {
        this.crewMember = crewMember;
    }

}
