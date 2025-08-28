package it.polimi.ingsoftware.ll13.model.ship_board.ship_components;

import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.model.general_enumerations.Direction;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.ShipCell;
import it.polimi.ingsoftware.ll13.model.ship_board.exceptions.InvalidCellException;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.ShipMatrixConfig;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.enumerations.ConnectorType;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.*;
import it.polimi.ingsoftware.ll13.server.controller.GameController;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * This class manages the logic to add tiles, remove tiles, and handles the validation part too
 */
public class ShipLayout implements Serializable {
    protected PlayerColors color;
    protected ShipCell[][] motherBoard;
    protected int wasteTiles = 0;
    protected final List<TileCoordinates> invalidCells = new ArrayList<>();

    public ShipLayout(PlayerColors color) {
        this.color = color;
        //placeStartingCabin();
    }

    /**
     * This method is used in the initialization phase of the ship assembly, and it places a StartingCabinTile
     * tile a the center of the ship
     */
    public void placeStartingCabin() {
        int centerRow = ShipMatrixConfig.CENTRAL_ROW;
        int centerCol = ShipMatrixConfig.CENTRAL_COL;
        if (!getMotherBoard()[centerRow][centerCol].isValid()) {
            throw new IllegalStateException("central cell is not valid for the placement");
        }
        Tile startingCabinTile = new StartingCabinTile("", color);
        getMotherBoard()[centerRow][centerCol].setTile(startingCabinTile);
    }

    // ---> Getters <---
    public PlayerColors getColor() {
        return color;
    }

    /**
     * Getter method to retrieve the MotherBoard
     * @return the MotherBoard as a matrix of ShipCell objects
     */
    public ShipCell[][] getMotherBoard() {
        return this.motherBoard;
    }

    /**
     * getter method to retrieve the waste tiles counter in a given context
     * @return the number of waste tiles as an int
     */
    public int getWasteTiles() {
        return wasteTiles;
    }

    /**
     * getter method to retrieve the invalidCells list
     * @return the ArrayList of invalid cells
     */
    public List<TileCoordinates> getInvalidCells() {
        return invalidCells;
    }

    private ShipCell getCentralCell() {
        return motherBoard[ShipMatrixConfig.CENTRAL_ROW][ShipMatrixConfig.CENTRAL_COL];
    }

