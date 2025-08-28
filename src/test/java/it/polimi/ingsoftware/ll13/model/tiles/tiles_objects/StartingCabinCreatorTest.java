package it.polimi.ingsoftware.ll13.model.tiles.tiles_objects;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StartingCabinCreatorTest {

    private final StartingCabinCreator creator = new StartingCabinCreator();

    @ParameterizedTest
    @EnumSource(PlayerColors.class)
    void createTile_shouldReturnStartingCabinTileWithValidColor(PlayerColors color) throws Exception {
        JSONObject data = new JSONObject();
        data.put("image", "");
        data.put("color", color.name());

        Tile tile = creator.createTile(data);
        assertInstanceOf(StartingCabinTile.class, tile);
        StartingCabinTile cabinTile = (StartingCabinTile) tile;
        assertEquals(color, cabinTile.getColor());
    }

    @Test
    void createTile_shouldThrowForMissingColorField() {
        JSONObject data = new JSONObject(); // empty object
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALID", "123"})
    void createTile_shouldThrowForInvalidColor(String invalidColor) {
        JSONObject data = new JSONObject();
        data.put("color", invalidColor);
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }

    @Test
    void createTile_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> creator.createTile(null));
    }

    @Test
    void createTile_shouldThrowForNonStringColorValue() {
        JSONObject data = new JSONObject();
        data.put("color", 123); // numeric value instead of string
        assertThrows(JSONException.class, () -> creator.createTile(data));
    }
}