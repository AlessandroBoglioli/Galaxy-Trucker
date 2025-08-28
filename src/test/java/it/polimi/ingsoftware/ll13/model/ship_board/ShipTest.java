package it.polimi.ingsoftware.ll13.model.ship_board;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.player.Player;
import it.polimi.ingsoftware.ll13.model.ship_board.exceptions.InvalidCellException;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_levels.LevelTwoShip;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import javafx.fxml.FXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors.RED;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {
    public  LevelTwoShip ship;
    private ShipCell[][] matrix;
    private PlayerColors color;
    private void setup(){
        PlayerColors color = RED;
        ship = new LevelTwoShip(color);
        this.matrix = ship.getShipLayout().getMotherBoard();
        this.color = color;
    }
    @Test
    public void testShipInitialization(){
        setup();
        assertEquals(ShipMatrixConfig.ROW,matrix.length,"Row count should match config");
        assertEquals(ShipMatrixConfig.COL,matrix[0].length,"Col count should match config");
        int centerRow = ShipMatrixConfig.CENTRAL_ROW;
        int centerCOl = ShipMatrixConfig.CENTRAL_COL;
        ShipCell centralCell = matrix[centerRow][centerCOl];
        assertTrue(centralCell.isValid(),"Central cell should be valid");
        assertNotNull(centralCell.getTile(),"Central cell should contain a tile");
        assertInstanceOf(StartingCabinTile.class, centralCell.getTile(), "Central tile must be a StartingCabinTile");
        StartingCabinTile startingCabinTile =(StartingCabinTile) centralCell.getTile();
        assertEquals(color, startingCabinTile.getColor(), "Starting cabin tile should have the player's chosen color");
    }
    @Test
    void testConnectCells(){
        setup();
        for(int row = 0; row<ShipMatrixConfig.ROW; row++){
            for(int col = 0; col<ShipMatrixConfig.COL; col++){
                ShipCell cell = matrix[row][col];
                if(!cell.isValid()){
                    assertNull(cell.getTop(), "Invalid cell (" + row + "," + col + ") top should be null");
                    assertNull(cell.getBottom(), "Invalid cell (" + row + "," + col + ") bottom should be null");
                    assertNull(cell.getLeft(), "Invalid cell (" + row + "," + col + ") left should be null");
                    assertNull(cell.getRight(), "Invalid cell (" + row + "," + col + ") right should be null");
                    continue;
                }
                if (row > 0 && matrix[row - 1][col].isValid()) {
                    assertNotNull(cell.getTop(), "Cell (" + row + "," + col + ") should have a top neighbor");
                } else {
                    assertNull(cell.getTop(), "Cell (" + row + "," + col + ") should not have a top neighbor");
                }
                if (row < ShipMatrixConfig.ROW - 1 && matrix[row + 1][col].isValid()) {
                    assertNotNull(cell.getBottom(), "Cell (" + row + "," + col + ") should have a bottom neighbor");
                } else {
                    assertNull(cell.getBottom(), "Cell (" + row + "," + col + ") should not have a bottom neighbor");
                }
                if (col > 0 && matrix[row][col - 1].isValid()) {
                    assertNotNull(cell.getLeft(), "Cell (" + row + "," + col + ") should have a left neighbor");
                } else {
                    assertNull(cell.getLeft(), "Cell (" + row + "," + col + ") should not have a left neighbor");
                }
                if (col < ShipMatrixConfig.COL - 1 && matrix[row][col + 1].isValid()) {
                    assertNotNull(cell.getRight(), "Cell (" + row + "," + col + ") should have a right neighbor");
                } else {
                    assertNull(cell.getRight(), "Cell (" + row + "," + col + ") should not have a right neighbor");
                }
            }
        }

    }
    @Test
    void testAddTileValidCell(){
        setup();
        Tile tile = new Tile("", ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SMOOTH);
        boolean result = ship.getShipLayout().addTile(tile,2,2);
        assertTrue(result,"Tile should be placed in (2,2) coordinates ");
        assertNotNull(ship.getShipLayout().getMotherBoard()[2][2].getTile(), "Tile should be placed in (2,2)");

    }
    @Test
    void testAddTileInvalidCell(){
        setup();
        Tile tile = new Tile("", ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SMOOTH);
        boolean result = ship.getShipLayout().addTile(tile,0,0); //invalid
        assertFalse(result,"Tile should not be placed in the given coordinates ");
        boolean result2 = ship.getShipLayout().addTile(tile,3,0); //isolated
        assertFalse(result,"Tile should not be placed in the given coordinates");
        assertNull(ship.getShipLayout().getMotherBoard()[0][0].getTile());
        assertNull(ship.getShipLayout().getMotherBoard()[3][0].getTile());
    }
    @Test
    void testAddMultipleTiles() {
        setup();
        Tile tile1 = new Tile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        boolean result1 = ship.getShipLayout().addTile(tile1, 2, 2);
        assertTrue(result1, "Tile should be successfully placed in (2,2).");
        Tile tile2 = new Tile("", ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH);
        boolean result2 = ship.getShipLayout().addTile(tile2, 3, 2);
        assertTrue(result2, "Tile should be successfully placed in (3,2).");
        Tile tile3 = new Tile("", ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH);
        boolean result3 = ship.getShipLayout().addTile(tile3, 2, 4);
        boolean result4 = ship.getShipLayout().addTile(tile3, 3, 1);
        assertTrue(result3, "Tile should be successfully placed in (2,4).");
        assertTrue(result4,"Tile should be succesfully placed in (3,1)");
    }
    @Test
    void testAddingToTempCell(){
        setup();
        Tile tile= new Tile("", ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        boolean result = ship.getShipLayout().addTile(tile,0,5);
        assertTrue(result,"Tile should be placed in the temp zone even");
        Tile tile1 = new Tile("", ConnectorType.SMOOTH, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        boolean result1 = ship.getShipLayout().addTile(tile1,0,6);
        assertTrue(result1,"Tile should be placed in the temp zone even");
        boolean result2 = ship.getShipLayout().addTile(tile,0,5);
        assertFalse(result2,"Tile should not be placed, cell already occupied");


    }
    @Test
    void testDrawTileFromTempZone() {
        setup();
        Tile tileToAdd = new Tile("", ConnectorType.UNIVERSAL, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tileToAdd, 0, 5);
        Tile drawnTile = ship.getShipLayout().drawTileFromTempZone(0, 5);
        assertNotNull(drawnTile, "Tile should be drawn from the temp zone.");
        assertEquals(tileToAdd, drawnTile, "The drawn tile should match the one placed in the temp zone.");
        ShipCell tempZoneCell = ship.getShipLayout().getMotherBoard()[0][5];
        assertNull(tempZoneCell.getTile(), "The temp zone cell should be empty after the tile is drawn.");
    }
    @Test
    void testDrawTileFromEmptyTempZone() {
        setup();
        Tile drawnTile = ship.getShipLayout().drawTileFromTempZone(0, 5);
        assertNull(drawnTile, "Drawing from an empty temp zone should return null.");
    }

    @Test    //returning an exception makes it crash
    void testDrawTileFromInvalidTempZone() {
        setup();
        Tile drawnTile = ship.getShipLayout().drawTileFromTempZone(1, 1);
        assertNull(drawnTile, "Drawing from an invalid temp zone should return null.");
    }
    @Test
    void testAddWhereAlreadyPresent(){
        setup();
        Ship ship = new LevelTwoShip(RED);
        Tile tile = new Tile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile,2,2);
        Tile tile2 = new MotorTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SINGLE, Direction.BOTTOM);
        boolean added = ship.getShipLayout().addTile(tile2,2,2);
        assertFalse(added, "cant insert on already occupied cell");
    }
    @Test
    void testBatteryCount(){
        setup();
        Ship ship = new LevelTwoShip(RED);
        Tile tile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL, CompartmentType.BLUE,2);
        Tile tile2 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SINGLE,3);
        Tile tile3 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.SINGLE,Direction.BOTTOM);
        Tile tile4 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE,3);
        Tile tile5 = new BatteryStorageTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,2);
        ship.getShipLayout().addTile(tile1,2,4);
        ship.getShipLayout().addTile(tile2,1,4);
        ship.getShipLayout().addTile(tile3,2,2);
        ship.getShipLayout().addTile(tile4,2,1);
        ship.getShipLayout().addTile(tile5,2,0);
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        assertEquals(8,ship.getBatteryManager().getBatteries());
        boolean used = ship.getBatteryManager().useBattery(ship.getShipLayout(),2,2);
        assertFalse(used,"Should not have used battery since not a battery tile");
        boolean used2 = ship.getBatteryManager().useBattery(ship.getShipLayout(),1,4);
        assertTrue(used2);
        assertEquals(2,((BatteryStorageTile) tile2).getBatteryNumber());
        ship.getBatteryManager().useBattery(ship.getShipLayout(),1,4);
        ship.getBatteryManager().useBattery(ship.getShipLayout(),1,4);
        boolean used3 = ship.getBatteryManager().useBattery(ship.getShipLayout(),1,4);
        assertFalse(used3);
        assertEquals(0,((BatteryStorageTile) tile2).getBatteryNumber());
    }
    @Test
    void testInvalidTiles(){
        Player player1 = new Player(1,"",RED);
        Ship ship = new LevelTwoShip(RED);
        player1.setShip(ship);
        Tile tile1 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile1,2,4);
        Tile tile2 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SINGLE,1);
        ship.getShipLayout().addTile(tile2,2,5);
        Tile tile3 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile3,2,6);
        Tile tile4 = new CabinTile("",ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile4,3,6);
        ship.getShipLayout().eliminateTile(2,6);
        ShipCell[][] layout = ship.getShipLayout().getMotherBoard();
    }
    @Test
    void testCrewCount(){
        Ship ship1 = new LevelTwoShip(RED);
        ship1.getShipStats().calculateAllStats(ship1.getShipLayout());
        int crewCount = ship1.getShipStats().getCrewMembers();
    }
    @Test
    void testShipValidation() throws InvalidCellException {
        Ship ship1 = new LevelTwoShip(RED);
        Tile tile1 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.SMOOTH,Direction.TOP);
        ship1.getShipLayout().addTile(tile1,2,2);
        Tile tile2 = new ShieldTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SINGLE,Direction.LEFT,Direction.BOTTOM);
        ship1.getShipLayout().addTile(tile2,3,2);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE,2);
        ship1.getShipLayout().addTile(tile3,3,2);
        Tile tile4 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE);
        ship1.getShipLayout().addTile(tile4,3,1);
        boolean valid = ship1.getShipLayout().isValidShip();
        assertTrue(valid,"Ship should be valid");
    }

}



