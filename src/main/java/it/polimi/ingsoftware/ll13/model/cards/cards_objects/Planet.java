package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;

import java.io.Serializable;
import java.util.List;

/**
 * The {@code Planet} represents a planet with a specific list of cargo colors.
 * It provides a way to retrieve the list of cargo colors associated with the planet.
 */
public class Planet implements Serializable {

    boolean isChosen;
    private final List<CargoColor> cargo;

    /**
     * Constructs a new Planet with the specified list of cargo colors.
     *
     * @param cargos The list of cargo colors for the planet.
     */
    public Planet(List<CargoColor> cargos) {
        this.cargo = cargos;
        isChosen = false;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the list of cargo colors available on the planet.
     *
     * @return The list of cargo colors.
     */
    public List<CargoColor> getCargos() {
        return cargo;
    }

    /**
     * Retrieves if the planet is already chosen or if it is not.
     *
     * @return a boolean that represent if the planet is chosen or not
     */
    public boolean isChosen() {
        return isChosen;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Set the planet occupation to false, which means that the planet is no more occupied
     */
    public void leavePlanet() {
        isChosen = false;
    }

    /**
     * Set the planet occupation to true, which means that the planet is occupied.
     */
    public void occupyPlanet() {
        isChosen = true;
    }
}

/* ILLUSTRATIVE EXAMPLE FOR THE CREATION OF THE PLANET
    Planet planet1 = new Planet(List.of(CargoColor.RED, CargoColor.BLUE));
    Planet planet2 = new Planet(List.of(CargoColor.YELLOW, CargoColor.GREEN));
*/