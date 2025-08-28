package it.polimi.ingsoftware.ll13.client.utils.ansi;

/**
 * AnsiColor is an enumeration that provides constants representing ANSI
 * color codes used for terminal text formatting.
 *
 * Each constant corresponds to a specific ANSI escape sequence associated
 * with a color. These colors can be used to apply styling to terminal
 * output, enhancing readability or aesthetic appeal.
 *
 * The getCode() method allows retrieval of the ANSI code string associated
 * with each color constant, which can then be applied to terminal output
 * using the appropriate formatting logic.
 *
 * Constants:
 * - DEFAULT: No specific color (default terminal settings).
 * - RED: ANSI code for red text.
 * - GREEN: ANSI code for green text.
 * - BLUE: ANSI code for blue text.
 * - PURPLE: ANSI code for purple text.
 * - CYAN: ANSI code for cyan text.
 * - WHITE: ANSI code for white text.
 * - YELLOW: ANSI code for yellow text.
 * - GREY: ANSI code for grey text.
 */
public enum AnsiColor {
    DEFAULT(""),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    YELLOW("\u001B[33m"),
    GREY("\u001B[90m");

    private final String code;

    AnsiColor(String code) {
        this.code = code;
    }

    /**
     * Retrieves the ANSI code string associated with the current instance.
     *
     * @return a String representing the ANSI escape sequence or formatting code
     *         used for terminal text styling or formatting.
     */
    public String getCode() {
        return code;
    }
}
