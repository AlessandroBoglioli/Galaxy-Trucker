package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;

import java.io.Serializable;

/**
 * The {@code Meteor} class represents a meteor in the game.
 * It contains information about the type of meteor and its direction.
 * This class provides getters and setters to access and modify its properties.
 *
 * @see ProblemType
 * @see Direction
 */
public class Meteor implements Serializable {

    private ProblemType meteorType;
    private Direction meteorDirection;

    /**
     * Constructs a new {@code Meteor} with the specified type and direction.
     *
     * @param meteorType      The type of the meteor.
     * @param meteorDirection The direction of the meteor.
     */
    public Meteor(ProblemType meteorType, Direction meteorDirection) {
        this.meteorType = meteorType;
        this.meteorDirection = meteorDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the type of the meteor.
     *
     * @return The type of the meteor.
     */
    public ProblemType getMeteorType() {
        return meteorType;
    }

    /**
     * Retrieves the direction of the meteor.
     *
     * @return The direction of the meteor.
     */
    public Direction getMeteorDirection() {
        return meteorDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets the type of the meteor.
     *
     * @param meteorType The new type of the meteor.
     */
    public void setMeteorType(ProblemType meteorType) {
        this.meteorType = meteorType;
    }

    /**
     * Sets the direction of the meteor.
     *
     * @param meteorDirection The new direction of the meteor.
     */
    public void setMeteorDirection(Direction meteorDirection) {
        this.meteorDirection = meteorDirection;
    }
}