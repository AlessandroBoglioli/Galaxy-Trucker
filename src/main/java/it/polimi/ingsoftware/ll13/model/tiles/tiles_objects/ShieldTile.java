package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;


import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

/**
 * This class represent the shield generator tile that will be included in the ship
 */
public class ShieldTile extends Tile {

    private Direction direction1;
    private Direction direction2;

    /**
     * The builder generates the array for representing the connectors of the tile and indicates where the shield is pointing.
     *
     * @param image         The image of the tile.
     * @param topType       Indicates the type of the connector positioned on the top side of the card.
     * @param rightType     Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType    Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType      Indicates the type of the connector positioned on the left side of the card.
     * @param direction1    Indicates the first direction where the shield is pointing.
     * @param direction2    Indicates the second direction where the shield is pointing.
     */
    public ShieldTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, Direction direction1, Direction direction2) throws IllegalArgumentException {
        super(image, topType, rightType, bottomType, leftType);

        if(direction1 == null || direction2 == null) {
            throw new IllegalArgumentException("Directions cannot be null");
        }
        this.direction1 = direction1;
        this.direction2 = direction2;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the first direction of the shield.
     *
     * @return first direction of the shield.
     */
    public Direction getDirection1() {
        return direction1;
    }

    /**
     * Retrieves the second direction of the shield.
     *
     * @return second direction of the shield.
     */
    public Direction getDirection2() {
        return direction2;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Modify the first direction of the shield.
     *
     * @param direction1    The first direction of the shield.
     */
    protected void setDirection1(Direction direction1) throws IllegalArgumentException {
        if(direction1 == null) {
            throw new IllegalArgumentException("direction1 cannot be null");
        }
        this.direction1 = direction1;
    }

    /**
     * Modify the second direction of the shield.
     *
     * @param direction2    The sectond direction of the shield.
     */
    protected void setDirection2(Direction direction2) throws IllegalArgumentException {
        if(direction2 == null) {
            throw new IllegalArgumentException("direction2 cannot be null");
        }
        this.direction2 = direction2;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other methods ~~~~~~~~~~~~~~~~~~~~~~~~
    // This method rotate the shields in a clockwise way.
    protected void rotateShieldsClockwise() throws IllegalStateException {
        switch(direction1) {
            case TOP : this.setDirection1(Direction.RIGHT); break;
            case RIGHT : this.setDirection1(Direction.BOTTOM); break;
            case BOTTOM : this.setDirection1(Direction.LEFT); break;
            case LEFT : this.setDirection1(Direction.TOP); break;
            default: throw new IllegalStateException("Invalid direction");
        }
        switch(direction2) {
            case TOP : this.setDirection2(Direction.RIGHT); break;
            case RIGHT : this.setDirection2(Direction.BOTTOM); break;
            case BOTTOM : this.setDirection2(Direction.LEFT); break;
            case LEFT : this.setDirection2(Direction.TOP); break;
            default: throw new IllegalStateException("Invalid direction");
        }
    }

    // This method rotate the shields in a counter clockwise way.
    protected void rotateShieldsCounterClockwise() throws IllegalStateException {
        switch(direction1) {
            case TOP : this.setDirection1(Direction.LEFT); break;
            case RIGHT : this.setDirection1(Direction.TOP); break;
            case BOTTOM : this.setDirection1(Direction.RIGHT); break;
            case LEFT : this.setDirection1(Direction.BOTTOM); break;
            default: throw new IllegalStateException("Invalid direction");
        }
        switch(direction2) {
            case TOP : this.setDirection2(Direction.LEFT); break;
            case RIGHT : this.setDirection2(Direction.TOP); break;
            case BOTTOM : this.setDirection2(Direction.RIGHT); break;
            case LEFT : this.setDirection2(Direction.BOTTOM); break;
            default: throw new IllegalStateException("Invalid direction");
        }
    }

    /**
     * This method will rotate the direction of the connectors and shields of the tiles clockwise.
     */
    @Override
    public void rotateClockwise() {
        super.rotateClockwise();
        rotateShieldsClockwise();
    }

    /**
     * This method will rotate the direction of the connectors and shields of the tiles counter clockwise.
     */
    @Override
    public void rotateCounterClockwise() {
        super.rotateCounterClockwise();
        rotateShieldsCounterClockwise();
    }

}