    /**
     * This method searches the ship's matrix for the specified ship cell and returns the corresponding row and column
     * indices. If the cell is not found, it returns null.
     *
     * @param cell the ShipCell whose coordinates are to be retrieved.
     * @return an array of integers containing the row and column indices of the given cell, or null if the cell is not found.
     */
    private int @Nullable [] getCoordinatesFromCell(ShipCell cell) {
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                if (motherBoard[row][col] == cell) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    /**
     * This method returns the cell above the given row and column in the ship's matrix (motherBoard),
     * or null if the specified cell is in the first row (i.e., there is no cell above it).
     *
     * @param row the row index of the current cell.
     * @param col the column index of the current cell.
     * @return the ShipCell directly above the given cell, or null if there is no cell above it.
     */
    @Contract(pure = true)
    @Nullable
    public ShipCell getTopCell(int row, int col) {
        return (row > 0) ? motherBoard[row - 1][col] : null;
    }

    /**
     * This method returns the cell to the right of the given row and column in the ship's matrix (motherBoard),
     * or null if the specified cell is in the last column (i.e., there is no cell to the right).
     *
     * @param row the row index of the current cell.
     * @param col the column index of the current cell.
     * @return the ShipCell directly to the right of the given cell, or null if there is no cell to the right.
     */
    @Contract(pure = true)
    @Nullable
    public ShipCell getRightCell(int row, int col) {
        return (col < motherBoard[0].length - 1) ? motherBoard[row][col + 1] : null;
    }

    /**
     * This method returns the cell directly below the given row and column in the ship's matrix (motherBoard),
     * or null if the specified cell is in the last row (i.e., there is no cell below).
     *
     * @param row the row index of the current cell.
     * @param col the column index of the current cell.
     * @return the ShipCell directly below the given cell, or null if there is no cell below.
     */
    @Contract(pure = true)
    @Nullable
    public ShipCell getBottomCell(int row, int col) {
        return (row < motherBoard.length - 1) ? motherBoard[row + 1][col] : null;
    }

    /**
     * This method returns the cell directly to the left of the given row and column in the ship's matrix (motherBoard),
     * or null if the specified cell is in the first column (i.e., there is no cell to the left).
     *
     * @param row the row index of the current cell.
     * @param col the column index of the current cell.
     * @return the ShipCell directly to the left of the given cell, or null if there is no cell to the left.
     */
    @Contract(pure = true)
    @Nullable
    public ShipCell getLeftCell(int row, int col) {
        return (col > 0) ? motherBoard[row][col - 1] : null;
    }

    /**
     * This method returns the ship cell located in the specified direction (TOP, RIGHT, BOTTOM, LEFT) from the given
     * row and column, or null if the direction is invalid or the cell does not exist in that direction.
     *
     * @param row the row index of the current cell.
     * @param col the column index of the current cell.
     * @param direction the direction in which to retrieve the adjacent cell.
     * @return the ShipCell in the specified direction, or null if the direction is invalid or the cell does not exist.
     */
    @Nullable
    private ShipCell getCellInDirection(int row, int col, @NotNull Direction direction) {
        return switch (direction) {
            case TOP -> getTopCell(row, col);
            case RIGHT -> getRightCell(row, col);
            case BOTTOM -> getBottomCell(row, col);
            case LEFT -> getLeftCell(row, col);
            default -> null;
        };
    }

    // ---> Setters <---
    public void setColor(PlayerColors color) {
        this.color = color;
    }

    public void setMotherBoard(ShipCell[][] motherBoard) {
        this.motherBoard = motherBoard;
    }

    public void setWasteTiles(int wasteTiles) {
        this.wasteTiles = wasteTiles;
    }


    // --> Other methods <--
    /**
     * This method checks if two adjacent tiles can be considered valid when searching for a way to the StartingCabinTile
     * The rules for valid connections are as follows:
     *     Two SMOOTH connectors cannot connect to each other.
     *     UNIVERSAL connectors can connect to anything except SMOOTH.
     *     DOUBLE connectors can only connect to other DOUBLE connectors.
     *     SINGLE connectors can only connect to other SINGLE connectors.
     *
     * @param current the connector type of the current tile.
     * @param adjacent the connector type of the adjacent tile.
     * @return true if the connection is valid, false otherwise.
     */
    protected boolean pathValidator(ConnectorType current, ConnectorType adjacent) {
        if (current == ConnectorType.SMOOTH && adjacent == ConnectorType.SMOOTH) return false;
        if (current == ConnectorType.UNIVERSAL) return adjacent != ConnectorType.SMOOTH;
        if (adjacent == ConnectorType.UNIVERSAL) return current != ConnectorType.SMOOTH;
        if (current == ConnectorType.DOUBLE) return adjacent == ConnectorType.DOUBLE;
        if (current == ConnectorType.SINGLE) return adjacent == ConnectorType.SINGLE;
        return false;
    }

    /**
     * This method checks if two adjacent tiles can be connected when checking for validity
     * The rules for valid connections are as follows:
     *     Two SMOOTH connectors can connect to each, but they both need to have a valid way to the center or else we get rid of that one.
     *     UNIVERSAL connectors can connect to anything except SMOOTH.
     *     DOUBLE connectors can only connect to other DOUBLE connectors.
     *     SINGLE connectors can only connect to other SINGLE connectors.
     *
     * @param currentCell the connector type of the current tile.
     * @param adjacentCell the connector type of the adjacent tile.
     * @param currentConnector the connector type of the current cell
     * @param adjacentConnector the connector type of the adjacent cell
     * @throws InvalidCellException if the cells passed are null
     * @return true if the connection is valid, false otherwise.
     */
    private boolean connectionValidator(ShipCell currentCell, ShipCell adjacentCell, @NotNull Connector currentConnector, @NotNull Connector adjacentConnector) throws InvalidCellException {
        ConnectorType currentType = currentConnector.getType();
        ConnectorType adjacentType = adjacentConnector.getType();
        if(currentType == ConnectorType.SMOOTH && adjacentType == ConnectorType.SMOOTH){
            int[] currentCoordinates = getCoordinatesFromCell(currentCell);
            int[] adjacentCoordinates = getCoordinatesFromCell(adjacentCell);
            if(currentCoordinates == null || adjacentCoordinates == null){
                throw new IllegalStateException();
            }
            int currentRow = currentCoordinates[0];
            int currentCol = currentCoordinates[1];
            int adjacentRow = adjacentCoordinates[0];
            int adjacentCol = adjacentCoordinates[1];
            if(!(hasValidPathToCenter(currentRow,currentCol)) && !(hasValidPathToCenter(adjacentRow,adjacentCol)) ){
                invalidateCell(currentCell);
                invalidateCell(adjacentCell);
                return false;
            }
            if(!(hasValidPathToCenter(adjacentRow,adjacentCol)) && (hasValidPathToCenter(currentRow,currentCol)) ){
                invalidateCell(adjacentCell);
                return false;
            }
            if((hasValidPathToCenter(adjacentRow,adjacentCol)) && !(hasValidPathToCenter(currentRow,currentCol)) ){
                invalidateCell(currentCell);
                return false;
            }
            if((hasValidPathToCenter(adjacentRow,adjacentCol)) && (hasValidPathToCenter(currentRow,currentCol)) ){
                return true;
            }

        }
        if(currentType == ConnectorType.UNIVERSAL && adjacentType != ConnectorType.SMOOTH){
            return true;
        }
        if(adjacentType == ConnectorType.UNIVERSAL && currentType != ConnectorType.SMOOTH){
            return true;
        }
        if(currentType == ConnectorType.SINGLE && adjacentType == ConnectorType.SINGLE){
            return true;
        }
        if(currentType == ConnectorType.DOUBLE && adjacentType == ConnectorType.DOUBLE){
            return true;
        }
        if(currentCell.getTile() instanceof StartingCabinTile ){
            invalidateCell(adjacentCell);
            return false;
        }
        if(adjacentCell.getTile() instanceof StartingCabinTile ){
            invalidateCell(currentCell);
            return false;
        }
        invalidateCell(currentCell);
        invalidateCell(adjacentCell);
        return false;

    }

    /**
     * Checks if there is a valid path from a given starting cell to the center of the ship.
     * A valid path means that the cells are connected via valid connections and each cell
     * is occupied and valid according to the ship's design rules.
     * <p>
     * The method performs a breadth-first search (BFS) starting from the given coordinates.
     * It checks if there is a valid path to the center (CENTRAL_ROW, CENTRAL_COL) following the
     * connections between adjacent tiles (based on the connector types).
     *
     * @param startRow the row index of the starting cell.
     * @param startCol the column index of the starting cell.
     * @return true if there is a valid path to the center; false otherwise.
     */
    public boolean hasValidPathToCenter(int startRow, int startCol) {
        int centerRow = ShipMatrixConfig.CENTRAL_ROW;
        int centerCol = ShipMatrixConfig.CENTRAL_COL;
        if (startRow == centerRow && startCol == centerCol) {
            return true;
        }
        boolean[][] visited = new boolean[ShipMatrixConfig.ROW][ShipMatrixConfig.COL];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        int[] deltaRow = {-1, 0, 1, 0};
        int[] deltaCol = {0, 1, 0, -1};
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            for (int d = 0; d < 4; d++) {
                int newRow = row + deltaRow[d];
                int newCol = col + deltaCol[d];
                if (newRow < 0 || newRow >= ShipMatrixConfig.ROW || newCol < 0 || newCol >= ShipMatrixConfig.COL) {
                    continue; //next
                }
                if (visited[newRow][newCol]) {
                    continue;
                }
                ShipCell currentCell = motherBoard[row][col];
                ShipCell adjacentCell = motherBoard[newRow][newCol];
                if (adjacentCell == null || !adjacentCell.isOccupied()) {
                    continue;
                }
                Tile currentTile = currentCell.getTile();
                Tile adjacentTile = adjacentCell.getTile();
                if (currentTile == null || adjacentTile == null) {
                    continue;
                }
                ConnectorType currentType = currentTile.getConnectors()[d].getType();
                int opposite = (d + 2) % 4;
                ConnectorType adjacentType = adjacentTile.getConnectors()[opposite].getType();
                if (!pathValidator(currentType, adjacentType)) {
                    continue;
                }
                visited[newRow][newCol] = true;
                queue.add(new int[]{newRow, newCol});
                if (newRow == centerRow && newCol == centerCol) {
                    return true;
                }


            }
        }
        return false;

    }

