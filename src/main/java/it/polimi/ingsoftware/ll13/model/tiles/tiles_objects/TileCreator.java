package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code TileCreator} is an abstract class representing a card creator.
 * This class defines a factory pattern interface for creating different types of {@link Tile} objects.
 * Subclasses must implement the {@link #createTile(JSONObject)} method to provide specific card creation logic.
 */
public abstract class TileCreator {
    /**
     * Creates a {@link Tile} object based on the provided {@link JSONObject} data.
     * Subclasses must implement this method to define the specific logic for
     * parsing the data and constructing a tile.
     *
     * @param data the JSON object containing the tile data. This must include all
     *             necessary information required to construct the tile.
     * @return a new instance of {@link Tile} constructed from the provided data.
     * @throws IllegalArgumentException if the provided data is invalid or incomplete.
     * @throws JSONException if there is an error parsing the JSON data.
     */
    public abstract Tile createTile(JSONObject data) throws IllegalArgumentException, JSONException;
}
