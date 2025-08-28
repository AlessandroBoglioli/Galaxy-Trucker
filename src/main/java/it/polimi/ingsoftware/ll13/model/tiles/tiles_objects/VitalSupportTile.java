package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;

/**
 * This class represent the vital support modules that will be included in the ship.
 */
public class VitalSupportTile extends Tile {

    VitalSupportColor vitalSupportColor;

    /**
     * The builder generates the array for representing the connectors of the tile.
     *
     * @param image         The image of the string.
     * @param topType       Indicates the type of the connector positioned on the top side of the card.
     * @param rightType     Indicates the type of the connector positioned on the right side of the card.
     * @param bottomType    Indicates the type of the connector positioned on the bottom side of the card.
     * @param leftType      Indicates the type of the connector positioned on the left side of the card.
     */
    public VitalSupportTile(String image, ConnectorType topType, ConnectorType rightType, ConnectorType bottomType, ConnectorType leftType, VitalSupportColor vitalSupportColor) {
        super(image, topType, rightType, bottomType, leftType);
        this.vitalSupportColor = vitalSupportColor;
    }

    /**
     * Retrieves the vital support tile color.
     *
     * @return vitalSupportColor of the vital support.
     */
    public VitalSupportColor getVitalSupportColor() {
        return vitalSupportColor;
    }

    /**
     * Set the value of the vitalSupportColor.
     *
     * @param vitalSupportColor The vital support color that need to be set.
     */
    public void setVitalSupportColor(VitalSupportColor vitalSupportColor) {
        this.vitalSupportColor = vitalSupportColor;
    }
}
