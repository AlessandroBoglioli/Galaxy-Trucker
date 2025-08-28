package it.polimi.ingsoftware.ll13.model.ship_board;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class represent the basic unit for creating the player's ship
 * In this class we have the connection to the tile, with the adjacent tiles
 * and a boolean for checking if the cell can contain a tile
 */

public class ShipCell implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    private ShipCell top;
    private ShipCell left;
    private ShipCell right;
    private ShipCell bottom;
    private int row;
    private int col;
    private Tile tile;
    private boolean isValid;

    /**
     * This is a default builder, the connections with the other cells are set to null
     *
     * @param tile connects the specified tile to this cell
     * @param valid represent if the cell can contain a tile
     *
     */

    public ShipCell(Tile tile, boolean valid) {
        this.tile = tile;
        this.isValid = valid;
        this.top = null;
        this.left = null;
        this.right = null;
        this.bottom = null;
    }

    // ---> Getters <---
    public ShipCell getShipCell(){
        return this;
    }

    public ShipCell getTop() {
        return top;
    }

    public ShipCell getLeft() {
        return left;
    }

    public ShipCell getRight() {
        return right;
    }

    public ShipCell getBottom() {
        return bottom;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isValid() {
        return isValid;
    }


    // ---> Setters <---

    public void setTop(ShipCell top) {
        this.top = top;
    }

    public void setLeft(ShipCell left) {
        this.left = left;
    }

    public void setRight(ShipCell right) {
        this.right = right;
    }

    public void setBottom(ShipCell bottom) {
        this.bottom = bottom;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


    // ---> Helping methods <---

    /**
     * Returns true if a tile is placed in this cell.
     *
     * @return true if this cell is occupied, false otherwise.
     */

    public boolean isOccupied(){
        return tile != null;
    }

    /**
     * Clears the tile from the cell.
     */

    public void clearTile(){
        this.tile=null;
    }

}
