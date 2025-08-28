package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.StellarSpaceDustDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

/**
 * The {@code StellarSpaceDustCard} class represents an "Stellar space dust" card in the game.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class StellarSpaceDustCard extends Card {

    /**
     * Constructor to create a new "Stellar space dust" card.
     *
     * @param level The level of the card.
     * @param image the image of the card.
     */
    public StellarSpaceDustCard(int level, String image) {
        super("Stellar space dust", level, image, new StellarSpaceDustDTO());
    }

    // --> Getters <--
    public StellarSpaceDustDTO getStellarSpaceDustDTO() {
        return (StellarSpaceDustDTO) super.getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getStellarSpaceDustDTO().getPlayerId());
        int exposedConnector = player.getShip().getShipStats().getExposedConnectors();
        gameModel.getGameMap().movePlayerBackward(player, exposedConnector);
        return false;
    }
}
