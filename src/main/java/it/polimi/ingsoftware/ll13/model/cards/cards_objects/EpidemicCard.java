package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.EpidemicDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

/**
 * The {@code EpidemicCard} class represents an "Epidemic" card in the game.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class EpidemicCard extends Card{

    /**
     * Constructor to create a new "Epidemic" card.
     *
     * @param level The level of the card.
     * @param image the image of the card.
     */
    public EpidemicCard(int level, String image) {
        super("Epidemic", level, image, new EpidemicDTO());
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public EpidemicDTO getEpidemicDTO() {
        return (EpidemicDTO) super.getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getEpidemicDTO().getPlayerId());
        player.getShip().getCabinManager().handleEpidemic(player.getShip().getShipLayout(), player.getShip().getShipStats());
        return true;
    }
}