    /**
     * Invalidates a given ShipCell and adds it to the invalidCells list.
     * If the cell is already invalid or empty, no action is taken.
     *
     * @param shipCell the ShipCell to invalidate.
     * @throws InvalidCellException if the shipCell is empty.
     */
    public void invalidateCell(@NotNull ShipCell shipCell) throws InvalidCellException {
        if (!shipCell.isOccupied()) {
            throw new InvalidCellException("Cannot invalidate an empty cell at this stage");
        }
        if (!shipCell.isValid()) {
            return;  //if it is already invalid, it has already been checked and added to the invalidCells List
        }
        int[] coordinates = getCoordinatesFromCell(shipCell);
        if (coordinates == null) {
            return;
        }
        int row = coordinates[0];
        int col = coordinates[1];
        if(isTempZone(row,col)){
            return;
        }
        motherBoard[row][col].setValid(false);
        invalidCells.add(new TileCoordinates(row, col, shipCell.getTile()));//this List will be sent to the client after the checking
    }

    //FIRST PHASE: TILE PLACING WITHOUT CHECKING FOR VALIDATION
    /**
     * Checks if a cell is valid cell for placement
     *
     * @param row The row index.
     * @param col The column index.
     * @return true if the cell is valid, false otherwise.
     */
    private boolean isValidCell(int row, int col) {
        return !motherBoard[row][col].isOccupied() && motherBoard[row][col].isValid();
    }

