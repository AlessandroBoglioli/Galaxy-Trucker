package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.SmugglersDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * The {@code SmugglersCard} class represents an "Smugglers" card in the game.
 * This card specifies a fire power needed to win the battle against smugglers, a cargo penalty in case of loss, a list of cargo rewards and a number of flight days reduction in case of win.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class SmugglersCard extends Card {

    private final int firePowerNeeded;
    private final int cargoPenalty;
    private final List<CargoColor> rewards;
    private final int flightDaysReduction;

    /**
     * Constructor to create a new "Smugglers" card.
     *
     * @param level the level of the card.
     * @param image the image of the ship.
     * @param firePowerNeeded the number of fire power needed to win the battle.
     * @param cargoPenalty the number of cargo penalty in case of lost.
     * @param rewards the list of cargos rewarded in case of win.
     * @param flightDaysReduction the number of flight days to reduce.
     */
    public SmugglersCard(int level, String image, int firePowerNeeded, int cargoPenalty, List<CargoColor> rewards, int flightDaysReduction) {
        super("Smugglers", level, image, new SmugglersDTO());
        this.firePowerNeeded = firePowerNeeded;
        this.cargoPenalty = cargoPenalty;
        this.rewards = rewards;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public int getFirePowerNeeded() {
        return firePowerNeeded;
    }

    public int getCargoPenalty() {
        return cargoPenalty;
    }

    public List<CargoColor> getRewards() {
        return rewards;
    }

    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    public SmugglersDTO getSmugglersDTO() {
        return (SmugglersDTO) super.getDTO();
    }

    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getSmugglersDTO().getPlayerId());
        if(!getSmugglersDTO().getChoice()) {
            player.getShip().getProblemHandler().handleSmugglers(
                    player.getShip().getShipLayout(),
                    player.getShip().getBatteryManager(),
                    player.getShip().getCargosManager(),
                    getCargoPenalty()
            );
            return false;
        }

        double totalFirePower = player.getShip().getShipStats().calculateTotalFirePower(
                player.getShip().getShipLayout(),player.getShip().getShipCrewPlacer(), player.getShip().getBatteryManager(), getSmugglersDTO().getUsingTilesWithBattery()
        );
        if(totalFirePower < getFirePowerNeeded()) {
            player.getShip().getProblemHandler().handleSmugglers(
                    player.getShip().getShipLayout(),
                    player.getShip().getBatteryManager(),
                    player.getShip().getCargosManager(),
                    getCargoPenalty()
            );
            return false;
        } else {
            if(totalFirePower == getFirePowerNeeded()) {
                return false;
            } else {
                player.getShip().getCargosManager().addCargos(player.getShip().getShipLayout(), getRewards());
                gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());
                return true;
            }
        }
    }
}
