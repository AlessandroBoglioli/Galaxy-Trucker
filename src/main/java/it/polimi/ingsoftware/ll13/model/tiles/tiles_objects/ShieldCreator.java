package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The {@code ShieldCreator} class represents a "Shield Generator" tile creator.
 * It is a factory concrete creator that creates {@link ShieldTile} objects.
 * This tile requires connector types for all four sides and two directions for the shields.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class ShieldCreator extends TileCreator {

    /**
     * Creates a new {@link ShieldTile} object using the provided parameters.
     *
     * @param data JSONObject that contains the data, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     *               - The first direction where the shield is pointing.
     *               - The second direction where the shield is pointing.
     * @return A new {@link ShieldTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws org.json.JSONException if the JSON reading is incorrect.
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));

            return new ShieldTile(
                    image,
                    connectorTypes.get(0),
                    connectorTypes.get(1),
                    connectorTypes.get(2),
                    connectorTypes.get(3),
                    Direction.TOP,
                    Direction.RIGHT
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }
    }
}