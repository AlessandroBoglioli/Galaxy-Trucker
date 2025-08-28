package it.polimi.ingsoftware.ll13.model.ship_board.ship_levels;

import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.ship_board.Ship;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.StartingCabinTile;

import java.util.stream.IntStream;

/**
 * This class represents a level two player's ship using a graph structure.
 * The graph starts with the central starting cabin with the color of the player.
 */
public class LevelTwoShip extends Ship {
    /**
     * Constructs a LevelTwoShip with the specified player color.
     * @param color indicates the color of the player for the starting tile
     * @throws IllegalArgumentException if color is null
     */
    public LevelTwoShip(PlayerColors color) {
        super(color);
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
        initializeMatrix();
        connectCells();
        initializeCrew();
    }

    /**
     * Initializes the matrix representing the ship's layout by creating a 2D array of ShipCell objects.
     * Each cell's validity is determined based on the pattern defined in ShipMatrixConfig.levelTwoPattern.
     */
    @Override
    protected void initializeMatrix() {
        ShipCell[][] motherBoard = new ShipCell[ShipMatrixConfig.ROW][ShipMatrixConfig.COL];

        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                motherBoard[row][col] = new ShipCell(null, ShipMatrixConfig.levelTwoPattern[row][col]);
            }
        }

        getShipLayout().setMotherBoard(motherBoard);
        getShipLayout().placeStartingCabin();
    }

    /**
     * This method connects the cells with their adjacent ones, if possible
     * the connections will be corrected at the end of the building phase
     */
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
     * Initializes the crew members in the starting cabin.
     */
    @Override
    public void initializeCrew() {
        int centerRow = ShipMatrixConfig.CENTRAL_ROW;
        int centerCol = ShipMatrixConfig.CENTRAL_COL;
        StartingCabinTile startingCabin = (StartingCabinTile)
                getShipLayout().getMotherBoard()[centerRow][centerCol].getTile();

        startingCabin.fillWithAstronauts(); // Starting cabin has 2 crew members
        getShipStats().calculateCrewMembers(getShipLayout());
    }
}