package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;


import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class represent a generic tile that will be used for the building of the ship.
 */
public class Tile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Connector[] connectors;
    private String image;
    private int rotation;


    /**
     * Builder method of the tile
     *
     * @param topType       Indicates the type of the connector positioned on the top side of the card.
     * @param rightType     Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType    Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType      Indicates the type of the connector positioned on the left side of the card.
     */
    public Tile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType) throws IllegalArgumentException {
        if (topType == null || rightType == null || bottomType == null || leftType == null) {
            throw new IllegalArgumentException("Connector types cannot be null.");
        }

        this.image = image;
        this.connectors = new Connector[4];
        connectors[0] = new Connector(Direction.TOP, topType);
        connectors[1] = new Connector(Direction.RIGHT, rightType);
        connectors[2] = new Connector(Direction.BOTTOM, bottomType);
        connectors[3] = new Connector(Direction.LEFT, leftType);
        this.rotation=0;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    public String getImage() {
        return image;
    }
    public int getRotation(){return rotation;}

    /**
     * Retrieves the array of connectors.
     *
     * @return the array of connectors.
     */
    public Connector[] getConnectors() {
        return connectors;
    }

    public Connector getTopConnector() {
        return connectors[0];
    }

    public Connector getRightConnector() {
        return connectors[1];
    }

    public Connector getBottomConnector() {
        return connectors[2];
    }

    public Connector getLeftConnector() {
        return connectors[3];
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Setters ~~~~~~~~~~~~~~~~~~~~~~~~
    public void setImage(String image) {
        this.image = image;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Other methods ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * This method will rotate the tile clockwise.
     */
    public void rotateClockwise() throws IllegalStateException {
        if (connectors == null || connectors.length != 4) {
            throw new IllegalStateException("Connectors array is not properly initialized.");
        }
        Connector[] temp = new Connector[4];
        temp[0] = new Connector(Direction.TOP, connectors[3].getType());
        temp[1] = new Connector(Direction.RIGHT, connectors[0].getType());
        temp[2] = new Connector(Direction.BOTTOM, connectors[1].getType());
        temp[3] = new Connector(Direction.LEFT, connectors[2].getType());
        connectors = temp;
        rotation = (rotation +1) % 4;

    }

    /**
     * This method will rotate the tile counter clockwise.
     */
    public void rotateCounterClockwise() throws IllegalStateException {
        if (connectors == null || connectors.length != 4) {
            throw new IllegalStateException("Connectors array is not properly initialized.");
        }
        Connector[] temp = new Connector[4];
        temp[0] = new Connector(Direction.TOP, connectors[1].getType());
        temp[1] = new Connector(Direction.RIGHT, connectors[2].getType());
        temp[2] = new Connector(Direction.BOTTOM, connectors[3].getType());
        temp[3] = new Connector(Direction.LEFT, connectors[0].getType());
        connectors = temp;
    }

    /**
     * Rotates the tile by a given number of 90° clockwise rotations.
     * For example, rotateMultiple(3) rotates the tile 270° clockwise.
     *
     * @param times the number of 90° clockwise rotations.
     */
    public void rotateMultiple(int times){
        int normalized = ((times % 4) + 4) % 4;
        for (int i = 0; i < normalized; i++) {
            rotateClockwise();
        }
    }

}
