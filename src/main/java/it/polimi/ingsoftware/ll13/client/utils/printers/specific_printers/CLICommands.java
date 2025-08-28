package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

/**
 * The CLICommands class provides static utility methods for managing terminal behavior
 * such as cursor position manipulation, screen clearing, and terminal size adjustments
 * using ANSI escape codes. These methods help in creating dynamic Command-Line Interface (CLI)
 * applications with enhanced control over the terminal display and input/output.
 *
 * This class includes functionality for:
 * - Cursor positioning and saving/restoring cursor location.
 * - Terminal screen manipulation (clear, resize, etc.).
 * - Input visibility toggling for secure input handling.
 */
public abstract class CLICommands {
    private static final int defaultWidth = 160;
    private static final int defaultHeight = 60;

    /**
     * Moves the terminal cursor to the home position (top-left corner, coordinate 1,1).
     * This functionality is achieved using an ANSI escape sequence.
     */
    public static void home() {
        System.out.print("\033[H");
        System.out.flush();
    }

    /**
     * Clears the terminal screen by sending ANSI escape sequences.
     * This method erases all content currently displayed in the terminal
     * and resets the cursor position to the top-left corner (1,1).
     * The screen clearing functionality depends on ANSI support in the terminal.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Clears the content of the current line in the terminal.
     *
     * This method erases all text displayed on the current line by using an ANSI escape
     * sequence. The operation is dependent on the terminal's support for ANSI escape codes.
     *
     * After invoking this method, the cursor remains at its current horizontal position.
     */
    public static void clearLine() {
        System.out.print("\033[2K");
        System.out.flush();
    }

    /**
     * Clears the specified number of consecutive lines in the terminal, starting from the current line.
     * For each line, this method erases its content and moves to the next.
     *
     * @param lineCount the number of lines to clear starting from the current cursor position
     */
    public static void clearNextLines(int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            clearLine();
            System.out.println("\n");
        }
    }

    /**
     * Clears the terminal screen starting from the current cursor position
     * to the end of the screen. This operation uses an ANSI escape sequence
     * to erase all characters from the cursor's current location onward.
     *
     * The functionality of this method is dependent on ANSI support in the terminal.
     */
    public static void clearScreenAfterCursor() {
        System.out.print("\033[0J");
        System.out.flush();
    }

    /**
     * Clears the input text displayed in the terminal by overwriting it with spaces
     * and resetting the cursor to the start of the input location.
     *
     * @param input the original string entered by the user, whose length determines
     *              the number of spaces to overwrite
     */
    public static void clearUserInput(String input) {
        CLICommands.restoreCursorPosition();     //Pushing back cursor to the beginning of the input

        for (int i = 0; i < input.length(); i++) System.out.print(" ");     //Overwriting input with spaces

        CLICommands.restoreCursorPosition();     //Pushing back cursor to the beginning of the input
    }


    /**
     * Adjusts the terminal screen size to the specified number of columns and rows.
     * This method sends an ANSI escape sequence to modify the terminal dimensions.
     *
     * @param cols the number of columns (width) for the terminal screen
     * @param rows the number of rows (height) for the terminal screen
     */
    public static void setScreenSize(int cols, int rows) {
        System.out.print("\033[8;" + rows + ";" + cols + "t");
    }

    /**
     * Initializes the terminal window with the specified screen size, clears the screen,
     * sets the cursor to the home position (1,1), and ensures the terminal input visibility
     * is reset.
     *
     * @param width the width of the terminal window (number of columns)
     * @param height the height of the terminal window (number of rows)
     */
    public static void initialize(int width, int height) {
        setScreenSize(width, height);
        resetInvisibleInput();
        home();
        clearScreen();
    }

    /**
     * Initializes the terminal window to the default size, clears the screen,
     * sets the cursor to the home position (1,1), and ensures the terminal
     * input visibility is reset.
     * The default size is determined by the `defaultWidth` and `defaultHeight`
     * fields in the class.
     */
    public static void initialize() {
        initialize(defaultWidth, defaultHeight);
    }

    /**
     * Sets the cursor position in the terminal to the specified column and row.
     * If the column or row values are less than 1, they will be adjusted to 1.
     *
     * @param col the column number to move the cursor to (1-based index)
     * @param row the row number to move the cursor to (1-based index)
     */
    public static void setPosition(int col, int row) {
        if (row < 1) row = 1;
        if (col < 1) col = 1;
        System.out.print("\033[" + row + ";" + col + "H");
    }

    /**
     * Saves the current cursor position in the terminal.
     * This method uses an ANSI escape sequence to store the cursor's location,
     * allowing it to be restored later using the appropriate functionality.
     *
     * The operation is dependent on ANSI escape code support in the terminal.
     */
    public static void saveCursorPosition() {
        System.out.print("\0337");
        System.out.flush();
    }

    /**
     * Restores the terminal cursor to the most recently saved position.
     * This method uses an ANSI escape sequence to move the cursor back
     * to the location stored by the `saveCursorPosition()` method.
     *
     * The functionality depends on ANSI escape code support in the terminal.
     */
    public static void restoreCursorPosition() {
        System.out.print("\0338");
        System.out.flush();
    }

    /**
     * Retrieves the default height of the terminal window.
     *
     * @return the default height value for the terminal window
     */
    public static int getDefaultHeight() {
        return defaultHeight;
    }

    /**
     * Retrieves the default width of the terminal window.
     *
     * @return the default width value for the terminal window
     */
    public static int getDefaultWidth() {
        return defaultWidth;
    }

    /**
     * Sets the terminal input mode to invisible and hides the cursor.
     * This method uses ANSI escape sequences to disable user input visibility
     * and remove the cursor from the terminal screen.
     *
     * The method may be useful in scenarios where sensitive input, such as passwords,
     * should not be visible on the screen. It is dependent on ANSI escape code support
     * within the terminal.
     */
    public static void setInvisibleInput() {
        System.out.print("\033[8m");
        System.out.flush();
        System.out.print("\033[?25l");
        System.out.flush();
    }

    /**
     * Resets the terminal input visibility to its default visible state.
     *
     * This method employs ANSI escape sequences to achieve the following:
     * - Restores the visibility of the terminal input by re-enabling the cursor.
     * - Ensures that hidden or obscured text input is no longer invisible.
     *
     * The method is primarily used to restore normal input visibility after operations
     * that may have hidden input or disabled the cursor visibility using escape sequences.
     *
     * Note: The functionality of this method relies on ANSI escape code support in the terminal.
     */
    public static void resetInvisibleInput() {
        System.out.print("\033[28m");
        System.out.flush();
        System.out.print("\033[?25h");
        System.out.flush();
    }
}
