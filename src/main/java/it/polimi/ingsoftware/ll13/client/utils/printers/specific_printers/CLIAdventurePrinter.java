package it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers;

import it.polimi.ingsoftware.ll13.client.utils.ansi.AnsiColor;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIInterface;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIState;
import it.polimi.ingsoftware.ll13.client.utils.printers.CLIString;
import it.polimi.ingsoftware.ll13.client.utils.printers.helper_printers.*;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CLIAdventurePrinter implements CLIInterface {
    private static final CLIString adventurePhase = new CLIString(
            ">> Latch your seatbelt! We are starting!\n",
            AnsiColor.YELLOW, 0, 20);
    private static final CLIString drawCard = new CLIString(
            ">> Write [draw] to draw a card:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString invalidDrawAction = new CLIString(
            ">> Invalid input!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString waitingFinishEffect = new CLIString(
            "Waiting to finish the effect of the card!\n",
            AnsiColor.YELLOW, 0, 20);

    // Ship
    private static CLIString ship = null;

    // Generic card
    private static CLIString cardDrew = null;
    private static final int ADVENTURE_CARD_START_ROW = 25;
    private static final int ADVENTURE_CARD_START_COL = 5;
    private static final int FIXED_PROMPT_INPUT_LINE_ROW = 21;
    private static final int FIXED_PROMPT_COL = 0;

    // Cards methods...
    // 1. abandoned ship Card
    private static final CLIString abandonedShipChoicePrompt = new CLIString(
            ">> Investigate the Abandoned Ship? (YES/NO):\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString abandonedShipChoiceError = new CLIString(
            ">> Wrong choice. Please select [yes] or [no]!\n",
            AnsiColor.RED, 0, 20);

    // 2. Abandoned Station Card
    private static final CLIString abandonedStationChoicePrompt = new CLIString(
            ">> Explore the Abandoned Station? (YES/NO):\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString abandonedStationChoiceError = new CLIString(
            ">> Wrong choice. Please select [yes] or [no]!\n",
            AnsiColor.RED, 0, 20);

    // 3. Epidemic Card
    private static final CLIString epidemicChoicePrompt = new CLIString(
            ">> The epidemic takes its course. Press [Enter] to continue...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString epidemicChoiceError = new CLIString(
            ">> Please, press [Enter] to continue...\n",
            AnsiColor.RED, 0, 20);

    // 4. Meteor shower card
    private static final CLIString meteorShowerChoicePrompt = new CLIString(
            ">> Incoming meteors! Select [row]:[col] of cannon/shield you want to turn on...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString meteorShowerChoiceError = new CLIString(
            ">> Wrong choice. Please insert valid row and column!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString meteorShowerWaiting = new CLIString(
            ">> Incoming meteors! Please wait...\n",
            AnsiColor.YELLOW, 0, 20);
    private static CLIString meteorDirection;


    // 5. Open space card
    private static final CLIString openSpaceChoicePrompt = new CLIString(
            ">> Engage thrusters to move forward? Select [row]:[col]-... of thrusters you want to turn on...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString openSpaceChoiceError = new CLIString(
            ">> Wrong choice. Please insert valid rows and columns!\n",
            AnsiColor.RED, 0, 20);

    // 6. Pirates card
    private static final CLIString pirateChoicePrompt = new CLIString(
            ">> Fight the Pirates? (YES/NO):\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString pirateChoiceError = new CLIString(
            ">> There is a problem in the choice you made!",
            AnsiColor.RED, 0, 20);
    private static final CLIString pirateActivateShieldPrompt = new CLIString(
            ">> Choose the [row]:[col] of the shield you want to activate:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString selectFirePowerPrompt = new CLIString(
            ">> Choose the [row]:[col]-[row]:[col] of the double cannons you want to activate:\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString wrongCoordinates = new CLIString(
            ">> Wrong choice of the coordinates!\n",
            AnsiColor.RED, 0, 20);
    private static CLIString pirateFireShotNumber;

    // 7. Planets card
    private static final CLIString planetsChoicePrompt = new CLIString(
            ">> Choose planet # to land (e.g., 1, 2) or 0 to skip exploration:\n>> ", AnsiColor.DEFAULT, 0, 21);
    private static final CLIString planetsChoiceError = new CLIString(
            ">> Wrong choice. Please insert valid rows and columns!\n",
            AnsiColor.RED, 0, 20);

    // 8. Slavers card
    private static final CLIString slaversChoicePrompt = new CLIString(
            ">> Confront the Slavers? (YES/NO):\n>> ", AnsiColor.DEFAULT, 0, 21);
    private static final CLIString slaversChoiceError = new CLIString(
            ">> Wrong choice. Please select [yes] or [no]!\n",
            AnsiColor.RED, 0, 21);

    // 9. Smugglers card
    private static final CLIString smugglersChoicePrompt = new CLIString(
            ">> Engage the Smugglers? (Fight/Surrender - YES/NO to fight):\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString smugglersChoiceError = new CLIString(
            ">> Wrong choice. Please select [yes] or [no]!\n",
            AnsiColor.RED, 0, 21);

    // 10. Stellar space dust card
    private static final CLIString stellarSpaceDustPrompt = new CLIString(
            ">> Your ship is slowed by the dust. Press [Enter] to continue...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString stellarSpaceDustError = new CLIString(
            ">> Please, press [Enter] to continue...\n",
            AnsiColor.RED, 0, 21);

    // 11. War zone card
    private static final CLIString warZonePrompt = new CLIString(
            ">> Entering War Zone! Press [Enter] to resolve penalties...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString warZoneError = new CLIString(
            ">> Please insert the correct data!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString lowestCrewMembers = new CLIString(
            ">> You have the lowest crew members! Press [Enter] to continue...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString lowestCrewMembersError = new CLIString(
            ">> You have selected a wrong option!\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString selectAShieldPrompt = new CLIString(
            ">> Select a shield to activate ([row]:[col]):\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString selectAShieldError = new CLIString(
            ">> Wrong shield selection.\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString warZoneEngineChoicePrompt = new CLIString(
            ">> Engage thrusters to move forward? Select [row]:[col]-... of thrusters you want to turn on...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString warZoneEngineChoiceError = new CLIString(
            ">> Wrong engine choice.\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString warZoneFirePowerChoicePrompt = new CLIString(
            ">> Fire cannons are ready to be activated! Select [row]:[col]-... of double cannons you want to turn on...\n>> ",
            AnsiColor.DEFAULT, 0, 21);
    private static final CLIString warZoneFirePowerChoiceError = new CLIString(
            ">> Wrong fire power choice.\n",
            AnsiColor.RED, 0, 20);
    private static final CLIString warZoneFireWaitingOtherPlayers = new CLIString(
            ">> Waiting other players!\n",
            AnsiColor.YELLOW, 0, 20);

    private static CLIString fireShotDirection;

    // Finish game
    private static CLIString finalResult;
    private static CLIString endGameDisplay;

    private CLIState state = new Default();

    @Override
    public void changeState(@NotNull CLIState state) {
        this.state = state;
    }

    @Override
    public void print(Object[] args) {
        state.apply(args);
    }

    // Helper methods
    private static void prepareStateTransition(String input) {
        CLICommands.restoreCursorPosition();
        CLICommands.clearUserInput(input);
    }

    private static void clearPreviousPrompt() {
        if(cardDrew != null && cardDrew.isVisible()) { cardDrew.deleteString(); }
        if(ship != null && ship.isVisible()) { ship.deleteString(); }
        if(drawCard.isVisible()) { drawCard.deleteString(); }
        if(abandonedShipChoicePrompt.isVisible()) { abandonedShipChoicePrompt.deleteString(); }
        if(abandonedStationChoicePrompt.isVisible()) { abandonedStationChoicePrompt.deleteString(); }
        if(epidemicChoicePrompt.isVisible()){ epidemicChoicePrompt.deleteString(); }
        if(meteorShowerChoicePrompt.isVisible()){ meteorShowerChoicePrompt.deleteString(); }
        if(meteorDirection != null && meteorDirection.isVisible()) { meteorDirection.deleteString(); }
        if(openSpaceChoicePrompt.isVisible()){ openSpaceChoicePrompt.deleteString(); }
        if(pirateChoicePrompt.isVisible()){ pirateChoicePrompt.deleteString(); }
        if(pirateActivateShieldPrompt.isVisible()){ pirateActivateShieldPrompt.deleteString(); }
        if(pirateFireShotNumber != null && pirateFireShotNumber.isVisible()) { pirateFireShotNumber.deleteString(); }
        if(selectFirePowerPrompt.isVisible()){ selectFirePowerPrompt.deleteString(); }
        if(planetsChoicePrompt.isVisible()){ planetsChoicePrompt.deleteString(); }
        if(slaversChoicePrompt.isVisible()){ slaversChoicePrompt.deleteString(); }
        if(smugglersChoicePrompt.isVisible()){ smugglersChoicePrompt.deleteString(); }
        if(stellarSpaceDustPrompt.isVisible()){ stellarSpaceDustPrompt.deleteString(); }
        if(warZonePrompt.isVisible()){ warZonePrompt.deleteString(); }
        if(waitingFinishEffect.isVisible()) { waitingFinishEffect.deleteString(); }
        if(finalResult != null && finalResult.isVisible()) { finalResult.deleteString(); }
        if(endGameDisplay != null && endGameDisplay.isVisible()) { endGameDisplay.deleteString(); }
        if(lowestCrewMembers.isVisible()) { lowestCrewMembers.deleteString(); }
        if(selectAShieldPrompt.isVisible()) { selectAShieldPrompt.deleteString(); }
        if(fireShotDirection != null && fireShotDirection.isVisible()) { fireShotDirection.deleteString(); }
        if(warZoneEngineChoicePrompt.isVisible()) { warZoneEngineChoicePrompt.deleteString(); }
        if(warZoneFirePowerChoicePrompt.isVisible()) { warZoneFirePowerChoicePrompt.deleteString(); }
        if(warZoneFireWaitingOtherPlayers.isVisible()) { warZoneFireWaitingOtherPlayers.deleteString(); }
    }

    private static void clearAllErrorMessages() {
        if(adventurePhase.isVisible()) adventurePhase.deleteString();
        if(invalidDrawAction.isVisible()) invalidDrawAction.deleteString();
        if(abandonedStationChoiceError.isVisible()) abandonedStationChoiceError.deleteString();
        if(abandonedShipChoiceError.isVisible()) abandonedShipChoiceError.deleteString();
        if(epidemicChoiceError.isVisible()) epidemicChoiceError.deleteString();
        if(meteorShowerChoiceError.isVisible()) meteorShowerChoiceError.deleteString();
        if(openSpaceChoiceError.isVisible()) openSpaceChoiceError.deleteString();
        if(pirateChoiceError.isVisible()) pirateChoiceError.deleteString();
        if(planetsChoiceError.isVisible()) planetsChoiceError.deleteString();
        if(slaversChoiceError.isVisible()) slaversChoiceError.deleteString();
        if(smugglersChoiceError.isVisible()) smugglersChoiceError.deleteString();
        if(stellarSpaceDustError.isVisible()) stellarSpaceDustError.deleteString();
        if(warZoneError.isVisible()) warZoneError.deleteString();
        if(selectAShieldError.isVisible()) selectAShieldError.deleteString();
        if(lowestCrewMembersError.isVisible()) { lowestCrewMembersError.deleteString(); }
        if(warZoneEngineChoiceError.isVisible()) warZoneEngineChoiceError.deleteString();
        if(warZoneFirePowerChoiceError.isVisible()) warZoneFirePowerChoiceError.deleteString();
    }

    public static class Default implements CLIState {
        /**
         * Applies a set of actions to update the CLI state based on the provided arguments.
         * This method clears the screen, updates the CLI display with player-specific color and
         * banners, prints the adventure phase, and saves the cursor position.
         *
         * @param args an array of arguments where the first element is expected to be
         *             a {@link PlayerColors} enum representing the player's color.
         */
        @Override
        public void apply(Object[] args) {
            if(translatePlayerColor((PlayerColors) args[0]) == AnsiColor.DEFAULT) return;

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayGalaxyTrucker(5, translatePlayerColor((PlayerColors) args[0]));

            adventurePhase.print();
            CLICommands.setPosition(4, 22);
            CLICommands.saveCursorPosition();
        }

        private AnsiColor translatePlayerColor(PlayerColors playerColor) {
            return switch (playerColor) {
                case RED -> AnsiColor.RED;
                case BLUE -> AnsiColor.CYAN;
                case GREEN -> AnsiColor.GREEN;
                case YELLOW -> AnsiColor.YELLOW;
                default -> AnsiColor.DEFAULT;
            };
        }
    }

    public static class DrawCardState implements CLIState {
        /**
         * Applies the specific state transition and updates the command-line interface for the "DrawCard" state.
         * This method performs the following tasks:
         * 1. Prepares state transition using the provided input.
         * 2. Clears all existing error messages on the interface.
         * 3. Ensures the visibility of the "Draw Card" element and renders it to the interface.
         * 4. Saves the current cursor position to maintain interface consistency.
         *
         * @param args an array of arguments where the first element is expected to be a String input
         *             required for the state transition process.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearPreviousPrompt();

            if(meteorShowerWaiting.isVisible()) {
                meteorShowerWaiting.deleteString();
            }

            if(!drawCard.isVisible())
                drawCard.print();
            else
                drawCard.print();
            CLICommands.saveCursorPosition();
        }
    }


    public static class DrawCardErrorState implements CLIState {
        /**
         * Applies the error state by preparing for a state transition, clearing all error messages,
         * and updating the visibility and display of specific CLI elements related to the draw card action.
         * Saves the current cursor position in the terminal after processing.
         *
         * @param args an array of arguments used to configure the state transition. The first element
         *             should be a String representing user input or context needed for transitioning states.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            clearAllErrorMessages();

            if(adventurePhase.isVisible())
                CLIString.replace(adventurePhase, invalidDrawAction);
            else
                invalidDrawAction.print();

            if(!drawCard.isVisible())
                drawCard.print();
            else
                drawCard.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class WaitingOtherPlayersState implements CLIState {
        /**
         * Applies the state transition logic for the "Waiting Other Players" phase in the CLI-based
         * application. This method processes the provided arguments to prepare the CLI for the next
         * user interaction, cleaning up previous states and updating the CLI's visual elements.
         *
         * @param args an array of objects where the first element is expected to be a String
         *             representing input necessary to prepare the state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);

            clearAllErrorMessages();
            clearPreviousPrompt();

            waitingFinishEffect.print();
            CLICommands.restoreCursorPosition();
        }

        private AnsiColor translatePlayerColor(PlayerColors playerColor) {
            return switch (playerColor) {
                case RED -> AnsiColor.RED;
                case BLUE -> AnsiColor.CYAN;
                case GREEN -> AnsiColor.GREEN;
                case YELLOW -> AnsiColor.YELLOW;
                default -> AnsiColor.DEFAULT;
            };
        }
    }

    // Abandoned ship
    public static class DrawnAbandonedShipState implements CLIState {

        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state logic for the "Drawn Abandoned Ship" scenario in the Command Line Interface (CLI).
         * This method processes the given arguments to handle the rendering of the Abandoned Ship card,
         * updates the prompt, and manages the cursor state in the CLI.
         *
         * @param args An array of objects containing the parameters required for this state:
         *             - args[0]: A string input used for state transition preparation.
         *             - args[1]: An instance of {@code AbandonedShipCard} representing the card to be displayed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            AbandonedShipCard card = (AbandonedShipCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = AbandonedShipCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            abandonedShipChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            abandonedShipChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnAbandonedShipErrorState implements CLIState {

        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Executes the state transition logic for the DrawnAbandonedShipErrorState.
         * This method carries out several tasks including preparing for the state
         * transition, clearing error messages, resetting prompts, and configuring
         * the CLI interface elements for the abandoned ship scenario.
         *
         * @param args an array of {@code Object}, where the first element is expected
         *             to be a {@code String} that contains the user input. This input
         *             is used during the preparation of the state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            AbandonedShipCard card = (AbandonedShipCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = AbandonedShipCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!abandonedShipChoiceError.isVisible()) {
                abandonedShipChoiceError.print();
            } else {
                abandonedShipChoiceError.print();
            }

            abandonedShipChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            abandonedShipChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Abandoned station
    public static class DrawnAbandonedStationState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state transition logic for handling the drawing of an Abandoned Station card
         * and manages the display of associated CLI elements.
         *
         * @param args An array of objects containing the necessary inputs:
         *             args[0] - A string representing the input necessary for state preparation.
         *             args[1] - An instance of {@code AbandonedStationCard} that represents the card to be processed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            AbandonedStationCard card = (AbandonedStationCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = AbandonedStationCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            abandonedStationChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            abandonedStationChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnAbandonedStationErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Updates the current CLI state by preparing for a state transition, clearing error messages,
         * and printing prompts for the abandoned station and abandoned ship choices. This method is
         * invoked as part of a specific CLI interaction sequence.
         *
         * @param args an array of objects where the first element is expected to be a string
         *             representing the input necessary for the state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            AbandonedStationCard card = (AbandonedStationCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = AbandonedStationCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!abandonedStationChoiceError.isVisible()) {
                abandonedStationChoiceError.print();
            } else {
                abandonedStationChoiceError.print();
            }

            abandonedStationChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            abandonedStationChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Epidemic card
    public static class DrawnEpidemicCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the specific state transition logic when an Epidemic card is drawn in the CLI.
         * Prepares necessary transitions, clears previous states, and displays the drawn Epidemic card
         * on the CLI with associated prompts for user interaction.
         *
         * @param args The arguments required for the state transition:
         *             args[0] - A {@code String} representing the user input to prepare the state transition.
         *             args[1] - An {@code EpidemicCard} object representing the card being drawn.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            EpidemicCard card = (EpidemicCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = EpidemicCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            epidemicChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            epidemicChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnEpidemicErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the specified state transition logic in the CLIState context by
         * preparing the state, clearing error messages, and updating prompts.
         *
         * This method is responsible for:
         * - Transitioning to a new CLI state based on the provided arguments.
         * - Clearing any previously displayed error messages in the CLI.
         * - Updating or displaying specific prompts to the user.
         * - Managing cursor and output positioning.
         *
         * @param args an array of objects where the first element is expected
         *             to be a {@code String} representing the input required
         *             for the state transition. Other elements in the array
         *             are ignored in this implementation.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            EpidemicCard card = (EpidemicCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = EpidemicCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!epidemicChoiceError.isVisible()) {
                epidemicChoiceError.print();
            } else {
                epidemicChoiceError.print();
            }

            epidemicChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            epidemicChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Meteor shower
    public static class DrawnMeteorShowerCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the current state transition logic when a "Meteor Shower" card is drawn.
         * This method handles preparing the state, formatting, and displaying the card and related prompts on the CLI.
         *
         * @param args an array of objects where:
         *             args[0] is a String representing the input specific to the state transition.
         *             args[1] is an instance of {@code MeteorShowerCard} that represents the drawn "Meteor Shower" card.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            MeteorShowerCard card = (MeteorShowerCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display meteor direction ---
            meteorDirection = new CLIString(
                    ">> The meteor is coming from the direction " + card.getMeteorShowerDTO().getMeteorDirectionNumber(),
                    AnsiColor.YELLOW, FIXED_PROMPT_COL, 20);
            meteorDirection.print();

            // --- Display Card ---
            String cardText = MeteorShowerCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            meteorShowerChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            meteorShowerChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnMeteorShowerErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the state transition for the meteor shower error state.
         * This method sets up the proper state by preparing the transition with the provided argument,
         * clearing all visible error messages, and resetting any previous prompt displayed on the CLI.
         * It then displays the necessary prompts and restores the cursor position.
         *
         * @param args an array of objects where the first element is expected to be a {@code String}
         *             parameter used to prepare the state transition. Additional elements in the array
         *             are ignored.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            MeteorShowerCard card = (MeteorShowerCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display meteor direction ---
            meteorDirection = new CLIString(
                    "The meteor is coming from the direction " + card.getMeteorShowerDTO().getMeteorDirectionNumber(),
                    AnsiColor.YELLOW, CARD_PRINT_COL, 20);
            meteorDirection.print();

            // --- Display Card ---
            String cardText = MeteorShowerCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString();
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            if(!meteorShowerChoiceError.isVisible()) {
                meteorShowerChoiceError.print();
            } else {
                meteorShowerChoiceError.print();
            }

            meteorShowerChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            meteorShowerChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnMeteorShowerWaitState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the state transition for the meteor shower error state.
         * This method sets up the proper state by preparing the transition with the provided argument,
         * clearing all visible error messages, and resetting any previous prompt displayed on the CLI.
         * It then displays the necessary prompts and restores the cursor position.
         *
         * @param args an array of objects where the first element is expected to be a {@code String}
         *             parameter used to prepare the state transition. Additional elements in the array
         *             are ignored.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            MeteorShowerCard card = (MeteorShowerCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = MeteorShowerCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString();
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display meteor direction ---
            if(!meteorShowerWaiting.isVisible()) {
                meteorShowerWaiting.print();
            } else {
                meteorShowerWaiting.print();
            }

            CLICommands.restoreCursorPosition();
        }
    }

    // Open space
    public static class DrawnOpenSpaceCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Executes the current state logic to process drawing and displaying an "Open Space" card.
         * This involves preparing the state transition, rendering the card using ASCII formatting,
         * clearing any previous prompts or error messages, and prompting the user for a choice.
         *
         * @param args An array containing the necessary arguments:
         *             - The first element is a string representing the input for state transition preparation.
         *             - The second element is an {@code OpenSpaceCard} representing the card to be drawn and displayed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            OpenSpaceCard card = (OpenSpaceCard) args[1];
            @SuppressWarnings("unchecked")
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = OpenSpaceCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            openSpaceChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            openSpaceChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnOpenSpaceErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the necessary state transitions and updates the command-line interface
         * to reflect the state of the "Drawn Open Space Error". This involves clearing
         * previous prompts and error messages, updating prompt positions, and ensuring
         * the appropriate error message and prompt are displayed.
         *
         * @param args an array of objects where the first element is expected to be
         *             a String input used for preparing the state transition
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            OpenSpaceCard card = (OpenSpaceCard) args[1];
            @SuppressWarnings("unchecked")
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = OpenSpaceCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            if(!openSpaceChoiceError.isVisible()) {
                openSpaceChoiceError.print();
            } else {
                openSpaceChoiceError.print();
            }

            openSpaceChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            openSpaceChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }


    // Pirates card
    public static class DrawnPirateCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the necessary actions and visual updates when transitioning to a new state
         * involving a drawn PirateCard.
         *
         * @param args An array of objects containing the state transition input and the drawn PirateCard:
         *        args[0] - A String representing the input necessary for preparing the state transition.
         *        args[1] - A PirateCard object representing the card that was drawn.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            pirateChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            pirateChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnPirateErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the necessary state transitions and updates the command-line interface
         * to reflect the state of the "Drawn Open Space Error". This involves clearing
         * previous prompts and error messages, updating prompt positions, and ensuring
         * the appropriate error message and prompt are displayed.
         *
         * @param args an array of objects where the first element is expected to be
         *             a String input used for preparing the state transition
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            if(!pirateChoiceError.isVisible()) {
                pirateChoiceError.print();
            } else {
                pirateChoiceError.print();
            }

            pirateChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            pirateChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class CalculateFirePowerState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the current state logic for displaying and processing the card and ship GUI representation
         * in the Command-Line Interface (CLI) during the firepower calculation phase of the game.
         *
         * @param args An array of objects containing the method arguments:
         *             - args[0] is a {@code String} representing the user input or state initialization parameter.
         *             - args[1] is a {@code PirateCard} representing the pirate card to be displayed.
         *             - args[2] is a {@code List<TileCoordinates>} containing the coordinates of the ship data.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            selectFirePowerPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            selectFirePowerPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class CalculateFirePowerErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the given arguments to transition the display state of the CLI, showing the pirate card,
         * associated ship data, and handling error messages or prompts related to selecting firepower.
         *
         * @param args an array of objects where:
         *             - args[0] is a {@code String} representing the input required for state transition.
         *             - args[1] is a {@code PirateCard} containing the card details to be displayed.
         *             - args[2] is a {@code List<TileCoordinates>} representing the ship data to be displayed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            wrongCoordinates.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            wrongCoordinates.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class PirateFireShotState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the logic for transitioning the CLI to the Pirate Fire Shot state, rendering the appropriate
         * visuals such as the pirate card and ship details, and displays prompts for user actions.
         *
         * @param args An array of objects containing the required data in the following order:
         *             args[0] - A string representing the input to handle state transition preparation.
         *             args[1] - A {@code PirateCard} object representing the card to be displayed.
         *             args[2] - A {@code List} of {@code TileCoordinates} representing the positions of the ship's components.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];


            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            pirateFireShotNumber = new CLIString(">> The fireshot is coming from the direction number " + card.getPiratesDTO().getFireShotDirectionNumber(),
                    AnsiColor.YELLOW, FIXED_PROMPT_COL, 20);
            pirateFireShotNumber.print();

            pirateActivateShieldPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            pirateActivateShieldPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class PirateFireShotErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the logic for transitioning the CLI to the Pirate Fire Shot state, rendering the appropriate
         * visuals such as the pirate card and ship details, and displays prompts for user actions.
         *
         * @param args An array of objects containing the required data in the following order:
         *             args[0] - A string representing the input to handle state transition preparation.
         *             args[1] - A {@code PirateCard} object representing the card to be displayed.
         *             args[2] - A {@code List} of {@code TileCoordinates} representing the positions of the ship's components.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PirateCard card = (PirateCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];


            clearAllErrorMessages();
            clearPreviousPrompt();

            // --- Display Card ---
            String cardText = PirateCardASCIIFormatter.format(card);
            if (cardDrew != null && cardDrew.isVisible()) {
                cardDrew.deleteString(); // Remove old card from screen
            }
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            if(!wrongCoordinates.isVisible()) {
                wrongCoordinates.print();
            } else {
                wrongCoordinates.print();
            }

            pirateActivateShieldPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            pirateActivateShieldPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Planets card
    public static class DrawnPlanetsCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state transition for drawing and processing a "Planets" card. This method handles the display of the
         * drawn card, clears previous error messages and prompts, and sets up the prompt for the next user action.
         *
         * @param args an array of objects where:
         *             - args[0]: a String representing the input used to prepare the state transition.
         *             - args[1]: a PlanetsCard object representing the card to be drawn and displayed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PlanetsCard card = (PlanetsCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = PlanetsCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            planetsChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            planetsChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnPlanetsErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the current state transition logic based on the provided arguments.
         * This method is responsible for managing the state and displaying or clearing
         * specific prompts and error messages within a command-line interface context.
         *
         * @param args An array of objects containing the arguments for the method.
         *             The first argument is expected to be a String, which serves
         *             as input for the state transition preparation. Other elements
         *             in the array may remain unused.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            PlanetsCard card = (PlanetsCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = PlanetsCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!planetsChoiceError.isVisible()) {
                planetsChoiceError.print();
            } else {
                planetsChoiceError.print();
            }

            planetsChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            planetsChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Slavers card
    public static class DrawnSlaversCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state transition logic for the drawn "Slavers" card scenario in the CLI.
         * This method prepares the state transition, clears the previous UI elements, formats and displays
         * the drawn card's data, and*/
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            SlaversCard card = (SlaversCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = SlaversCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            slaversChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            slaversChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnSlaversErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Executes the logic required to apply a state-specific transition and update
         * the command-line interface (CLI) visuals.
         *
         * This method performs the following:
         * - Prepares the state transition using the first argument.
         * - Clears all error messages and previous prompts displayed in the CLI.
         * - Manages and reprints the slavers choice error and prompt.
         * - Restores the cursor position after updates to the CLI elements.
         *
         * @param args an array of arguments, where the first element is expected to
         *             be a string input used during the state transition preparation.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            SlaversCard card = (SlaversCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = SlaversCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!slaversChoiceError.isVisible()) {
                slaversChoiceError.print();
            } else {
                slaversChoiceError.print();
            }

            slaversChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            slaversChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Smugglers card
    public static class DrawnSmugglersCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies changes to the current state based on the provided arguments. This method transitions
         * the CLI state, processes a drawn "Smugglers" card, and updates the screen with the formatted
         * card text and prompt for user interaction.
         *
         * @param args an array of {@code Object} arguments where:
         *             args[0] is a {@code String} representing user input for state transition.
         *             args[1] is a {@code SmugglersCard} object representing the drawn card.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            SmugglersCard card = (SmugglersCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = SmugglersCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            smugglersChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            smugglersChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnSmugglersErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state transition along with updating and rendering prompts
         * and error messages in the current context.
         *
         * This method handles the following:
         * 1. Prepares the state transition based on the provided argument.
         * 2. Clears all previously displayed error messages.
         * 3. Clears the previous prompt displayed to the user.
         * 4. Checks the visibility status of the smugglers' choice error and ensures it is printed.
         * 5. Positions and prints the smugglers' choice prompt.
         * 6. Restores the cursor position to its saved state.
         *
         * @param args An array of Objects where the first element is expected to be a String
         *             representing input necessary for state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            SmugglersCard card = (SmugglersCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = SmugglersCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!smugglersChoiceError.isVisible()) {
                smugglersChoiceError.print();
            } else {
                smugglersChoiceError.print();
            }

            smugglersChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            smugglersChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Stellar space dust card
    public static class DrawnStellarSpaceDustCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the current state transition and displays the corresponding card and prompt
         * in the Command Line Interface (CLI).
         *
         * @param args An array of arguments required for applying the state. The first element
         *             is expected to be a {@code String} representing user input needed for
         *             state transition, while the second element is expected to be a
         *             {@code StellarSpaceDustCard} to be displayed in the CLI.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            StellarSpaceDustCard card = (StellarSpaceDustCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = StellarSpaceDustCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            stellarSpaceDustPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            stellarSpaceDustPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnStellarSpaceDustErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the current state transition logic and updates the command-line interface display
         * by handling error messages, prompt positioning, and printed outputs related to stellar space dust.
         *
         * @param args an array of {@code Object} where the first element is expected to be
         *             a {@code String} input used to prepare the state transition.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            StellarSpaceDustCard card = (StellarSpaceDustCard) args[1];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = StellarSpaceDustCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            if(!stellarSpaceDustError.isVisible()) {
                stellarSpaceDustError.print();
            } else {
                stellarSpaceDustError.print();
            }

            stellarSpaceDustPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            stellarSpaceDustPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // War zone card
    public static class DrawnWarZoneCardState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;

        /**
         * Applies the state transition when a War Zone Card is drawn in the game.
         *
         * This method prepares the required state updates, formats the card details for display,
         * clears previous errors and prompts, and sets up the CLI representation of the newly drawn card.
         *
         * @param args an array of objects containing the arguments required for the operation:
         *             args[0] is a {@code String} representing the input for state preparation,
         *             args[1] is a {@code WarZoneCard} representing the drawn card.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZonePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZonePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class DrawnWarZoneErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies a state transition and updates the CLI display with relevant War Zone card details,
         * ship data, and error messages. It manages the visualization of the drawn card and ship,
         * clears previous messages, and prints new prompt and error messages to the CLI.
         *
         * @param args an array of objects containing the necessary parameters:
         *             args[0] - a string input for state transition preparation.
         *             args[1] - a {@code WarZoneCard} object containing details about the drawn War Zone card.
         *             args[2] - a {@code List<TileCoordinates>} representing the ship's data for visualization.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();

            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            if(!warZoneError.isVisible()) {
                warZoneError.print();
            } else {
                warZoneError.print();
            }

            warZonePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZonePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneLowestCrewState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the state transition and displays the necessary UI components for the War Zone Lowest Crew State in the CLI.
         * This method handles transitioning to the appropriate state, rendering the ship data, card information,
         * and any associated UI updates such as clearing error messages and clearing the previous prompt.
         *
         * @param args an array of Object arguments. The expected content is as follows:
         *             args[0] - A string representing the state transition input.
         *             args[1] - An instance of {@code WarZoneCard} representing the card being displayed in this state.
         *             args[2] - A list of {@code TileCoordinates} objects representing the ship data.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();


            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            lowestCrewMembers.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            lowestCrewMembers.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneLowestCrewErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the WarZoneLowestCrewErrorState functionality by preparing state transitions,
         * clearing error messages, displaying ship data, and drawing the current War Zone card.
         *
         * @param args an array of {@link Object} that contains:
         *             args[0] - a {@link String} representing the input used for preparing state transition.
         *             args[1] - a {@link WarZoneCard} representing the card to be displayed.
         *             args[2] - a {@link List} of {@link TileCoordinates} representing the ship data to be displayed.
         */
        @Override
        public void apply(Object[] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            clearAllErrorMessages();
            clearPreviousPrompt();


            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            lowestCrewMembersError.print();
            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectShieldState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Executes the logic for transitioning to the "War Zone Select Shield" state,
         * including displaying a graphical representation of the current card and ship,
         * configuring prompts, and printing related on-screen elements.
         *
         * @param args an array of arguments where:
         *             args[0] is a string representing input data for state transition,
         *             args[1] is a {@code WarZoneCard} object representing the current card,
         *             args[2] is a {@code List<TileCoordinates>} containing ship data for display.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            fireShotDirection = new CLIString(
                    "The fire shot is coming from the direction number " + (card.getWarZoneDTO().getFireShotDirectionNumber()),
                    AnsiColor.YELLOW, 0, 20);
            fireShotDirection.print();

            selectAShieldPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            selectAShieldPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectShieldErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the current state transition by updating the CLI display with game-related data.
         * This method processes the input arguments to prepare and render the game state, including ship details,
         * the current card, and relevant prompts or error messages.
         *
         * @param args an array of objects containing the following elements:
         *             args[0]: A {@code String} representing the input needed for state transitions.
         *             args[1]: A {@code WarZoneCard} object representing the card to be displayed.
         *             args[2]: A {@code List<TileCoordinates>} containing the coordinates of tiles that represent the ship's state.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            selectAShieldError.print();

            selectAShieldPrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            selectAShieldPrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectThrustPowerState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the current state logic to display and process the necessary game elements on the CLI interface.
         *
         * @param args an array of objects where:
         *             - args[0] is a {@code String} representing input for transitioning state.
         *             - args[1] is a {@code WarZoneCard} representing the card to be displayed.
         *             - args[2] is a {@code List<TileCoordinates>} containing data representing the ship's layout or coordinates.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZoneEngineChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZoneEngineChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectThrustPowerErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the current state logic, updating the Command Line Interface (CLI) with the relevant details
         * and displaying the specified card and ship data. Prepares the state for the next transition.
         *
         * @param args an array of {@code Object} containing arguments for the method:
         *             args[0] - a {@code String} representing input data for state transition.
         *             args[1] - a {@code WarZoneCard} object representing the card to be displayed.
         *             args[2] - a {@code List<TileCoordinates>} containing coordinates of the ship tiles.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZoneEngineChoiceError.print();

            warZoneEngineChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZoneEngineChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectFirePowerState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the logic for transitioning to the "War Zone Select Fire Power" state in the CLI.
         * This method clears the screen, displays updated state information including the current "War Zone Card"
         * and related ship data, and invokes prompts for interaction as part of the state transition in the CLI.
         *
         * @param args an array of input arguments where:
         *             args[0] is a {@link String} representing the input for preparing the state transition,
         *             args[1] is a {@link WarZoneCard} representing the card to display in the state,
         *             args[2] is a {@link List} of {@link TileCoordinates} representing the ship data to be displayed.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZoneFirePowerChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZoneFirePowerChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneSelectFirePowerErrorState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the state's behavior using the provided arguments. This method prepares the
         * state transition, clears the screen, and updates the CLI view by displaying the ship data
         * and the War Zone card details. Prompts and error messages associated with choosing firepower
         * are also presented on the CLI.
         *
         * @param args an array containing the arguments required to update the state and display.
         *             - args[0] (String): Input string used for state transition.
         *             - args[1] (WarZoneCard): The War Zone card to be displayed.
         *             - args[2] (List<TileCoordinates>): The data representing the ship's tile coordinates.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZoneFirePowerChoiceError.print();

            warZoneFirePowerChoicePrompt.setPosition(FIXED_PROMPT_COL, FIXED_PROMPT_INPUT_LINE_ROW);
            warZoneFirePowerChoicePrompt.print();

            CLICommands.restoreCursorPosition();
        }
    }

    public static class WarZoneWaitingOtherPlayersState implements CLIState {

        private static final int CARD_PRINT_COL = ADVENTURE_CARD_START_COL;
        private static final int CARD_PRINT_ROW = ADVENTURE_CARD_START_ROW;
        private static final AnsiColor CURRENT_CARD_COLOR = AnsiColor.DEFAULT;
        private static final int CARD_WIDTH = 48;
        private static final int SHIP_HORIZONTAL_GAP = 5;

        /**
         * Applies the logic for the "WarZoneWaitingOtherPlayersState" by transitioning state,
         * rendering visual elements such as the ship and card on the Command-Line Interface (CLI),
         * and updating the display to show the appropriate game state.
         *
         * @param args an array of objects containing necessary parameters for the method:
         *             args[0] is a {@code String} representing the input required for state transition,
         *             args[1] is a {@code WarZoneCard} containing the current card's details,
         *             and args[2] is a {@code List<TileCoordinates>} describing the ship data to be displayed.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            prepareStateTransition((String) args[0]);
            WarZoneCard card = (WarZoneCard) args[1];
            List<TileCoordinates> shipData = (List<TileCoordinates>) args[2];

            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            // --- Display Ship ---
            String shipText = CLIValidationPrinter.buildShipOutputString(shipData);
            int shipPrintCol = CARD_PRINT_COL + CARD_WIDTH + SHIP_HORIZONTAL_GAP;
            int shipPrintRow = CARD_PRINT_ROW;

            if (ship != null && ship.isVisible()) {
                ship.deleteString();
            }
            ship = new CLIString(shipText, AnsiColor.DEFAULT, shipPrintCol, shipPrintRow);
            ship.print();

            // --- Display Card ---
            String cardText = WarZoneCardASCIIFormatter.format(card);
            cardDrew = new CLIString(cardText, CURRENT_CARD_COLOR, CARD_PRINT_COL, CARD_PRINT_ROW);
            cardDrew.print();

            warZoneFireWaitingOtherPlayers.print();

            CLICommands.restoreCursorPosition();
        }
    }

    // Finished game
    public static class FinishedGameWonState implements CLIState {
        /**
         * Applies the terminal update logic for the "game won" state, including clearing
         * the display, showing the winner banner, printing the final score, and saving
         * the cursor position.
         *
         * @param args an array of objects representing input arguments. The first element
         *             of the array (args[0]) is expected to be an integer representing the final score.
         */
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayWinner();

            int score = (int) args[0];

            finalResult = new CLIString("Your final score is: " + score, AnsiColor.YELLOW,0, 30);
            finalResult.centerPrint();

            CLICommands.saveCursorPosition();
        }

    }
    public static class FinishedGameLostState implements CLIState {
        /**
         * Executes actions specific to the "game lost" state in the CLI-based game interface.
         * The method clears the screen, displays a "loser" banner, centers a message showing
         * the player's final score, and saves the cursor position for potential future operations.
         *
         * @param args an array of objects where:
         *             - args[0] is expected to be an integer representing the player's final score
         */
        @Override
        public void apply(Object @NotNull [] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.displayLoser();

            int score = (int) args[0];

            finalResult = new CLIString("Your final score is: " + score, AnsiColor.YELLOW,0, 30);
            finalResult.centerPrint();

            CLICommands.saveCursorPosition();
        }

    }

    public static class FinishedGame implements CLIState {

        private CLIString endGameDisplay;

        /**
         * Applies the logic to display the end game state in the CLI environment. This method formats
         * and displays a leaderboard-style table with player names and their corresponding scores.
         *
         * @param args an array of {@code Object} where:
         *             <ul>
         *               <li>{@code args[0]} is expected to be a {@code List<String>} containing player names.</li>
         *               <li>{@code args[1]} is expected to be a {@code List<Float>} containing player scores, corresponding by index to the names.</li>
         *             </ul>
         */
        @Override
        @SuppressWarnings("unchecked")
        public void apply(Object @NotNull [] args) {
            CLICommands.clearScreen();
            CLICommands.home();
            CLIBanners.finishedGame();

            List<String> playerNames = (List<String>) args[0];
            List<Float> playerScores = (List<Float>) args[1];

            StringBuilder sb = new StringBuilder();

            int minNameWidth = "Giocatore".length();
            int maxNameLength = minNameWidth;
            for (String playerName : playerNames) {
                if (playerName != null && playerName.length() > maxNameLength) {
                    maxNameLength = playerName.length();
                }
            }

            int minScoreWidth = "Punti".length();
            int maxScoreLength = minScoreWidth;
            for (Float score : playerScores) {
                if (score != null) {
                    String scoreStr = String.format("%.2f", score);
                    if (scoreStr.length() > maxScoreLength) {
                        maxScoreLength = scoreStr.length();
                    }
                }
            }
            maxNameLength = Math.max(minNameWidth, maxNameLength);
            maxScoreLength = Math.max(minScoreWidth, maxScoreLength);


            String rowFormat = String.format("| %%-%ds | %%%ds |\n", maxNameLength, maxScoreLength);

            String nameDashes = "-".repeat(maxNameLength);
            String scoreDashes = "-".repeat(maxScoreLength);
            String lineSeparator = "+" + "-".repeat(maxNameLength + 2) +
                    "+" + "-".repeat(maxScoreLength + 2) +
                    "+\n";

            sb.append(lineSeparator);
            sb.append(String.format(rowFormat, "Giocatore", "Punti"));
            sb.append(lineSeparator);

            for (int i = 0; i < playerNames.size(); i++) {
                String playerName = playerNames.get(i);
                if (playerName == null) {
                    playerName = "N/A";
                }

                Float playerScoreObj = playerScores.get(i);
                String playerScoreStr;
                if (playerScoreObj == null) {
                    playerScoreStr = "N/A";
                    playerScoreStr = String.format("%" + maxScoreLength + "s", playerScoreStr);
                } else {
                    playerScoreStr = String.format("%.2f", playerScoreObj);
                    playerScoreStr = String.format("%" + maxScoreLength + "s", playerScoreStr);

                }
                sb.append(String.format(rowFormat, playerName, playerScoreStr));
            }

            sb.append(lineSeparator);

            endGameDisplay = new CLIString(sb.toString(), AnsiColor.YELLOW, 0, 27);
            endGameDisplay.centerPrint();

            CLICommands.saveCursorPosition();
        }
    }
}
