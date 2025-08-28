package it.polimi.ingsoftware.ll13.model.cards.dtos;

/**
 * This is a DTO (Data Transfer Object) for the StellarSpaceDust card case
 */
public class StellarSpaceDustDTO extends GeneralDTO {
    /**
     * Constructor of the StellarSpaceDustDTO.
     */
    public StellarSpaceDustDTO() {
        super();
    }

    /**
     * Constructor of the StellarSpaceDustDTO.
     * @param senderId the playerId that will be used in the DTO.
     */
    public StellarSpaceDustDTO(int senderId) {
        super(senderId);
    }
}
