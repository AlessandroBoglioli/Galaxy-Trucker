package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.OpenSpaceDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

/**
 * The {@code OpenSpaceCard} class represents an "Open space" card in the game.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class OpenSpaceCard extends Card {

    /**
     * Constructor to create a new "Open space" card.
     *
     * @param level The level of the card.
     * @param image the image of the card.
     */
    public OpenSpaceCard(int level, String image) {
        super("Open space", level, image, new OpenSpaceDTO());
    }

    // --> Getters <--
    public OpenSpaceDTO getOpenSpaceDTO() {
        return (OpenSpaceDTO) getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getOpenSpaceDTO().getPlayerId());
        int totalThrustPower = player.getShip().getShipStats().calculateTotalThrustPower(
                player.getShip().getShipLayout(),
                player.getShip().getShipCrewPlacer(),
                player.getShip().getBatteryManager(),
                getOpenSpaceDTO().getUsingTilesWithBattery()
        );

        // If thrust power is equal to zero, the player that called this, lose.
        if(totalThrustPower == 0) {
            return false;
        }
        gameModel.getGameMap().movePlayerForward(player, totalThrustPower);
        return true;
    }
}
