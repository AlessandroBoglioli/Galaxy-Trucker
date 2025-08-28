package it.polimi.ingsoftware.ll13.model.cards.dtos;

/**
 * This is a DTO (Data Transfer Object) representing the Abandoned Station card case.
 * It extends the GeneralDTO to include additional information specific to this scenario.
 */
public class AbandonedStationDTO extends GeneralDTO {
    private boolean choice;

    /**
     * Default constructor for the AbandonedStationDTO.
     * Initializes the object with default values, setting the playerId to its default value
     * as defined in*/
    public AbandonedStationDTO() {
        super();
        this.choice = false;
    }

    /**
     * Constructs an AbandonedStationDTO object with the specified player ID.
     * This constructor initializes the object with the provided player ID and sets
     * the default state for the choice field to false.
     *
     * @param playerId the ID*/
    public AbandonedStationDTO(int playerId) {
        super(playerId);
        this.choice = false;
    }

    /**
     * Constructs an AbandonedStationDTO object with the specified player ID and choice state.
     *
     * @param playerId the ID of the player associated with this DTO
     * @param choice the choice state associated with the Abandoned Station card
     */
    public AbandonedStationDTO(int playerId, boolean choice) {
        super(playerId);
        this.choice = choice;
    }

    // --> GETTERS <--

    /**
     * Retrieves the current state of the 'choice' field.
     *
     * @return the value of the 'choice' field, representing the state or decision*/
    public boolean getChoice() {
        return choice;
    }

    // --> SETTERS <--

    /**
     * Sets the value of the 'choice' field.
     *
     * @param choice the new value to set for the 'choice' field
     */
    public void setChoice(boolean choice) {
        this.choice = choice;
    }
}
