package it.polimi.ingsoftware.ll13.model.cards.cards_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;

import java.io.Serializable;

/**
 * The {@code FireShot} class represents a fire shot in the game.
 * It contains information about the type of fire shot and its direction.
 * This class provides getters and setters to access and modify its properties.
 *
 * @see ProblemType
 * @see Direction
 */
public class FireShot implements Serializable {

    private ProblemType fireShotType;
    private Direction fireShotDirection;

    /**
     * Constructs a new {@code FireShot} with the specified type and direction.
     *
     * @param fireShotType      The type of the fire shot.
     * @param fireShotDirection The direction of the fire shot.
     */
    public FireShot(ProblemType fireShotType, Direction fireShotDirection) {
        this.fireShotType = fireShotType;
        this.fireShotDirection = fireShotDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the type of the fire shot.
     *
     * @return The type of the fire shot.
     */
    public ProblemType getFireShotType() {
        return fireShotType;
    }

    /**
     * Retrieves the direction of the fire shot.
     *
     * @return The direction of the fire shot.
     */
    public Direction getFireShotDirection() {
        return fireShotDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets the type of the fire shot.
     *
     * @param fireShotType The new type of the fire shot.
     */
    public void setFireShotType(ProblemType fireShotType) {
        this.fireShotType = fireShotType;
    }

    /**
     * Sets the direction of the fire shot.
     *
     * @param fireShotDirection The new direction of the fire shot.
     */
    public void setFireShotDirection(Direction fireShotDirection) {
        this.fireShotDirection = fireShotDirection;
    }
}