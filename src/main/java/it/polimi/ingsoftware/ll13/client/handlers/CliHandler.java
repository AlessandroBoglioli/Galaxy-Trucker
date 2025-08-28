package it.polimi.ingsoftware.ll13.client.handlers;

import it.polimi.ingsoftware.ll13.client.controller.CliController;
import it.polimi.ingsoftware.ll13.client.utils.printers.specific_printers.CLIGamePrinter;
import it.polimi.ingsoftware.ll13.client.view.cli.CliView;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.MeteorShowerCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.PirateCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.WarZoneCard;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.network.connection.ClientHearthBeat;
import it.polimi.ingsoftware.ll13.network.requests.PongRequest;
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
 * This class is used to handle the response received by the server (CLI)
 */
public class CliHandler implements Handler{
    private static CliHandler instance;
    private static final CliController controller = CliController.getInstance();
    private static boolean isFlippedHourglass = false;

    public static CliHandler getInstance(){
        if(instance == null){
            instance = new CliHandler();
        }
        return instance;
    }

    // --> Getters <--
    public CliView getCliView() {
        return CliView.getInstance();
    }

    public boolean isFlippedHourglass() {
        return isFlippedHourglass;
    }

    // --> Setters <--
    public void flipHourglass() {
        isFlippedHourglass = true;
    }

    @Override
    public void handleSenderIdResponse(SenderIdResponse response) {
        //already handled in client main thread
    }
    @Override
    public void handleUsernameTakenResponse(UsernameTakenResponse response) {
        synchronized (controller.getLock()){
            controller.setResult(CliController.LoginResult.USERNAME_TAKEN);
            controller.getLock().notifyAll();
        }
    }

    @Override
    public void handleMatchStartedResponse(MatchStartedResponse matchStartedResponse) {
        synchronized (controller.getLock()) {
            try {
                // Initial state of the client
                controller.getClient().setInMatch(true);
                controller.getClient().setGameLevel(matchStartedResponse.getLevel());
                controller.getClient().setColor(matchStartedResponse.getColor());

                // Initialize the interface of the client
                getCliView().setCliInterface(new CLIGamePrinter());
                getCliView().displayPage(new Object[]{matchStartedResponse.getColor()});

                if(matchStartedResponse.isHost()){
                    controller.setBeingHost();
                }

            } catch (Exception e) {
                System.err.println("Error handling MatchStartedResponse: " + e.getMessage());
                controller.getClient().setColor(PlayerColors.RED);
                getCliView().displayPage(new Object[]{PlayerColors.RED});
            } finally {
                // Notifies the controller that the game is started
                controller.getLock().notifyAll();
            }
        }
    }

    @Override
    public void handleJoinedMatchResponse(JoinedMatchResponse joinedMatchResponse) {
        synchronized (controller.getLock()) {
            if (joinedMatchResponse.isSuccess()) {
                controller.getClient().setLogged(true);
                controller.setResult(CliController.LoginResult.SUCCESS);
            } else {
                if(!joinedMatchResponse.isInMatch()){
                    controller.setResult(CliController.LoginResult.NO_MATCH);
                }else {
                    controller.setResult(CliController.LoginResult.MATCH_STARTED);
                }
            }
            controller.getLock().notifyAll();
        }
    }

    @Override
    public void handlePongResponse(PongResponse pongResponse) {
        //System.out.println("Resetting hearthBeat");
        ClientHearthBeat.reset();
    }

    @Override
    public void handlePingResponse(PingResponse pingResponse) {
        controller.send(new PongRequest(controller.getId()));
    }

