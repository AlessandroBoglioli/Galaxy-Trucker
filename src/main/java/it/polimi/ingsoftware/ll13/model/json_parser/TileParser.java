package it.polimi.ingsoftware.ll13.model.json_parser;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parser for converting JSON data into Tile objects.
 * This class provides functionality to parse a JSON input stream containing tile data and convert it into a list of corresponding Tile objects using a factory pattern.
 */
public class TileParser {
    private final Map<String, TileCreator> creators;

    /**
     * Constructs a new TileParser and initializes the tile creators.
     * The constructor populates the creators map with all known tile types and their corresponding creator implementations.
     */
    public TileParser() {
        this.creators = new HashMap<>();
        initializeCreators();
    }

    private void initializeCreators() {
        creators.put("BatteryStorage", new BatteryStorageCreator());
        creators.put("Cabin", new CabinCreator());
        creators.put("Cannon", new CannonCreator());
        creators.put("CargoHold", new CargoHoldCreator());
        creators.put("DoubleCannon", new DoubleCannonCreator());
        creators.put("DoubleMotor", new DoubleMotorCreator());
        creators.put("Motor", new MotorCreator());
        creators.put("Shield", new ShieldCreator());
        creators.put("StructuralModule", new StructuralModuleCreator());
        creators.put("VitalSupport", new VitalSupportCreator());
        creators.put("StartingCabin", new StartingCabinCreator());
    }

    /**
     * Parses a JSON input stream and converts it into a list of Tile objects.
     * The method expects the input stream to contain a JSON object with a "tiles" array, where each element represents a tile with a type and associated data.
     *
     * @param inputStream The input stream containing the JSON data to parse
     * @return A list of Tile objects created from the JSON data
     * @throws IllegalArgumentException If an unknown tile type is encountered in the JSON data
     * @throws JSONException If there is an error parsing the JSON data
     */
    public List<Tile> parseTiles(InputStream inputStream) throws IllegalArgumentException, JSONException {
        List<Tile> tiles = new ArrayList<>();

        JSONTokener token = new JSONTokener(inputStream);
        JSONObject root = new JSONObject(token);
        JSONArray tilesArray = root.getJSONArray("tiles");

        for (int i = 0; i < tilesArray.length(); i++) {
            JSONObject tileObj = tilesArray.getJSONObject(i);
            String type = tileObj.getString("type");
            JSONObject data = tileObj.getJSONObject("data");

            TileCreator creator = creators.get(type);
            if (creator == null) {
                throw new IllegalArgumentException("Unknown card type: " + type);
            }
            tiles.add(creator.createTile(data));
        }

        return tiles;
    }
}