    /**
     * Checks if the coordinates passed belong to one of the temp zone cells
     * @param row The row index
     * @param col The column index
     * @return true if the cell is a temporary zone cell
     */
    private boolean isTempZone(int row, int col) {
        return (row == 0 && (col == 5 || col == 6));
    }

    /**
     * This method checks if the coordinates of the cell in which the user wants to place a tile has atleast
     * one adjacent tile connected to it
     *
     * @param row The row index
     * @param col The column index
     * @return true if the cell has adjacent,false otherwise
     */
    private boolean hasAdjacentTile(int row, int col) {
        ShipCell[] adjacent = {
                getTopCell(row, col),
                getRightCell(row, col),
                getBottomCell(row, col),
                getLeftCell(row, col)
        };

        int[][] deltas = {
                {-1, 0}, // top
                {0, 1},  // right
                {1, 0},  // bottom
                {0, -1}  // left
        };

        for (int i = 0; i < adjacent.length; i++) {
            ShipCell a = adjacent[i];
            int adjRow = row + deltas[i][0];
            int adjCol = col + deltas[i][1];
            if (a != null && a.getTile() != null && !isTempZone(adjRow, adjCol)) {
                return true;
            }
        }
        return false;
    }


    /**
     * The method calls the setter for insert a specific tile into the cell of the ship
     *
     * @param tile is the tile to insert
     * @param row  represent the first index of the matrix
     * @param col  represent the second index of the matrix
     * @return true if tile has been added, false otherwise
     */
    public boolean addTile(Tile tile, int row, int col) {
        if (isTempZone(row, col) && !motherBoard[row][col].isOccupied()) {
            motherBoard[row][col].setTile(tile);
            return true;
        }
        if (!isValidCell(row, col)) {
            return false;
        }
        if (!hasAdjacentTile(row, col)) {
            return false;
        }
        ShipCell cell = motherBoard[row][col];
        cell.setTile(tile);
        return true;
    }

