package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.BatteryStorageTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the ship class, and its used to control the batteries added on the ship tiles.
 * contains methods to add, remove and count the number of batteries on the ship
 */
public class BatteryManager implements Serializable {
    List<Coordinates> batteryTiles;
    private int batteries;

    public BatteryManager() {
        this.batteryTiles = new ArrayList<>();
        this.batteries = 0;
    }

    // ---> Getters <---
    /**
     * getter method to retrieve the batteries of the ship in a given context
     * @return the number of batteries as an int
     */
    public int getBatteries() {
        return this.batteries;
    }

    public List<Coordinates> getBatteryTiles() {
        return batteryTiles;
    }

    // ---> Setters <---
    protected void setBatteryTiles(List<Coordinates> batteryTiles) {
        this.batteryTiles = batteryTiles;
    }

    protected void setBatteries(int batteries) {
        this.batteries = batteries;
    }

    // --> Other Methods <--
    /**
     * Battery tile adder to the list of battery tiles
     * @param batteryTile the coordinates of the battery tile
     */
    protected void addBatteryTile(Coordinates batteryTile) {
        batteryTiles.add(batteryTile);
    }

    /**
     * Battery tile remover to the list of battery tiles
     * @param batteryTile the coordinates of the battery tile
     */
    protected void removeBatteryTile(Coordinates batteryTile) {
        batteryTiles.remove(batteryTile);
    }

    // --> Calculating methods <--
    //PARAMETERS CALCULATION
    /**
     * Fill the battery tiles list that contains all the batteries of the ship.
     * @param shipLayout this is the layout of the ship
     */
    public void fillBatteryList(ShipLayout shipLayout) {
        batteryTiles.clear();
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell != null && currentCell.isOccupied()) {
                    Tile tile = currentCell.getTile();
                    if (tile instanceof BatteryStorageTile) {
                        getBatteryTiles().add(new Coordinates(row, col));
                    }
                }
            }
        }
    }

    /**
     * This is a method that calculate the batteries in the ship scrolling through the created list
     * for a faster and easier implementation.
     * @param shipLayout the layout of the ship.
     */
    public void calculateBatteries(ShipLayout shipLayout) {
        int totalBatteries = 0;
        for (Coordinates batteryTile : batteryTiles) {
            ShipCell cell = shipLayout.getMotherBoard()[batteryTile.getRow()][batteryTile.getCol()];
            if (cell != null && cell.isOccupied() && cell.getTile() instanceof BatteryStorageTile tile) {
                totalBatteries += tile.getBatteryNumber();
            }
        }
        setBatteries(totalBatteries);
    }

    // BATTERIES ELIMINATION
    /**
     * Used when a player want to use a battery, if available in his ship.
     * @param shipLayout the layout of the ship.
     * @param row The chosen row.
     * @param col The chosen col.
     * @return true if battery was used, false if not (if not available or not a battery storage tile).
     */
    public boolean useBattery(ShipLayout shipLayout, int row, int col){
        boolean used = false;
        if(getBatteries() <= 0){
            return used; // if not battery is there we return false straight away
        }
        ShipCell shipCell = shipLayout.getMotherBoard()[row][col];
        if(shipCell != null && shipCell.isOccupied() && shipCell.isValid()){
            Tile tile = shipCell.getTile();
            if(tile instanceof BatteryStorageTile batteryTile){
                if(batteryTile.getBatteryNumber() > 0) {
                    batteryTile.useBattery();
                    used = true;
                }
            }
        }
        calculateBatteries(shipLayout); // Updates the number of batteries once player decides to use a battery
        return used;
    }

    /**
     * Use a random battery.
     * @param shipLayout the layout of the ship.
     * @return a boolean that represent if the battery was actually discarded or not.
     */
    public boolean useRandomBattery(ShipLayout shipLayout){
        if(getBatteries() <= 0){
            return false;
        }
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell.isOccupied() && currentCell.getTile() instanceof BatteryStorageTile) {
                    if(((BatteryStorageTile) currentCell.getTile()).getBatteryNumber() > 0) {
                        ((BatteryStorageTile) currentCell.getTile()).useBattery();
                        calculateBatteries(shipLayout);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
