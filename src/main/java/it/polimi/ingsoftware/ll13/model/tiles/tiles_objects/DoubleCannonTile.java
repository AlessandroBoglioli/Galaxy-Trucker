package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

/**
 * This class represents a double cannon tile.
 */
public class DoubleCannonTile extends CannonBaseTile {

    private float firePower;

    /**
     * Constructor for DoubleCannonTile.
     *
     * @param image             The image of the tile.
     * @param topType           Indicates the type of the connector positioned on the top side of the card.
     * @param rightType         Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType        Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType          Indicates the type of the connector positioned on the left side of the card.
     * @param cannonDirection   Indicates where the cannon is pointing.
     */
    public DoubleCannonTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction cannonDirection) {
        super(image, topType, rightType, bottomType, leftType, cannonDirection);
        setFirePower();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the fire power of the cannon tile.
     *
     * @return the fire power of the tile.
     */
    public float getFirePower() {
        return firePower;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setter methods ~~~~~~~~~~~~~~~~~~~~~~~~
    protected void setFirePower() {
        this.firePower = calculateFirePower();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other methods ~~~~~~~~~~~~~~~~~~~~~~~~
    protected float calculateFirePower() {
        if (cannonDirection == Direction.TOP) {
            return 2F;
        }
        return 1F;
    }

    /**
     * This method will rotate the direction of the connectors and double cannon of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        setFirePower();
    }

    /**
     * This method will rotate the direction of the connectors and double cannon of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        setFirePower();
    }
}