    /**
     * Method used to retrieve the tile present in the temp zone and send it back to who asked for it
     *
     * @param tempRow The Row of the temp cell
     * @param tempCol The Col of the temp cell
     * @return the Tile to be returned
     */
    public Tile drawTileFromTempZone(int tempRow, int tempCol) {
        if (!isTempZone(tempRow, tempCol)) {
            return null;
        }
        ShipCell tempZoneCell = motherBoard[tempRow][tempCol];
        if (tempZoneCell.isOccupied()) {
            Tile temp = tempZoneCell.getTile();
            tempZoneCell.setTile(null);
            return temp;
        }
        return null;
    }

    //SECOND PHASE: CHECK IF THE SHIP IS ALL RIGHT
    /**
     * This method checks if a cannon tile's target cell is empty or invalid. A cannon should not have a tile in the
     * direction it is pointing to. If the target cell in the direction of the cannon is either empty or invalid,
     * the cannon is considered valid.
     *
     * @param cannonTile the cannon tile to check.
     * @param row the row of the cannon tile in the ship's grid.
     * @param col the column of the cannon tile in the ship's grid.
     * @return true if the target cell is empty or invalid (i.e., the cannon is valid), false otherwise.
     */
    public boolean checkCannon(@NotNull CannonBaseTile cannonTile, int row, int col){
        Direction cannonDirection = cannonTile.getCannonDirection();
        ShipCell targetCell = switch (cannonDirection) {
            case TOP -> getTopCell(row, col);
            case RIGHT -> getRightCell(row, col);
            case BOTTOM -> getBottomCell(row, col);
            case LEFT -> getLeftCell(row, col);
        };
        return targetCell == null || !targetCell.isOccupied();
    }


    /**
     * This method checks all the connections of the tiles in the ship and validates if they follow the correct
     * connection rules. It checks each tile in the ship and its adjacent tiles to ensure that their connections are
     * valid based on their connector types (i.e., SMOOTH, UNIVERSAL, SINGLE, DOUBLE).
     * If any invalid connection is found, the method invalidates the corresponding cells.
     *
     * @return true if all the connections are valid, false if there is at least one invalid connection.
     * @throws InvalidCellException if there is an issue with a cell during validation.
     */
    public boolean checkConnections() throws InvalidCellException {
        boolean isValid = true;
        for (int row = 0; row < ShipMatrixConfig.ROW; row++){
            for(int col = 0; col < ShipMatrixConfig.COL; col++){
                ShipCell currentCell = motherBoard[row][col];
                if (currentCell == null || !currentCell.isValid() || currentCell.getTile() == null) {
                    continue;
                }
                ShipCell[] adjacentCells = {getTopCell(row, col), getRightCell(row, col), getBottomCell(row, col), getLeftCell(row, col)};
                Connector[] currentConnectors = currentCell.getTile().getConnectors();
                for(int i=0; i< adjacentCells.length; i++){

                    Tile adjacentTile = null;
                    if (adjacentCells[i] != null) {
                        adjacentTile = adjacentCells[i].getTile();
                    }
                    if(adjacentTile == null){
                        continue;
                    }
                    Connector[] adjacentConnectors = adjacentTile.getConnectors();
                    if (!connectionValidator(currentCell, adjacentCells[i], currentConnectors[i], adjacentConnectors[(i + 2) % 4])) {
                        isValid = false;
                    }

                }

            }
        }
        return isValid;
    }

