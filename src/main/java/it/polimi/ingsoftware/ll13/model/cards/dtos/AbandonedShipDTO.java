package it.polimi.ingsoftware.ll13.model.cards.dtos;

/**
 * This is a Data Transfer Object (DTO) class representing the Abandoned Ship scenario.
 * It extends the GeneralDTO class and includes a single additional field, choice,
 * which indicates a player's decision.
 */
public class AbandonedShipDTO extends GeneralDTO {
    private boolean choice;

    /**
     * Default constructor for the AbandonedShipDTO class.
     * It initializes the object with default values, including setting the
     * choice field to false.
     */
    public AbandonedShipDTO() {
        super();
        this.choice = false;
    }

    /**
     * Constructor for the AbandonedShipDTO class. Initializes the playerId*/
    public AbandonedShipDTO(int playerId) {
        super(playerId);
        this.choice = false;
    }

    /**
     * Constructor for the AbandonedShipDTO class. Initializes the object with the specified
     * player ID and choice indicating the decision of the player.
     *
     * @param playerId the ID of the player associated with the DTO.
     * @param choice the decision of the player, represented as a boolean value.
     */
    public AbandonedShipDTO(int playerId, boolean choice) {
        super(playerId);
        this.choice = choice;
    }

    // --> GETTERS <--

    /**
     * Retrieves the player's decision represented by the choice field.
     *
     * @return a boolean value indicating the player's decision.
     */
    public boolean getChoice() {
        return choice;
    }

    // --> SETTERS <--

    /**
     * Sets the player's choice or decision.
     *
     * @param choice a boolean value representing the player's decision.
     */
    public void setChoice(boolean choice) {
        this.choice = choice;
    }
}
