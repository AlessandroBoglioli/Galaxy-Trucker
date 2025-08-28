package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;

import java.io.Serializable;
import java.util.List;

/**
 * this class is used to calculate and save stats everytime something gets added
 */
public class ShipStats implements Serializable {
    private int crewMembers;
    private double firePower;
    private int thrustPower;
    private int exposedConnectors;

    public ShipStats() {
        this.crewMembers = 0;
        this.firePower = 0;
        this.thrustPower = 0;
        this.exposedConnectors = 0;
    }

    // --> Getters <--
    /**
     * Getter method to retrieve the crew members of the ship in a given context
     * @return the fire power as a Double
     */
    public int getCrewMembers() {
        return crewMembers;
    }

    /**
     * Getter method to retrieve the fire power of the ship in a given context
     * @return the fire power as a Double
     */
    public double getFirePower() {
        return firePower;
    }

    /**
     * Getter method to retrieve the thrust power of the ship in a given context
     * @return the thrust power as Double
     */
    public int getThrustPower() {
        return thrustPower;
    }

    /**
     * getter method to retrieve the number of exposed connectors in a given context
     * @return the exposed connectors as an int
     */
    public int getExposedConnectors() {
        return exposedConnectors;
    }

    // --> Setters <--
    /**
     * setter of the crew members
     * @param crewMembers the given Double value
     */
    protected void setCrewMembers(int crewMembers) {
        this.crewMembers = crewMembers;
    }

    /**
     * setter of the fire power
     * @param firePower the given Double value
     */
    protected void setFirePower(double firePower) {
        this.firePower = firePower;
    }

    /**
     * setter of the thrust power
     * @param thrustPower the given Double value
     */
    protected void setThrustPower(int thrustPower) {
        this.thrustPower = thrustPower;
    }

    /**
     * setter of the exposed connectors
     * @param exposedConnectors the given Double value
     */
    protected void setExposedConnectors(int exposedConnectors) {
        this.exposedConnectors = exposedConnectors;
    }

