package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

/**
 * This class represents the base functionality for cannon tiles.
 */
public abstract class CannonBaseTile extends Tile {

    protected Direction cannonDirection;

    /**
     * Constructor for CannonBaseTile.
     *
     * @param image             The image of the tile.
     * @param topType           Indicates the type of the connector positioned on the top side of the card.
     * @param rightType         Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType        Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType          Indicates the type of the connector positioned on the left side of the card.
     * @param cannonDirection   Indicates where the cannon is pointing.
     */
    public CannonBaseTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction cannonDirection) {
        super(image, topType, rightType, bottomType, leftType);
        this.cannonDirection = cannonDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the direction where the cannon is aiming.
     *
     * @return The direction of the cannon.
     */
    public Direction getCannonDirection() {
        return cannonDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets the direction of the cannon.
     *
     * @param cannonDirection   The new direction of the cannon. Cannot be null.
     * @throws IllegalArgumentException if cannonDirection is null.
     */
    protected void setCannonDirection(Direction cannonDirection) {
        if (cannonDirection == null) {
            throw new IllegalArgumentException("cannonDirection cannot be null");
        }
        this.cannonDirection = cannonDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other methods ~~~~~~~~~~~~~~~~~~~~~~~~
    private void rotateCannonClockwise() {
        switch (cannonDirection) {
            case TOP: this.setCannonDirection(Direction.RIGHT); break;
            case RIGHT: this.setCannonDirection(Direction.BOTTOM); break;
            case BOTTOM: this.setCannonDirection(Direction.LEFT); break;
            case LEFT: this.setCannonDirection(Direction.TOP); break;
        }
    }

    private void rotateCannonCounterclockwise() {
        switch (cannonDirection) {
            case TOP: this.setCannonDirection(Direction.LEFT); break;
            case RIGHT: this.setCannonDirection(Direction.TOP); break;
            case BOTTOM: this.setCannonDirection(Direction.RIGHT); break;
            case LEFT: this.setCannonDirection(Direction.BOTTOM); break;
        }
    }

    /**
     * This method will rotate the direction of the connectors and cannon of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        rotateCannonClockwise();
    }

    /**
     * This method will rotate the direction of the connectors and cannon of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        rotateCannonCounterclockwise();
    }
}