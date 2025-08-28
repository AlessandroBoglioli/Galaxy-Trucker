package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.AbandonedShipDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

/**
 * Represents an "Abandoned Ship" card, a specific type of card with unique effects in the game.
 * This card requires the sacrifice of a certain number of crew members in exchange for rewards
 * such as credits and a reduction in flight days. It extends the {@code Card} class and utilizes
 * the {@code AbandonedShipDTO} for transfer and management of specific data.
 *
 * The card has the following properties:
 * - {@code crewSacrifice}: The number of crew members required to sacrifice.
 * - {@code creditsReward}: The number of credits rewarded to the player.
 * - {@code flightDaysReduction}: The reduction in flight days for the player.
 */
public class AbandonedShipCard extends Card{

    private final int crewSacrifice;
    private final int creditsReward;
    private final int flightDaysReduction;

    /**
     * Constructs an AbandonedShipCard object. This card represents a scenario where players may sacrifice
     * a specific number of crew members to gain rewards in the form of credits and a reduction in the
     * required flight days. It extends the {@code Card} class and utilizes an {@code AbandonedShipDTO}
     * for transferring and managing card-specific data.
     *
     * @param level               The level or difficulty of the card.
     * @param image               The image path or identifier of the card.
     * @param crewSacrifice       The number of crew members that need to be sacrificed.
     * @param creditsReward       The amount of credits rewarded upon using the card.
     * @param flightDaysReduction The reduction in flight days rewarded upon using the card.
     */
    public AbandonedShipCard(int level, String image, int crewSacrifice, int creditsReward, int flightDaysReduction) {
        super("Abandoned Ship", level, image, new AbandonedShipDTO());
        this.crewSacrifice = crewSacrifice;
        this.creditsReward = creditsReward;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Retrieves the number of crew members that need to be sacrificed
     * to use this AbandonedShipCard.
     *
     * @return The number of crew members required to be sacrificed.
     */
    public int getCrewSacrifice() {
        return crewSacrifice;
    }

    /**
     * Retrieves the amount of credits rewarded by this AbandonedShipCard.
     *
     * @return The amount of credits reward.
     */
    public int getCreditsReward() {
        return creditsReward;
    }

    /**
     * Retrieves the reduction in flight days rewarded by this AbandonedShipCard.
     *
     * @return The number of flight days reduced.
     */
    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    /**
     * Retrieves the specific {@code AbandonedShipDTO} associated with this card.
     * This method casts and returns the general data transfer object of the card
     * to its specific type {@code AbandonedShipDTO}.
     *
     * @return The {@code AbandonedShipDTO} containing the data specific to the AbandonedShipCard.
     */
    public AbandonedShipDTO getAbandonedShipDTO() {
        return (AbandonedShipDTO) getDTO();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other functions ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Applies the effect of the AbandonedShipCard to the game.
     * This effect involves sacrificing a specified number of crew members
     * in exchange for a reward of credits and a reduction in flight days.
     *
     * The method checks if the player agrees to the sacrifice, if the player belongs to the game,
     * and whether the player has enough crew members to meet the required sacrifice. If all
     * conditions are met, it deducts the crew members, awards the credits, and reduces the flight days.
     *
     * @param gameModel the game model representing the current state of the game,
     *                  including players, their ships, and the game map.
     * @return {@code true} if the effect was successfully applied by sacrificing
     *         crew members, awarding credits, and reducing flight days;
     *         {@code false} otherwise.
     */
    @Override
    public boolean applyEffect(GameModel gameModel) {
        // Checking if the player wants to sacrifice crew members for the abandoned ship
        if(!getAbandonedShipDTO().getChoice()) return false;

        Player player = gameModel.getPlayerById(getAbandonedShipDTO().getPlayerId());

        // Checking if the player hasn't enough crew to sacrifice
        if(player.getShip().getShipStats().getCrewMembers() < getCrewSacrifice()) return false;

        player.getShip().getShipStats().eliminateCrewMembers(player.getShip().getShipLayout(), getCrewSacrifice());
        player.addCredits(getCreditsReward());
        gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());

        return true;
    }
}