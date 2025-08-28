package it.polimi.ingsoftware.ll13.model.network.response;

import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.decks.TileDeck;
import it.polimi.ingsoftware.ll13.model.tiles.decks.TileDecksManager;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Connector;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.DrawFromTempResponse;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.DrawTileResponse;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.RotatedTileResponse;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TileResponseTest {

    @Test
    public void testDrawTileResponseWithJsonLoadedTile() {
        TileDecksManager manager = new TileDecksManager();
        TileDeck tileDeck = manager.getTileDeck();
        Tile drawnTile = tileDeck.drawTile();
        assertNotNull(drawnTile, "No tile was drawn - deck may be empty or JSON failed to load");
        DrawTileResponse response = new DrawTileResponse(drawnTile, true);
        Tile tileFromResponse = response.getDrawnTile();
        assertNotNull(tileFromResponse, "Response returned a null tile");
        assertEquals(drawnTile, tileFromResponse, "Drawn tile and response tile should be the same");

        String image = tileFromResponse.getImage();
        assertNotNull(image, "Image string should not be null");
        assertFalse(image.isBlank(), "Image string should not be blank");
        System.out.println("✅ Image loaded: " + image);
    }
    @Test
    public void testDrawTileResponseSerialization() throws IOException, ClassNotFoundException {
        TileDecksManager manager = new TileDecksManager();
        TileDeck deck = manager.getTileDeck();
        Tile drawnTile = deck.drawTile();
        assertNotNull(drawnTile, "Drawn tile should not be null");

        DrawTileResponse original = new DrawTileResponse(drawnTile, true);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(original);
        out.close();

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        DrawTileResponse deserialized = (DrawTileResponse) in.readObject();

        assertNotNull(deserialized);
        assertTrue(deserialized.isSuccess(), "Success flag should be true after deserialization");
        assertNotNull(deserialized.getDrawnTile(), "Deserialized tile should not be null");

        Tile deserializedTile = deserialized.getDrawnTile();
        assertEquals(drawnTile.getImage(), deserializedTile.getImage(), "Image should match after deserialization");
        Connector[] originalConnectors = drawnTile.getConnectors();
        Connector[] deserializedConnectors = deserializedTile.getConnectors();
        assertEquals(originalConnectors.length, deserializedConnectors.length);
        for (int i = 0; i < originalConnectors.length; i++) {
            assertEquals(originalConnectors[i].getType(), deserializedConnectors[i].getType());
            assertEquals(originalConnectors[i].getDirection(), deserializedConnectors[i].getDirection());
        }

    }
    @Test
    public void testDrawFromTempResponse() throws IOException, ClassNotFoundException {
        TileDecksManager manager = new TileDecksManager();
        TileDeck deck = manager.getTileDeck();
        Tile tile = deck.drawTile();
        assertNotNull(tile, "Tile must not be null");

        TileCoordinates c1 = new TileCoordinates(1, 2, tile);
        TileCoordinates c2 = new TileCoordinates(3, 4, tile);
        List<TileCoordinates> updatedTiles = List.of(c1, c2);

        DrawFromTempResponse original = new DrawFromTempResponse(tile, updatedTiles, true);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(original);
        out.close();

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        DrawFromTempResponse deserialized = (DrawFromTempResponse) in.readObject();

        assertNotNull(deserialized);
        assertTrue(deserialized.isSuccess(), "Success should be true");
        assertNotNull(deserialized.getTile(), "Tile should not be null after deserialization");
        assertEquals(tile.getImage(), deserialized.getTile().getImage(), "Tile image should match");

        List<TileCoordinates> deserializedCoords = deserialized.getUpdatedTiles();
        assertNotNull(deserializedCoords);
        assertEquals(2, deserializedCoords.size(), "Should have 2 TileCoordinates");

        TileCoordinates desCoord1 = deserializedCoords.get(0);
        assertEquals(c1.getRow(), desCoord1.getRow());
        assertEquals(c1.getCol(), desCoord1.getCol());
        assertEquals(c1.getTile().getImage(), desCoord1.getTile().getImage());
    }

    @Test
    public void testRotateTileResponse() throws IOException, ClassNotFoundException {
        TileDecksManager manager = new TileDecksManager();
        TileDeck deck = manager.getTileDeck();
        Tile originalTile = deck.drawTile();
        assertNotNull(originalTile, "Tile must not be null");
        
        RotatedTileResponse originalResponse = new RotatedTileResponse(originalTile,true);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(originalResponse);
        out.close();

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        RotatedTileResponse deserializedResponse = (RotatedTileResponse) in.readObject();

        assertNotNull(deserializedResponse);
        assertNotNull(deserializedResponse.getTile(), "Tile should not be null after deserialization");

        Tile deserializedTile = deserializedResponse.getTile();
        assertEquals(originalTile.getImage(), deserializedTile.getImage(), "Tile image should match after deserialization");

        Connector[] originalConnectors = originalTile.getConnectors();
        Connector[] deserializedConnectors = deserializedTile.getConnectors();
        assertEquals(originalConnectors.length, deserializedConnectors.length, "Connectors length should match");

        for (int i = 0; i < originalConnectors.length; i++) {
            assertEquals(originalConnectors[i].getType(), deserializedConnectors[i].getType(), "Connector type should match");
            assertEquals(originalConnectors[i].getDirection(), deserializedConnectors[i].getDirection(), "Connector direction should match");
        }
    }


}

