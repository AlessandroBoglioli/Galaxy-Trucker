package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * This class represent the double motor tiles that will be included in the ship
 */
public abstract class MotorBaseTile extends Tile {

    private Direction motorDirection;

    /**
     * The builder generates the array for representing the connectors of the tile and the direction where the motor is pointing.
     *
     * @param image             The image of the tile.
     * @param topType           Indicates the type of the connector positioned on the top side of the card.
     * @param rightType         Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType        Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType          Indicates the type of the connector positioned on the left side of the card.
     * @param motorDirection    Indicates where the motor is pointing.
     */
    public MotorBaseTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction motorDirection) throws IllegalArgumentException {
        super(image, topType, rightType, bottomType, leftType);
        if (motorDirection == null) {
            throw new IllegalArgumentException("motorDirection cannot be null");
        }
        this.motorDirection = motorDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the motor current facing direction.
     *
     * @return motor direction.
     */
    public Direction getMotorDirection() {
        return motorDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Sets the direction of the motor.
     *
     * @param motorDirection       The new direction of the motor. Motor be null.
     * @throws IllegalArgumentException if motorDirection is null.
     */
    public void setMotorDirection(Direction motorDirection) throws IllegalStateException {
        if (motorDirection == null) {
            throw new IllegalArgumentException("motorDirection cannot be null");
        }
        this.motorDirection = motorDirection;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other methods ~~~~~~~~~~~~~~~~~~~~~~~~
    private void rotateMotorClockwise() {
        switch (motorDirection) {
            case TOP: this.setMotorDirection(Direction.RIGHT); break;
            case RIGHT: this.setMotorDirection(Direction.BOTTOM); break;
            case BOTTOM: this.setMotorDirection(Direction.LEFT); break;
            case LEFT: this.setMotorDirection(Direction.TOP); break;
        }
    }

    private void rotateMotorCounterclockwise() {
        switch (motorDirection) {
            case TOP: this.setMotorDirection(Direction.LEFT); break;
            case RIGHT: this.setMotorDirection(Direction.TOP); break;
            case BOTTOM: this.setMotorDirection(Direction.RIGHT); break;
            case LEFT: this.setMotorDirection(Direction.BOTTOM); break;
        }
    }

    /**
     * This method will rotate the direction of the connectors and motor of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        rotateMotorClockwise();
    }

    /**
     * This method will rotate the direction of the connectors and motor of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        rotateMotorCounterclockwise();
    }

}
