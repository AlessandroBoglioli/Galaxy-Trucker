package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.WarZoneDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * The {@code WarZoneCard} class represents an "War zone" card in the game.
 * This card specifies the list of the 3 penalty of the card.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class WarZoneCard extends Card {

    private final List<WarZonePenalty> penalties;

    /**
     * Constructor to create a new "War zone" card.
     *
     * @param level the level of the card.
     * @param image the image of the card.
     * @param penalties a list of the penalties of the card.
     */
    public WarZoneCard(int level, String image, List<WarZonePenalty> penalties) {
        super("War zone", level, image, new WarZoneDTO());
        this.penalties = penalties;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public List<WarZonePenalty> getPenalties() {
        return penalties;
    }

    public WarZoneDTO getWarZoneDTO() {
        return (WarZoneDTO) super.getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getWarZoneDTO().getPlayerId());
        WarZonePenalty penalty = getPenalties().get(getWarZoneDTO().getPenaltyNumber());
        switch(penalty.getWarZonePenaltyEffect()) {
            case MOVE_BACK:
                gameModel.getGameMap().movePlayerBackward(player, penalty.getValue());
                break;
            case LOSE_CREW:
                player.getShip().getShipStats().eliminateCrewMembers(player.getShip().getShipLayout(), penalty.getValue());
                break;
            case LOSE_CARGOS:
                // It's the same process as the smugglers ones...
                player.getShip().getProblemHandler().handleSmugglers(
                        player.getShip().getShipLayout(),
                        player.getShip().getBatteryManager(),
                        player.getShip().getCargosManager(),
                        penalty.getValue()
                );
                break;
            case HIT_BY_PROJECTILES: {
                FireShot fireShot = penalty.getFireShots().get(getWarZoneDTO().getFireShotNumber());
                if (getWarZoneDTO().getUsingTilesWithBattery().isEmpty()){
                    handlePirateFireShot(
                            player,
                            fireShot,
                            getWarZoneDTO().getFireShotDirectionNumber(),
                            -1,
                            -1
                    );
                } else {
                    handlePirateFireShot(
                            player,
                            fireShot,
                            getWarZoneDTO().getFireShotDirectionNumber(),
                            getWarZoneDTO().getUsingTilesWithBattery().getFirst().getRow(),
                            getWarZoneDTO().getUsingTilesWithBattery().getFirst().getCol()
                    );
                }

                break;
            }
        }
        if (getPenalties().get(getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyEffect() == WarZonePenaltyEffect.HIT_BY_PROJECTILES && getWarZoneDTO().getFireShotNumber() < getPenalties().get(getWarZoneDTO().getPenaltyNumber()).getFireShots().size() - 1 ) {
            return false;
        }
        return getWarZoneDTO().getPenaltyNumber() == (getPenalties().size() - 1);
    }

    private void handlePirateFireShot(Player player, FireShot fireShot, int directionNumber, int row, int col) {
        if(fireShot.getFireShotType() == ProblemType.SMALL) {
            player.getShip().getProblemHandler().handleSmallFireShots(
                    player.getShip().getShipLayout(),
                    player.getShip().getShipStats(),
                    player.getShip().getBatteryManager(),
                    player.getShip().getCargosManager(),
                    fireShot, directionNumber, row, col);
        } else {
            player.getShip().getProblemHandler().handleBigFireShot(
                    player.getShip().getShipLayout(),
                    player.getShip().getShipStats(),
                    player.getShip().getBatteryManager(),
                    player.getShip().getCargosManager(),
                    fireShot, directionNumber
            );
        }
    }
}