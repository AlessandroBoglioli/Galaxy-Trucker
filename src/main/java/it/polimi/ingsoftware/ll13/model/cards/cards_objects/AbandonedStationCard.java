package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.dtos.AbandonedStationDTO;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * Represents a specific type of card in the game denoted as "Abandoned Station".
 * This card can have effects such as rewarding the player with cargo elements,
 * reducing the flight days, and requiring a minimum crew to proceed with its effect.
 * It extends the abstract Card class to include properties and behavior specific to an abandoned station interaction.
 */
public class AbandonedStationCard extends Card {

    private final int requiredCrew;
    private final List<CargoColor> rewards;
    private final int flightDaysReduction;

    /**
     * Constructs a new {@code AbandonedStationCard} with the specified parameters.
     * This card represents an abandoned station encounter, which requires a certain
     * crew size, provides specific rewards, and may reduce the travel flight days.
     *
     * @param level               The level or difficulty of the card.
     * @param image               The image path or identifier for the card.
     * @param requiredCrew        The minimum crew required to engage with the card's effects.
     * @param rewards             The list of cargo rewards available for successfully using the card.
     * @param flightDaysReduction The number of flight days reduced when utilizing this card's effect.
     */
    public AbandonedStationCard(int level, String image, int requiredCrew, List<CargoColor> rewards, int flightDaysReduction) {
        super("Abandoned Station", level, image, new AbandonedStationDTO());
        this.requiredCrew = requiredCrew;
        this.rewards = rewards;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Retrieves the minimum crew required to engage with the effects of this card.
     *
     * @return The minimum number of crew members required.
     */
    public int getRequiredCrew() {
        return requiredCrew;
    }

    /**
     * Retrieves the list of rewards associated with this card.
     * The rewards are represented as a list of cargo colors that
     * the player can gain upon successfully engaging with this card's effect.
     *
     * @return A list of {@code CargoColor} representing the rewards of this card.
     */
    public List<CargoColor> getRewards() {
        return rewards;
    }

    /**
     * Retrieves the number of flight days reduced associated with this card's effect.
     *
     * @return The number of flight days reduced.
     */
    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    /**
     * Retrieves the data transfer object (DTO) specific to the {@code AbandonedStationCard}.
     * This DTO contains information relevant to an abandoned station scenario
     * in the context of the game.
     *
     * @return The {@code AbandonedStationDTO} associated with this card,
     *         containing data related to the abandoned station encounter.
     */
    public AbandonedStationDTO getAbandonedStationDTO() {
        return (AbandonedStationDTO) super.getDTO();
    }

    /**
     * Applies the effect of the abandoned station card on the game model.
     * This involves verifying the player's decision to interact with the station,
     * ensuring the player has enough crew members, adding rewards to the player's
     * ship, and adjusting the player's position on the game map by reducing flight days.
     *
     * @param gameModel The current instance of the game model to apply the effect on.
     *                  This contains all game state data, including player information.
     * @return {@code true} if the effect is successfully applied, {@code false} otherwise.
     *         The effect fails if the player does not choose to engage or lacks the required crew.
     */
    @Override
    public boolean applyEffect(GameModel gameModel) {
        // Checking if the player wants to attack the abandoned station
        if(!getAbandonedStationDTO().getChoice()) return false;

        Player player = gameModel.getPlayerById(getAbandonedStationDTO().getPlayerId());

        // Check if the crew that the player has is enough
        if(player.getShip().getShipStats().getCrewMembers() < getRequiredCrew()) return false;

        player.getShip().getCargosManager().addCargos(player.getShip().getShipLayout(), getRewards());
        gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());

        return true;
    }
}

