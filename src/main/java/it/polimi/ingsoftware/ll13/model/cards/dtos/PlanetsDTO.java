package it.polimi.ingsoftware.ll13.model.cards.dtos;

/**
 * This is a DTO (Data Transfer Object) for the Planet card case.
 */
public class PlanetsDTO extends GeneralDTO {
    private int choice; // 0 (no planet), 1 (first planet), 2 ...

    /**
     * Constructor of the planetsDTO,
     */
    public PlanetsDTO() {
        super();
        this.choice = 0;
    }

    /**
     * Constructor of the planetsDTO,
     * @param playerId the playerId that will be used in the DTO.
     */
    public PlanetsDTO(int playerId) {
        super(playerId);
        this.choice = 0;
    }

    // --> GETTERS <--
    public int getChoice() {
        return choice;
    }

    // --> SETTERS <--
    public void setChoice(int choice) {
        this.choice = choice;
    }
}
