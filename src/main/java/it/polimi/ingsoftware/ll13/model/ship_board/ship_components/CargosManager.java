package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.general_enumerations.CargoColor;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.CompartmentType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.CargoHoldTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the ship class, and its used to manage the cargos.
 * contains methods to add, remove and count the number of cargos on the ship, and points that those add to the total of a player
 */
public class CargosManager {
    private List<Coordinates> cargoHoldTiles;
    private int redCargos;
    private int yellowCargos;
    private int greenCargos;
    private int blueCargos;

    public CargosManager() {
        this.cargoHoldTiles = new ArrayList<>();
        this.redCargos = 0;
        this.yellowCargos = 0;
        this.greenCargos = 0;
        this.blueCargos = 0;
    }

    // --> Getters <--
    /**
     * Retrieves the list of coordinates of the CargoHoldTiles that are placed in the ship
     * @return the list of coordinates of cargoHoldTiles in the ship
     */
    public List<Coordinates> getCargoHoldTiles() {
        return cargoHoldTiles;
    }

    public int getRedCargos() {
        return redCargos;
    }

    public int getYellowCargos() {
        return yellowCargos;
    }

    public int getGreenCargos() {
        return greenCargos;
    }

    public int getBlueCargos() {
        return blueCargos;
    }

    // --> Setters <--

    /**
     * Set the list of the coordinates of the cargoHoldTiles
     * @param cargoHoldTiles list of coordinated of the cargoHoldTile
     */
    public void setCargoHoldTiles(List<Coordinates> cargoHoldTiles) {
        this.cargoHoldTiles = cargoHoldTiles;
    }

    public void setRedCargos(int redCargos) {
        this.redCargos = redCargos;
    }

    public void setYellowCargos(int yellowCargos) {
        this.yellowCargos = yellowCargos;
    }

    public void setGreenCargos(int greenCargos) {
        this.greenCargos = greenCargos;
    }

    public void setBlueCargos(int blueCargos) {
        this.blueCargos = blueCargos;
    }

    // --> Others functions <--
    /**
     * Add to the list of cargoHoldTiles a new cargoHoldTile
     * @param coordinates of the cargoHoldTile that needs to be added
     */
    public void addCargoHoldTile(Coordinates coordinates) {
        cargoHoldTiles.add(coordinates);
    }

    /**
     * Remove from the list of cargoHoldTiles a cargoHoldTile
     * @param coordinates of the cargoHoldTile that needs to be removed
     */
    public void removeCargoHoldTile(Coordinates coordinates) {
        if (coordinates != null) {
            cargoHoldTiles.remove(coordinates);
        }
    }

