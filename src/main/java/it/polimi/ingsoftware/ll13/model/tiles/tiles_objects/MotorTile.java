package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

/**
 * This class represent a single motor tile.
 */
public class MotorTile extends MotorBaseTile {

    private int thrust;

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
    public MotorTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction motorDirection) throws IllegalArgumentException {
        super(image, topType, rightType, bottomType, leftType, motorDirection);
        setThrust();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the thrust of the motor.
     *
     * @return thrust that the motor provide to the ship
     */
    public int getThrust() {
        return thrust;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    protected void setThrust() {
        if (super.getMotorDirection() == Direction.BOTTOM) {
            thrust = 1;
        } else {
            thrust = 0;
        }
    }

    /**
     * This method will rotate the direction of the connectors and motor of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        setThrust();
    }

    /**
     * This method will rotate the direction of the connectors and motor of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        setThrust();
    }
}
