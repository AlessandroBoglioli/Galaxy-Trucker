package it.polimi.ingsoftware.ll13.client.handlers;

import it.polimi.ingsoftware.ll13.network.response.PingResponse;
import it.polimi.ingsoftware.ll13.network.response.PongResponse;
import it.polimi.ingsoftware.ll13.network.response.adventure.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.construction.*;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.AdventurePhase;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.ConstructionPhase;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.CrewPlacePhaseStarted;
import it.polimi.ingsoftware.ll13.network.response.match_responses.phase_change.ValidationPhaseStarted;
import it.polimi.ingsoftware.ll13.network.response.match_responses.validation.CrewStatusResponse;
import it.polimi.ingsoftware.ll13.network.response.match_responses.validation.ValidatedShip;
import it.polimi.ingsoftware.ll13.network.response.menu_response.JoinedMatchResponse;
import it.polimi.ingsoftware.ll13.network.response.menu_response.UsernameTakenResponse;
import it.polimi.ingsoftware.ll13.network.response.SenderIdResponse;

/**
 * The Handler interface defines the contract for processing various types
 * of responses received from a server during the execution of the game.
 * It provides methods to handle each specific type of response, allowing
 * for a modular and extensible way for clients to react to server messages.
 * Implementing classes should provide concrete definitions for these methods
 * to manage the game client state and behavior in response to server events.
 */
public interface Handler {

    void handleSenderIdResponse(SenderIdResponse response);
    void handleUsernameTakenResponse(UsernameTakenResponse response);
    void handleMatchStartedResponse(MatchStartedResponse matchStartedResponse);
    void handlePongResponse(PongResponse pongResponse);
    void handlePingResponse(PingResponse pingResponse);
    void handleJoinedMatchResponse(JoinedMatchResponse joinedMatchResponse);
    void handleUpdateMapResponse(UpdateMap updateMap);
    void handleUpdateShipResponse(UpdatedShip updatedShip);
    void handleViewAnotherShipResponse(ViewAnotherShipResponse viewAnotherShipResponse);
    void handleNotifyHourglass(NotifyHourglass notifyHourglass);
    void handleConstructionPhase(ConstructionPhase constructionPhase);
    void handleDrawFromTemp(DrawFromTempResponse drawFromTempResponse);
    void handleDrawTile(DrawTileResponse drawTileResponse);
    void handleRotateTile(RotatedTileResponse rotatedTileResponse);
    void handleTilePlaced(TilePlacedResponse tilePlacedResponse);
    void handleUpdateFlipped(UpdatedFlippedDeck updatedFlippedDeck);
    void handleUpdateDeck(UpdatedTileDeck updatedTileDeck);
    void handleViewStack(ViewStackResponse viewStackResponse);
    void handleValidationPhase(ValidationPhaseStarted validationPhaseStarted);
    void handleValidateShip(ValidatedShip validatedShip);
    void handleAdventurePhase(AdventurePhase adventurePhase);
    void handleCrewPhase(CrewPlacePhaseStarted crewPlacePhaseStarted);
    void handleCrewStatusResponse(CrewStatusResponse crewStatusResponse);


    void handleOutOfGameResponse(OutOfGameResponse outOfGameResponse);
    void handleFinishedEffectResponse(FinishedCardEffectResponse finishedCardEffectResponse);
    void handleEndGameResponse(EndGameResponse endGameResponse);
    //----HANDLE DRAW//
    void handleDrawAbandonedShipResponse(DrawAbandonedShipResponse drawAbandonedShipResponse);
    void handleDrawAbandonedStationResponse(DrawAbandonedStationResponse drawAbandonedStationResponse);
    void handleDrawEpidemicResponse(DrawEpidemicResponse drawEpidemicResponse);
    void handleDrawMeteorShowerResponse(DrawMeteorShowerResponse drawMeteorShowerResponse);
    void handleDrawOpenSpaceResponse(DrawOpenSpaceResponse drawOpenSpaceResponse);
    void handleDrawPirateResponse(DrawPirateResponse drawPirateResponse);
    void handleDrawPlanetsResponse(DrawPlanetsResponse drawPlanetsResponse);
    void handleDrawSlaversResponse(DrawSlaversResponse drawSlaversResponse);
    void handleDrawSmugglersResponse(DrawSmugglersResponse drawSmugglersResponse);
    void handleDrawStellarSpaceDustResponse(DrawStellarSpaceDustResponse drawStellarSpaceDustResponse);
    void handleDrawWarZoneResponse(DrawWarZoneResponse drawWarZoneResponse);
   //----HANDLE EFFECT ONGOING---//
    void handleAbandonedShipOngoing(ApplyAbandonedShipOngoingResponse abandonedShipOngoingResponse);
    void handleAbandonedStationOngoing(ApplyAbandonedStationOngoingResponse abandonedStationOngoingResponse);
    void handleOnGoingMeteorShowerResponse(ApplyMeteorShowerOngoingResponse OnGoingMeteorShowerResponse);
    void handleStellarSpaceDustOnGoing(ApplyStellarSpaceDustOngoingResponse StellarSpaceDustOngoingResponse);
    void handlePlanetsOngoingResponse(ApplyPlanetsOngoingResponse planetsOngoingResponse);
    void handlePiratesOngoingResponse(ApplyPirateOngoingResponse piratesOngoingResponse);
    void handleOnGoingOpenSpaceResponse(ApplyOpenSpaceOngoingResponse applyOpenSpaceOngoingResponse);
    void handleWarZoneFirePower(CalculateFirePowerResponse calculateFirePowerResponse);
    void handleWarZoneEffectOnGoing(ApplyWarZoneEffectResponseOngoing applyWarZoneEffectResponseOngoing);
    void handleCalculateThrustPower(CalculateThrustPowerResponse calculateThrustPowerResponse);
    void handleWarZoneProjectileOnGoing(ApplyWarZoneProjectileOngoingResponse applyWarZoneProjectileOngoingResponse);
    void handleOngoingSlaversResponse(ApplySlaversOngoingResponse applySlaversOngoingResponse);
    void handleOngoingSmugglersResponse(ApplySmugglersOngoingResponse applySmugglersOngoingResponse);
}
