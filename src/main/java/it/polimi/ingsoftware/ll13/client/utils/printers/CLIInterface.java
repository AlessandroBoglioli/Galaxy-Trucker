package it.polimi.ingsoftware.ll13.client.utils.printers;

import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a Command Line Interface (CLI) interaction layer.
 * This interface outlines basic functionalities for printing output
 * and modifying the current state of the CLI.
 */
public interface CLIInterface {
    void print(Object[] args);
    void changeState(@NotNull CLIState state);
}
