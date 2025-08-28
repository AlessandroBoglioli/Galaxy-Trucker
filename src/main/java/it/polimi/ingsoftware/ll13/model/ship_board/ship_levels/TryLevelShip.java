package it.polimi.ingsoftware.ll13.model.ship_board.ship_levels;

import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.CabinTile;

import java.util.stream.IntStream;

/**
 * Represents a trial level ship with specific configuration for testing purposes.
 * This ship has a predefined pattern and initializes with human crew members in all cabins.
 */
public class TryLevelShip extends Ship {
    /**
     * Constructs a TryLevelShip with the specified player color.
     * @param color indicates the color of the player for the starting tile
     * @throws IllegalArgumentException if color is null
     */
    public TryLevelShip(PlayerColors color) {
        super(color);
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
        initializeMatrix();
        connectCells();
        initializeCrew();
    }

    /**
     * Initializes the matrix representing the ship's layout using the tryLevelPattern.
     * Each cell's validity is determined by the pattern defined in ShipMatrixConfig.
     */
    @Override
    protected void initializeMatrix() {
        ShipCell[][] motherBoard = new ShipCell[ShipMatrixConfig.ROW][ShipMatrixConfig.COL];

        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                motherBoard[row][col] = new ShipCell(null, ShipMatrixConfig.tryLevelPattern[row][col]);
            }
        }

        getShipLayout().setMotherBoard(motherBoard);
        getShipLayout().placeStartingCabin();
    }

    @Override
    public void connectCells() {
        IntStream.range(0, ShipMatrixConfig.ROW)
                .forEach(row ->
                        IntStream.range(0, ShipMatrixConfig.COL)
                                .forEach(col -> {
                                    ShipCell current = getShipLayout().getMotherBoard()[row][col];
                                    if (!current.isValid()) {
                                        return; // skip invalid cells
                                    }
                                    current.setTop(
                                            (row > 0 && getShipLayout().getMotherBoard()[row - 1][col].isValid()) ? getShipLayout().getMotherBoard()[row - 1][col] : null
                                    );
                                    current.setBottom(
                                            (row < ShipMatrixConfig.ROW - 1 && getShipLayout().getMotherBoard()[row + 1][col].isValid()) ? getShipLayout().getMotherBoard()[row + 1][col] : null
                                    );
                                    current.setLeft(
                                            (col > 0 && getShipLayout().getMotherBoard()[row][col - 1].isValid()) ? getShipLayout().getMotherBoard()[row][col - 1] : null
                                    );
                                    current.setRight(
                                            (col < ShipMatrixConfig.COL - 1 && getShipLayout().getMotherBoard()[row][col + 1].isValid()) ? getShipLayout().getMotherBoard()[row][col + 1] : null
                                    );
                                })
                );
    }

    /**
     * Initializes crew members in all cabin tiles of the ship.
     * Adds two human crew members to each cabin tile.
     */
    @Override
    public void initializeCrew() {
        ShipCell[][] motherBoard = getShipLayout().getMotherBoard();

        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell currentCell = motherBoard[row][col];
                if (currentCell != null && currentCell.isOccupied() && currentCell.getTile() instanceof CabinTile cabinTile) {

                    // Add two humans to each cabin
                    cabinTile.addCrewMember(new Human());
                }
            }
        }
        // Update ship stats after crew initialization
        getShipStats().calculateCrewMembers(getShipLayout());
    }
}