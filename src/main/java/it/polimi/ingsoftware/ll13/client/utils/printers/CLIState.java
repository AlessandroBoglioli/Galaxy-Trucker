package it.polimi.ingsoftware.ll13.client.utils.printers;

/**
 * Represents a state within the Command Line Interface (CLI) layer.
 * This interface defines a behavior for applying state-specific logic
 * with the provided arguments.
 * Implementing classes are meant to encapsulate the logic for a particular
 * state and interact with the CLI when transitioning, retaining, or performing
 * actions related to this state.
 */
public interface CLIState {
    void apply(Object[] args);
}
