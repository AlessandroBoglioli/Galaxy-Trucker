package it.polimi.ingsoftware.ll13.client.view.cli;

import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;

public class CliView {
    private CLIInterface cli_interface;
    private static CliView instance;

    private CliView() {
        // Private constructor for Singleton
    }

    /**
     * Sets the current page of the CLI view.
     *
     * @param cli_interface the CLI interface instance to be set as the current page
     */
    public void setCliInterface(CLIInterface cli_interface) {
        this.cli_interface = cli_interface;
    }

    /**
     * Updates the state of the current page in the Command Line Interface (CLI).
     *
     * @param state the new state to be applied to the current CLI page
     */
    public void updatePageState(CLIState state) {
        cli_interface.changeState(state);
    }

    /**
     * Displays the current page of the Command Line Interface (CLI) with the provided arguments.
     *
     * @param args an array of objects containing the arguments to be displayed on the current page
     */
    public void displayPage(Object[] args) {
        cli_interface.print(args);
    }

    public CLIInterface getCliInterface() {
        return this.cli_interface;
    }

    /**
     * Provides the Singleton instance of the {@code CliView} class.
     * Ensures that only one instance of the {@code CliView} exists throughout
     * the application lifecycle.
     *
     * @return the singleton instance of the {@code CliView}
     */
    public static CliView getInstance() {
        if (instance == null) {
            instance = new CliView();
        }
        return instance;
    }
}
