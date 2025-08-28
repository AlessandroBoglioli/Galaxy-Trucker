package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.CabinTile;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class is part of the ship class, and its used to control the cabin tiles logic.
 * contains methods to check compatibility of two different tiles, and verify if a tile is connected to another and handle
 * effects of cards that need that information (adventure phase)
 */
public class CabinManager {

    public CabinManager() {
        ;
    }

    /**
     * A class that handle the epidemic effect.
     * @param shipLayout the layout of the ship.
     * @param shipStats the stats of the ship.
     */
    public void handleEpidemic(ShipLayout shipLayout, ShipStats shipStats) {
        List<CabinTile> connectedCabins = getConnectedCabinTile(shipLayout);
        for (CabinTile cabinTile : connectedCabins) {
            cabinTile.eliminateCrewMembers(1);
        }
        shipStats.calculateCrewMembers(shipLayout);
    }

    /**
     * This method returns all cabin tiles that are connected to other cabin tiles
     * through valid connectors (following the same rules as pathValidator).
     * @param shipLayout the ship layout.
     * @return a list of connected CabinTile instances.
     */
    private List<CabinTile> getConnectedCabinTile(ShipLayout shipLayout) {
        List<CabinTile> connectedCabinTiles = new ArrayList<>();
        boolean[][] visited = new boolean[ShipMatrixConfig.ROW][ShipMatrixConfig.COL];

        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                if (visited[row][col]) continue;

                ShipCell currentCell = shipLayout.getMotherBoard()[row][col];

                if (currentCell == null || !currentCell.isValid() || !currentCell.isOccupied()) {
                    continue;
                }

                Tile currentTile = currentCell.getTile();

                if (currentTile instanceof CabinTile) {
                    List<CabinTile> connectedCabins = findConnectedCabins(shipLayout, row, col, visited);

                    if (connectedCabins.size() > 1) {
                        connectedCabinTiles.addAll(connectedCabins);
                    }

                    // Mark all visited (already done in findConnectedCabins)
                }
            }
        }

        return connectedCabinTiles;
    }

    /**
     * Helper method to perform BFS and find all CabinTiles connected to the starting position.
     * @param shipLayout the layout of the ship.
     * @param startRow the row where the "expansion" starts.
     * @param startCol the column where the "expansion" starts.
     * @param visited a matrix of booleans that represent if a specific element in the ship is already visited or not.
     * @return a list of connected cabins.
     */
    private List<CabinTile> findConnectedCabins(ShipLayout shipLayout, int startRow, int startCol, boolean[][] visited) {
        List<CabinTile> connectedCabins = new ArrayList<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int[] deltaRow = {-1, 0, 1, 0};
        int[] deltaCol = {0, 1, 0, -1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            ShipCell currentCell = shipLayout.getMotherBoard()[row][col];
            Tile currentTile = currentCell.getTile();

            // Add to results if it's a CabinTile
            if (currentTile instanceof CabinTile) {
                connectedCabins.add((CabinTile) currentTile);
            }

            // Check all 4 directions
            for (int d = 0; d < 4; d++) {
                int newRow = row + deltaRow[d];
                int newCol = col + deltaCol[d];

                // Check bounds
                if (newRow < 0 || newRow >= ShipMatrixConfig.ROW || newCol < 0 || newCol >= ShipMatrixConfig.COL) {
                    continue;
                }

                // Skip if already visited
                if (visited[newRow][newCol]) {
                    continue;
                }

                ShipCell adjacentCell = shipLayout.getMotherBoard()[newRow][newCol];

                // Skip if invalid or empty
                if (adjacentCell == null || !adjacentCell.isValid() ||
                        !adjacentCell.isOccupied()) {
                    continue;
                }

                Tile adjacentTile = adjacentCell.getTile();

                // We only want to traverse through CabinTiles
                if (!(adjacentTile instanceof CabinTile)) {
                    continue;
                }

                // Check connector validity
                ConnectorType currentType = currentTile.getConnectors()[d].getType();
                int opposite = (d + 2) % 4;
                ConnectorType adjacentType = adjacentTile.getConnectors()[opposite].getType();

                if (shipLayout.pathValidator(currentType, adjacentType)) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
        return connectedCabins;
    }
}