    // --> Calculating methods <--
    /**
     * This is a method that fills the list of CargoHoldTiles
     * @param shipLayout is the layout of the ship
     */
    public void fillCargoHoldList(ShipLayout shipLayout) {
        cargoHoldTiles.clear();
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
                if (currentCell != null && currentCell.isOccupied()) {
                    Tile tile = currentCell.getTile();
                    if (tile instanceof CargoHoldTile) {
                        getCargoHoldTiles().add(new Coordinates(row, col));
                    }
                }
            }
        }
    }

    // CALCULATION OF THE CARGOS
    public void calculateAllCargos(ShipLayout shipLayout) {
        prepareCargoCounting();
        fillCargoHoldList(shipLayout);

        for (Coordinates coordinates : cargoHoldTiles) {
            ShipCell cell = shipLayout.getMotherBoard()[coordinates.getRow()][coordinates.getCol()];
            if (cell != null && cell.isOccupied() && cell.getTile() instanceof CargoHoldTile cargoHoldTile) {
                for (CargoColor cargoColor : cargoHoldTile.getCargos()) {
                    addCargoColor(cargoColor);
                }
            }
        }

    }


    private void prepareCargoCounting() {
        setRedCargos(0);
        setYellowCargos(0);
        setGreenCargos(0);
        setBlueCargos(0);
    }

    private void addCargoColor(CargoColor cargoColor) {
        switch (cargoColor) {
            case RED:
                redCargos++;
                break;
            case YELLOW:
                yellowCargos++;
                break;
            case GREEN:
                greenCargos++;
                break;
            case BLUE:
                blueCargos++;
                break;
            default:
                break;
        }
    }

    public void addCargo(ShipLayout shipLayout, Coordinates coordinates, CargoColor cargoColor) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();

        // Validate matrix bounds
        if (row < 0 || row >= ShipMatrixConfig.ROW || col < 0 || col >= ShipMatrixConfig.COL) {
            return; // Or handle the error appropriately
        }

        ShipCell cell = shipLayout.getMotherBoard()[row][col];
        if (cell != null && cell.isOccupied()) {
            Tile tile = cell.getTile();
            if (tile instanceof CargoHoldTile cargoHoldTile) {
                cargoHoldTile.addCargo(cargoColor);
            }
        }
        calculateAllCargos(shipLayout);
    }

    /**
     * This is a method that add cargos to the ship until everyone of the existing cargos are full.
     * @param cargos the list of cargos that needs to be inserted in the ship
     */
    public void addCargos(ShipLayout shipLayout, List<CargoColor> cargos) {
        for(CargoColor cargo : cargos) {
            ShipCell cell;
            if(cargo == CargoColor.RED) {
                cell = searchEmptyCargoHolder(shipLayout, CompartmentType.RED);
            } else {
                cell = searchEmptyCargoHolder(shipLayout, CompartmentType.BLUE);
                if(cell == null && cargo == CargoColor.GREEN) {
                    cell = searchEmptyCargoHolder(shipLayout, CompartmentType.RED);
                }
            }
            if(cell != null) {
                ((CargoHoldTile) cell.getTile()).addCargo(cargo);
            }
        }
        fillCargoHoldList( shipLayout );
        calculateAllCargos(shipLayout);
    }

    private ShipCell searchEmptyCargoHolder(ShipLayout shipLayout, CompartmentType compartmentType) {
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell cell = shipLayout.getMotherBoard()[row][col];
                if (cell.isOccupied() && cell.getTile() instanceof CargoHoldTile) {
                    if(((CargoHoldTile) cell.getTile()).getCargos().size() < ((CargoHoldTile) cell.getTile()).getCompartmentCapacity() && ((CargoHoldTile) cell.getTile()).getCompartmentType() == compartmentType) {
                        return cell;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This is a method that removes the number specified of best cargos present in the ship.
     * @param shipLayout the layout of the ship.
     * @param cargoNumber the number of cargo that needs to be eliminated.
     * @return an integer representing the number of cargo that still needs to be eliminated.
     */
    public int removeBestCargo(ShipLayout shipLayout, int cargoNumber) {
        for(int i = 0; i < cargoNumber; i++) {
            boolean result = removeBestCargo(shipLayout);
            if(!result) {
                calculateAllCargos(shipLayout);
                return (cargoNumber - i);
            }
        }
        calculateAllCargos(shipLayout);
        return 0;
    }

    /**
     * This is a method that removes the best cargo present in the ship.
     * @param shipLayout the layout of the ship.
     * @return if the cargo was correctly removed.
     */
    public boolean removeBestCargo(ShipLayout shipLayout) {
        Coordinates bestCargoColorTile = new Coordinates(-1, -1);
        CargoColor bestCargoColor = null;
        for (Coordinates coordinates : getCargoHoldTiles()) {
            CargoHoldTile cargoHoldTile = (CargoHoldTile) shipLayout.getMotherBoard()[coordinates.getRow()][coordinates.getCol()].getTile();
            for (CargoColor cargoColor : cargoHoldTile.getCargos()) {
                if (cargoColor == null) continue;
                if (isCargoColorBetter(bestCargoColor, cargoColor)) {
                    bestCargoColor = cargoColor;
                    bestCargoColorTile.setRow(coordinates.getRow());
                    bestCargoColorTile.setCol(coordinates.getCol());
                }
            }
        }
        if (bestCargoColor != null) {
            ((CargoHoldTile) shipLayout.getMotherBoard()[bestCargoColorTile.getRow()][bestCargoColorTile.getCol()].getTile())
                    .removeCargo(bestCargoColor);
            return true;
        }
        return false;
    }


    private boolean isCargoColorBetter(CargoColor bestCargoColor, CargoColor cargoColor) {
        if(bestCargoColor == null) return true;

        return switch (bestCargoColor) {
            case BLUE -> cargoColor != CargoColor.BLUE;
            case GREEN -> (cargoColor == CargoColor.YELLOW) || (cargoColor == CargoColor.RED);
            case YELLOW -> cargoColor == CargoColor.RED;
            default -> false; // It means that the best cargo selected is a red one
        };
    }


     public int calculateTotalCargosPoints() {
        return (getRedCargos()*4)+(getYellowCargos()*3)+(getGreenCargos()*2)+(getBlueCargos());
     }
}
