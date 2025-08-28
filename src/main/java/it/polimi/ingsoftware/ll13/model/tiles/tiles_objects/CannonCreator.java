package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The {@code CannonCreator} class represents a "Cannon" tile creator.
 * It is a factory concrete creator that creates {@link CannonTile} objects.
 * This tile requires connector types for all four sides and a direction for the cannon.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class CannonCreator extends TileCreator {

    /**
     * Creates a new {@link CannonTile} object using the provided parameters.
     *
     * @param data JSONObject that contains the elements, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     *               - The direction where the cannon is pointing.
     * @return A new {@link CannonTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws JSONException if the read from the JSON creates errors.
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));

            return new CannonTile(
                    image,
                    connectorTypes.get(0),
                    connectorTypes.get(1),
                    connectorTypes.get(2),
                    connectorTypes.get(3),
                    Direction.TOP
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }
    }
}