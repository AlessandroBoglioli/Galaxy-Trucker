package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.PiratesDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * The {@code PirateCard} class represents an "Pirate" card in the game.
 * This card specifies a fire power needed for the win against pirates, a list of direction of fire shot in case of lost against them, reward credits in case of win and the number of flight reduction in case of win.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class PirateCard extends Card {

    private final int firePowerNeeded;
    private final List<FireShot> fireShots;
    private final int creditsReward;
    private final int flightDaysReduction;

    /**
     * Constructor to create a new "Pirate" card.
     * @param level The level of the card.
     * @param image the image of the card.
     * @param firePowerNeeded the fire power value needed to win.
     * @param fireShots the fire shot taken in case of loss.
     * @param creditsReward the number of credits earned.
     * @param flightDaysReduction the number of flight days lost.
     */
    public PirateCard(int level, String image, int firePowerNeeded, List<FireShot> fireShots, int creditsReward, int flightDaysReduction) {
        super("Pirate", level, image, new PiratesDTO());
        this.firePowerNeeded = firePowerNeeded;
        this.fireShots = fireShots;
        this.creditsReward = creditsReward;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public int getFirePowerNeeded() {
        return firePowerNeeded;
    }

    public List<FireShot> getFireShots() {
        return fireShots;
    }

    public int getCreditsReward() {
        return creditsReward;
    }

    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    public PiratesDTO getPiratesDTO() {
        return (PiratesDTO) getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getPiratesDTO().getPlayerId());
        if (!getPiratesDTO().getChoice()) {
            Coordinates shield = getPiratesDTO().getUsingShield();
            if (shield == null) {
                return false;
            }

            int row = shield.getRow();
            int col = shield.getCol();

            return handlePirateFireShot(
                    player,
                    getFireShots().get(getPiratesDTO().getFireShotNumber()),
                    getPiratesDTO().getFireShotDirectionNumber(),
                    row, col
            );
        }

        int firePower = (int) player.getShip().getShipStats().calculateTotalFirePower(
                player.getShip().getShipLayout(),
                player.getShip().getShipCrewPlacer(),
                player.getShip().getBatteryManager(),
                getPiratesDTO().getUsingDoubleCannons()
        );
        if (firePower < getFirePowerNeeded()) {
            return false;
        }
        player.addCredits(getCreditsReward());
        gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());
        return true;
    }



    private boolean handlePirateFireShot(Player player, FireShot fireShot, int directionNumber, int row, int col) {
        if(fireShot.getFireShotType() == ProblemType.SMALL) {
            // remember: if the return is false, the card pass to the next player
            player.getShip().getProblemHandler().handleSmallFireShots(
                player.getShip().getShipLayout(),
                player.getShip().getShipStats(),
                player.getShip().getBatteryManager(),
                player.getShip().getCargosManager(),
                fireShot, directionNumber, row, col
            );
        } else {
            player.getShip().getProblemHandler().handleBigFireShot(
                player.getShip().getShipLayout(),
                player.getShip().getShipStats(),
                player.getShip().getBatteryManager(),
                player.getShip().getCargosManager(),
                fireShot, directionNumber
            );
        }
        return false;
    }
}
