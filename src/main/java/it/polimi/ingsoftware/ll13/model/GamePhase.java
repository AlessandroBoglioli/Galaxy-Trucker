package it.polimi.ingsoftware.ll13.model;

/**
 * Represents the different phases in a game lifecycle.
 * This enum defines distinct stages that the game can be in at a given time.
 * Each phase serves a specific purpose within the game's overall logic flow.
 *
 * Enumerations:
 * - IDLE: The game is in an inactive or waiting state.
 * - SETUP: The phase where initial configurations or setups are performed.
 * - VALIDATION: Represents a stage where game-related data or configurations are being validated.
 * - CREW_PHASE: A phase specific to crew-related actions or activities within the game.
 * - FLIGHT: The phase representing the flight-related gameplay or actions.
 */
public enum GamePhase {
    IDLE,SETUP, VALIDATION,CREW_PHASE, FLIGHT
}
