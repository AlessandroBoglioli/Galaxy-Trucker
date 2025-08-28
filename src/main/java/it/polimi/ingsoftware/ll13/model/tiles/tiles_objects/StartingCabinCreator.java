package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code StartingCabinCreator} class represents a "Starting Cabin" tile creator.
 * It is a factory concrete creator that creates {@link StartingCabinTile} objects.
 * This tile represents the central tile of each ship and has a unique color for each player.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class StartingCabinCreator extends TileCreator {

    /**
     * Creates a new {@link StartingCabinTile} object using the provided parameters.
     *
     * @param data JSONObject that contains elements, which in this case is:
     *               - The image of the tile.
     *               - The color of the starting cabin.
     * @return A new {@link StartingCabinTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws org.json.JSONException if the reading from the JSON creates errors.
     */
    public Tile createTile(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            PlayerColors playerColors = extractAndValidatePlayerColors(data);

            return new StartingCabinTile(
                    image,
                    playerColors
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }
    }
}