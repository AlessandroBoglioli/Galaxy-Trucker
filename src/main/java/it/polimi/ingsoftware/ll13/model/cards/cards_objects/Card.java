package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.dtos.GeneralDTO;

import java.io.Serializable;

/**
 * Represents an abstract card in the game.
 * This class is designed to be extended by multiple card types,
 * each implementing their specific effects on the gameModel.
 * Each card has a name, a level indicating its difficulty or tier, an image for its representation,
 * and a general data transfer object (DTO) to hold card-specific information.
 */
public abstract class Card implements Serializable {

    private final String name;
    private final int level;
    private final String image;
    private final GeneralDTO generalDTO;

    /**
     * Constructs a new {@code Card} with the specified name, level, image, and general data transfer object.
     *
     * @param name        The name of the card.
     * @param level       The level of the card.
     * @param image       The image path or identifier of the card.
     * @param generalDTO  A {@code GeneralDTO} object containing general data related to the card.
     */
    public Card(String name, int level, String image, GeneralDTO generalDTO) {
        this.name = name;
        this.level = level;
        this.image = image;
        this.generalDTO = generalDTO;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Retrieves the name of the card.
     *
     * @return The name of the card.
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the image path or identifier associated with the card.
     *
     * @return The image path or identifier of the card.
     */
    public String getImage() {
        return image;
    }

    /**
     * Retrieves the general data transfer object (DTO) associated with the card.
     *
     * @return The {@code GeneralDTO} object containing the general data related to the card.
     */
    public GeneralDTO getDTO() {
        return generalDTO;
    }

    /**
     * Applies the specific effect of the card to the provided game model.
     * Each card type should define its unique behavior when this method is invoked.
     *
     */
    public abstract boolean applyEffect(GameModel gameModel);
}
