package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * The {@code VitalSupportCreator} class represents a "Vital Support" tile creator.
 * It is a factory concrete creator that creates {@link VitalSupportTile} objects.
 * This tile represents a vital support module of the ship and requires connector types for all four sides.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class VitalSupportCreator extends TileCreator {

    /**
     * Creates a new {@link VitalSupportTile} object using the provided parameters.
     *
     * @param data  JSONObject that contains all the data, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     * @return A new {@link VitalSupportTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws JSONException if the elements read from the JSON are incorrect
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));
            VitalSupportColor vitalSupportColor = extractAndValidateVitalSupportColor(data);

            return new VitalSupportTile(
                    image,
                    connectorTypes.get(0),
                    connectorTypes.get(1),
                    connectorTypes.get(2),
                    connectorTypes.get(3),
                    vitalSupportColor
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }
    }
}