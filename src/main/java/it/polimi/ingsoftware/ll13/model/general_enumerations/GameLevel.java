package it.polimi.ingsoftware.ll13.model.general_enumerations;

/**
 * Represents the levels within the game.
 *
 * The GameLevel enumeration defines distinct stages or levels
 * that a player may encounter during gameplay. Each constant
 * corresponds to a specific game level.
 *
 * The fromString method allows for creating an instance of
 * GameLevel based on a given string representation of a level.
 */
public enum GameLevel {
    TRY_LEVEL,
    LEVEL_2;
    public static GameLevel fromString(String level) {
        return switch (level) {
            case "TRY LEVEL" -> TRY_LEVEL;
            case "LEVEL 2"-> LEVEL_2;
            default -> throw new IllegalArgumentException("Unknown game level: " + level);
        };
    }
}
