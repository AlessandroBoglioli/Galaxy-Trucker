package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * This class represents a single cannon tile.
 */
public class CannonTile extends CannonBaseTile {

    private float firePower;

    /**
     * Constructor for CannonTile.
     *
     * @param image             The image of the tile.
     * @param topType           Indicates the type of the connector positioned on the top side of the card.
     * @param rightType         Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType        Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType          Indicates the type of the connector positioned on the left side of the card.
     * @param cannonDirection   Indicates where the cannon is pointing.
     */
    public CannonTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction cannonDirection) throws IllegalArgumentException {
        super(image, topType, rightType, bottomType, leftType, cannonDirection);
        if(topType == null || rightType == null || bottomType == null || leftType == null) {
            throw new IllegalArgumentException("Connector types cannot be null.");
        }

        if (cannonDirection == null) {
            throw new IllegalArgumentException("cannonDirection cannot be null");
        }
        setFirePower();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the direction where the fire power of the cannon.
     *
     * @return fire power of the cannon
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
            return 1.0F;
        }
        return 0.5F;
    }


    /**
     * This method will rotate the direction of the connectors and cannon of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        setFirePower();
    }

    /**
     * This method will rotate the direction of the connectors and cannon of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        setFirePower();
    }
}