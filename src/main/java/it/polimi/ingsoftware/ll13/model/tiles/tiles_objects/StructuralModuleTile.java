package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * This class represent the structural module that will be included in the ship
 */
public class StructuralModuleTile extends Tile {

    /**
     * The builder generates the array for representing the connectors of the tile.
     *
     * @param image         The image of the tile.
     * @param topType       Indicates the type of the connector positioned on the top side of the card.
     * @param rightType     Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType    Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType      Indicates the type of the connector positioned on the left side of the card.
     */
    public StructuralModuleTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType) {
        super(image, topType, rightType, bottomType, leftType);
    }

}