    /**
     * This method checks if the ship is valid by verifying the connections between tiles, and ensuring that the
     * motor and cannon rules are respected. If any invalid cells are found (whether due to improper connections or
     * invalid motors/cannons), those cells are marked as invalid and the method will return false.
     * <p>
     *     note that cannonTile is invalid if it is not pointing to the BOTTOM of the ship,or if it has a tile in the
     *     cell towards which he is pointing
     * </p>
     * @return true if the ship is valid, false otherwise.
     * @throws InvalidCellException if an issue occurs during the validation of a cell.
     */
    public boolean isValidShip() throws InvalidCellException {
        invalidCells.clear();
        for (int row = 0; row < ShipMatrixConfig.ROW; row++) {
            for (int col = 0; col < ShipMatrixConfig.COL; col++) {
                ShipCell cell = motherBoard[row][col];
                if (cell != null && cell.getTile() != null) {
                    cell.setValid(true);
                }
            }
        }
        boolean isValid = true;
        checkConnections();
        for(int row = 0; row < ShipMatrixConfig.ROW; row++){
            for(int col = 0; col < ShipMatrixConfig.COL; col++){
                ShipCell currentCell = motherBoard[row][col];
                if (currentCell == null || !currentCell.isValid() || currentCell.getTile() == null) {
                    continue;
                }
                if(currentCell.getTile() instanceof MotorBaseTile){
                    MotorBaseTile motorTile = (MotorBaseTile) currentCell.getTile();
                    if(motorTile.getMotorDirection() != Direction.BOTTOM){ //if direction is not bottom engine is invalid
                        invalidateCell(currentCell);
                    }
                    ShipCell belowCell = getBottomCell(row,col);
                    if(belowCell != null && belowCell.isOccupied()){
                        invalidateCell(belowCell);
                        invalidateCell(currentCell);
                    }

                }
                if(currentCell.getTile() instanceof CannonBaseTile cannonTile){
                    boolean cannonCheck = checkCannon(cannonTile,row,col);
                    if(!cannonCheck){
                        Direction cannonDirection = cannonTile.getCannonDirection();
                        ShipCell targetCell = getCellInDirection(row,col,cannonDirection);
                        if(targetCell == null){
                            throw new RuntimeException();
                        }
                        invalidateCell(targetCell);
                        invalidateCell(currentCell);
                    }

                }
            }


        }
        if(!invalidCells.isEmpty()){
            isValid = false;
        }
        return isValid;
    }

    // Method that handle the further elimination of tiles
    private void propagateElimination(int row, int col) {
        int[] tempRow = {-1, 0, 1, 0};
        int[] tempCol = {0, 1, 0, -1};
        for (int d = 0; d < 4; d++) {
            int newRow = row + tempRow[d];
            int newCol = col + tempCol[d];
            if (newRow < 0 || newRow >= ShipMatrixConfig.ROW || newCol < 0 || newCol >= ShipMatrixConfig.COL) {
                continue;
            }
            ShipCell adjacent = motherBoard[newRow][newCol];
            if (adjacent != null && adjacent.isOccupied() ){
                if (!hasValidPathToCenter(newRow, newCol)) {
                    eliminateTile(newRow, newCol);
                }
            }

        }
    }

    /**
     * Eliminates the tile at the specified coordinates, invalidates the corresponding cell,
     * and propagates the invalidation to adjacent cells if necessary.
     *
     * @param row The row index of the tile to be eliminated.
     * @param col The column index of the tile to be eliminated.
     */
    public void eliminateTile (int row, int col){


        ShipCell shipCell = motherBoard[row][col];
        if (shipCell == null || !shipCell.isOccupied()) {
            return;
        }
        Iterator<TileCoordinates> iterator = invalidCells.iterator();
        while (iterator.hasNext()) {
            TileCoordinates tileCoordinates = iterator.next();
            if (tileCoordinates.getRow() == row && tileCoordinates.getCol() == col) {
                iterator.remove();
                break;
            }
        }
        shipCell.setTile(null);
        shipCell.setValid(false);
        wasteTiles++;
        propagateElimination(row, col);

    }
}
