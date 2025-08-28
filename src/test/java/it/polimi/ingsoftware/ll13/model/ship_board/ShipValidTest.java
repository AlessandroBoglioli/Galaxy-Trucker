package it.polimi.ingsoftware.ll13.model.ship_board;

import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.exceptions.InvalidCellException;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.ship_board.ship_levels.LevelTwoShip;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.VitalSupportColor;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ShipValidTest {
    @Test
    void connectionCheck() throws InvalidCellException {
        Ship ship1 = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new Tile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship1.getShipLayout().addTile(tile1,1,3);
        assertTrue(ship1.getShipLayout().checkConnections());
        Tile tile2 = new Tile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE);
        ship1.getShipLayout().addTile(tile2,2,4);
        assertTrue(ship1.getShipLayout().checkConnections());
        Tile tile3 = new Tile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE);
        ship1.getShipLayout().addTile(tile3,2,2);
        assertTrue(ship1.getShipLayout().checkConnections());
        Tile tile4 = new Tile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH);
        ship1.getShipLayout().addTile(tile4,3,3);
        assertFalse(ship1.getShipLayout().checkConnections());
        Ship ship2 = new LevelTwoShip(PlayerColors.RED);
        ship2.getShipLayout().addTile(tile1,1,3);
        ship2.getShipLayout().addTile(tile2,2,4);
        ship2.getShipLayout().addTile(tile3,2,2);
        assertTrue(ship2.getShipLayout().checkConnections());
        Tile tile5 = new Tile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE);
        ship2.getShipLayout().addTile(tile5,2,5);
        assertFalse(ship2.getShipLayout().checkConnections());
        //------Special case
        Ship ship3 = getShip();
        assertTrue(ship3.getShipLayout().checkConnections(),"Should be Valid");
    }

    private static @NotNull Ship getShip() {
        Ship ship3 = new LevelTwoShip(PlayerColors.YELLOW);
        Tile tile6 = new Tile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.DOUBLE);
        ship3.getShipLayout().addTile(tile6,1,3);
        Tile tile7 = new Tile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE);
        ship3.getShipLayout().addTile(tile7,1,4);
        Tile tile8 = new Tile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship3.getShipLayout().addTile(tile8,2,4);
        return ship3;
    }

    @Test
    void testValidPathToCenter() throws InvalidCellException{
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        List<TileCoordinates> invalidCells = ship.getShipLayout().getInvalidCells();
        Tile tile1 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SINGLE,3);
        ship.getShipLayout().addTile(tile1,1,3);
        Tile tile2 = new CabinTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile2,1,2);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE,2);
        ship.getShipLayout().addTile(tile3,1,1);
        Tile tile4 = new BatteryStorageTile("", ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,2);
        ship.getShipLayout().addTile(tile4,2,1);
        Tile tile5 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.SMOOTH, Direction.LEFT);
        ship.getShipLayout().addTile(tile5,2,2);
        Tile tile6 = new CannonTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile6,3,3);
        Tile tile7 = new CargoHoldTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,CompartmentType.BLUE,2);
        ship.getShipLayout().addTile(tile7,3,2);
        Tile tile8 = new DoubleCannonTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SMOOTH,Direction.LEFT);
        ship.getShipLayout().addTile(tile8,3,1);
        Tile tile9 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile9,2,4);
        Tile tile10 = new StructuralModuleTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SMOOTH);
        ship.getShipLayout().addTile(tile10,1,4);
        boolean path1 = ship.getShipLayout().hasValidPathToCenter(3,1);
        assertTrue(path1);
        boolean path2 = ship.getShipLayout().hasValidPathToCenter(1,4);
        assertTrue(path2);
        boolean path3 = ship.getShipLayout().hasValidPathToCenter(2,1);
        assertTrue(path3);
    }
    @Test
    void testInvalidateCell() throws InvalidCellException {
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        List<TileCoordinates> invalidCells = ship.getShipLayout().getInvalidCells();
        Tile tile1 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile1,2,4);
        Tile tile2 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SINGLE,2);
        ship.getShipLayout().addTile(tile2,3,4);
        Tile tile3 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile3,3,5);
        Tile tile4 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile4,2,5);
        Tile tile5 = new VitalSupportTile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE, VitalSupportColor.PURPLE);
        ship.getShipLayout().addTile(tile5,4,5);
        Tile tile6 = new CabinTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile6,4,6);
        boolean isValid = ship.getShipLayout().isValidShip();
        assertFalse(isValid,"Ship should not be valid");
        assertTrue(invalidCells.contains(new TileCoordinates(2,4,tile1)),"2,4");
        assertTrue(invalidCells.contains(new TileCoordinates(3,4,tile2)),"3,4");
        assertEquals(2,invalidCells.size());
    }
    @Test
    void eliminateCell() throws  InvalidCellException{
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        List<TileCoordinates> invalidCells = ship.getShipLayout().getInvalidCells();
        Tile tile1 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile1,2,4);
        Tile tile2 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.SINGLE,2);
        ship.getShipLayout().addTile(tile2,3,4);
        Tile tile3 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile3,3,5);
        Tile tile4 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile4,2,5);
        Tile tile5 = new VitalSupportTile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE, VitalSupportColor.PURPLE);
        ship.getShipLayout().addTile(tile5,4,5);
        Tile tile6 = new CabinTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile6,4,6);
        ship.getShipLayout().eliminateTile(2,4);
        boolean isValid = ship.getShipLayout().isValidShip();
        assertTrue(isValid);
        int centralRow = ShipMatrixConfig.CENTRAL_ROW;
        int centralCol = ShipMatrixConfig.CENTRAL_COL;
        boolean isOnlyCentralOccupied = true;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = ship.getShipLayout().getMotherBoard()[row][col];
                if (currentCell.isOccupied()) {
                    if (row != centralRow || col != centralCol) {
                        isOnlyCentralOccupied = false;
                        break;
                    }
                }
            }
        }
        assertTrue(isOnlyCentralOccupied,"Only the central cell should be occupied");

    }
    @Test
    void testMotorPlacement() throws InvalidCellException {
        Ship ship = new LevelTwoShip(PlayerColors.GREEN);
        Tile tile1 = new DoubleMotorTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,Direction.TOP);
        ship.getShipLayout().addTile(tile1,2,4);
        boolean isValid = ship.getShipLayout().isValidShip();
        List<TileCoordinates> invalidCells = ship.getShipLayout().getInvalidCells();
        assertFalse(isValid,"Double cannon should be invalidated");
        assertTrue(invalidCells.contains(new TileCoordinates(2,4,tile1)));
        assertEquals(1,invalidCells.size());
        Tile tile2 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SINGLE);
        ship.getShipLayout().addTile(tile2,2,2);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,2);
        ship.getShipLayout().addTile(tile3,3,2);
        Tile tile4 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile4,2,1);
        boolean isValid2 = ship.getShipLayout().isValidShip();
        assertTrue(invalidCells.contains(new TileCoordinates(2,4,tile1)));
        Tile tile5 = new StructuralModuleTile("", ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile5,3,1);
        boolean isValid3 = ship.getShipLayout().isValidShip();
        assertFalse(isValid3);
        assertTrue(invalidCells.contains(new TileCoordinates(2,1,tile4)));
        assertTrue(invalidCells.contains(new TileCoordinates(3,1,tile5)));
        assertEquals(3,invalidCells.size());

    }
    @Test
    void testCannonPlacement(){
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tileTest = new DoubleCannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,Direction.TOP);
        ship.getShipLayout().addTile(tileTest,2,4);
        Tile tile1 = new CabinTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE);
        ship.getShipLayout().addTile(tile1,1,3);
        Tile tile2 = new ShieldTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SINGLE,Direction.LEFT,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile2,1,4);
        boolean cannonCheck = ship.getShipLayout().checkCannon((CannonBaseTile) tileTest,2,4);
        assertFalse(cannonCheck);
    }
    @Test
    void testFinalSimulation() throws InvalidCellException{
      Ship ship = new LevelTwoShip(PlayerColors.RED);
      Tile tile1 = new DoubleCannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,Direction.TOP);
      ship.getShipLayout().addTile(tile1,1,3);
      Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
      ship.getShipLayout().addTile(tile2,1,2);
      Tile tile6 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
      ship.getShipLayout().addTile(tile6,2,4);
      Tile tile3 = new BatteryStorageTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,2);
      ship.getShipLayout().addTile(tile3,1,4);
      Tile tile4 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
      ship.getShipLayout().addTile(tile4,1,5);
      Tile tile5 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,Direction.TOP);
      ship.getShipLayout().addTile(tile5,0,4);
      Tile tile7 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
      ship.getShipLayout().addTile(tile7,2,2);
      Tile tile8 = new ShieldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,Direction.TOP,Direction.RIGHT);
      ship.getShipLayout().addTile(tile8,2,5);
      Tile tile9 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.BOTTOM);
      ship.getShipLayout().addTile(tile9,2,6);
      Tile tile10 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.BOTTOM);
      ship.getShipLayout().addTile(tile10,2,1);
      Tile tile11 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
      ship.getShipLayout().addTile(tile11,3,3);
      Tile tile12 = new VitalSupportTile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,VitalSupportColor.YELLOW);
      ship.getShipLayout().addTile(tile12,3,2);
      Tile tile13 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,CompartmentType.BLUE,2);
      ship.getShipLayout().addTile(tile13,3,4);
      assertTrue(ship.getShipLayout().isValidShip());

    }
    @Test
    void testFinalSimulationInvalid() throws InvalidCellException{
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new DoubleCannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,Direction.TOP);
        ship.getShipLayout().addTile(tile1,1,3);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile2,1,2);
        Tile tile6 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile6,2,4);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,2);
        ship.getShipLayout().addTile(tile3,1,4);
        Tile tile4 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile4,1,5);
        Tile tile5 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,Direction.TOP);
        ship.getShipLayout().addTile(tile5,0,4);
        Tile tile7 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile7,2,2);
        Tile tile8 = new ShieldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,Direction.TOP,Direction.RIGHT);
        ship.getShipLayout().addTile(tile8,2,5);
        Tile tile9 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.TOP);
        ship.getShipLayout().addTile(tile9,2,6);
        Tile tile10 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile10,2,1);
        Tile tile11 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
        ship.getShipLayout().addTile(tile11,3,3);
        Tile tile12 = new VitalSupportTile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,VitalSupportColor.YELLOW);
        ship.getShipLayout().addTile(tile12,3,2);
        Tile tile13 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,CompartmentType.BLUE,2);
        ship.getShipLayout().addTile(tile13,3,4);
        assertFalse(ship.getShipLayout().isValidShip());

    }
    @Test
    void testDeleteSimulation(){
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new DoubleCannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,Direction.TOP);
        ship.getShipLayout().addTile(tile1,1,3);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile2,1,2);
        Tile tile6 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE);
        ship.getShipLayout().addTile(tile6,2,4);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.UNIVERSAL,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SMOOTH,2);
        ship.getShipLayout().addTile(tile3,1,4);
        Tile tile4 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile4,1,5);
        Tile tile5 = new CannonTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,Direction.TOP);
        ship.getShipLayout().addTile(tile5,0,4);
        Tile tile7 = new StructuralModuleTile("", ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile7,2,2);
        Tile tile8 = new ShieldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,Direction.TOP,Direction.RIGHT);
        ship.getShipLayout().addTile(tile8,2,5);
        Tile tile9 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile9,2,6);
        Tile tile10 = new MotorTile("", ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile10,2,1);
        Tile tile11 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
        ship.getShipLayout().addTile(tile11,3,3);
        Tile tile12 = new VitalSupportTile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,VitalSupportColor.YELLOW);
        ship.getShipLayout().addTile(tile12,3,2);
        Tile tile13 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,CompartmentType.BLUE,2);
        ship.getShipLayout().addTile(tile13,3,4);
        ship.getShipLayout().eliminateTile(2,4);
        assertEquals(6,ship.getShipLayout().getWasteTiles());
        ship.getShipLayout().eliminateTile(2,3);
        assertEquals(14,ship.getShipLayout().getWasteTiles());
    }
    @Test
    void testCalculateExposedConnections(){
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new DoubleMotorTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile1,2,2);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile2,2,4);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
        ship.getShipLayout().addTile(tile3,2,5);
        Tile tile4 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,3);
        ship.getShipLayout().addTile(tile4,1,4);
        Tile tile5 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.DOUBLE,CompartmentType.BLUE,3);
        ship.getShipLayout().addTile(tile5,3,4);
        ship.getShipStats().calculateExposedConnectors(ship.getShipLayout());
        int exposedConnectors = ship.getShipStats().getExposedConnectors();
        assertEquals(7,exposedConnectors);
    }
    @Test
    void testBatteryCalculation(){
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new DoubleMotorTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile1,2,2);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile2,2,4);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
        ship.getShipLayout().addTile(tile3,2,5);
        Tile tile4 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,3);
        ship.getShipLayout().addTile(tile4,1,4);
        Tile tile5 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.DOUBLE,CompartmentType.BLUE,3);
        ship.getShipLayout().addTile(tile5,3,4);
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        int batteryCount = ship.getBatteryManager().getBatteries();
        assertEquals(5,batteryCount);
    }
    @Test
    void testCalculationAfterElimination(){
        Ship ship = new LevelTwoShip(PlayerColors.RED);
        Tile tile1 = new DoubleMotorTile("", ConnectorType.SINGLE,ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.SMOOTH,Direction.BOTTOM);
        ship.getShipLayout().addTile(tile1,2,2);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.SINGLE,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile2,2,4);
        Tile tile3 = new BatteryStorageTile("", ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,2);
        ship.getShipLayout().addTile(tile3,2,5);
        Tile tile4 = new BatteryStorageTile("", ConnectorType.DOUBLE,ConnectorType.DOUBLE,ConnectorType.SINGLE,ConnectorType.SMOOTH,3);
        ship.getShipLayout().addTile(tile4,1,2);
        Tile tile5 = new CargoHoldTile("", ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.DOUBLE,CompartmentType.BLUE,3);
        ship.getShipLayout().addTile(tile5,3,4);
        Tile tile6 = new BatteryStorageTile("", ConnectorType.SINGLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.SMOOTH,3);
        ship.getShipLayout().addTile(tile6,4,4);
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getShipStats().calculateExposedConnectors(ship.getShipLayout());
        int batteryCount = ship.getBatteryManager().getBatteries();
        int exposedCount = ship.getShipStats().getExposedConnectors();
        assertEquals(8,batteryCount);
        assertEquals(7,exposedCount);
        ship.getShipLayout().eliminateTile(2,4);
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getShipStats().calculateExposedConnectors(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        int newBatteryCount = ship.getBatteryManager().getBatteries();
        int newExposedCount = ship.getShipStats().getExposedConnectors();
        assertEquals(3,newBatteryCount);
        assertEquals(5,newExposedCount);


    }

}
