package it.polimi.ingsoftware.ll13.client.utils.printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiFont;
import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiSpecial;
import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiString;
import it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers.CLICommands;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CLIString {
    private final String string;
    private AnsiColor color;
    private AnsiFont font;
    private final int[] position = new int[2];
    private boolean centered = false;
    private boolean visible = false;
    private final int realLength;

    // --> Constructors <--
    public CLIString(@NotNull String string, @NotNull AnsiColor color, @NotNull AnsiFont font, int col, int row, int widthLimit) {
        this.string = string.length() > widthLimit ? string.substring(0, widthLimit - 4) + "..." + AnsiSpecial.RESET.getCode() : string;
        this.realLength = Math.min(string.length(), widthLimit);

        setColor(color);
        setFont(font);

        setPosition(col, row);

        setCentered(false);
        setVisible(false);
    }

    public CLIString(String string, AnsiColor color, AnsiFont font, int col, int row) {
        this.string = string;
        this.realLength = string.length();

        setColor(color);
        setFont(font);

        setPosition(col, row);
    }

    public CLIString(String string, AnsiColor color, int col, int row, int widthLimit) {
        this(string, color, AnsiFont.DEFAULT, col, row, widthLimit);
    }

    public CLIString(String string, AnsiFont font, int col, int row, int widthLimit) {
        this(string, AnsiColor.DEFAULT, font, col, row, widthLimit);
    }

    public CLIString(String string, AnsiColor color, int col, int row) {
        this(string, color, AnsiFont.DEFAULT, col, row);
    }

    public CLIString(String string, AnsiFont font, int col, int row) {
        this(string, AnsiColor.DEFAULT, font, col, row);
    }

    public CLIString(String string, int col, int row) {
        this(string, AnsiColor.DEFAULT, AnsiFont.DEFAULT, col, row);
    }

    // --> Getters <--
    public String getString() {
        return string;
    }

    public AnsiColor getColor() {
        return color;
    }

    public AnsiFont getFont() {
        return font;
    }

    public int[] getPosition() {
        return position;
    }

    public boolean isCentered() {
        return centered;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getRealLength() {
        return realLength;
    }

    // --> Setters <--
    public void setColor(AnsiColor color) {
        this.color = color;
    }

    public void setFont(AnsiFont font) {
        this.font = font;
    }

    public void setPosition(int col, int row) {
        this.position[0] = col;
        this.position[1] = row;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // --> Other methods <--
    /**
     * This method is used to replace a string with " " chars in order to
     * effectively delete it on the CLI.
     * It sets the isVisible flag to false after
     */
    public void deleteString() {
        CLICommands.setPosition(getPosition()[0], getPosition()[1]);
        int row = getPosition()[1];
        Arrays.stream(this.getString().split("\n"))
                .forEach(
                        line -> {
                            int lineLength = line.length();

                            if(line.contains(AnsiSpecial.RESET.getCode())) lineLength -= AnsiSpecial.RESET.getCode().length();
                            if(line.contains(getFont().getCode())) lineLength -= getFont().getCode().length();
                            if(line.contains(getColor().getCode())) lineLength -= getColor().getCode().length();

                            System.out.print(" ".repeat(lineLength));
                            CLICommands.setPosition(getPosition()[0], ++getPosition()[1]);
                        }
                );
        getPosition()[1] = row;
        setVisible(false);

        CLICommands.restoreCursorPosition(); //Restoring cursor position to saved position before deletion
    }

    /**
     * Updates the position of the CLIString object to the specified column and row values.
     * If the object is currently visible, it will be removed from its current position
     * and reprinted at the new position. If the object is not visible, only its position
     * properties will be updated.
     *
     * @param col the new column position
     * @param row the new row position
     */
    public void reposition(int col, int row) {
        if (isVisible()) {
            deleteString();
            getPosition()[0] = col;
            getPosition()[1] = row;
            print();
        } else {
            getPosition()[0] = col;
            getPosition()[1] = row;
        }
    }

    /**
     * Prints the current CLIString object to the command-line interface at its
     * specified position. The text content is rendered line by line, adhering
     * to the object's color and font settings.
     *
     * Each line of the string is printed at the current vertical position (row).
     * After printing a line, the row position is incremented for the next line.
     * Once all lines have been printed, the initial row position is restored.
     *
     * This method sets the `visible` property of the object to true after the
     * text is rendered.
     *
     * Dependencies:
     * - The current position (`position` property) determines the starting
     *   point (column and row) for printing.
     * - Uses CLICommands.setPosition() to set the cursor position.
     * - Utilizes AnsiString.print() to render the text with specific color
     *   and font attributes.
     */
    public void print() {
        CLICommands.setPosition(getPosition()[0], getPosition()[1]);
        int row = getPosition()[1];
        Arrays.stream(getString().split("\n")).forEach(
                line -> {
                    CLICommands.setPosition(getPosition()[0], getPosition()[1]);
                    AnsiString.print(line, this.color, getFont());
                    getPosition()[1]++;
                }
        );
        getPosition()[1] = row;
        setVisible(true);
    }

    /**
     * Centers the text representation of the current CLIString object
     * horizontally within the default width of the command-line interface.
     * The text is aligned based on the maximum line length within the given string.
     *
     * This method adjusts the horizontal position of the string and updates
     * its visibility and centered state. Once repositioned, the string is
     * immediately printed to the console.
     *
     * Effects:
     * - The horizontal position of the string is modified to achieve centering.
     * - The text is printed to the command-line interface.
     * - Sets the `centered` and `visible` properties of the object to true.
     */
    public void centerPrint() {
        reposition((CLICommands.getDefaultWidth() - Arrays.stream(getString().split("\n"))
                        .mapToInt(String::length)
                        .max().orElse(0)) / 2
                , getPosition()[1]);

        print();
        setCentered(true);
        setVisible(true);
    }

    /**
     * Replaces the content and position of an old CLIString with a new CLIString on the command-line interface.
     *
     * The method performs the following steps:
     * - Deletes the old CLIString, effectively removing it from the CLI.
     * - Updates the new CLIString's position to match the position of the old CLIString.
     * - Prints the new CLIString, either centered or aligned depending on the alignment of the old CLIString.
     *
     * @param oldString the CLIString object to be replaced. It is deleted and its visibility is set to false.
     * @param newString the CLIString object that replaces the old one. It inherits the position from the old string and is printed accordingly.
     */
    public static void replace(@NotNull CLIString oldString, @NotNull CLIString newString) {
        oldString.deleteString();

        newString.reposition(oldString.getPosition()[0], oldString.getPosition()[1]);

        if (oldString.isCentered()) newString.centerPrint();
        else newString.print();

    }
}
