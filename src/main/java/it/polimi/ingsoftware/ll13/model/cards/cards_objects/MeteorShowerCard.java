package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.MeteorShowerDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * The {@code MeteorShowerCard} class represents an "Meteor shower" card in the game.
 * This card specifies a list of direction of small meteors and a list of direction of great meteors.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class MeteorShowerCard extends Card{
    private final List<Meteor> meteors;

    /**
     * Constructor to create a new "Meteor shower" card.
     * @param level The level of the card.
     * @param image the image of the card.
     * @param meteors The list of meteors of great meteors.
     */
    public MeteorShowerCard(int level, String image, List<Meteor> meteors) {
        super("Meteor Shower", level, image, new MeteorShowerDTO());
        this.meteors = meteors;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public MeteorShowerDTO getMeteorShowerDTO() {
        return (MeteorShowerDTO) getDTO();
    }

    public List<Meteor> getMeteors() {
        return meteors;
    }

    /**
     * Apply the effect of the meteorShowerCard of a single effect.
     * @param gameModel the game model that needs to be updated.
     * @return a boolean representing if the effect is finished.
     */
    @Override
    public boolean applyEffect(GameModel gameModel) {
        Player player = gameModel.getPlayerById(getMeteorShowerDTO().getPlayerId());
        // The creation of the MeteorDirectionNumber is delegated to the gameModel in a random way
        // The creation of the MeteorDirectionNumber will take place in the first part of the total request (model -> controller -> view -> controller -> model)
        Meteor currentMeteor = getMeteors().get(getMeteorShowerDTO().getMeteorNumber());
        if(currentMeteor.getMeteorType() == ProblemType.SMALL) {
            player.getShip().getProblemHandler().handleSmallMeteorImpact(
                player.getShip().getShipLayout(),
                player.getShip().getShipStats(),
                player.getShip().getBatteryManager(),
                player.getShip().getCargosManager(),
                currentMeteor,
                getMeteorShowerDTO().getMeteorDirectionNumber(), getMeteorShowerDTO().getUsingTileWithBattery().getRow(), getMeteorShowerDTO().getUsingTileWithBattery().getCol()
            );
        } else {
            player.getShip().getProblemHandler().handleBigMeteorImpact(
                player.getShip().getShipLayout(),
                player.getShip().getShipStats(),
                player.getShip().getBatteryManager(),
                player.getShip().getCargosManager(),
                currentMeteor,
                getMeteorShowerDTO().getMeteorDirectionNumber(), getMeteorShowerDTO().getUsingTileWithBattery().getRow(), getMeteorShowerDTO().getUsingTileWithBattery().getCol()
            );
        }
        return getMeteorShowerDTO().getMeteorNumber() == (getMeteors().size()-1);
    }
}
