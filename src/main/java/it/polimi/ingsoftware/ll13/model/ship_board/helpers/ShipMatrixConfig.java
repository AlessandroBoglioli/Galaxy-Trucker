package it.polimi.ingsoftware.ll13.model.ship_board.helpers;

/**
 * This class defines the structure of the matrix for the ships used in the game (try level and level2)
 */
public class ShipMatrixConfig {

    public static final int ROW = 5;
    public static final int COL = 7;

    public static final boolean[][] levelTwoPattern = {
        { false, false, true, false, true, false, false },
        { false, true, true, true, true, true, false },
        { true, true, true, true, true, true, true },
        { true, true, true, true, true, true, true },
        { true, true, true, false, true, true, true }
    };

    public static final boolean[][] tryLevelPattern = {
        { false, false, false, true, false, false, false },
        { false, false, true, true, true, false, false },
        { false, true, true,  true,  true, true, false },
        { false, true, true, true, true, true, false },
        { false, true, true, false, true, true, false }
    };

    public static final int CENTRAL_ROW = 2;
    public static final int CENTRAL_COL = 3;
}
