package it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private int row;
    private int col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // --> GETTERS <--
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    // --> SETTERS <--
    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
