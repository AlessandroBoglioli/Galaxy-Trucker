package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.cards.dtos.GeneralDTO;
import it.polimi.ingsoftware.ll13.model.cards.dtos.PiratesDTO;
import it.polimi.ingsoftware.ll13.model.cards.dtos.PlanetsDTO;
import it.polimi.ingsoftware.ll13.model.GameModel;
import it.polimi.ingsoftware.ll13.model.player.Player;

import java.util.List;

/**
 * The {@code PlanetsCard} class represents a "Planets" card in the game.
 * This card specifies a list of planets with inside cargos that you can take and a number of flight days reduction in case of landing on the planet.
 * It extends the abstract class {@code Card} and implements the specific properties of this card type.
 *
 * @see Card
 */
public class PlanetsCard extends Card {

    private final List<Planet> planets;
    private final int flightDaysReduction;

    /**
     * Constructor to create a new "Planets" card.
     * @param level the level of the card.
     * @param image the image of the ship.
     * @param planets the list of planets that can be discovered.
     * @param flightDaysReduction the number of flight days to reduce.
     */
    public PlanetsCard(int level, String image, List<Planet> planets, int flightDaysReduction) {
        super("Planets", level, image, new PlanetsDTO());
        this.planets = planets;
        this.flightDaysReduction = flightDaysReduction;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public List<Planet> getPlanets() {
        return planets;
    }

    public PlanetsDTO getPlanetsDTO() {
        return (PlanetsDTO) getDTO();
    }

    public int getFlightDaysReduction() {
        return flightDaysReduction;
    }

    /*
        A method that return if the planet chosen is correct or not.
     */
    private boolean isChooseCorrect(int number) {
        if(number >= planets.size() || number <= -1) return false;
        if(planets.get(number).isChosen()) return false;
        return true;
    }

    /**
     * This is the effect of the card Planets applied only on a player.
     * @param gameModel the game model that needs to be updated.
     * @return if the effect needs to be applied to the next player or not.
     */
    @Override
    public boolean applyEffect(GameModel gameModel) {
        int choice = getPlanetsDTO().getChoice();

        if (choice < 0 || choice > getPlanets().size()) return false;
        if (choice == 0) return false;
        if (getPlanets().get(choice - 1).isChosen()) return false;

        getPlanets().get(choice - 1).occupyPlanet();
        Player player = gameModel.getPlayerById(getPlanetsDTO().getPlayerId());

        player.getShip().getCargosManager().addCargos(
                player.getShip().getShipLayout(),
                getPlanets().get(choice - 1).getCargos()
        );

        gameModel.getGameMap().movePlayerBackward(player, getFlightDaysReduction());

        return false;
    }

}
