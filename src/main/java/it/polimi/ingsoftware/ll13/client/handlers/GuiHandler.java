package it.polimi.ingsoftware.ll13.client.handlers;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.client.view.gui.*;
import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.dtos.AbandonedStationDTO;
import it.polimi.ingsoftware.ll13.model.cards.dtos.WarZoneDTO;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyType;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.PirateCard;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.Planet;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.PlanetsCard;
import it.polimi.ingsoftware.ll13.model.cards.dtos.AbandonedShipDTO;
import it.polimi.ingsoftware.ll13.model.general_enumerations.GameLevel;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import it.polimi.ingsoftware.ll13.network.connection.ClientHearthBeat;
import it.polimi.ingsoftware.ll13.network.requests.PongRequest;
import it.polimi.ingsoftware.ll13.network.response.Dtos.PlayerDTO;
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
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to handle the response received by the server (GUI)
 */
public class GuiHandler implements Handler{
    private static GuiHandler instance;
    private static GuiController controller = GuiController.getInstance();
    private List<PlayerDTO> latestPlayerPositions = new ArrayList<>();
    public List<PlayerDTO> getLatestPlayerPositions() {
        return this.latestPlayerPositions;
    }

    public static GuiHandler getInstance(){
        if(instance == null){
            instance = new GuiHandler();
        }
        return instance;
    }
    private GuiSceneController getSceneController(){
        return controller.getPage();
    }
    @Override
    public void handleSenderIdResponse(SenderIdResponse response) {
        //already handled
    }
    @Override
    public void handleUsernameTakenResponse(UsernameTakenResponse response) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof LoginController loginController){
                loginController.handleUsernameTaken();
            }
        });
    }

    @Override
    public void handleJoinedMatchResponse(JoinedMatchResponse joinedMatchResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof LoginController loginController) {
                if (joinedMatchResponse.isSuccess()) {
                    loginController.handleLoginSuccess();
                } else {
                    if (!joinedMatchResponse.isInMatch()) {
                        loginController.noMatchStarted();
                    } else {
                        loginController.handleMatchStarted();
                    }
                }
            }
        });

    }

    @Override
    public void handleMatchStartedResponse(MatchStartedResponse matchStartedResponse) {
        GameLevel level = matchStartedResponse.getLevel();
        PlayerColors color = matchStartedResponse.getColor();
        controller.getClient().setColor(color);
        boolean isHost = matchStartedResponse.isHost();
        Platform.runLater(()->{
            GuiSceneController sceneController;
            controller.getClient().setGameLevel(level);
            switch (level){
                case TRY_LEVEL -> sceneController = new BuildingShipPhaseTryLevelController();
                case LEVEL_2 -> sceneController = new BuildingShipPhaseLevel2Controller();
                default -> throw new IllegalStateException("no level: "+level);
            }
            controller.changeScene(sceneController);
            if(controller.getPage() instanceof BuildingShipPhaseTryLevelController){
                ((BuildingShipPhaseTryLevelController) controller.getPage()).placeStartingCabinTile(color);
                ((BuildingShipPhaseTryLevelController) controller.getPage()).setupInitialVisibility(isHost);

            } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller) {
                ((BuildingShipPhaseLevel2Controller) controller.getPage()).placeStartingCabinTile(color);
                ((BuildingShipPhaseLevel2Controller) controller.getPage()).setupInitialVisibility(isHost);

            }
        });
    }

    @Override
    public void handlePongResponse(PongResponse pongResponse) {
        ClientHearthBeat.reset();
    }

    @Override
    public void handlePingResponse(PingResponse pingResponse) {
        controller.send(new PongRequest(controller.getId()));

    }

    @Override
    public void handleUpdateMapResponse(UpdateMap updateMap) {
        Platform.runLater(()->{
            this.latestPlayerPositions = updateMap.getPositionDTOList();
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                if(updateMap.isSuccess()) {
                    for (PlayerDTO dto : updateMap.getPositionDTOList()) {
                        level2Controller.updateMap(dto);
                    }
                }else{
                    //warn him he did not place
                }
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                if(updateMap.isSuccess()){
                    for(PlayerDTO dto : updateMap.getPositionDTOList()){
                        tryLevelController.updateMap(dto);
                    }
                }else{
                    //warning
                }

            }
        });
    }

    @Override
    public void handleUpdateShipResponse(UpdatedShip updatedShip) {
        List<TileCoordinates> updatedTiles = updatedShip.getUpdatedTiles();
        Integer bc = updatedShip.getBatteryCount();
        Integer cc = updatedShip.getCrewCount();
        Integer wtc = updatedShip.getWasteTilesCount();
        Integer crgc = updatedShip.getCargoCount();
        Integer expc = updatedShip.getExposedConnectorsCount();
        Integer crec = updatedShip.getCreditCount();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.updateShipView(updatedTiles);
                tryLevelController.updateCounter(bc,cc,wtc,crgc,expc,crec);
            } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.updateShipView(updatedTiles);
                level2Controller.updateCounter(bc,cc,wtc,crgc,expc,crec);
            }
        });
    }

    @Override
    public void handleViewAnotherShipResponse(ViewAnotherShipResponse viewAnotherShipResponse) {
        boolean myShip = viewAnotherShipResponse.isMyShip();
        List<TileCoordinates> updatedTiles = viewAnotherShipResponse.getUpdatedTiles();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                if(myShip){
                    level2Controller.updateShipView(updatedTiles);
                    level2Controller.setReadOnly(false);
                    level2Controller.placeStartingCabinTile(controller.getClient().getColor());
                }else if(!myShip){
                    final PlayerColors color = viewAnotherShipResponse.getColor();
                    level2Controller.updateShipView(updatedTiles);
                    level2Controller.setReadOnly(true);
                    level2Controller.placeStartingCabinTile(color);

                }
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                if(myShip){
                    tryLevelController.updateShipView(updatedTiles);
                    tryLevelController.setReadOnly(false);
                    tryLevelController.placeStartingCabinTile(controller.getClient().getColor());
                }else if(!myShip){
                    final PlayerColors color = viewAnotherShipResponse.getColor();
                    tryLevelController.updateShipView(updatedTiles);
                    tryLevelController.setReadOnly(true);
                    tryLevelController.placeStartingCabinTile(color);

                }

            }
        });
    }

    @Override
    public void handleNotifyHourglass(NotifyHourglass notifyHourglass) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                if(notifyHourglass.isFlipped()) {
                    level2Controller.setHourglassFlipped(true);
                    level2Controller.startTimer();
                }
            }
        });
    }

    @Override
    public void handleConstructionPhase(ConstructionPhase constructionPhase) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.setPhase(GamePhase.SETUP);
                tryLevelController.showGetReadyPopup();
                tryLevelController.setConstructionPhase();
            } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.setBufferedCardStack(constructionPhase.getCardStacks());
                level2Controller.setPhase(GamePhase.SETUP);
                level2Controller.showGetReadyPopup();
                level2Controller.setConstructionPhase();
            }
        });
    }

    @Override
    public void handleDrawFromTemp(DrawFromTempResponse drawFromTempResponse) {
        Tile tile = drawFromTempResponse.getTile();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.handleDrawFromTemp(tile);

            } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.handleDrawFromTemp(tile);

            }
        });
    }

    @Override
    public void handleDrawTile(DrawTileResponse drawTileResponse) {
        Platform.runLater(()->{
            Tile tile = drawTileResponse.getDrawnTile();
            if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.handleDrawnTile(tile);
            } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.handleDrawnTile(tile);
            }
        });

    }

    @Override
    public void handleRotateTile(RotatedTileResponse rotatedTileResponse) {
        Tile rotatedTile = rotatedTileResponse.getTile();
        Platform.runLater(()->{
                if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                    tryLevelController.showRotatedTile(rotatedTile);
                } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                    level2Controller.showRotatedTile(rotatedTile);
                }
        });
    }

    @Override
    public void handleTilePlaced(TilePlacedResponse tilePlacedResponse) {
        if(!tilePlacedResponse.isSuccess()){
            Platform.runLater(()->{
                if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                    tryLevelController.showPlacementWarning();
                } else if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                    level2Controller.showPlacementWarning();
                }

            });
        }
    }

    @Override
    public void handleUpdateFlipped(UpdatedFlippedDeck updatedFlippedDeck) {
        List<Tile> updatedTiles = updatedFlippedDeck.getFlippedTiles();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setBufferedTileList(updatedTiles);
                if(level2Controller.getPopupStage() != null && level2Controller.getPopupStage().isShowing()){
                    level2Controller.renderDiscardedTiles();
                }
            }else if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.setBufferedTileList(updatedTiles);
                if(tryLevelController.getPopupStage() != null && tryLevelController.getPopupStage().isShowing()){
                    tryLevelController.renderDiscardedTiles();
                }
            }
        });
    }

    @Override
    public void handleUpdateDeck(UpdatedTileDeck updatedTileDeck) {

    }

    @Override
    public void handleViewStack(ViewStackResponse viewStackResponse) {

    }

    @Override
    public void handleValidationPhase(ValidationPhaseStarted validationPhaseStarted) {
       Platform.runLater(()->{
           if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
               level2Controller.setPhase(GamePhase.VALIDATION);
               level2Controller.handleValidationPhase();
           }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
               tryLevelController.setPhase(GamePhase.VALIDATION);
               tryLevelController.handleValidationPhase();
           }
       });
    }

    @Override
    public void handleValidateShip(ValidatedShip validatedShip) {
        boolean validated = validatedShip.isValid();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                if(!validated){
                    level2Controller.highlightInvalidTiles(validatedShip.getInvalidTiles());
                } else if (validated) {
                    level2Controller.shipValidated();
                }
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                if(!validated){
                    tryLevelController.highlightInvalidTiles(validatedShip.getInvalidTiles());
                }else if(validated){
                    tryLevelController.shipValidated();
                }

            }
        });
    }

    @Override
    public void handleAdventurePhase(AdventurePhase adventurePhase) {
        boolean isFirst = adventurePhase.isFirst();
        Integer bc = adventurePhase.getBatteryCount();
        Integer cc = adventurePhase.getCrewCount();
        Integer wtc = adventurePhase.getWasteTilesCount();
        Integer crgc = adventurePhase.getCargoCount();
        Integer expc = adventurePhase.getExposedConnectorsCount();
        Integer crec = adventurePhase.getCreditCount();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setPhase(GamePhase.FLIGHT);
                level2Controller.setUpAdventurePhase();
                level2Controller.updateCounter(bc,cc,wtc,crgc,expc,crec);
                if(isFirst){
                    level2Controller.drawCardPopUp();
                }else {
                    level2Controller.waitingAdventureCardPopUp();
                }
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.setPhase(GamePhase.FLIGHT);
                tryLevelController.setUpAdventurePhase();
                tryLevelController.updateCounter(bc,cc,wtc,crgc,expc,crec);
                if(isFirst){
                    tryLevelController.drawCardPopUp();
                }else {
                    tryLevelController.waitingAdventureCardPopUp();
                }
            }
        });
    }

    @Override
    public void handleCrewPhase(CrewPlacePhaseStarted crewPlacePhaseStarted) {
        if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
            level2Controller.setPhase(GamePhase.CREW_PHASE);
            level2Controller.setupCrewPlacementPhase();
            level2Controller.highlightCabinTiles(crewPlacePhaseStarted.getCabinTiles());
        }
    }

    @Override
    public void handleCrewStatusResponse(CrewStatusResponse crewStatusResponse) {
        Integer humanCount = crewStatusResponse.getHumanCount();
        Integer yellowAlienCount = crewStatusResponse.getYellowAlienCount();
        Integer purpleAlienCount = crewStatusResponse.getPurpleAlienCount();
        Platform.runLater(()->{
            if(crewStatusResponse.isSuccess()){
                BuildingShipPhaseLevel2Controller level2Controller = (BuildingShipPhaseLevel2Controller)controller.getPage();
                level2Controller.updateCrewOnResponse(humanCount,purpleAlienCount,yellowAlienCount);
                level2Controller.highlightCabinTiles(crewStatusResponse.getCabinTiles());

            }else if (!crewStatusResponse.isSuccess()){
                ((BuildingShipPhaseLevel2Controller)controller.getPage()).highlightCabinTiles(crewStatusResponse.getCabinTiles());
            }
        });
    }

    @Override
    public void handleOutOfGameResponse(OutOfGameResponse outOfGameResponse) {
        Platform.runLater(() -> {
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.switchOutOfGameScreen(outOfGameResponse.getPoints());
            } else if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.switchOutOfGameScreen(outOfGameResponse.getPoints());
            }
        });
    }

    @Override
    public void handleEndGameResponse(EndGameResponse endGameResponse) {
        Platform.runLater(() -> {
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.switchEndGameScreen(endGameResponse.getPlayersName(), endGameResponse.getPlayersPoints());
            } else if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.switchEndGameScreen(endGameResponse.getPlayersName(), endGameResponse.getPlayersPoints());
            } else if (controller.getPage() instanceof OutOfGameController outOfGameController) {
                outOfGameController.switchEndGameScreen(endGameResponse.getPlayersName(), endGameResponse.getPlayersPoints());
            }
        });
    }

    @Override
    public void handleFinishedEffectResponse(FinishedCardEffectResponse finishedCardEffectResponse) {
        boolean isFirst = finishedCardEffectResponse.isFirst();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                if(isFirst){
                    level2Controller.closeWaitingRoomPopUp();
                    level2Controller.drawCardPopUp();
                }else {
                    level2Controller.waitingAdventureCardPopUp();
                }
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                if(isFirst){
                    tryLevelController.closeWaitingRoomPopUp();
                    tryLevelController.drawCardPopUp();
                }else {
                    tryLevelController.waitingAdventureCardPopUp();
                }
            }

        });

    }
    //----Handle----//

    @Override
    public void handleDrawMeteorShowerResponse(DrawMeteorShowerResponse drawMeteorShowerResponse) {
        Platform.runLater(()->{
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawMeteorShowerResponse.getCard());
                level2Controller.showMeteorShowerPopUp(drawMeteorShowerResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawMeteorShowerResponse.getCard());
                tryLevelController.showMeteorShowerPopUp(drawMeteorShowerResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleOnGoingMeteorShowerResponse(ApplyMeteorShowerOngoingResponse OnGoingMeteorShowerResponse){
        Platform.runLater(()->{
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(OnGoingMeteorShowerResponse.getCard());
                level2Controller.showMeteorShowerPopUp(OnGoingMeteorShowerResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(OnGoingMeteorShowerResponse.getCard());
                tryLevelController.showMeteorShowerPopUp(OnGoingMeteorShowerResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawEpidemicResponse(DrawEpidemicResponse drawEpidemicResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawEpidemicResponse.getCard());
                level2Controller.showNoResponseCard(drawEpidemicResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawOpenSpaceResponse(DrawOpenSpaceResponse drawOpenSpaceResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawOpenSpaceResponse.getCard());
                level2Controller.showOpenSpace(drawOpenSpaceResponse.getCard().getImage());
            }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawOpenSpaceResponse.getCard());
                tryLevelController.showOpenSpace(drawOpenSpaceResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawPirateResponse(DrawPirateResponse drawPirateResponse) {
        PirateCard card = (PirateCard) drawPirateResponse.getCard();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawPirateResponse.getCard());
                level2Controller.showPirateCardPopUp(card.getImage());
            }
        });

    }

    @Override
    public void handleDrawPlanetsResponse(DrawPlanetsResponse drawPlanetsResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawPlanetsResponse.getCard());
                level2Controller.planetsPopUp(drawPlanetsResponse.getCard().getImage(),((PlanetsCard)drawPlanetsResponse.getCard()));
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawPlanetsResponse.getCard());
                tryLevelController.planetsPopUp(drawPlanetsResponse.getCard().getImage(),((PlanetsCard)drawPlanetsResponse.getCard()));
            }
        });
    }

    @Override
    public void handleDrawSlaversResponse(DrawSlaversResponse drawSlaversResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawSlaversResponse.getCard());
                level2Controller.showSlaversCardPopUp(drawSlaversResponse.getCard().getImage());
            }
        });
    }
    @Override
    public void handleDrawSmugglersResponse(DrawSmugglersResponse drawSmugglersResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawSmugglersResponse.getCard());
                level2Controller.showSmugglersCardPopUp(drawSmugglersResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawSmugglersResponse.getCard());
                tryLevelController.showSmugglersCardPopUp(drawSmugglersResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawStellarSpaceDustResponse(DrawStellarSpaceDustResponse drawStellarSpaceDustResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawStellarSpaceDustResponse.getCard());
                level2Controller.showNoResponseCard(drawStellarSpaceDustResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawStellarSpaceDustResponse.getCard());
                tryLevelController.showNoResponseCard(drawStellarSpaceDustResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawWarZoneResponse(DrawWarZoneResponse drawWarZoneResponse) {

        // TODO: Check if the switch is necessary

        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(drawWarZoneResponse.getCard());
                switch (((WarZoneCard)(drawWarZoneResponse.getCard())).getPenalties().get(((WarZoneDTO)(drawWarZoneResponse.getCard().getDTO())).getPenaltyNumber()).getWarZonePenaltyType()){
                    case WarZonePenaltyType.LOWEST_CREW : {
                        level2Controller.showWarZoneCrewPenalty(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                    case  WarZonePenaltyType.LOWEST_FIREPOWER:  {
                        level2Controller.showWarZoneFirePower(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                    case WarZonePenaltyType.LOWEST_THRUST: {
                        level2Controller.showWarZoneThrustPower(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                }

            } else if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(drawWarZoneResponse.getCard());
                switch (((WarZoneCard)(drawWarZoneResponse.getCard())).getPenalties().get(((WarZoneDTO)(drawWarZoneResponse.getCard().getDTO())).getPenaltyNumber()).getWarZonePenaltyType()){
                    case WarZonePenaltyType.LOWEST_CREW : {
                        tryLevelController.showWarZoneCrewPenalty(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                    case  WarZonePenaltyType.LOWEST_FIREPOWER:  {
                        tryLevelController.showWarZoneFirePower(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                    case WarZonePenaltyType.LOWEST_THRUST: {
                        tryLevelController.showWarZoneThrustPower(drawWarZoneResponse.getCard().getImage());
                        break;
                    }
                }

            }
        });
    }

    @Override
    public void handleWarZoneFirePower(CalculateFirePowerResponse calculateFirePowerResponse){
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(calculateFirePowerResponse.getCard());
                level2Controller.showWarZoneFirePower(calculateFirePowerResponse.getCard().getImage());
            } else if(controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(calculateFirePowerResponse.getCard());
                tryLevelController.showWarZoneFirePower(calculateFirePowerResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawAbandonedShipResponse(DrawAbandonedShipResponse drawAbandonedShipResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setBufferedCard(drawAbandonedShipResponse.getCard());
                level2Controller.abandonedShipPopUp(drawAbandonedShipResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.setBufferedCard(drawAbandonedShipResponse.getCard());
                tryLevelController.abandonedShipPopUp(drawAbandonedShipResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleDrawAbandonedStationResponse(DrawAbandonedStationResponse drawAbandonedStationResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setBufferedCard(drawAbandonedStationResponse.getCard());
                level2Controller.abandonedStationPopUp(drawAbandonedStationResponse.getCard().getImage());
            }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.setBufferedCard(drawAbandonedStationResponse.getCard());
                tryLevelController.abandonedStationPopUp(drawAbandonedStationResponse.getCard().getImage());
            }
        });
    }

    //----ONGOINGS----//

    @Override
    public void handleAbandonedShipOngoing(ApplyAbandonedShipOngoingResponse abandonedShipOngoingResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setBufferedCard(abandonedShipOngoingResponse.getCard());
                level2Controller.closeWaitingRoomPopUp();
                AbandonedShipDTO dto = (AbandonedShipDTO) abandonedShipOngoingResponse.getCard().getDTO();
                dto.setPlayerId(controller.getId());
                level2Controller.abandonedShipPopUp(abandonedShipOngoingResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.setBufferedCard(abandonedShipOngoingResponse.getCard());
                tryLevelController.closeWaitingRoomPopUp();
                AbandonedShipDTO dto = (AbandonedShipDTO) abandonedShipOngoingResponse.getCard().getDTO();
                dto.setPlayerId(controller.getId());
                tryLevelController.abandonedShipPopUp(abandonedShipOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleAbandonedStationOngoing(ApplyAbandonedStationOngoingResponse abandonedStationOngoingResponse) {
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.setBufferedCard(abandonedStationOngoingResponse.getCard());
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.abandonedStationPopUp(abandonedStationOngoingResponse.getCard().getImage());
            }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.setBufferedCard(abandonedStationOngoingResponse.getCard());
                tryLevelController.closeWaitingRoomPopUp();
                AbandonedStationDTO dto = (AbandonedStationDTO) abandonedStationOngoingResponse.getCard().getDTO();
                dto.setPlayerId(controller.getId());
                tryLevelController.abandonedStationPopUp(abandonedStationOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleStellarSpaceDustOnGoing(ApplyStellarSpaceDustOngoingResponse stellarSpaceDustOngoingResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(stellarSpaceDustOngoingResponse.getCard());
                level2Controller.showNoResponseCard(stellarSpaceDustOngoingResponse.getCard().getImage());
            }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(stellarSpaceDustOngoingResponse.getCard());
                tryLevelController.showNoResponseCard(stellarSpaceDustOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handlePlanetsOngoingResponse(ApplyPlanetsOngoingResponse planetsOngoingResponse) {

        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(planetsOngoingResponse.getCard());
                level2Controller.planetsPopUp(planetsOngoingResponse.getCard().getImage(),((PlanetsCard)planetsOngoingResponse.getCard()));
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(planetsOngoingResponse.getCard());
                tryLevelController.planetsPopUp(planetsOngoingResponse.getCard().getImage(),((PlanetsCard)planetsOngoingResponse.getCard()));
            }
        });
    }

    @Override
    public void handlePiratesOngoingResponse(ApplyPirateOngoingResponse piratesOngoingResponse) {
        PirateCard card = (PirateCard) piratesOngoingResponse.getCard();
        Platform.runLater(()->{
            if(controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(piratesOngoingResponse.getCard());
                level2Controller.showPirateOngoingPopUp(piratesOngoingResponse.getCard().getImage(), card);
            }
        });
    }

    @Override
    public void handleOnGoingOpenSpaceResponse(ApplyOpenSpaceOngoingResponse applyOpenSpaceOngoingResponse) {
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(applyOpenSpaceOngoingResponse.getCard());
                level2Controller.showOpenSpace(applyOpenSpaceOngoingResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(applyOpenSpaceOngoingResponse.getCard());
                tryLevelController.showOpenSpace(applyOpenSpaceOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleOngoingSlaversResponse(ApplySlaversOngoingResponse applySlaversOngoingResponse) {
        Platform.runLater(()->{
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(applySlaversOngoingResponse.getCard());
                level2Controller.showSlaversCardPopUp(applySlaversOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleOngoingSmugglersResponse(ApplySmugglersOngoingResponse applySmugglersOngoingResponse) {
        Platform.runLater(()->{
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(applySmugglersOngoingResponse.getCard());
                level2Controller.showSmugglersCardPopUp(applySmugglersOngoingResponse.getCard().getImage());
            }else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(applySmugglersOngoingResponse.getCard());
                tryLevelController.showSmugglersCardPopUp(applySmugglersOngoingResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleWarZoneEffectOnGoing(ApplyWarZoneEffectResponseOngoing applyWarZoneEffectResponseOngoing){
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(applyWarZoneEffectResponseOngoing.getCard());
                level2Controller.showWarZonePenalty(applyWarZoneEffectResponseOngoing.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(applyWarZoneEffectResponseOngoing.getCard());
                tryLevelController.showWarZonePenalty(applyWarZoneEffectResponseOngoing.getCard().getImage());
            }
        });
    }

    @Override
    public void handleCalculateThrustPower(CalculateThrustPowerResponse calculateThrustPowerResponse){
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller){
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(calculateThrustPowerResponse.getCard());
                level2Controller.showWarZoneThrustPower(calculateThrustPowerResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController){
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(calculateThrustPowerResponse.getCard());
                tryLevelController.showWarZoneThrustPower(calculateThrustPowerResponse.getCard().getImage());
            }
        });
    }

    @Override
    public void handleWarZoneProjectileOnGoing(ApplyWarZoneProjectileOngoingResponse applyWarZoneProjectileOngoingResponse){
        Platform.runLater(() -> {
            if (controller.getPage() instanceof BuildingShipPhaseLevel2Controller level2Controller) {
                level2Controller.closeWaitingRoomPopUp();
                level2Controller.setBufferedCard(applyWarZoneProjectileOngoingResponse.getCard());
                level2Controller.showWarZoneProjectilePenalty(applyWarZoneProjectileOngoingResponse.getCard().getImage());
            } else if (controller.getPage() instanceof BuildingShipPhaseTryLevelController tryLevelController) {
                tryLevelController.closeWaitingRoomPopUp();
                tryLevelController.setBufferedCard(applyWarZoneProjectileOngoingResponse.getCard());
                tryLevelController.showWarZoneProjectilePenalty(applyWarZoneProjectileOngoingResponse.getCard().getImage());
            }
        });
    }
}
