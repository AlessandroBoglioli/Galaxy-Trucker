package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationUtils.*;
import static it.polimi.ingsoftware.ll13.utils.json.JsonValidationTileUtils.*;

import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * The {@code BatteryStorageCreator} class represents a "Battery Storage" tile creator.
 * It is a factory concrete creator that creates {@link BatteryStorageTile} objects.
 * This tile requires connector types for all four sides and a battery capacity.
 * It extends the abstract class {@code TileCreator} and implements the specific properties of this tile type.
 *
 * @see TileCreator
 */
public class BatteryStorageCreator extends TileCreator {
    /**
     * Creates a new {@link BatteryStorageTile} object using the provided parameters.
     *
     * @param data JSONObject where the values are taken, which in this case are:
     *               - The image of the tile.
     *               - The connector types for the top, right, bottom, and left sides (in that order).
     *               - The battery capacity of the storage.
     * @return A new {@link BatteryStorageTile} object.
     * @throws IllegalArgumentException if the parameters are invalid.
     * @throws JSONException if the capture from the JSON file is wrong.
     */
    @Override
    public Tile createTile(JSONObject data) throws IllegalArgumentException, JSONException {
        validateDataObject(data);

        try {
            String image = data.getString("image");
            List<ConnectorType> connectorTypes = extractAndValidateConnector(data.getJSONArray("connectors"));
            int batteryCapacity = data.getInt("battery_capacity");

            return new BatteryStorageTile(
                    image,
                    connectorTypes.get(0),
                    connectorTypes.get(1),
                    connectorTypes.get(2),
                    connectorTypes.get(3),
                    batteryCapacity
            );
        } catch (IndexOutOfBoundsException e) {
            throw new JSONException("Invalid number of connectors, expected 4", e);
        }
    }
}