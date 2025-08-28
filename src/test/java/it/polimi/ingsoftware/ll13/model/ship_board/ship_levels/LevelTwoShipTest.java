package it.polimi.ingsoftware.ll13.model.ship_board.ship_levels;

import it.polimi.ingsoftware.ll13.model.cards.cards_objects.FireShot;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Meteor;
import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.ProblemType;
import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelTwoShipTest {

    private Ship ship;

    @BeforeEach
    public void setUp() {
        ship = new LevelTwoShip(PlayerColors.RED);
    }

    @Test
    void testPlaceCabin_ShouldNotPlaceOutsideShip() {
        Tile tile1 = new CabinTile("", ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.UNIVERSAL);
        Tile tile2 = new CabinTile("", ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.SINGLE);
        ship.getShipLayout().addTile(tile1, 2, 4);
        ship.getShipLayout().addTile(tile2, 2, 5);
        ship.getShipLayout().addTile(tile2, 2, 6);
        ship.getShipLayout().addTile(tile2, 1, 6);

        // Control if the tile was placed or not
        assertNotNull(ship.getShipLayout().getMotherBoard()[2][5].getTile());
        assertNotNull(ship.getShipLayout().getMotherBoard()[2][6].getTile());
        assertNull(ship.getShipLayout().getMotherBoard()[1][6].getTile());
    }

    @Test
    void testCountCrewMembers_ShouldNotCountWrongNumberOfCrewMembers() {
        Tile tile1 = new CabinTile("", ConnectorType.SMOOTH,ConnectorType.SINGLE,ConnectorType.SINGLE,ConnectorType.UNIVERSAL);
        Tile tile2 = new StructuralModuleTile("", ConnectorType.UNIVERSAL,ConnectorType.SMOOTH,ConnectorType.UNIVERSAL,ConnectorType.SINGLE);
        Tile tile3 = new CabinTile("", ConnectorType.DOUBLE,ConnectorType.SMOOTH,ConnectorType.DOUBLE,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile1,2,4);
        ship.getShipLayout().addTile(tile2,2,5);
        ship.getShipLayout().addTile(tile3,3,5);
        ship.initializeCrew();

        assertEquals(2,ship.getShipStats().getCrewMembers()); // It is correct, because the player needs to choose what to place and not only astronauts
        ship.getShipStats().eliminateCrewMembers(ship.getShipLayout(), 5);
        ship.getShipStats().calculateCrewMembers(ship.getShipLayout());
        assertEquals(0, ship.getShipStats().getCrewMembers());

        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 2, 3, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 2, 4, new Alien(AlienColor.PURPLE));
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 5, new Human());
        ship.getShipStats().calculateCrewMembers(ship.getShipLayout());
        assertEquals(4, ship.getShipStats().getCrewMembers());
    }

    @Test
    void testRemoveCrewMembers_ShouldCountCorrectNumberOfCrewMembers() {
        Tile tile1 = new CabinTile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL ,ConnectorType.SINGLE);
        Tile tile2 = new CabinTile("", ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE,ConnectorType.DOUBLE);
        Tile tile3 = new CabinTile("", ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.SINGLE,ConnectorType.DOUBLE);
        Tile tile4 = new CabinTile("", ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH,ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(tile1, 1, 3);
        ship.getShipLayout().addTile(tile4, 1, 4);
        ship.getShipLayout().addTile(tile2, 2, 2);
        ship.getShipLayout().addTile(tile3, 3, 2);
        ship.getShipLayout().addTile(tile2, 3, 4);
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 1, 3, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 1, 4, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 2, 2, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 2, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 4, new Human());
        ship.initializeCrew();

        // Tile placed out of space
        assertNull(ship.getShipLayout().getMotherBoard()[3][4].getTile());

        // Checking the removal of the players
        assertEquals(10, ship.getShipStats().getCrewMembers());
        ship.getShipStats().eliminateCrewMembers(ship.getShipLayout(), 5);
        ship.getShipStats().calculateCrewMembers(ship.getShipLayout());
        assertEquals(0, ((CabinTile)ship.getShipLayout().getMotherBoard()[1][3].getTile()).getCrewCount());
        assertEquals(2, ((CabinTile)ship.getShipLayout().getMotherBoard()[2][3].getTile()).getCrewCount());
        assertEquals(1, ((CabinTile)ship.getShipLayout().getMotherBoard()[2][2].getTile()).getCrewCount());
        assertEquals(5, ship.getShipStats().getCrewMembers());
    }

    @Test
    void testCountFirePower_ShouldCountCorrectFirePower() {
        Tile tile1 = new CabinTile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL ,ConnectorType.SINGLE);
        Tile tile2 = new CannonTile("", ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE,ConnectorType.DOUBLE, Direction.TOP);
        Tile tile3 = new CannonTile("", ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.SINGLE,ConnectorType.DOUBLE, Direction.LEFT);
        Tile tile4 = new CannonTile("", ConnectorType.SMOOTH, ConnectorType.SMOOTH, ConnectorType.SMOOTH,ConnectorType.UNIVERSAL, Direction.LEFT);
        ship.getShipLayout().addTile(tile1, 1, 3);
        ship.getShipLayout().addTile(tile4, 1, 4);
        ship.getShipLayout().addTile(tile2, 2, 2);
        ship.getShipLayout().addTile(tile3, 3, 2);
        ship.getShipLayout().addTile(tile2, 3, 4);

        // Check if are wrongly added crew member to non CabinTiles
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 1, 4, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 1, 3, new Human());
        ship.initializeCrew();
        assertEquals(4, ship.getShipStats().getCrewMembers());

        ship.getShipStats().calculateFirePower(ship.getShipLayout());
        assertEquals(2, ship.getShipStats().getFirePower());
    }

    @Test
    void testCountTotalFirePower_ShouldCountCorrectTotalFirePower() {
        Tile tile1 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.SMOOTH, ConnectorType.SMOOTH ,ConnectorType.UNIVERSAL, 3);
        Tile tile2 = new DoubleCannonTile("", ConnectorType.SINGLE, ConnectorType.DOUBLE, ConnectorType.UNIVERSAL,ConnectorType.SINGLE, Direction.TOP);
        Tile tile3 = new CannonTile("", ConnectorType.DOUBLE, ConnectorType.UNIVERSAL, ConnectorType.SINGLE,ConnectorType.DOUBLE, Direction.LEFT);
        Tile tile4 = new CannonTile("", ConnectorType.SINGLE, ConnectorType.SINGLE, ConnectorType.SINGLE,ConnectorType.DOUBLE, Direction.LEFT);
        ship.getShipLayout().addTile(tile2, 1, 3);
        ship.getShipLayout().addTile(tile1, 1, 4);
        ship.getShipLayout().addTile(tile3, 2, 2);
        ship.getShipLayout().addTile(tile4, 3, 2);

        List<Coordinates> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Coordinates(1,3));

        // Calculation of all the base stats
        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        // Calculation of the battery stats
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());

        assertEquals(3, ship.getShipStats().calculateTotalFirePower(ship.getShipLayout(),ship.getShipCrewPlacer(), ship.getBatteryManager(), coordinatesList));
    }

    @Test
    void testCalculateThrustPower_ShouldCalculateThrustPower() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(motorTile, 2, 5);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 3, 2);
        ship.getShipLayout().addTile(motorTile, 4, 2);
        ship.getShipLayout().addTile(motorTile, 3, 1);

        ship.getShipStats().calculateThrustPower(ship.getShipLayout());

        assertEquals(3, ship.getShipStats().getThrustPower());
    }

    @Test
    void testCalculateExposedConnectors_ShouldCalculateExposedConnectors() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(motorTile, 2, 5);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 3, 2);
        ship.getShipLayout().addTile(motorTile, 4, 2);
        ship.getShipLayout().addTile(motorTile, 3, 1);

        ship.getShipStats().calculateExposedConnectors(ship.getShipLayout());

        assertEquals(16, ship.getShipStats().getExposedConnectors());
    }

    @Test
    void testCalculateAllStats_ShouldCalculateAllStats() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        Tile cannonTile = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(motorTile, 2, 5);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 3, 2);
        ship.getShipLayout().addTile(motorTile, 4, 2);
        ship.getShipLayout().addTile(motorTile, 3, 1);
        ship.getShipLayout().addTile(cannonTile, 1, 3);
        ship.getShipLayout().addTile(cannonTile, 1, 4);

        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        assertEquals(18, ship.getShipStats().getExposedConnectors());
        assertEquals(2, ship.getShipStats().getFirePower());
        assertEquals(3, ship.getShipStats().getThrustPower());
    }

    @Test
    void testCalculateAllBatteries_ShouldCalculateAllBatteries() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile batteryTile1 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile1_2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 2);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(batteryTile1, 3, 2);
        ship.getShipLayout().addTile(batteryTile1_2, 2, 5);
        ship.getShipLayout().addTile(batteryTile2, 3, 4);

        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        assertEquals(8, ship.getBatteryManager().getBatteries());

        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        assertEquals(7, ship.getBatteryManager().getBatteries());


    }

    @Test
    void testCalculateAllBatteriesAndCheckThatAreCorrectlyRemoving() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile batteryTile1 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile1_2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 2);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(batteryTile1, 3, 2);
        ship.getShipLayout().addTile(batteryTile1_2, 2, 5);
        ship.getShipLayout().addTile(batteryTile2, 3, 4);

        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        assertEquals(8, ship.getBatteryManager().getBatteries());

        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        assertEquals(7, ship.getBatteryManager().getBatteries());

        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);

        assertEquals(5, ship.getBatteryManager().getBatteries());
    }

    @Test
    void testShouldRemoveAllBatteriesWithRandomFunction() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        Tile batteryTile1 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile1_2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        Tile batteryTile2 = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 2);
        ship.getShipLayout().addTile(normalTile, 2, 2);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(batteryTile1, 3, 2);
        ship.getShipLayout().addTile(batteryTile1_2, 2, 5);
        ship.getShipLayout().addTile(batteryTile2, 3, 4);

        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        assertEquals(8, ship.getBatteryManager().getBatteries());

        ship.getBatteryManager().useBattery(ship.getShipLayout(), 2, 5);
        assertEquals(7, ship.getBatteryManager().getBatteries());

        ship.getBatteryManager().useRandomBattery(ship.getShipLayout());
        ship.getBatteryManager().useRandomBattery(ship.getShipLayout());
        ship.getBatteryManager().useRandomBattery(ship.getShipLayout());
        ship.getBatteryManager().useRandomBattery(ship.getShipLayout());
        assertEquals(3, ship.getBatteryManager().getBatteries());
    }

    @Test
    void testEpidemic_ShouldRemoveAllCrewMembersThatNeedsToBeRemoved() {
        Tile normalTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile1 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile2 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile3 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile4 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(normalTile, 2, 4);
        ship.getShipLayout().addTile(cabinTile1, 2, 5);
        ship.getShipLayout().addTile(cabinTile2, 3, 4);
        ship.getShipLayout().addTile(cabinTile3, 2, 2);
        ship.getShipLayout().addTile(cabinTile4, 3, 2);

        // Filling of the ship.
        ship.initializeCrew();
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 2, 5, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 4, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 2, 2, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 2, new Human());

        ship.getCabinManager().handleEpidemic(ship.getShipLayout(), ship.getShipStats());
        assertEquals(1, cabinTile3.getCrewCount());
        assertEquals(1, cabinTile4.getCrewCount());
        assertEquals(1, ((CabinTile)ship.getShipLayout().getMotherBoard()[2][3].getTile()).getCrewCount());
        assertEquals(2, cabinTile1.getCrewCount());
        assertEquals(2, cabinTile2.getCrewCount());
    }

    @Test
    void testEpidemic2_ShouldRemoveAllCrewMembersThatNeedsToBeRemoved() {
        CabinTile cabinTile1 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile2 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CabinTile cabinTile3 = new CabinTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        ship.getShipLayout().addTile(cabinTile1, 3, 3);
        ship.getShipLayout().addTile(cabinTile2, 3, 2);
        ship.getShipLayout().addTile(cabinTile3, 3, 4);

        // Filling of the ship.
        ship.initializeCrew();
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 2, new Human());
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 3, new Alien(AlienColor.PURPLE));
        ship.getShipCrewPlacer().placeCrewMembers(ship.getShipLayout(), 3, 4, new Human());

        ship.getCabinManager().handleEpidemic(ship.getShipLayout(), ship.getShipStats());
        assertEquals(0, cabinTile1.getCrewCount());
        assertEquals(1, cabinTile2.getCrewCount());
        assertEquals(1, cabinTile3.getCrewCount());
        assertEquals(1, ((CabinTile)ship.getShipLayout().getMotherBoard()[2][3].getTile()).getCrewCount());
    }

    @Test
    void testCargoHoldTiles_ShouldFillCorrectlyTheCargoHoldList() {
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        CargoHoldTile cargoHoldTile2 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 2);
        CargoHoldTile cargoHoldTile3 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 2);
        CargoHoldTile cargoHoldTile4 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 1);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile2, 1, 3);
        ship.getShipLayout().addTile(cargoHoldTile3, 2, 4);
        ship.getShipLayout().addTile(cargoHoldTile4, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);

        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);
        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());

        // Calculating the total points
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        assertEquals(13, ship.getCargosManager().calculateTotalCargosPoints());

        // Wrong section for the adding of a cargo
        ship.getCargosManager().addCargo(ship.getShipLayout(), new Coordinates(1, 4), CargoColor.BLUE);
        assertEquals(13, ship.getCargosManager().calculateTotalCargosPoints());

        // Adding a cargo and recalculating
        ship.getCargosManager().addCargo(ship.getShipLayout(), new Coordinates(2, 2), CargoColor.YELLOW);
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        assertEquals(16, ship.getCargosManager().calculateTotalCargosPoints());
    }

    @Test
    void testCargoHoldTiles_ShouldFillCorrectlyHugeNumberOfCargos() {
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        CargoHoldTile cargoHoldTile2 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 2);
        CargoHoldTile cargoHoldTile3 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 2);
        CargoHoldTile cargoHoldTile4 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 1);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile2, 1, 3);
        ship.getShipLayout().addTile(cargoHoldTile3, 2, 4);
        ship.getShipLayout().addTile(cargoHoldTile4, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);

        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);
        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());

        // Calculating the total points
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        assertEquals(17, ship.getCargosManager().calculateTotalCargosPoints());
    }

    @Test
    void testCargoManager_ShouldRemoveCorrectlyTheBestCargo() {
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        CargoHoldTile cargoHoldTile2 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 2);
        CargoHoldTile cargoHoldTile3 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 2);
        CargoHoldTile cargoHoldTile4 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.RED, 1);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile2, 1, 3);
        ship.getShipLayout().addTile(cargoHoldTile3, 2, 4);
        ship.getShipLayout().addTile(cargoHoldTile4, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        cargoToAdd.add(CargoColor.RED);
        cargoToAdd.add(CargoColor.RED);

        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);
        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());

        // Calculating the total points
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        assertEquals(13, ship.getCargosManager().calculateTotalCargosPoints());

        assertTrue(ship.getCargosManager().removeBestCargo(ship.getShipLayout()));
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        assertEquals(9, ship.getCargosManager().calculateTotalCargosPoints());

        assertEquals(0, ship.getCargosManager().removeBestCargo(ship.getShipLayout(), 2));
        assertEquals(2, ship.getCargosManager().calculateTotalCargosPoints());
    }

    @Test
    void testProblemHandler_ShouldCorrectlyHandleABigMeteorImpact() {
        CannonTile cannonTile1 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        CannonTile cannonTile2 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.LEFT);
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        BatteryStorageTile batteryStorageTile = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        MotorTile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ShieldTile shieldTile = new ShieldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP, Direction.RIGHT);
        ship.getShipLayout().addTile(cannonTile1, 1, 3);
        ship.getShipLayout().addTile(cannonTile2, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 4);
        ship.getShipLayout().addTile(batteryStorageTile, 2, 5);
        ship.getShipLayout().addTile(motorTile, 3, 4);
        ship.getShipLayout().addTile(shieldTile, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);

        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(1.5, ship.getShipStats().getFirePower());
        assertEquals(1, ship.getShipStats().getThrustPower());

        Meteor meteor = new Meteor(ProblemType.BIG, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleBigMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 7, 0, 0));
        assertNotNull(ship.getShipLayout().getMotherBoard()[1][3].getTile());
        assertEquals(1.5, ship.getShipStats().getFirePower());

        assertTrue(ship.getProblemHandler().handleBigMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 6, 0, 0));
        assertNull(ship.getShipLayout().getMotherBoard()[2][2].getTile());
        assertEquals(1, ship.getShipStats().getFirePower());

        assertNotNull(ship.getShipLayout().getMotherBoard()[2][4].getTile());
        assertEquals(1, ship.getShipStats().getThrustPower());
        assertTrue(ship.getProblemHandler().handleBigMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 8, 0, 0));
        assertNull(ship.getShipLayout().getMotherBoard()[2][4].getTile());
        assertNull(ship.getShipLayout().getMotherBoard()[2][5].getTile());
        assertEquals(0, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(0, ship.getBatteryManager().getBatteries());
        assertEquals(1, ship.getShipStats().getThrustPower());
    }

    @Test
    void testProblemHandler_ShouldCorrectlyHandleABigMeteorImpactWithCannonBehind() {
        StructuralModuleTile structuralModuleTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        CannonTile cannonTile = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        ship.getShipLayout().addTile(structuralModuleTile, 1, 3);
        ship.getShipLayout().addTile(structuralModuleTile, 1, 2);
        ship.getShipLayout().addTile(structuralModuleTile, 3, 3);
        ship.getShipLayout().addTile(cannonTile, 3, 2);

        Meteor meteor = new Meteor(ProblemType.BIG, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleBigMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 2, 0, 0));
        assertNotNull(ship.getShipLayout().getMotherBoard()[1][2].getTile());
        assertNotNull(ship.getShipLayout().getMotherBoard()[3][2].getTile());
    }

    @Test
    void testProblemHandler_ShouldCorrectlyHandleABigMeteorImpactWithDoubleCannon() {
        StructuralModuleTile structuralModuleTile = new StructuralModuleTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL);
        DoubleCannonTile doubleCannonTile = new DoubleCannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        BatteryStorageTile batteryStorageTile = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        ship.getShipLayout().addTile(structuralModuleTile, 1, 3);
        ship.getShipLayout().addTile(structuralModuleTile, 1, 2);
        ship.getShipLayout().addTile(structuralModuleTile, 3, 3);
        ship.getShipLayout().addTile(doubleCannonTile, 3, 2);
        ship.getShipLayout().addTile(batteryStorageTile, 2, 4);

        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());

        Meteor meteor = new Meteor(ProblemType.BIG, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleBigMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 6, 3, 2));
        assertNotNull(ship.getShipLayout().getMotherBoard()[1][2].getTile());
        assertNotNull(ship.getShipLayout().getMotherBoard()[3][2].getTile());
        assertEquals(2, ship.getBatteryManager().getBatteries());
    }

    @Test
    void testProblemHandler2_ShouldCorrectlyHandleASmallMeteor() {
        CannonTile cannonTile1 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        CannonTile cannonTile2 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.LEFT);
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.SMOOTH, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        BatteryStorageTile batteryStorageTile = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        MotorTile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ShieldTile shieldTile = new ShieldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP, Direction.RIGHT);
        ship.getShipLayout().addTile(cannonTile1, 1, 3);
        ship.getShipLayout().addTile(cannonTile2, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 4);
        ship.getShipLayout().addTile(batteryStorageTile, 2, 5);
        ship.getShipLayout().addTile(motorTile, 3, 4);
        ship.getShipLayout().addTile(shieldTile, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);

        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        Meteor meteor = new Meteor(ProblemType.SMALL, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleSmallMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 8, -1, -1));
        assertNotNull(ship.getShipLayout().getMotherBoard()[2][4].getTile());
        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(3, ship.getBatteryManager().getBatteries());

        assertTrue(ship.getProblemHandler().handleSmallMeteorImpact(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), meteor, 9, 3, 3));
        assertNotNull(ship.getShipLayout().getMotherBoard()[2][5].getTile());
        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(2, ship.getBatteryManager().getBatteries());
    }

    @Test
    void testProblemHandler3_ShouldCorrectlyHandleABigFireShot() {
        CannonTile cannonTile1 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        CannonTile cannonTile2 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.LEFT);
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        BatteryStorageTile batteryStorageTile = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        MotorTile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ShieldTile shieldTile = new ShieldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP, Direction.RIGHT);
        ship.getShipLayout().addTile(cannonTile1, 1, 3);
        ship.getShipLayout().addTile(cannonTile2, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 4);
        ship.getShipLayout().addTile(batteryStorageTile, 2, 5);
        ship.getShipLayout().addTile(motorTile, 3, 4);
        ship.getShipLayout().addTile(shieldTile, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);

        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(1.5, ship.getShipStats().getFirePower());
        assertEquals(1, ship.getShipStats().getThrustPower());

        FireShot fireShot = new FireShot(ProblemType.BIG, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleBigFireShot(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), fireShot, 8));
        assertEquals(0, ship.getBatteryManager().getBatteries());
        assertEquals(0, ship.getCargosManager().calculateTotalCargosPoints());
    }

    @Test
    void testProblemHandler4_ShouldCorrectlyHandleASmallFireShot() {
        CannonTile cannonTile1 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP);
        CannonTile cannonTile2 = new CannonTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.LEFT);
        CargoHoldTile cargoHoldTile1 = new CargoHoldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, CompartmentType.BLUE, 3);
        BatteryStorageTile batteryStorageTile = new BatteryStorageTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, 3);
        MotorTile motorTile = new MotorTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.BOTTOM);
        ShieldTile shieldTile = new ShieldTile("", ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, Direction.TOP, Direction.RIGHT);
        ship.getShipLayout().addTile(cannonTile1, 1, 3);
        ship.getShipLayout().addTile(cannonTile2, 2, 2);
        ship.getShipLayout().addTile(cargoHoldTile1, 2, 4);
        ship.getShipLayout().addTile(batteryStorageTile, 2, 5);
        ship.getShipLayout().addTile(motorTile, 3, 4);
        ship.getShipLayout().addTile(shieldTile, 3, 3);

        List<CargoColor> cargoToAdd = new ArrayList<>();
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.BLUE);
        cargoToAdd.add(CargoColor.YELLOW);
        ship.getCargosManager().addCargos(ship.getShipLayout(), cargoToAdd);

        ship.getCargosManager().fillCargoHoldList(ship.getShipLayout());
        ship.getCargosManager().calculateAllCargos(ship.getShipLayout());
        ship.getBatteryManager().fillBatteryList(ship.getShipLayout());
        ship.getBatteryManager().calculateBatteries(ship.getShipLayout());
        ship.getShipStats().calculateAllStats(ship.getShipLayout());

        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());
        assertEquals(1.5, ship.getShipStats().getFirePower());
        assertEquals(1, ship.getShipStats().getThrustPower());

        FireShot fireShot = new FireShot(ProblemType.SMALL, Direction.TOP);
        assertTrue(ship.getProblemHandler().handleSmallFireShots(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), fireShot, 8, 3, 3));
        assertEquals(2, ship.getBatteryManager().getBatteries());
        assertEquals(5, ship.getCargosManager().calculateTotalCargosPoints());

        assertTrue(ship.getProblemHandler().handleSmallFireShots(ship.getShipLayout(), ship.getShipStats(), ship.getBatteryManager(), ship.getCargosManager(), fireShot, 8, -1, -1));
        assertEquals(0, ship.getBatteryManager().getBatteries());
        assertEquals(0, ship.getCargosManager().calculateTotalCargosPoints());
    }
}
