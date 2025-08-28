package it.polimi.ingsoftware.ll13.client.utils.ansi;

/**
 * AnsiFont is an enumeration that provides constants representing ANSI
 * font styles used for terminal text formatting.
 *
 * Each constant is associated with a specific ANSI escape sequence
 * that applies a visual style (such as bold, italic, or underline)
 * to terminal output.
 *
 * The getCode() method retrieves the ANSI code string associated with
 * each font constant, which can be used alongside colors and other
 * formatting options for terminal text styling.
 *
 * Available constants:
 * - DEFAULT: No specific font styling (default terminal settings).
 * - UNDERLINE: Applies an underline to the text.
 * - BOLD: Applies bold styling to the text.
 * - CROSSED: Applies a strikethrough effect to the text.
 * - ITALIC: Applies italic styling to the text.
 * - LIGHT: Applies a lighter (dimmed) text styling.
 */
public enum AnsiFont {
    DEFAULT(""),
    UNDERLINE("\u001B[4m"),
    BOLD("\u001B[1m"),
    CROSSED("\u001B[9m"),
    ITALIC("\u001B[3m"),
    LIGHT("\u001B[2m");

    private final String code;

    AnsiFont(String code) {
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
