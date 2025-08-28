package it.polimi.ingsoftware.ll13.model.cards.dtos;

/**
 * This is a DTO (Data Transfer Object) for the Epidemic card case
 */
public class EpidemicDTO extends GeneralDTO {
    /**
     * Constructor of the Epidemic DTO.
     */
    public EpidemicDTO() {
        super();
    }

    /**
     * Constructor of the Epidemic DTO.
     * @param senderId the playerId that the DTO will use.
     */
    public EpidemicDTO(int senderId) {
        super(senderId);
    }
}