    // Sent by the server receive placeRocketRequest
    @Override
    public void handleUpdateMapResponse(UpdateMap updateMap) {
        synchronized (controller.getLock()) {
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                if(controller.getClient().getGameLevel() == GameLevel.LEVEL_2) {
                    controller.setBuildingPhase(CliController.BuildingPhase.WAITING_FINISH);
                    controller.getLock().notify();
                } else {
                    // Level try
                    controller.setBuildingPhase(CliController.BuildingPhase.WAITING_FINISH);
                    controller.getLock().notify();
                }
            }
        }
    }

    @Override
    public void handleUpdateShipResponse(UpdatedShip updatedShip) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                if(updatedShip.isSuccess()) {
                    controller.setCoordinatesOfTheShip(updatedShip.getUpdatedTiles());
                    controller.setBuildingPhase(CliController.BuildingPhase.PLACED_TILE_CORRECTLY);
                    controller.getLock().notify();
                } else {
                    controller.setBuildingPhase(CliController.BuildingPhase.PLACED_TILE_WRONGLY);
                    controller.getLock().notify();
                }

            } else if(controller.getGamePhase() == CliController.GamePhase.VALIDATING) {
                if(updatedShip.isSuccess()) {
                    controller.setCoordinatesOfTheShip(updatedShip.getUpdatedTiles());
                    controller.setValidationPhase(CliController.ValidationPhase.VALIDATING_ERROR);
                } else {
                    controller.setCoordinatesOfTheShip(updatedShip.getUpdatedTiles());
                    controller.setValidationPhase(CliController.ValidationPhase.VALIDATING_ERROR);
                }
                controller.getLock().notify();
            } else if(controller.getGamePhase() == CliController.GamePhase.ADVENTURE) {
                controller.setCoordinatesOfTheShip(updatedShip.getUpdatedTiles());
            }
        }
    }

    // Response immediately after notifyHourglassRequest
    @Override
    public void handleNotifyHourglass(NotifyHourglass notifyHourglass) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                if(controller.getClient().getGameLevel() == GameLevel.LEVEL_2) {
                    if(notifyHourglass.isFlipped()) {
                        if(!isFlippedHourglass()) {
                            flipHourglass();
                            controller.setBuildingPhase(CliController.BuildingPhase.FLIPPED_HOURGLASS);
                        } else {
                            controller.setBuildingPhase(CliController.BuildingPhase.WAITING_FINISH);
                        }
                    }
                    controller.getLock().notify();
                } else {
                    controller.setBuildingPhase(CliController.BuildingPhase.FINISHED_BUILDING);
                    controller.getLock().notify();
                }
            }
        }
    }

    @Override
    public void handleConstructionPhase(ConstructionPhase constructionPhase) {
        synchronized (controller.getLock()) {
            controller.setBuildingPhase(CliController.BuildingPhase.CHOOSING);
            controller.setCardStacks(constructionPhase.getCardStacks());
            controller.setFlippedTiles(constructionPhase.getFlippedTiles());
            controller.getLock().notifyAll();
        }
    }

    @Override
    public void handleDrawFromTemp(DrawFromTempResponse drawFromTempResponse) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                controller.setCurrentTile(drawFromTempResponse.getTile());
                controller.setBuildingPhase(CliController.BuildingPhase.DRAWING_TILE);
                controller.getLock().notify();
            }
        }
    }

    @Override
    public void handleDrawTile(DrawTileResponse drawTileResponse) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                controller.setBuildingPhase(CliController.BuildingPhase.DRAWING_TILE);
                controller.setCurrentTile(drawTileResponse.getDrawnTile());
                controller.getLock().notify();

            }
        }
    }

    @Override
    public void handleRotateTile(RotatedTileResponse rotatedTileResponse) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                controller.setBuildingPhase(CliController.BuildingPhase.ROTATING_TILE);
                controller.setCurrentTile(rotatedTileResponse.getTile());

                controller.getLock().notify();
            }
        }
    }

    @Override
    public void handleTilePlaced(TilePlacedResponse tilePlacedResponse) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                if(!tilePlacedResponse.isSuccess()){
                    controller.setBuildingPhase(CliController.BuildingPhase.PLACED_TILE_WRONGLY);
                    controller.getLock().notify();
                }
            }
        }
    }

    @Override
    public void handleUpdateFlipped(UpdatedFlippedDeck updatedFlippedDeck) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                controller.setFlippedTiles(updatedFlippedDeck.getFlippedTiles());
                controller.setBuildingPhase(CliController.BuildingPhase.DISCARDED_TILE);
                controller.getLock().notify();
            }
        }
    }

    @Override
    public void handleUpdateDeck(UpdatedTileDeck updatedTileDeck) {

    }

    @Override
    public void handleViewStack(ViewStackResponse viewStackResponse) {

    }

    @Override
    public void handleViewAnotherShipResponse(ViewAnotherShipResponse viewAnotherShipResponse) {

    }

    // Arrives when the hourglass is finished -> Cannot build -> [validate] [eliminate]
    @Override
    public void handleValidationPhase(ValidationPhaseStarted validationPhaseStarted) {
        synchronized (controller.getLock()) {
            if(controller.getGamePhase() == CliController.GamePhase.BUILDING) {
                controller.setBuildingPhase(CliController.BuildingPhase.FINISHED_BUILDING);
                controller.getLock().notify();
            } else if(controller.getGamePhase() == CliController.GamePhase.SETTING_EQUIPE) {
                controller.setValidationPhase(CliController.ValidationPhase.VALIDATING);
                controller.getLock().notify();
            }
        }
    }

    @Override
    public void handleValidateShip(ValidatedShip validatedShip) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.VALIDATING) {
                if(validatedShip.isValid()) {
                    controller.setValidationPhase(CliController.ValidationPhase.VALIDATED);
                } else {
                    controller.setValidationPhase(CliController.ValidationPhase.VALIDATING_ERROR);
                    controller.setInvalidCoordinates(validatedShip.getInvalidTiles());
                }
                controller.getLock().notify();
            }
        }
    }

    @Override
    public void handleCrewPhase(CrewPlacePhaseStarted crewPlacePhaseStarted) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.SETTING_EQUIPE) {
                if(!crewPlacePhaseStarted.getCabinTiles().isEmpty()) {
                    controller.setSetUpPhase(CliController.SetUpPhase.SETTING_UP);
                } else {
                    controller.setSetUpPhase(CliController.SetUpPhase.SET_UP_FINISHED);
                }
                controller.getLock().notifyAll();
            } else {
                // System.out.println("[WARN] CliHandler: Received CrewPlacePhaseStarted but GamePhase of the controller is " + controller.getGamePhase() + ". Message ignored.");
            }
        }
    }

    @Override
    public void handleCrewStatusResponse(CrewStatusResponse crewStatusResponse) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.SETTING_EQUIPE) {
                if(crewStatusResponse.isSuccess()) {
                    if(crewStatusResponse.getCabinTiles().isEmpty()) {
                        controller.setSetUpPhase(CliController.SetUpPhase.SET_UP_FINISHED);
                    } else {
                        controller.setSetUpPhase(CliController.SetUpPhase.PLACED_CREW_MEMBERS);
                    }
                } else {
                    controller.setSetUpPhase(CliController.SetUpPhase.SETTING_UP_ERROR);
                }
                controller.getLock().notifyAll();
            }
        }
    }

    @Override
    public void handleAdventurePhase(AdventurePhase adventurePhase) {
        synchronized (controller.getLock()){
            if(controller.getGamePhase() == CliController.GamePhase.SETTING_EQUIPE) {
                controller.setGamePhase(CliController.GamePhase.ADVENTURE);
            }
            controller.setIsFirst(adventurePhase.isFirst());
            controller.setBatteryCount(adventurePhase.getBatteryCount());
            controller.setCrewCount(adventurePhase.getCrewCount());
            controller.setWasteTilesCount(adventurePhase.getWasteTilesCount());
            controller.setCargoCount(adventurePhase.getCargoCount());
            controller.setExposedConnectorsCount(adventurePhase.getExposedConnectorsCount());
            controller.setCreditCount(adventurePhase.getCreditCount());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleOutOfGameResponse(OutOfGameResponse outOfGameResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.OUT_OF_GAME);
            controller.setScore((int) outOfGameResponse.getPoints());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleFinishedEffectResponse(FinishedCardEffectResponse finishedCardEffectResponse) {
        synchronized (controller.getLock()){
            controller.setIsFirst(finishedCardEffectResponse.isFirst());
            controller.setAdventurePhase(CliController.AdventurePhase.FINISHED_CARD_EFFECT);

            controller.getLock().notifyAll();
        }
    }

    @Override
    public void handleEndGameResponse(EndGameResponse endGameResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.FINISHED);
            controller.setPlayersPositions(endGameResponse.getPlayersName());
            controller.setPlayersPoints(endGameResponse.getPlayersPoints());

            controller.getLock().notify();
        }
    }

    //---HANDLE DRAW----//
    @Override
    public void handleDrawAbandonedShipResponse(DrawAbandonedShipResponse drawAbandonedShipResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawAbandonedShipResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawAbandonedStationResponse(DrawAbandonedStationResponse drawAbandonedStationResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawAbandonedStationResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawEpidemicResponse(DrawEpidemicResponse drawEpidemicResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawEpidemicResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawMeteorShowerResponse(DrawMeteorShowerResponse drawMeteorShowerResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawMeteorShowerResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawOpenSpaceResponse(DrawOpenSpaceResponse drawOpenSpaceResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawOpenSpaceResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawPirateResponse(DrawPirateResponse drawPirateResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawPirateResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawPlanetsResponse(DrawPlanetsResponse drawPlanetsResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawPlanetsResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawSlaversResponse(DrawSlaversResponse drawSlaversResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawSlaversResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawSmugglersResponse(DrawSmugglersResponse drawSmugglersResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawSmugglersResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawStellarSpaceDustResponse(DrawStellarSpaceDustResponse drawStellarSpaceDustResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawStellarSpaceDustResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleDrawWarZoneResponse(DrawWarZoneResponse drawWarZoneResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(drawWarZoneResponse.getCard());

            controller.getLock().notify();
        }
    }

    //----ONGOINGS----//
    @Override
    public void handleAbandonedShipOngoing(ApplyAbandonedShipOngoingResponse abandonedShipOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(abandonedShipOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }
    @Override
    public void handleAbandonedStationOngoing(ApplyAbandonedStationOngoingResponse abandonedStationOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(abandonedStationOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleOnGoingMeteorShowerResponse(ApplyMeteorShowerOngoingResponse applyMeteorShowerOngoingResponse){
        synchronized (controller.getLock()){
            MeteorShowerCard meteorShowerCard = (MeteorShowerCard) applyMeteorShowerOngoingResponse.getCard();
            if(meteorShowerCard.getMeteorShowerDTO().getMeteorNumber() == (meteorShowerCard.getMeteors().size())){
                controller.setAdventurePhase(CliController.AdventurePhase.METEOR_SHOWER_FINISHED);
            } else {
                controller.setAdventurePhase(CliController.AdventurePhase.METEOR_SHOWER_ONGOING);
            }
            controller.setCurrentCard(applyMeteorShowerOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleStellarSpaceDustOnGoing(ApplyStellarSpaceDustOngoingResponse applyStellarSpaceDustOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(applyStellarSpaceDustOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleOnGoingOpenSpaceResponse(ApplyOpenSpaceOngoingResponse applyOpenSpaceOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(applyOpenSpaceOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handlePlanetsOngoingResponse(ApplyPlanetsOngoingResponse planetsOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(planetsOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handlePiratesOngoingResponse(ApplyPirateOngoingResponse applyPirateOngoingResponse) {
        synchronized (controller.getLock()){
            PirateCard pirateCard = (PirateCard) applyPirateOngoingResponse.getCard();
            if(pirateCard.getPiratesDTO().getFireShotNumber() == (pirateCard.getFireShots().size())) {
                controller.setAdventurePhase(CliController.AdventurePhase.PIRATES_FINISHED);
            } else {
                controller.setAdventurePhase(CliController.AdventurePhase.PIRATES_ONGOING);
            }
            controller.setCurrentCard(applyPirateOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleOngoingSlaversResponse(ApplySlaversOngoingResponse applySlaversOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(applySlaversOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleOngoingSmugglersResponse(ApplySmugglersOngoingResponse applySmugglersOngoingResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(applySmugglersOngoingResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleWarZoneEffectOnGoing(ApplyWarZoneEffectResponseOngoing applyWarZoneEffectResponseOngoing){
        synchronized (controller.getLock()){
            if(((WarZoneCard)applyWarZoneEffectResponseOngoing.getCard()).getPenalties().get(((WarZoneCard)applyWarZoneEffectResponseOngoing.getCard()).getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyType() == WarZonePenaltyType.LOWEST_FIREPOWER) {
                controller.setAdventurePhase(CliController.AdventurePhase.WAR_ZONE_LOWEST_FIRE_POWER);
                controller.setCurrentCard(applyWarZoneEffectResponseOngoing.getCard());
            } else if(((WarZoneCard)applyWarZoneEffectResponseOngoing.getCard()).getPenalties().get(((WarZoneCard)applyWarZoneEffectResponseOngoing.getCard()).getWarZoneDTO().getPenaltyNumber()).getWarZonePenaltyType() == WarZonePenaltyType.LOWEST_THRUST) {
                controller.setAdventurePhase(CliController.AdventurePhase.WAR_ZONE_LOWEST_THRUST_POWER);
                controller.setCurrentCard(applyWarZoneEffectResponseOngoing.getCard());

            }
            controller.getLock().notifyAll();
        }
    }

    @Override
    public void handleWarZoneFirePower(CalculateFirePowerResponse calculateFirePowerResponse) {
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(calculateFirePowerResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleCalculateThrustPower(CalculateThrustPowerResponse calculateThrustPowerResponse){
        synchronized (controller.getLock()){
            controller.setAdventurePhase(CliController.AdventurePhase.DREW_CARD);
            controller.setCurrentCard(calculateThrustPowerResponse.getCard());

            controller.getLock().notify();
        }
    }

    @Override
    public void handleWarZoneProjectileOnGoing(ApplyWarZoneProjectileOngoingResponse applyWarZoneProjectileOngoingResponse){
        synchronized (controller.getLock()){
            if(((WarZoneCard)applyWarZoneProjectileOngoingResponse.getCard()).getWarZoneDTO().getFireShotNumber() == ((WarZoneCard)applyWarZoneProjectileOngoingResponse.getCard()).getPenalties().size()) {
                controller.setAdventurePhase(CliController.AdventurePhase.WAR_ZONE_FIRE_SHOTS_FINISHED);
                controller.setCurrentCard(applyWarZoneProjectileOngoingResponse.getCard());
            } else {
                controller.setAdventurePhase(CliController.AdventurePhase.WAR_ZONE_ONGOING);
                controller.setCurrentCard(applyWarZoneProjectileOngoingResponse.getCard());
            }
            controller.getLock().notify();
        }
    }
}