package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.extractAndValidateConnector;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.validateDataObject;

/**
 * The {@code DoubleCannonCreator} class represents a "Double Cannon" tile creator.
 * It is a factory concrete creator that creates {@link DoubleCannonTile} objects.
 * This tile requires connector types for all four sides and a direction for the cannon.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class DoubleCannonCreator extends TileCreator {

    /**
     * Creates a new {@link DoubleCannonTile} object using the provided parameters.
     *
     * @param data JSONObject that contains data, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     *               - The direction where the cannon is pointing.
     * @return A new {@link DoubleCannonTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws org.json.JSONException if the read from the JSON is incorrect.
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));

            return new DoubleCannonTile(
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