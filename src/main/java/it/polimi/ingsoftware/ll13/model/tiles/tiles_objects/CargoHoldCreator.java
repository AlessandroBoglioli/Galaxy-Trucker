package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The {@code CargoHoldCreator} class represents a "Cargo Hold" tile creator.
 * It is a factory concrete creator that creates {@link CargoHoldTile} objects.
 * This tile requires connector types for all four sides, a compartment type, and a compartment capacity.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class CargoHoldCreator extends TileCreator {

    /**
     * Creates a new {@link CargoHoldTile} object using the provided parameters.
     *
     * @param data JSONObject that contains all the data, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     *               - The compartment type (e.g., SMALL, MEDIUM, LARGE).
     *               - The compartment capacity (number of cargo holders).
     * @return A new {@link CargoHoldTile} object.
     * @throws IllegalArgumentException if the parameters are invalid or missing.
     * @throws JSONException if the read from the JSON is not correct.
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));
            CompartmentType compartmentType = extractAndValidateCompartmentType(data);
            int compartmentCapacity = extractAndValidatePositiveInt(data, "compartment_capacity");

            return new CargoHoldTile(
                    image,
                    connectorTypes.get(0),
                    connectorTypes.get(1),
                    connectorTypes.get(2),
                    connectorTypes.get(3),
                    compartmentType,
                    compartmentCapacity
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }

    }
}