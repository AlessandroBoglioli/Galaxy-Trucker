package it.polimi.ingsoftware.ll13.model.ship_board.helpers;

import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * this class represents a tile with its coordinates in the grid
 */
public class TileCoordinates implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;
    private final int row;
    private final int col;
    private final Tile tile;

    public TileCoordinates(int row, int col, Tile tile) {
        this.row = row;
        this.col = col;
        this.tile = tile;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TileCoordinates other = (TileCoordinates) o;
        return row == other.row && col == other.col && Objects.equals(tile, other.tile);
    }

}
