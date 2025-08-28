package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.SlaversDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

/**
 * The {@code SlaversCard} class represents an "Slavers" card in the game.
 * This card specifies a fire power needed, a crew sacrifice in case of loss, a number of credit rewarded in case of won and a number of flights days reduction in case of won.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class SlaversCard extends Card {

    private final int firePowerNeeded;
    private final int crewSacrifice;
    private final int creditsReward;
    private final int flightDaysReduction;

    /**
     * Constructor to create a new "Slavers" card.
     *
     * @param level The level of the card.
     * @param image the image of the card.
     * @param firePowerNeeded the fire power needed to win.
     * @param crewSacrifice the number of crew members sacrificed in case of loss.
     * @param creditsReward the number of credit gained in case of win.
     * @param flightDaysReduction the number of flight days to reduce.
     */
    public SlaversCard(int level, String image, int firePowerNeeded, int crewSacrifice, int creditsReward, int flightDaysReduction) {
        super("Slavers", level, image, new SlaversDTO());
        this.firePowerNeeded = firePowerNeeded;
        this.crewSacrifice = crewSacrifice;
        this.creditsReward = creditsReward;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public int getFirePowerNeeded() {
        return firePowerNeeded;
    }

    public int getCrewSacrifice() {
        return crewSacrifice;
    }

    public int getCreditsReward() {
        return creditsReward;
    }

    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    public SlaversDTO getSlaversDTO() {
        return (SlaversDTO) super.getDTO();
    }

    /**
     * This is the function that applies the effect of the Slavers card to a specific player.
     * @param gameModel the game model that needs to be updated.
     * @return a boolean that represent if the slavers are defeated or not.
     */
    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getSlaversDTO().getPlayerId());

        // If the choice is negative, remove crew members
        if(!getSlaversDTO().getChoice()) {
            handleRemovalCrewMembers(player, getCrewSacrifice());
            return false;
        }

        double totalFirePower = player.getShip().getShipStats().calculateTotalFirePower(
                player.getShip().getShipLayout(),
                player.getShip().getShipCrewPlacer(),
                player.getShip().getBatteryManager(),
                getSlaversDTO().getUsingTilesWithBattery()
        );
        if(totalFirePower < getFirePowerNeeded()) {
            // Fire power not enough
            handleRemovalCrewMembers(player, getCrewSacrifice());
            return false;
        } else {
            if(totalFirePower == getFirePowerNeeded()) {
                return false;
            } else {
                player.addCredits(getCreditsReward());
                gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());
                return true;
            }
        }
    }

    private void handleRemovalCrewMembers(Player player, int crewMembers) {
//        if(player.getShip().getShipStats().getCrewMembers() <= crewMembers) {
//            return;
//        }
        player.getShip().getShipStats().eliminateCrewMembers(player.getShip().getShipLayout(), getCrewSacrifice());
    }
}