    // --> Other methods <--
    //PARAMETERS CALCULATION
    /**
     * This is a method that calculate the crew members count and set the general stats to that
     * @param shipLayout a shipLayout for the ship.
     */
    public void calculateCrewMembers(ShipLayout shipLayout) {
        int crewMembers = 0;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if(currentCell.getTile() instanceof CabinTile && currentCell.isOccupied() && currentCell.isValid()){
                    crewMembers += ((CabinTile) currentCell.getTile()).getCrewCount();
                }
            }
        }
        setCrewMembers(crewMembers);
    }

    /**
     * Eliminates a specified number of crew members from the ship's cabins.
     * The method iterates over the ship's cabins and removes crew members one at a time.
     * If a cabin contains only one crew member, it will not be emptied.
     * The process stops once the specified number of crew members has been eliminated or
     * once there are no more crew members to remove.
     *
     * <p>For each cabin, the method calls the {@link CabinTile#eliminateCrewMembers(int)}
     * method to eliminate crew members, ensuring that no cabin is left without any crew members
     * unless it only contains one.</p>
     *
     * @param shipLayout a shipLayout for the ship.
     * @param number The number of crew members to eliminate.
     *               The method will remove the specified number of crew members from the cabins,
     *               ensuring that at least one crew member remains in cabins that have multiple members.
     */
    public void eliminateCrewMembers(ShipLayout shipLayout, int number){
        int remainingToEliminate = number;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell != null && currentCell.getTile() instanceof CabinTile cabinTile) {
                    while (remainingToEliminate > 0 && cabinTile.getCrewCount()>0) {
                        cabinTile.eliminateCrewMembers(1);
                        remainingToEliminate--;
                        if(remainingToEliminate == 0){
                            break;
                        }
                    }
                }
                if (remainingToEliminate == 0) {
                    break;
                }
            }
            if(remainingToEliminate == 0){
                break;
            }
        }
        calculateCrewMembers(shipLayout);
    }

    /**
     * This is the method that calculate the firePower of the ship without any double cannon.
     */
    public void calculateFirePower(ShipLayout shipLayout) {
        double firePower = 0;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell != null && (currentCell.getTile() instanceof CannonTile)) {
                    firePower += ((CannonTile) currentCell.getTile()).getFirePower();
                }
            }
        }
        setFirePower(firePower);
    }

    /**
     * This is a method that calculate the fire power of the ship including all the double cannon activated.
     * @param shipLayout the layout of the ship.
     * @param shipCrewPlacer crewPlacer of the ship
     * @param batteryManager the battery manager of the ship.
     * @param usingTilesWithBattery A list of coordinates of the double cannon activated.
     * @return the total fire power.
     */
    public double calculateTotalFirePower(ShipLayout shipLayout, ShipCrewPlacer shipCrewPlacer, BatteryManager batteryManager, List<Coordinates> usingTilesWithBattery) {
        double totalFirePower = getFirePower();
        if (!usingTilesWithBattery.isEmpty() && usingTilesWithBattery.getFirst().getCol() != -1) {
            for(Coordinates usingTile : usingTilesWithBattery) {
                ShipCell currentCell = shipLayout.getMotherBoard()[usingTile.getRow()][usingTile.getCol()];
                if(checkIfDoubleCannon(currentCell) && currentCell.isOccupied()) {
                    if(batteryManager.getBatteries() > 0) {
                        batteryManager.useRandomBattery(shipLayout);
                        totalFirePower += ((DoubleCannonTile)currentCell.getTile()).getFirePower();
                    } else {
                        break;
                    }
                }
            }
        }
        int purpleAlienCount = shipCrewPlacer.getPurpleAlienCount();
        totalFirePower = totalFirePower + 2*purpleAlienCount;
        return totalFirePower;
    }

    private boolean checkIfDoubleCannon(ShipCell cell) {
        return cell.getTile() instanceof DoubleCannonTile;
    }

    public void calculateThrustPower(ShipLayout shipLayout) {
        int thrustPower = 0;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell != null && (currentCell.getTile() instanceof MotorTile motorTile)) {
                    thrustPower += motorTile.getThrust();
                }
            }
        }
        setThrustPower(thrustPower);
    }

    /**
     * This method calculate the total thrust power including all the double motor turned on.
     *
     * @param usingTilesWithBattery a list of Coordinates of the double motors turned on.
     * @return the total thrust power including all the double motor turned on.
     */
    public int calculateTotalThrustPower(ShipLayout shipLayout, ShipCrewPlacer crewPlacer, BatteryManager batteryManager, List<Coordinates> usingTilesWithBattery) {
        int totalThrustPower = getThrustPower();
        if (!usingTilesWithBattery.isEmpty() && usingTilesWithBattery.getFirst().getCol() != -1) {
            for(Coordinates usingTile : usingTilesWithBattery) {
                ShipCell currentCell = shipLayout.getMotherBoard()[usingTile.getRow()][usingTile.getCol()];
                if(checkIfDoubleMotor(currentCell) && currentCell.isOccupied()) {
                    if(batteryManager.getBatteries() > 0) {
                        batteryManager.useRandomBattery(shipLayout);
                        totalThrustPower += ((DoubleMotorTile)currentCell.getTile()).getThrust();
                    } else {
                        break;
                    }
                }
            }
        }
        int yellowAlienCount = crewPlacer.getPurpleAlienCount();
        totalThrustPower = totalThrustPower + 2*yellowAlienCount;
        return totalThrustPower;
    }

    private boolean checkIfDoubleMotor(ShipCell cell) {
        return cell.getTile() instanceof DoubleMotorTile;
    }

    /**
     * Calculates the initial number of exposed connectors for the ship.
     * Exposed connectors are those on the outer edges of the ship that are not connected to any adjacent tiles.
     * This method checks each cell of the ship and determines if its connectors are exposed based on its adjacent cells.
     * Only connectors that are not of type SMOOTH are considered exposed.
     * The exposed connectors are counted and stored in the `exposedConnectors` attribute of the ship.
     * This method should be called once the ship is validated and all tiles are correctly placed.
     * @param shipLayout a shipLayout for the ship.
     */
    public void calculateExposedConnectors(ShipLayout shipLayout) {
        int exposedConnectors = 0;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell == null || !currentCell.isValid() || currentCell.getTile() == null) {
                    continue;
                }

                Tile currentTile = currentCell.getTile();
                Connector[] currentConnectors = currentTile.getConnectors();
                ShipCell topCell = shipLayout.getTopCell(row, col);
                if (topCell == null || !topCell.isOccupied()) {
                    if (currentConnectors[0].getType() != ConnectorType.SMOOTH) {
                        exposedConnectors++;
                    }
                }
                ShipCell rightCell = shipLayout.getRightCell(row, col);
                if (rightCell == null || rightCell.getTile() == null || !rightCell.isOccupied()) {
                    if (currentConnectors[1].getType() != ConnectorType.SMOOTH) {
                        exposedConnectors++;
                    }
                }
                ShipCell bottomCell = shipLayout.getBottomCell(row, col);
                if (bottomCell == null || !bottomCell.isOccupied()) {
                    if (currentConnectors[2].getType() != ConnectorType.SMOOTH) {
                        exposedConnectors++;
                    }
                }
                ShipCell leftCell = shipLayout.getLeftCell(row, col);
                if (leftCell == null || leftCell.getTile() == null || !leftCell.isOccupied()) {
                    if (currentConnectors[3].getType() != ConnectorType.SMOOTH) {
                        exposedConnectors++;
                    }
                }
            }
        }
        setExposedConnectors(exposedConnectors);
    }

    public void calculateAllStats(ShipLayout shipLayout) {
        calculateCrewMembers(shipLayout);
        calculateFirePower(shipLayout);
        calculateThrustPower(shipLayout);
        calculateExposedConnectors(shipLayout);
    }
}
