package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

/**
 * The class will represent the central tile in each ship.
 * It will have a different color for each player
 */
public class StartingCabinTile extends CabinTile {

    private final PlayerColors color;

    /**
     * This create the base cabin of the ship.
     *
     * @param image     The image of the tile.
     * @param color     Indicates the color cabin.
     */
    public StartingCabinTile(String image, PlayerColors color){
        super(image, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
        this.color = color;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~ Getters ~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Retrieves the color of the starting cabin.
     *
     * @return The color of the starting cabin.
     */
    public PlayerColors getColor() {
        return color ;
    }
}
