package it.polimi.ingsoftware.ll13.client.utils.ansi;

/**
 * AnsiString is a utility class that provides methods for printing strings
 * to the terminal with ANSI color and font formatting.
 *
 * The class contains overloaded static methods that allow combining various
 * styles and colors to enhance the appearance of terminal output. This can
 * improve readability, highlight important information, or add aesthetic
 * styling to command-line interfaces.
 *
 * Methods:
 * - print(String string, AnsiColor color, AnsiFont format): Prints a string
 *   with the specified ANSI color and font style applied.
 * - print(String string, AnsiColor color): Prints a string with the
 *   specified ANSI color and default font style.
 * - print(String string, AnsiFont format): Prints a string with the
 *   specified ANSI font style and default color.
 *
 * The ANSI formatting codes are applied using the color and font enumeration
 * classes (AnsiColor and AnsiFont) and are reset after each print operation
 * to ensure subsequent terminal output remains unaffected.
 */
public class AnsiString {
    /**
     * Prints a string to the terminal with the specified ANSI color and font style.
     * The method applies the given color and font formatting to the string,
     * ensuring that the output is visually styled as per the provided parameters.
     *
     * @param string the text to be printed to the terminal
     * @param color  the {@link AnsiColor} to apply to the text
     * @param format the {@link AnsiFont} style to apply to the text
     */
    public static void print(String string, AnsiColor color, AnsiFont format) {
        System.out.print(AnsiSpecial.RESET.getCode() +
                color.getCode() +
                format.getCode() +
                string +
                AnsiSpecial.RESET.getCode()
        );
    }

    /**
     * Prints*/
    public static void print(String string, AnsiColor color) {
        print(string, color, AnsiFont.DEFAULT);
    }

    /**
     * Prints a string to the terminal with the specified ANSI font style.
     * The method applies the given font formatting to the string, using the default ANSI color.
     *
     * @param string the text to be printed to the terminal
     * @param format the {@link AnsiFont} style to apply to the text
     */
    public static void print(String string, AnsiFont format) {
        print(string, AnsiColor.DEFAULT, format);
    }
}
