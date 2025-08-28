package it.polimi.ingsoftware.ll13.client.view.gui;

import it.polimi.ingsoftware.ll13.client.controller.GuiController;
import it.polimi.ingsoftware.ll13.client.handlers.GuiHandler;
import it.polimi.ingsoftware.ll13.model.GamePhase;
import it.polimi.ingsoftware.ll13.model.cards.cards_objects.*;
import it.polimi.ingsoftware.ll13.model.cards.decks.CardStack;
import it.polimi.ingsoftware.ll13.model.cards.dtos.*;
import it.polimi.ingsoftware.ll13.model.cards.dtos.helper_classes.Coordinates;
import it.polimi.ingsoftware.ll13.model.cards.enumerations.WarZonePenaltyEffect;
import it.polimi.ingsoftware.ll13.model.crew_members.Alien;
import it.polimi.ingsoftware.ll13.model.crew_members.AlienColor;
import it.polimi.ingsoftware.ll13.model.crew_members.CrewMember;
import it.polimi.ingsoftware.ll13.model.crew_members.Human;
import it.polimi.ingsoftware.ll13.model.general_enumerations.PlayerColors;
import it.polimi.ingsoftware.ll13.model.general_enumerations.RankingPosition;
import it.polimi.ingsoftware.ll13.network.requests.ViewShipRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.adventure.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.construction.*;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.EliminateTileRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.PlaceCrewRequest;
import it.polimi.ingsoftware.ll13.network.requests.game_requests.validation.ValidateShipRequest;
import it.polimi.ingsoftware.ll13.network.response.Dtos.PlayerDTO;
import javafx.animation.*;
import it.polimi.ingsoftware.ll13.model.ship_board.helpers.TileCoordinates;
import it.polimi.ingsoftware.ll13.model.tiles.tiles_objects.Tile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class BuildingShipPhaseLevel2Controller implements GuiSceneController {
    private final GuiController controller = GuiController.getInstance();

    @FXML
    public VBox warZoneOptions;
    @FXML
    public Button confirmWarZoneFirePower;
    @FXML
    public Button resetWarZoneFirePower;
    @FXML
    public VBox thrustPowerOptions;
    @FXML
    public ImageView redShip;
    @FXML
    public ImageView blueShip;
    @FXML
    public ImageView yellowShip;
    @FXML
    public ImageView greenShip;
    @FXML
    public VBox projectileWarZoneOptions;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public ImageView tileToPlace;
    @FXML
    public Button rotateButton;
    @FXML
    public Button discardTileButton;
    @FXML
    public Button drawTileButton;
    @FXML
    public Button selectDiscartedTilesButton;
    @FXML
    public Button saveTileButton;
    @FXML
    public ImageView saveTileSpace1;
    @FXML
    public ImageView saveTileSpace2;
    @FXML
    public GridPane gridPane;
    @FXML
    public Button finishBuildingButton;
    @FXML
    public Button filpHourGlassButton;
    @FXML
    public HBox firstHBox;
    @FXML
    public ImageView astronautImage;
    @FXML
    public VBox leftContainer;
    @FXML
    public Pane rightContainer;
    @FXML
    public Label timer;
    @FXML
    public Button savedButton1;
    @FXML
    public Button savedButton2;
    @FXML
    public Pane cardPane;
    @FXML
    public ImageView deckImage1;
    @FXML
    public ImageView deckImage2;
    @FXML
    public ImageView deckImage3;
    @FXML
    public Button deckButton1;
    @FXML
    public Button deckButton2;
    @FXML
    public Button deckButton3;
    @FXML
    public ImageView startConstruction;
    @FXML
    public ImageView magazineImage;
    @FXML
    public ImageView saveTileImage;
    @FXML
    public ImageView drawTileImage;
    @FXML
    public ImageView mapImage;
    @FXML
    public ImageView hourglassImage;
    @FXML
    public Button confirmRotation;
    @FXML
    public ImageView validationImage;
    @FXML
    public Button validationButton;
    @FXML
    public Pane validationPane;
    @FXML
    public Pane crewPane;
    @FXML
    public ImageView humanImage;
    @FXML
    public ImageView purpleAlienImage;
    @FXML
    public ImageView yellowAlienImage;
    @FXML
    public Label humanCount;
    @FXML
    public Label purpleAlienCount;
    @FXML
    public Label yellowAlienCount;

    @FXML
    public VBox openSpaceOptions;
    @FXML
    public Button confirmOpenSpace;
    @FXML
    public Button resetOpenSpace;

    @FXML
    public VBox meteorShowerOptions;
    @FXML
    public Button confirmMeteorShower;
    @FXML
    public Button resetMeteorShower;
    @FXML
    public VBox pirateCannonOptions;
    @FXML
    public Button confirmCannonsButton;
    @FXML
    public VBox pirateShieldOptions;
    @FXML
    public Button confirmShieldButton;
    @FXML
    public VBox slaversCannonOptions;
    @FXML
    public Button confirmSlaversCannonsButton;
    @FXML
    public VBox smugglersCannonOptions;
    @FXML
    public Button confirmSmugglersCannonsButton;



    @FXML
    public VBox shipCounters;
    @FXML
    public Label crewCount;
    @FXML
    public Label batteryCount;
    @FXML
    public Label wasteTileCount;
    @FXML
    public Label resourcesCount;
    @FXML
    public Label creditiCount;
    @FXML
    public Label exposedBoardersCount;

    private Scene adventureCardScene;

    private boolean rocketPlaced = false;
    List<ImageView> rocketTokens = new ArrayList<>();
    private boolean hourglassFlipped = false;
    private boolean validated = false;
    private boolean waitingForValidation = true;
    private boolean readOnlyView = false;


    private Integer numberOfRotations = 0;
    private Integer totalRotations = 0;
    private Tile bufferedTile;
    private Card bufferedCard;
    private List<Tile> bufferedTileList = new ArrayList<>();
    private List<CardStack> bufferedCardStack = new ArrayList<>();
    private Tile savedTile1;
    private Tile savedTile2;
    private int savedIndex = 0;
    public Timeline countdown;
    private int remainingSeconds = 60;
    private int lastPlacementRow = -1;
    private int lastPlacementCol = -1;
    private int rowAdjuster = 5;
    private int colAdjuster = 4;
    private Stage popupStage;
    private GridPane discardedGrid;
    private GamePhase phase = GamePhase.IDLE;
    private List<Coordinates> usingTilesWithBattery;

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }
    public void setBufferedCardStack(List<CardStack> cardStack){
        this.bufferedCardStack = cardStack;
    }
    public void setBufferedTileList(List<Tile> tiles){
        this.bufferedTileList = tiles;
    }
    public void setBufferedCard(Card card){
        this.bufferedCard = card;
    }
    public Stage getPopupStage(){
        return popupStage;
    }
    public void setHourglassFlipped(Boolean flipped){
        this.hourglassFlipped = flipped;
    }
    @Override
    public String getFxml(){
        return "/gui/fxml/BuildingShipPhaseLevel2.fxml";
    }
    @Override
    public List<String> getCss() {
        return List.of(
                "/gui/css/Style.css"
        );
    }
    public void setupInitialVisibility(boolean isHost) {
        firstHBox.setVisible(false);
        leftContainer.setVisible(false);
        rightContainer.setVisible(false);
        drawTileButton.setVisible(false);
        saveTileButton.setVisible(false);
        discardTileButton.setVisible(false);
        selectDiscartedTilesButton.setVisible(false);
        finishBuildingButton.setVisible(false);
        timer.setVisible(false);
        astronautImage.setVisible(true);
        filpHourGlassButton.setVisible(false);
        rotateButton.setVisible(false);
        deckImage1.setVisible(false);
        deckImage2.setVisible(false);
        deckImage3.setVisible(false);
        drawTileImage.setVisible(false);
        magazineImage.setVisible(false);
        saveTileImage.setVisible(false);
        mapImage.setVisible(false);
        hourglassImage.setVisible(false);
        redShip.setVisible(false);
        blueShip.setVisible(false);
        yellowShip.setVisible(false);
        greenShip.setVisible(false);
        redShip.setDisable(true);
        blueShip.setDisable(true);
        yellowShip.setDisable(true);
        greenShip.setDisable(true);

        if (isHost) {
            startConstruction.setVisible(true);
            startConstruction.setOnMouseClicked(e -> {
                startConstruction.setDisable(true);
                startConstruction.setVisible(false);
                controller.send(new StartConstruction(controller.getId()));
            });
        }
    }
    public void startTimer() {
        if(hourglassFlipped){
            filpHourGlassButton.setDisable(true);
        }
        remainingSeconds = 60;
        timer.setText(remainingSeconds + " sec");
        timer.setVisible(true);

        countdown = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingSeconds--;
            timer.setText(remainingSeconds + " sec");

            if (remainingSeconds <= 0) {
                countdown.stop();
            }
        }));
        countdown.setCycleCount(60);
        countdown.play();
    }
    public void showGetReadyPopup() {
        Stage getReadyStage = new Stage();
        getReadyStage.initModality(Modality.APPLICATION_MODAL);
        getReadyStage.setResizable(false);
        getReadyStage.setTitle("Get Ready to Assemble");
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-alignment: center;");
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/Construction.png").toExternalForm()));
        background.setFitWidth(1200);
        background.setFitHeight(800);
        background.setPreserveRatio(false);
        Label getReadyLabel = new Label("PREPARATI A VOLARE");
        getReadyLabel.setStyle(
                "-fx-text-fill: white;" +

                        "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;"
        );
        StackPane.setAlignment(getReadyLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(getReadyLabel, new Insets(0, 100, 300, 0));
        root.getChildren().addAll(background, getReadyLabel);
        Scene scene = new Scene(root, 1200, 800);
        getReadyStage.setScene(scene);
        getReadyStage.setX((rootPane.getScene().getWindow().getX() + rootPane.getScene().getWindow().getWidth() / 2) - 600);
        getReadyStage.setY((rootPane.getScene().getWindow().getY() + rootPane.getScene().getWindow().getHeight() / 2) - 400);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(e -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), root);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(exitEvent -> getReadyStage.close());
                fadeOut.play();
            });
            pause.play();
        });
        fadeIn.play();
        getReadyStage.show();
    }
    public void setConstructionPhase(){
        leftContainer.setVisible(true);
        rightContainer.setVisible(true);
        drawTileButton.setVisible(true);
        saveTileButton.setVisible(true);
        discardTileButton.setVisible(true);
        selectDiscartedTilesButton.setVisible(true);
        finishBuildingButton.setVisible(true);
        timer.setVisible(true);
        filpHourGlassButton.setVisible(true);
        rotateButton.setVisible(true);
        astronautImage.setVisible(false);
        deckImage1.setVisible(true);
        deckImage2.setVisible(true);
        deckImage3.setVisible(true);
        drawTileImage.setVisible(true);
        magazineImage.setVisible(true);
        saveTileImage.setVisible(true);
        mapImage.setVisible(true);
        hourglassImage.setVisible(true);
        filpHourGlassButton.setDisable(true);
        savedButton2.setVisible(true);
        firstHBox.setVisible(true);
        redShip.setVisible(true);
        blueShip.setVisible(true);
        yellowShip.setVisible(true);
        greenShip.setVisible(true);
        redShip.setDisable(false);
        blueShip.setDisable(false);
        yellowShip.setDisable(false);
        greenShip.setDisable(false);
        initializeRockets();
        startTimer();
    }
    public void initializeRockets(){
        redShip.setOnMouseClicked(event -> sendViewShipRequest(PlayerColors.RED));
        blueShip.setOnMouseClicked(event -> sendViewShipRequest(PlayerColors.BLUE));
        yellowShip.setOnMouseClicked(event -> sendViewShipRequest(PlayerColors.YELLOW));
        greenShip.setOnMouseClicked(event -> sendViewShipRequest(PlayerColors.GREEN));
    }
    private void sendViewShipRequest(PlayerColors color) {
        controller.send(new ViewShipRequest(controller.getId(), color));
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnlyView = readOnly;
        for (Node node : gridPane.getChildren()) {
            node.setDisable(readOnly);
        }
        if(phase == GamePhase.SETUP) {
            if(rocketPlaced){
               return;
            }
            drawTileButton.setDisable(readOnly);
            saveTileButton.setDisable(readOnly);
            discardTileButton.setDisable(readOnly);
            selectDiscartedTilesButton.setDisable(readOnly);
            finishBuildingButton.setDisable(readOnly);
            rotateButton.setDisable(readOnly);
            confirmRotation.setDisable(readOnly);
        } else if (phase == GamePhase.VALIDATION) {
            finishBuildingButton.setDisable(false);
            validationButton.setDisable(readOnly);
        } else if (phase == GamePhase.CREW_PHASE) {
            finishBuildingButton.setDisable(false);
            humanImage.setDisable(readOnly);
            purpleAlienCount.setDisable(readOnly);
            yellowAlienCount.setDisable(readOnly);
        }
    }

    @FXML
    public void rotateTileToPlace(ActionEvent event) {
        if(tileToPlace.getImage() == null){
            return;
        }
        numberOfRotations++;
    }
    @FXML
    public void confirmRotation(ActionEvent actionEvent) {
        if(bufferedTile == null || numberOfRotations == 0){
            return;
        }
        controller.send(new RotateTileRequest(controller.getId(), bufferedTile, numberOfRotations));
    }
    public void showRotatedTile(Tile tile){
        this.bufferedTile = tile;
        totalRotations = (totalRotations + numberOfRotations)%4;
        tileToPlace.setImage(new Image(getClass().getResource(tile.getImage()).toExternalForm()));
        tileToPlace.setRotate(totalRotations * 90);
        numberOfRotations = 0;
    }
    @FXML
    public void clearTileToPlace(ActionEvent event) {
        if(bufferedTile == null){
            return;
        }
        tileToPlace.setImage(null);
        numberOfRotations = 0;
        totalRotations = 0;
        DiscardRequest discardRequest = new DiscardRequest(controller.getId(), bufferedTile);
        controller.send(discardRequest);
        bufferedTile = null;
    }
    @FXML
    public void drawTile(ActionEvent event) {
        if(tileToPlace.getImage() != null){
            return;
        }
        numberOfRotations = 0;
        totalRotations = 0;
        controller.send(new DrawTileRequest(controller.getId(), false, -1));
    }
    public void handleDrawnTile(Tile tile){
        this.bufferedTile = tile;
        Image image = new Image(getClass().getResource(tile.getImage()).toExternalForm());
        tileToPlace.setImage(image);
        tileToPlace.setRotate(tile.getRotation() * 90);
        resetRotation();
    }
    @FXML
    public void showDiscardedTiles(ActionEvent event) {
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Discarded Tiles");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(600, 400);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        discardedGrid = new GridPane();
        discardedGrid.setHgap(20);
        discardedGrid.setVgap(10);
        discardedGrid.setPadding(new Insets(20));
        discardedGrid.setAlignment(Pos.CENTER);

        // Set background image directly on the GridPane so it scrolls with content
        Image bgImage = new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.REPEAT, // or NO_REPEAT if you prefer
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, false)
        );
        discardedGrid.setBackground(new Background(backgroundImage));

        renderDiscardedTiles();

        scrollPane.setContent(discardedGrid);

        Scene scene = new Scene(scrollPane, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/gui/css/Style.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/gui/images/icon.png"));
        popupStage.getIcons().add(icon);

        popupStage.setScene(scene);
        popupStage.setResizable(false);
        scrollPane.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), scrollPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Handle fade-out when closing
        popupStage.setOnCloseRequest(e -> {
            e.consume(); // prevent immediate close
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), scrollPane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> popupStage.close());
            fadeOut.play();
        });

        // Optional: close on ESC key with fade-out
        scene.setOnKeyPressed(event1 -> {
            if (event1.getCode() == KeyCode.ESCAPE) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), scrollPane);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(ev -> popupStage.close());
                fadeOut.play();
            }
        });

        popupStage.showAndWait();

    }
    public void renderDiscardedTiles() {
        discardedGrid.getChildren().clear();

        int cols = 5;
        for (int i = 0; i < bufferedTileList.size(); i++) {
            int row = i / cols;
            int col = i % cols;

            Tile tile = bufferedTileList.get(i);
            ImageView imageView = new ImageView();
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setImage(new Image(getClass().getResource(tile.getImage()).toExternalForm()));

            Button selectButton = new Button("Select");
            int tileIndex = i;
            selectButton.setOnAction(e -> {
                if(tileToPlace.getImage() != null){
                    return;
                }
                DrawTileRequest drawTileRequest = new DrawTileRequest(controller.getId(), true, tileIndex);
                controller.send(drawTileRequest);
                popupStage.close();
            });

            discardedGrid.add(imageView, col, row * 2);
            GridPane.setHalignment(imageView, HPos.CENTER);
            discardedGrid.add(selectButton, col, row * 2 + 1);
            GridPane.setHalignment(selectButton, HPos.CENTER);

        }
    }
    @FXML
    public void saveTile(ActionEvent event) {
        if (tileToPlace.getImage() != null) {
            if (saveTileSpace1.getImage() == null) {
                PlaceTileRequest placeTileRequest = new PlaceTileRequest(controller.getId(),5,9,bufferedTile);
                controller.send(placeTileRequest);
            }
            else if (saveTileSpace2.getImage() == null) {
                PlaceTileRequest placeTileRequest = new PlaceTileRequest(controller.getId(),5,10,bufferedTile);
                controller.send(placeTileRequest);
            }
        }
    }
    @FXML
    public void gridButtonClicked(ActionEvent event) {
        switch (phase){
            case SETUP -> {
                if(bufferedTile == null){
                    return;
                }
                Button clickedButton = (Button) event.getSource();
                int rowIndex = GridPane.getRowIndex(clickedButton);
                int columnIndex = GridPane.getColumnIndex(clickedButton);
                lastPlacementRow = rowIndex;
                lastPlacementCol = columnIndex;
                int serverRow = rowIndex + rowAdjuster;
                int serverCol = columnIndex + colAdjuster;
                controller.send(new PlaceTileRequest(
                        controller.getId(),
                        serverRow,
                        serverCol,
                        bufferedTile
                ));
            }
            case VALIDATION -> {
                if(!waitingForValidation){
                    Button clickedButton = (Button) event.getSource();
                    int rowIndex = GridPane.getRowIndex(clickedButton);
                    int columnIndex = GridPane.getColumnIndex(clickedButton);
                    int serverRow = rowIndex + rowAdjuster;
                    int serverCol = columnIndex + colAdjuster;
                    controller.send(new EliminateTileRequest(
                            controller.getId(),
                            serverRow,
                            serverCol
                    ));
                }

            }
            case CREW_PHASE -> {

            }
            case IDLE -> {
                break;
            }

            case FLIGHT -> {
                Button clickedButton = (Button) event.getSource();
                clickedButton.setDisable(true);
                int rowIndex = GridPane.getRowIndex(clickedButton);
                int columnIndex = GridPane.getColumnIndex(clickedButton);
                int serverRow = rowIndex;
                int serverCol = columnIndex;
                usingTilesWithBattery.add(new Coordinates(serverRow, serverCol));
                clickedButton.setStyle(
                        "-fx-border-color: yellow; " +
                                "-fx-border-width: 3px; " +
                                "-fx-background-color: transparent;"
                );

                break;
            }
            default -> System.out.println("Error clicking grid button");
        }
    }
    public void placeTile(Tile tile, int row, int col){
        ImageView target = findImageViewAt(row, col);
        if (target != null) {
            Image img = new Image(getClass().getResource(tile.getImage()).toExternalForm());
            target.setImage(null);
            target.setImage(img);
            target.setRotate(tile.getRotation() * 90);
        }

    }
    public void updateShipView(List<TileCoordinates> updatedTiles){
        if(phase == GamePhase.VALIDATION){
            waitingForValidation = true;
        }
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);
                if (row != null && col != null && !(row == 2 && col == 3)) {
                    ((ImageView) node).setImage(null);
                }
            }
        }
        for(TileCoordinates tileCoordinates : updatedTiles){
            if(tileCoordinates.getRow() == 0 && tileCoordinates.getCol() == 5){
                savedTile1 = tileCoordinates.getTile();
                saveTileSpace1.setImage(new Image(getClass().getResource(tileCoordinates.getTile().getImage()).toExternalForm()));
            } else if (tileCoordinates.getRow() == 0 && tileCoordinates.getCol() == 6) {
                savedTile2 = tileCoordinates.getTile();
                saveTileSpace2.setImage(new Image(getClass().getResource(tileCoordinates.getTile().getImage()).toExternalForm()));
            }else{
                placeTile(tileCoordinates.getTile(), tileCoordinates.getRow(), tileCoordinates.getCol());
            }
        }
        if(tileToPlace != null){
            tileToPlace.setImage(null);
        }
        bufferedTile = null;
        if(phase == GamePhase.FLIGHT){
            //i will update the labels
        }
    }
    public void showPlacementWarning() {
        Button target = findButtonAt(lastPlacementRow, lastPlacementCol);
        if (target == null) return;
        target.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-background-color: transparent;");
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(10);
        glow.setSpread(0.3);
        target.setEffect(glow);
        FadeTransition ft = new FadeTransition(Duration.seconds(2), target);
        ft.setFromValue(1.0);
        ft.setToValue(1.0);
        ft.setOnFinished(_ -> target.setStyle("-fx-background-color: transparent;"));
        ft.play();
    }
    private Button findButtonAt(int row, int col) {
        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (rowIndex != null && rowIndex == row && colIndex != null && colIndex == col && node instanceof Button) {
                return (Button) node;
            }
        }
        return null;
    }
    private ImageView findImageViewAt(int rowIndex, int columnIndex) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == rowIndex && GridPane.getColumnIndex(node) == columnIndex) {
                if (node instanceof ImageView) {
                    return (ImageView) node;
                }
            }
        }
        return null;
    }
    private void resetRotation(){
        double currentRotation = tileToPlace.getRotate();
        tileToPlace.setRotate(currentRotation - 90 * numberOfRotations);
        numberOfRotations = 0;
    }
    @FXML
    public void placeRocketOnMap(ActionEvent event) {
        if(tileToPlace.getImage() != null){
            return;
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Map");

        // Background image
        ImageView background = new ImageView(
                new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm())
        );
        background.setFitWidth(1000);
        background.setFitHeight(600);
        background.setPreserveRatio(false);

        // Main map image
        ImageView image = new ImageView(
                new Image(getClass().getResource("/cardboard/cardboard-5.png").toExternalForm())
        );
        image.setFitWidth(600);
        image.setFitHeight(450);
        image.setPreserveRatio(true);
        image.setEffect(new DropShadow(30, Color.CYAN));

        // Close button
        Button closeButton = new Button("\u2716");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> stage.close());
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));

        VBox vbox = new VBox(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(image);

        // AnchorPane with tokens
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMouseTransparent(false);
        anchorPane.setPrefSize(400, 300);
        anchorPane.setTranslateX(200);
        anchorPane.setTranslateY(105);

        double[][] positions = {
                {95, 40}, {128, 25}, {173, 15}, {218, 10}, {263, 10}, {308, 15},
                {353, 25}, {393, 40}, {428, 65}, {458, 115}, {453, 165}, {428, 195},
                {383, 225}, {343, 235}, {298, 245}, {253, 250}, {208, 250}, {163, 245},
                {123, 240}, {78, 225}, {38, 195}, {8, 155}, {13, 110}, {48, 65}
        };
        anchorPane.getChildren().clear(); // remove old tokens & buttons
        rocketTokens.clear();
        for (int i = 0; i < positions.length; i++) {
            ImageView token = new ImageView();
            token.setFitWidth(50);
            token.setFitHeight(50);
            token.setPreserveRatio(true);
            token.setPickOnBounds(true);
            token.setLayoutX(positions[i][0]);
            token.setLayoutY(positions[i][1]);
            token.setId("mapPosition_" + i);
            anchorPane.getChildren().add(token);
            rocketTokens.add(token);
        }
        updateAllRocketPositions(GuiHandler.getInstance().getLatestPlayerPositions());

        // Position buttons
        Button startPositionButton1 = new Button();
        startPositionButton1.getStyleClass().add("transparent-button"); //4
        startPositionButton1.setLayoutX(110);
        startPositionButton1.setLayoutY(45);
        startPositionButton1.setOnAction(e->{
            if(rocketPlaced){
                return;
            }
            controller.send(new PlaceRocketRequest(controller.getId(), RankingPosition.Fourth));
        });

        Button startPositionButton2 = new Button();
        startPositionButton2.getStyleClass().add("transparent-button"); //3
        startPositionButton2.setLayoutX(155);
        startPositionButton2.setLayoutY(30);
        startPositionButton2.setOnAction(e->{
            if(rocketPlaced){
                return;
            }
            controller.send(new PlaceRocketRequest(controller.getId(), RankingPosition.Third));
        });



        Button startPositionButton3 = new Button();
        startPositionButton3.getStyleClass().add("transparent-button"); //2
        startPositionButton3.setLayoutX(240);
        startPositionButton3.setLayoutY(15);
        startPositionButton3.setOnAction(e->{
            if(rocketPlaced){
                return;
            }
            controller.send(new PlaceRocketRequest(controller.getId(), RankingPosition.Second));
        });

        Button startPositionButton4 = new Button();
        startPositionButton4.getStyleClass().add("transparent-button");
        startPositionButton4.setLayoutX(375);
        startPositionButton4.setLayoutY(30);
        startPositionButton4.setOnAction(e->{
            if(rocketPlaced){
                return;
            }
            controller.send(new PlaceRocketRequest(controller.getId(), RankingPosition.First));
        });

        anchorPane.getChildren().addAll(startPositionButton1, startPositionButton2, startPositionButton3, startPositionButton4);

        StackPane root = new StackPane(background, vbox, anchorPane, closeButton);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );

        Scene scene = new Scene(root, 1000, 600);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/gui/css/Style.css").toExternalForm());

        root.setOpacity(0);
        root.setScaleX(0.7);
        root.setScaleY(0.7);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), root);
        scaleIn.setFromX(0.7);
        scaleIn.setFromY(0.7);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        new ParallelTransition(fadeIn, scaleIn).play();
    }
    public void updateAllRocketPositions(List<PlayerDTO> allPlayers) {
        for (PlayerDTO dto : allPlayers) {
            updateMap(dto);
        }
    }
    public void updateMap(PlayerDTO playerDTO){
        int position = playerDTO.getPosition();
        if(playerDTO.getId() == controller.getId() && playerDTO.getPosition() != -1){
            rocketPlaced = true;
            disableConstructionButtons();
        }
        if(position >= 0 && position < rocketTokens.size()){
            ImageView image = rocketTokens.get(position);
            PlayerColors color = playerDTO.getColor();
            String path;
            switch (color){
                case RED -> path = "/gui/images/redRocket1.png";
                case BLUE -> path = "/gui/images/bluRocker.png";
                case GREEN -> path = "/gui/images/GreenRocket.png";
                case YELLOW -> path = "/gui/images/yelloRocket.png";
                default -> path = "";
            }
            Image rocketImage = new Image(getClass().getResource(path).toExternalForm());
            try{
                image.setImage(rocketImage);
            } catch (Exception e) {
                System.out.println("Error");
            }
        }else{
            return;
        }

    }
    public void disableConstructionButtons(){
        drawTileButton.setDisable(true);
        rotateButton.setDisable(true);
        savedButton1.setDisable(true);
        savedButton2.setDisable(true);
        discardTileButton.setDisable(true);
        confirmRotation.setDisable(true);
        selectDiscartedTilesButton.setDisable(true);
        saveTileButton.setDisable(true);
        if(!hourglassFlipped){
            filpHourGlassButton.setDisable(false);
        }

    }
    @FXML
    public void flipHourGlass(ActionEvent event) {
        if(hourglassFlipped){
            return;
        }
        FlipHourGlassRequest flipHourGlassRequest = new FlipHourGlassRequest(controller.getId());
        controller.send(flipHourGlassRequest);
    }
    public void showCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) return;
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Carte Avventura");
        HBox cardBox = new HBox(30);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(40));
        cardBox.setStyle("-fx-background-color: transparent");
        for (Card card : cards) {
            try {
                String imagePath = card.getImage();
                Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                imageView.setEffect(new DropShadow(30, Color.CYAN));
                cardBox.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("⚠ image not found: " + card.getImage());
            }
        }
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> popupStage.close());
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(800);
        background.setFitHeight(400);
        background.setPreserveRatio(false);
        StackPane root = new StackPane(background,cardBox, closeButton);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );
        Scene scene = new Scene(root, 800, 400);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/gui/css/Style.css").toExternalForm());
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        root.setOpacity(0);
        root.setScaleX(0.7);
        root.setScaleY(0.7);
        popupStage.show();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), root);
        scaleIn.setFromX(0.7);
        scaleIn.setFromY(0.7);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        new ParallelTransition(fadeIn, scaleIn).play();
    }
    public void placeStartingCabinTile(PlayerColors color){
        String tilePath;
        switch(color){
            case YELLOW -> tilePath = "/tiles/GT-tiles_133.jpg";
            case RED -> tilePath = "/tiles/GT-tiles_134.jpg";
            case BLUE -> tilePath = "/tiles/GT-tiles_135.jpg";
            case GREEN -> tilePath = "/tiles/GT-tiles_136.jpg";
            default -> throw new IllegalStateException("Unexpected color: "+color);
        }
        ImageView cabinTile = new ImageView(new Image(getClass().getResource(tilePath).toExternalForm()));
        cabinTile.setFitWidth(100);
        cabinTile.setFitHeight(174);
        cabinTile.setPreserveRatio(true);
        int centerRow = 2;
        int centerColumn = 3;
        GridPane.setRowIndex(cabinTile, centerRow);
        GridPane.setColumnIndex(cabinTile, centerColumn);
        gridPane.getChildren().add(cabinTile);
    }
    @FXML
    public void onSavedSlot1Clicked(ActionEvent actionEvent) {
        if(tileToPlace.getImage() != null){
            return;
        }
        if(saveTileSpace1.getImage() != null && savedTile1 != null){
            controller.send(new DrawTileTempRequest(controller.getId(),5,9));
            savedIndex = 1;
        }
    }
    @FXML
    public void onSavedSlot2Clicked(ActionEvent actionEvent){
        if(tileToPlace.getImage() != null){
            return;
        }
        if(saveTileSpace2.getImage() != null && savedTile2 != null){
            controller.send(new DrawTileTempRequest(controller.getId(),5,10));
            savedIndex = 2;
        }
    }
    public void handleDrawFromTemp(Tile tile){
        this.bufferedTile = tile;
        if(savedIndex == 1){
            savedTile1 = null;
            saveTileSpace1.setImage(null);
        } else if (savedIndex == 2) {
            savedTile2 = null;
            saveTileSpace2.setImage(null);
        }
        Image image = new Image(getClass().getResource(tile.getImage()).toExternalForm());
        tileToPlace.setImage(image);
        tileToPlace.setRotate(tile.getRotation() * 90);
        resetRotation();
    }
    @FXML
    public void seeCards1(ActionEvent actionEvent) {
        try {
            showCards(bufferedCardStack.get(0).getCards());
        } catch (Exception e) {
            e.printStackTrace(); // <-- this will print the REAL cause
        }
    }
    @FXML
    public void seeCards2(ActionEvent actionEvent) {
        showCards(bufferedCardStack.get(1).getCards());

    }
    @FXML
    public void seeCards3(ActionEvent actionEvent) {
        showCards(bufferedCardStack.get(2).getCards());
    }
    public void handleValidationPhase(){
        leftContainer.setVisible(false);
        firstHBox.setVisible(false);
        cardPane.setVisible(false);
        timer.setVisible(false);
        hourglassImage.setVisible(false);
        filpHourGlassButton.setVisible(false);
        drawTileImage.setVisible(false);
        drawTileButton.setVisible(false);
        drawTileButton.setVisible(false);
        validationPane.setVisible(true);
        tileToPlace.setImage(null);
    }
    @FXML
    public void validate(ActionEvent actionEvent){
        ValidateShipRequest validateShipRequest = new ValidateShipRequest(controller.getId());
        waitingForValidation = true;
        controller.send(validateShipRequest);
    }
    public void highlightInvalidTiles(List<TileCoordinates> invalidTiles) {
        waitingForValidation = false;
        for (TileCoordinates coord : invalidTiles) {
            int row = coord.getRow();
            int col = coord.getCol();
            Button target = findButtonAt(row, col);
            if (target == null) continue;

            target.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-background-color: transparent;");
            DropShadow glow = new DropShadow();
            glow.setColor(Color.RED);
            glow.setRadius(10);
            glow.setSpread(0.3);
            target.setEffect(glow);
            FadeTransition ft = new FadeTransition(Duration.seconds(2), target);
            ft.setFromValue(1.0);
            ft.setToValue(1.0);
            ft.setOnFinished(_ -> target.setStyle("-fx-background-color: transparent;"));
            ft.play();
        }
    }
    public void shipValidated(){
        validated = true;
        validationButton.setVisible(false);
        validationButton.setDisable(false);
    }
    public void setupCrewPlacementPhase(){
        crewPane.setVisible(true);
        validationImage.setVisible(false);
        setupCrewPlacementDragAndDrop();
        setupCrewDrag(humanImage,"HUMAN");
        setupCrewDrag(purpleAlienImage,"PURPLE_ALIEN");
        setupCrewDrag(yellowAlienImage,"YELLOW_ALIEN");
        Platform.runLater(()->{
            purpleAlienCount.setText("0");
            yellowAlienCount.setText("0");
        });
        redShip.setVisible(false);
        blueShip.setVisible(false);
        greenShip.setVisible(false);
        yellowShip.setVisible(false);

    }
    private void setupCrewDrag(ImageView imageView, String crewType) {
        imageView.setOnDragDetected(event -> {
            Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(crewType);
            Image original = imageView.getImage();
            if (original != null) {
                Image dragImage = new Image(original.getUrl(), 120, 120, true, true);
                db.setDragView(dragImage);
            }
            db.setContent(content);
            event.consume();
        });
    }
    public void setupCrewPlacementDragAndDrop(){
        if (phase != GamePhase.CREW_PHASE) return;

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setOnDragOver(event -> {
                    if (event.getGestureSource() != button && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });

                button.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        String crewType = db.getString();
                        int row = GridPane.getRowIndex(button) + rowAdjuster;
                        int col = GridPane.getColumnIndex(button) + colAdjuster;

                        CrewMember crew;
                        switch (crewType) {
                            case "HUMAN" -> crew = new Human();
                            case "PURPLE_ALIEN" -> crew = new Alien(AlienColor.PURPLE);
                            case "YELLOW_ALIEN" -> crew = new Alien(AlienColor.YELLOW);
                            default -> throw new IllegalStateException("Unknown crew type: " + crewType);
                        }

                        PlaceCrewRequest placeCrewRequest = new PlaceCrewRequest(controller.getId(),row,col,crew);
                        controller.send(placeCrewRequest);
                        success = true;
                    }
                    event.setDropCompleted(success);
                    event.consume();
                });
            }
        }
    }
    public void highlightCabinTiles(List<TileCoordinates> cabinTiles) {
        for (TileCoordinates coord : cabinTiles) {
            int row = coord.getRow();
            int col = coord.getCol();
            Button target = findButtonAt(row, col);
            if (target == null) continue;

            target.setStyle("-fx-border-color: #B8860B; -fx-border-width: 3px; -fx-background-color: transparent;");
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GOLD);
            glow.setRadius(10);
            glow.setSpread(0.3);
            target.setEffect(glow);
            FadeTransition ft = new FadeTransition(Duration.seconds(2), target);
            ft.setFromValue(1.0);
            ft.setToValue(1.0);
            ft.setOnFinished(_ -> target.setStyle("-fx-background-color: transparent;"));
            ft.play();
        }
    }
    public void updateCrewOnResponse(Integer hc, Integer pal, Integer yac){
        humanCount.setText(hc.toString());
        purpleAlienCount.setText(pal.toString());
        yellowAlienCount.setText(yac.toString());
    }

    // -----------------------------------------------------
    //                  CARD-SECTION
    // -----------------------------------------------------

    @FXML
    public void setUpAdventurePhase(){
        usingTilesWithBattery = new ArrayList<>();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setDisable(true);
            }
        }
        crewPane.setDisable(true);
        crewPane.setVisible(false);
        shipCounters.setVisible(true);
        redShip.setVisible(true);
        blueShip.setVisible(true);
        greenShip.setVisible(true);
        yellowShip.setVisible(true);
    }
    public void updateCounter(Integer bc, Integer cc, Integer wtc, Integer crgc, Integer ecc, Integer crec){
        if(phase == GamePhase.FLIGHT){
            batteryCount.setText(bc.toString());
            crewCount.setText(cc.toString());
            wasteTileCount.setText(wtc.toString());
            resourcesCount.setText(crgc.toString());
            exposedBoardersCount.setText(ecc.toString());
            creditiCount.setText(crec.toString());
        }

    }
    private void setAdventureCardPopUp( String title){
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
    }

    // Setting up the scene and transition

    private void setAdventureCardScene(Scene scene, Parent root, double width, double height){
        scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/gui/css/Style.css").toExternalForm());
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        root.setOpacity(0);
        root.setScaleX(0.7);
        root.setScaleY(0.7);
        popupStage.show();
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), root);
        scaleIn.setFromX(0.7);
        scaleIn.setFromY(0.7);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        new ParallelTransition(fadeIn, scaleIn).play();
    }
    private void enablePopupDragging(StackPane root) {
        popupStage.initStyle(StageStyle.UNDECORATED); // Ensures no window decorations

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });
    }

    public void drawCardPopUp() {
        setAdventureCardPopUp("Pesca una carta avventura");

        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(900);
        background.setFitHeight(600);
        background.setPreserveRatio(false);

        ImageView cardBack = new ImageView(new Image(getClass().getResource("/cards/GT-cards_2_back_card.jpg").toExternalForm()));
        cardBack.setFitHeight(300);
        cardBack.setPreserveRatio(true);

        StackPane cardContainer = new StackPane(cardBack);
        cardContainer.setPadding(new Insets(10));
        StackPane.setAlignment(cardContainer, Pos.CENTER);
        StackPane.setMargin(cardContainer, new Insets(40, 0, 0, 0));

        Button drawAdventureCardButton = new Button("Pesca Carta");
        drawAdventureCardButton.setStyle(
                "-fx-background-color: #FADA03;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2px;" +
                        "-fx-cursor: hand;"
        );
        drawAdventureCardButton.setOnAction(e -> {
            controller.send(new DrawCardRequest(controller.getId()));
            popupStage.close();
        });
        StackPane.setAlignment(drawAdventureCardButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(drawAdventureCardButton, new Insets(0, 0, 40, 0));

        StackPane root = new StackPane(background, cardContainer, drawAdventureCardButton);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, 900, 600);
    }

    // This pop-up will be shown to the last players in the leaderboard
    // they will wait for the first player to draw the adventure card

    public void waitingAdventureCardPopUp() {
        setAdventureCardPopUp("Avventura in corso...");
        popupStage.initModality(Modality.NONE);
        popupStage.initStyle(StageStyle.UNDECORATED);
        ImageView background = new ImageView(
                new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm())
        );
        background.setFitWidth(900);
        background.setFitHeight(600);
        background.setPreserveRatio(false);

        ImageView cardBackImageView = new ImageView(
                new Image(getClass().getResource("/cards/GT-cards_2_back_card.jpg").toExternalForm())
        );
        cardBackImageView.setPreserveRatio(true);
        cardBackImageView.setFitHeight(300);
        Label waitingLabel = new Label("🪐 In attesa degli altri esploratori...");
        waitingLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-family: 'Segoe UI Semibold';" +
                        "-fx-effect: dropshadow(gaussian, black, 3, 0.5, 1, 1);"
        );

        VBox centerContent = new VBox(30, waitingLabel, cardBackImageView);
        centerContent.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, centerContent);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, 900, 600);
    }
    public void closeWaitingRoomPopUp(){
        if (popupStage != null) popupStage.close();
    }

    public void showMeteorShowerPopUp(String imageUrl) {
        setAdventureCardPopUp("Meteora in arrivo!");
        meteorShowerOptions.setVisible(true);
        meteorShowerOptions.setDisable(false);
        confirmMeteorShower.setDisable(false);
        resetMeteorShower.setVisible(true);
        resetMeteorShower.setDisable(false);
        enableGridInteraction();
        int meteorNumber = ((MeteorShowerDTO) bufferedCard.getDTO()).getMeteorNumber() + 1;
        int direction = ((MeteorShowerDTO) bufferedCard.getDTO()).getMeteorDirectionNumber();
        String instruction = "Meteora numero " + meteorNumber + " in arrivo dalla direzione " + direction;
        showSingleActionPopup(
                "Meteora in arrivo!",
                imageUrl,
                instruction,
                () -> {},
                900, 600
        );
    }

    private Button createStyledButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #FADA03; -fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold; -fx-cursor: hand;");
        button.setPrefSize(100, 50);
        button.setOnAction(handler);
        return button;
    }
    private ImageView createCardImageView(String imageUrl, double height) {
        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView view = new ImageView(cardImage);
        view.setPreserveRatio(true);
        view.setFitHeight(height);
        return view;
    }
    private ImageView createBackground(double width, double height) {
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(width);
        background.setFitHeight(height);
        background.setPreserveRatio(false);
        return background;
    }
    private Button createCloseButton() {
        Button closeButton = new Button("✖");
        closeButton.setDisable(false);
        closeButton.setVisible(true);
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        closeButton.setOnAction(e -> popupStage.close());
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        return closeButton;
    }
    private void enableGridInteraction() {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(false);
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }
        }
    }
    private void showFightFleeCardPopup(String title, String imageUrl, Runnable onFight, Runnable onFlee, double width, double height) {
        setAdventureCardPopUp(title);
        popupStage.initModality(Modality.NONE);
        enableGridInteraction();

        ImageView background = createBackground(width, height);
        ImageView cardImage = createCardImageView(imageUrl, 350);

        Button fightButton = new Button("Combatti");
        fightButton.setStyle("-fx-background-color: #FADA03; -fx-font-weight: bold;");
        fightButton.setOnAction(e -> {
            popupStage.close();
            onFight.run();
        });

        Button fleeButton = new Button("Arrenditi");
        fleeButton.setStyle("-fx-background-color: #FADA03; -fx-font-weight: bold;");
        fleeButton.setOnAction(e -> {
            popupStage.close();
            onFlee.run();
        });
        HBox buttons = new HBox(20, fightButton, fleeButton);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(20, cardImage, buttons);
        content.setAlignment(Pos.CENTER);
        StackPane root = new StackPane(background, content);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");
        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, width, height);
    }

    private void showChoiceCardPopup(String imageUrl, String title, Runnable onAccept, Runnable onDecline, double width, double height) {
        setAdventureCardPopUp(title);
        ImageView background = createBackground(width, height);
        ImageView cardImageView = createCardImageView(imageUrl, 300);

        Button acceptButton = createStyledButton("Accetta", e -> {
            onAccept.run();
            popupStage.close();
        });

        Button declineButton = createStyledButton("Rifiuta", e -> {
            onDecline.run();
            popupStage.close();
        });

        HBox buttonsBox = new HBox(15, acceptButton, declineButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        VBox contentBox = new VBox(20, cardImageView, buttonsBox);
        contentBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, contentBox);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-border-color: #00ffff; -fx-border-width: 4; -fx-border-radius: 18; -fx-background-radius: 18; -fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, width, height);
    }
    private void showSingleActionPopup(String title, String imageUrl, String instruction, Runnable onConfirm, double width, double height) {
        setAdventureCardPopUp(title);
        popupStage.initModality(Modality.NONE);
        enableGridInteraction();
        usingTilesWithBattery.clear();

        ImageView background = createBackground(width, height);
        ImageView cardImage = createCardImageView(imageUrl, 350);
        Label label = new Label(instruction);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 30px;");
        StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
        Button close = createCloseButton();

        VBox content = new VBox(20, cardImage, label);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, content, close);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");

        close.setOnAction(e -> {
            popupStage.close();
            onConfirm.run();
        });

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, width, height);
    }
    private void showPirateShotPopup(String title, String imageUrl, String dynamicText, Runnable onClose, double width, double height) {
        setAdventureCardPopUp(title);
        popupStage.initModality(Modality.NONE);

        enableGridInteraction();
        usingTilesWithBattery.clear();

        pirateCannonOptions.setVisible(false);
        pirateCannonOptions.setDisable(true);
        pirateShieldOptions.setVisible(true);
        pirateShieldOptions.setDisable(false);
        confirmShieldButton.setDisable(false);

        ImageView background = createBackground(width, height);
        ImageView cardImage = createCardImageView(imageUrl, 350);

        Label label = new Label(dynamicText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 30px;");
        VBox content = new VBox(20, cardImage, label);
        content.setAlignment(Pos.CENTER);

        Button close = createCloseButton();
        close.setOnAction(e -> {
            popupStage.close();
            if (onClose != null) onClose.run();
        });
        StackPane root = new StackPane(background, content, close);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, width, height);
    }
    private void showChoiceBoxPopup(String title, String imageUrl, List<Integer> choices, String labelText, Consumer<Integer> onChoiceConfirmed, Runnable onDecline, double width, double height) {
        setAdventureCardPopUp(title);

        ImageView background = createBackground(width, height);
        ImageView cardImage = createCardImageView(imageUrl, 300);

        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Segoe UI Semibold'; -fx-effect: dropshadow(gaussian, #000000, 2, 0, 0, 1);");

        ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(choices);

        Button acceptButton = new Button("Visita");
        acceptButton.setStyle("-fx-background-color: #FADA03; -fx-font-weight: bold;");
        acceptButton.setDisable(true);

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            acceptButton.setDisable(newVal == null);
        });

        acceptButton.setOnAction(e -> {
            onChoiceConfirmed.accept(choiceBox.getValue());
            popupStage.close();
        });

        Button declineButton = new Button("Rifiuta");
        declineButton.setStyle("-fx-background-color: #FADA03; -fx-font-weight: bold;");
        declineButton.setOnAction(e -> {
            onDecline.run();
            popupStage.close();
        });

        HBox buttons = new HBox(15, acceptButton, declineButton);
        buttons.setAlignment(Pos.CENTER);
        VBox content = new VBox(20, cardImage, label, choiceBox, buttons);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(background, content);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-border-color: #00ffff; -fx-border-width: 4; -fx-border-radius: 18; -fx-background-radius: 18; -fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, width, height);
    }

    public void abandonedShipPopUp(String imageUrl) {
        showChoiceCardPopup(imageUrl, "Carta Abbandonata",
                () -> {
                    ((AbandonedShipDTO) bufferedCard.getDTO()).setChoice(true);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyAbandonedShipEffectRequest(controller.getId(), bufferedCard));
                },
                () -> {
                    ((AbandonedShipDTO) bufferedCard.getDTO()).setChoice(false);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyAbandonedShipEffectRequest(controller.getId(), bufferedCard));
                },
                600, 600
        );
    }
    public void abandonedStationPopUp(String imageUrl) {
        showChoiceCardPopup(imageUrl, "Stazione Abbandonata",
                () -> {
                    ((AbandonedStationDTO) bufferedCard.getDTO()).setChoice(true);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyAbandonedStationEffectRequest(controller.getId(), bufferedCard));
                },
                () -> {
                    ((AbandonedStationDTO) bufferedCard.getDTO()).setChoice(false);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyAbandonedStationEffectRequest(controller.getId(), bufferedCard));
                },
                600, 600
        );
    }
    public void showSlaversCardPopUp(String imageUrl) {
        showFightFleeCardPopup("Carta Schiavisti", imageUrl,
                () -> {
                    slaversCannonOptions.setVisible(true);
                    slaversCannonOptions.setDisable(false);
                    confirmSlaversCannonsButton.setDisable(false);
                    enableGridInteraction();
                },
                () -> {
                    SlaversDTO dto = (SlaversDTO) bufferedCard.getDTO();
                    dto.setChoice(false);
                    dto.setPlayerId(controller.getId());
                    controller.send(new ApplySlaversEffectRequest(controller.getId(), bufferedCard));
                },
                900, 600
        );
    }


    @FXML
    public void confirmSlaversCannons(ActionEvent event){
        Button pressedButton = (Button) event.getSource();
        pressedButton.setDisable(true);

        List<Coordinates> selectedCannons = new ArrayList<>(usingTilesWithBattery);
        SlaversDTO dto = (SlaversDTO) bufferedCard.getDTO();
        dto.setChoice(true);
        dto.setUsingTilesWithBattery(selectedCannons);
        dto.setPlayerId(controller.getId());
        controller.send(new ApplySlaversEffectRequest(controller.getId(), bufferedCard));

        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                button.setDisable(true);
            }
        }

        slaversCannonOptions.setVisible(false);
        slaversCannonOptions.setDisable(true);
    }

    // Pirate pop-up
    public void showPirateCardPopUp(String imageUrl) {
        showFightFleeCardPopup("Carta Pirati", imageUrl,
                () -> {
                    pirateCannonOptions.setVisible(true);
                    pirateCannonOptions.setDisable(false);
                    confirmCannonsButton.setDisable(false);
                    enableGridInteraction();
                },
                () -> {
                    PiratesDTO dto = (PiratesDTO) bufferedCard.getDTO();
                    dto.setChoice(false);
                    if (dto.getFireShotNumber() == -1) dto.setFireShotNumber(0);
                    showPirateOngoingPopUp(bufferedCard.getImage(), (PirateCard) bufferedCard);
                },
                900, 600
        );
    }



    @FXML
    public void confirmPirateCannons(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        pressedButton.setDisable(true);

        List<Coordinates> selectedCannons = new ArrayList<>(usingTilesWithBattery);
        ((PiratesDTO) bufferedCard.getDTO()).setChoice(true);
        ((PiratesDTO) bufferedCard.getDTO()).setUsingDoubleCannons(selectedCannons);
        controller.send(new ApplyPirateEffectRequest(controller.getId(), bufferedCard));

        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                button.setDisable(true);
            }
        }

        pirateCannonOptions.setVisible(false);
        pirateCannonOptions.setDisable(true);
    }
    public void showPirateOngoingPopUp(String imageUrl, PirateCard pirateCard) {
        int n = pirateCard.getPiratesDTO().getFireShotNumber() + 1;
        int d = pirateCard.getPiratesDTO().getFireShotDirectionNumber();
        showPirateShotPopup("Attacco Pirati!", imageUrl, "Proiettile numero " + n + " in arrivo dalla direzione " + d, null, 900, 600);
    }

    @FXML
    public void confirmPirateShield(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        pressedButton.setDisable(true);

        if (usingTilesWithBattery.isEmpty()) {
            ((PiratesDTO) bufferedCard.getDTO()).setUsingShield(-1, -1);
        } else {
            Coordinates selectedShield = usingTilesWithBattery.getLast();
            ((PiratesDTO) bufferedCard.getDTO()).setUsingShield(selectedShield.getRow(), selectedShield.getCol());
        }

        controller.send(new ApplyPirateEffectRequest(controller.getId(), bufferedCard));

        usingTilesWithBattery.clear();

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                button.setDisable(true);
            }
        }

        pirateShieldOptions.setVisible(false);
        pirateShieldOptions.setDisable(true);
    }
    public void planetsPopUp(String imageUrl, PlanetsCard card) {
        List<Integer> available = new ArrayList<>();
        List<Planet> planets = card.getPlanets();
        for (int i = 0; i < planets.size(); i++) {
            if (!planets.get(i).isChosen()) available.add(i + 1);
        }

        showChoiceBoxPopup("Carta Pianeti", imageUrl, available,
                "Seleziona il pianeta che vuoi visitare:",
                choice -> {
                    ((PlanetsDTO) card.getDTO()).setChoice(choice);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyPlanetsEffectRequest(controller.getId(), card));
                },
                () -> {
                    ((PlanetsDTO) card.getDTO()).setChoice(0);
                    bufferedCard.getDTO().setPlayerId(controller.getId());
                    controller.send(new ApplyPlanetsEffectRequest(controller.getId(), card));
                },
                600, 600
        );
    }

    public void showSmugglersCardPopUp(String imageUrl) {
        showFightFleeCardPopup("Carta Contrabbandieri", imageUrl,
                () -> {
                    smugglersCannonOptions.setVisible(true);
                    smugglersCannonOptions.setDisable(false);
                    confirmSmugglersCannonsButton.setDisable(false);
                    enableGridInteraction();
                },
                () -> {
                    SmugglersDTO dto = (SmugglersDTO) bufferedCard.getDTO();
                    dto.setChoice(false);
                    dto.setPlayerId(controller.getId());
                    controller.send(new ApplySmugglersEffectRequest(controller.getId(), bufferedCard));
                },
                900, 600
        );
    }

    @FXML
    public void confirmSmugglersCannons(ActionEvent event){
        Button pressedButton = (Button) event.getSource();
        pressedButton.setDisable(true);

        List<Coordinates> selectedCannons = new ArrayList<>(usingTilesWithBattery);
        SmugglersDTO dto = (SmugglersDTO) bufferedCard.getDTO();
        dto.setChoice(true);
        dto.setUsingTilesWithBattery(selectedCannons);
        dto.setPlayerId(controller.getId());
        controller.send(new ApplySmugglersEffectRequest(controller.getId(), bufferedCard));

        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button button) {
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                button.setDisable(true);
            }
        }

        smugglersCannonOptions.setVisible(false);
        smugglersCannonOptions.setDisable(true);
    }

    @FXML
    public void resetTilesWithBattery(ActionEvent event) {
        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(false);
            }
        }
    }

    @FXML
    public void confirmMeteorShower(ActionEvent event) {
        Button pressedButton = (Button) event.getSource();
        pressedButton.setDisable(true);
        if (usingTilesWithBattery.isEmpty()) {
            ((MeteorShowerDTO)bufferedCard.getDTO()).setCoordinates(-1, -1);
        } else {
            ((MeteorShowerDTO)bufferedCard.getDTO()).setCoordinates(usingTilesWithBattery.getLast().getRow(), usingTilesWithBattery.getLast().getCol());
        }
        ((MeteorShowerDTO)bufferedCard.getDTO()).setMeteorDirectionNumber(((MeteorShowerDTO)bufferedCard.getDTO()).getMeteorDirectionNumber() );
        controller.send(new ApplyMeteorShowerEffectRequest(controller.getId(), bufferedCard));
        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(false);
            }
        }
        meteorShowerOptions.setVisible(false);
        meteorShowerOptions.setDisable(true);
        confirmMeteorShower.setDisable(true);
        resetMeteorShower.setVisible(false);
        resetMeteorShower.setDisable(true);
    }
    public void showNoResponseCard(String imageUrl) {
        setAdventureCardPopUp("Adventure card");
        ImageView cardImage = createCardImageView(imageUrl, 400);
        Label label = new Label("Chiudere la pagina per applicare l'effetto");
        StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
        Button close = createCloseButton();
        close.setOnAction(e -> {
            popupStage.close();
            if (bufferedCard instanceof EpidemicCard){
                ((EpidemicDTO)bufferedCard.getDTO()).setPlayerId(controller.getId());
                controller.send(new ApplyEpidemicEffectRequest(controller.getId(), bufferedCard));
            }
            else if (bufferedCard instanceof StellarSpaceDustCard){
                ((StellarSpaceDustDTO)bufferedCard.getDTO()).setPlayerId(controller.getId());
                controller.send(new ApplyStellarSpaceDustEffectRequest(controller.getId(), bufferedCard));
            }
        });

        ImageView background = createBackground(600, 600);
        StackPane root = new StackPane();
        root.getChildren().addAll(background, cardImage, close, label);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-border-color: #00ffff; -fx-border-width: 4; -fx-border-radius: 18; -fx-background-radius: 18; -fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);");

        enablePopupDragging(root);
        setAdventureCardScene(adventureCardScene, root, 600, 600);
    }
    public void showOpenSpace(String imageUrl) {
        openSpaceOptions.setVisible(true);
        openSpaceOptions.setDisable(false);
        confirmOpenSpace.setDisable(false);
        resetOpenSpace.setDisable(false);

        showSingleActionPopup(
                "Open Space - Attiva i motori",
                imageUrl,
                "Seleziona i motori che vuoi attivare",
                () -> {},
                900, 600
        );
    }
    @FXML
    public void confirmOpenSpace(ActionEvent e){
        Button pressedButton = (Button) e.getSource();
        OpenSpaceDTO dto = (OpenSpaceDTO) bufferedCard.getDTO();
        dto.setUsingTilesWithBattery(usingTilesWithBattery);
        dto.setPlayerId(controller.getId());
        controller.send(new ApplyOpenSpaceRequest(controller.getId(), bufferedCard));
        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(true);
            }
        }
        openSpaceOptions.setDisable(true);
        openSpaceOptions.setVisible(false);
    }

    private void setPenaltyLabel(Label label, WarZonePenaltyEffect penalty){
        String message = "Subirai la seguente penalità : ";

        switch(penalty){
            case HIT_BY_PROJECTILES : {
                message += "Verrai colpito da proiettili, preparati!\n" + "Arriva il proiettile numero " + (((WarZoneDTO)bufferedCard.getDTO()).getFireShotNumber() + 1) + " nella direzione numero : " + ((WarZoneDTO)bufferedCard.getDTO()).getFireShotDirectionNumber();
                break;
            }
            case LOSE_CREW : {
                message += "\nPerderai " + ((WarZoneCard)bufferedCard).getPenalties().get(((WarZoneDTO)bufferedCard.getDTO()).getPenaltyNumber()).getValue() + " astronauti";
                break;
            }
            case LOSE_CARGOS : {
                message += "\nPerderai " + ((WarZoneCard)bufferedCard).getPenalties().get(((WarZoneDTO)bufferedCard.getDTO()).getPenaltyNumber()).getValue() + " risorse";
                break;
            }
            case MOVE_BACK : {
                message += "\nTorna indietro nella mappa di " + ((WarZoneCard)bufferedCard).getPenalties().get(((WarZoneDTO)bufferedCard.getDTO()).getPenaltyNumber()).getValue() + " caselle";
                break;
            }
        }
        label.setText(message);
        label.setAlignment(Pos.CENTER);
    }

    public void showWarZoneCrewPenalty(String imageUrl){

        if (((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect() == WarZonePenaltyEffect.HIT_BY_PROJECTILES){
            projectileWarZoneOptions.setDisable(false);
            projectileWarZoneOptions.setVisible(true);

            for (Node node :  gridPane.getChildren()) {
                if (node instanceof Button) {
                    ((Button)node).setDisable(false);
                }
            }

        }

        setAdventureCardPopUp("Adventure card");

        // Card image
        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setFitHeight(400);
        StackPane.setAlignment(cardImageView, Pos.CENTER); // Should not be necessary

        // Label

        Label indicationsLabel = new Label();
        setPenaltyLabel(indicationsLabel, ((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect());
        StackPane.setAlignment(indicationsLabel, Pos.BOTTOM_CENTER);

        // Close pop-up button
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> {
            popupStage.close();
            if (((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect() != WarZonePenaltyEffect.HIT_BY_PROJECTILES) {
                controller.send(new ApplyWarZoneEffectRequest(controller.getId(), bufferedCard));
            }
        });
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));

        // Background image
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(1000);
        background.setFitHeight(800);
        background.setPreserveRatio(false);

        // Main stackpane
        StackPane root = new StackPane(background, cardImageView, closeButton, indicationsLabel);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );

        // Enable dragging of the popup window
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });

        // Scene and pop-up set up
        setAdventureCardScene(adventureCardScene, root, 1000, 800);

    }

    public void showWarZoneFirePower(String imageUrl) {

        warZoneOptions.setDisable(false);
        warZoneOptions.setVisible(true);

        for (Node node :  gridPane.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setDisable(false);
            }
        }

        setAdventureCardPopUp("Adventure card");

        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setFitHeight(400);
        StackPane.setAlignment(cardImageView, Pos.CENTER); // Should not be necessary
        Label indicationsLabel = new Label("Penalità per chi ha meno potenza di fuoco \nPrepara i cannoni");
        StackPane.setAlignment(indicationsLabel, Pos.BOTTOM_CENTER);
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> {
            popupStage.close();
        });
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(600);
        background.setFitHeight(600);
        background.setPreserveRatio(false);
        StackPane root = new StackPane(background, cardImageView, closeButton, indicationsLabel);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });
        setAdventureCardScene(adventureCardScene, root, 600, 600);
    }

    public void showWarZonePenalty(String imageUrl) {
        setAdventureCardPopUp("Adventure card");
        if (((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect() == WarZonePenaltyEffect.HIT_BY_PROJECTILES){
            projectileWarZoneOptions.setDisable(false);
            projectileWarZoneOptions.setVisible(true);
            for (Node node :  gridPane.getChildren()) {
                if (node instanceof Button) {
                    ((Button)node).setDisable(false);
                }
            }

        }
        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setFitHeight(400);
        StackPane.setAlignment(cardImageView, Pos.CENTER); // Should not be necessary
        Label indicationsLabel = new Label();
        setPenaltyLabel(indicationsLabel, ((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect());
        StackPane.setAlignment(indicationsLabel, Pos.BOTTOM_CENTER);
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> {
            popupStage.close();
            if (((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect() != WarZonePenaltyEffect.HIT_BY_PROJECTILES) {
                controller.send(new ApplyWarZoneEffectRequest(controller.getId(), bufferedCard));
            }
        });
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(1000);
        background.setFitHeight(800);
        background.setPreserveRatio(false);
        StackPane root = new StackPane(background, cardImageView, closeButton, indicationsLabel);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });
        setAdventureCardScene(adventureCardScene, root, 1000, 800);
    }

    @FXML
    public void confirmWarZoneFirePower(ActionEvent actionEvent) {
        if (usingTilesWithBattery.isEmpty()){
            usingTilesWithBattery.add(new Coordinates(-1, -1));
        }

        ((WarZoneDTO)bufferedCard.getDTO()).setUsingTilesWithBattery(usingTilesWithBattery);
        ((WarZoneDTO)bufferedCard.getDTO()).setPlayerId(controller.getId());
        controller.send(new SendFirePowerRequest(controller.getId(), bufferedCard));
        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(true);
            }
        }
        warZoneOptions.setDisable(true);
        warZoneOptions.setVisible(false);
    }

    public void showWarZoneThrustPower(String imageUrl) {

        thrustPowerOptions.setDisable(false);
        thrustPowerOptions.setVisible(true);

        for (Node node :  gridPane.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setDisable(false);
            }
        }

        setAdventureCardPopUp("Adventure card");
        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setFitHeight(400);
        StackPane.setAlignment(cardImageView, Pos.CENTER); // Should not be necessary
        Label indicationsLabel = new Label("Penalità per chi ha meno potenza motrice \nPrepara i motori");
        indicationsLabel.setAlignment(Pos.CENTER);
        StackPane.setAlignment(indicationsLabel, Pos.BOTTOM_CENTER);
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> {
            popupStage.close();
        });
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(600);
        background.setFitHeight(600);
        background.setPreserveRatio(false);
        StackPane root = new StackPane(background, cardImageView, closeButton, indicationsLabel);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });
        setAdventureCardScene(adventureCardScene, root, 600, 600);
    }

    @FXML
    public void confirmWarZoneThrustPower(ActionEvent e){
        ((WarZoneDTO)bufferedCard.getDTO()).setUsingTilesWithBattery(usingTilesWithBattery);
        bufferedCard.getDTO().setPlayerId(controller.getId());
        controller.send(new SendThrustPowerRequest(controller.getId(), bufferedCard));
        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(true);
            }
        }
        thrustPowerOptions.setDisable(true);
        thrustPowerOptions.setVisible(false);
    }

    public void showWarZoneProjectilePenalty(String imageUrl) {
        setAdventureCardPopUp("Adventure card");

        projectileWarZoneOptions.setDisable(false);
        projectileWarZoneOptions.setVisible(true);

        for (Node node :  gridPane.getChildren()) {
            if (node instanceof Button) {
                ((Button)node).setDisable(false);
            }
        }
        Image cardImage = new Image(getClass().getResource(imageUrl).toExternalForm());
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setPreserveRatio(true);
        cardImageView.setFitHeight(400);
        StackPane.setAlignment(cardImageView, Pos.CENTER);
        Label indicationsLabel = new Label();
        setPenaltyLabel(indicationsLabel, ((WarZoneCard)(bufferedCard)).getPenalties().get(((WarZoneDTO)(bufferedCard.getDTO())).getPenaltyNumber()).getWarZonePenaltyEffect());
        StackPane.setAlignment(indicationsLabel, Pos.BOTTOM_CENTER);
        Button closeButton = new Button("✖");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> {
            popupStage.close();
        });
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10, 15, 0, 0));
        ImageView background = new ImageView(new Image(getClass().getResource("/gui/images/PurpleGalaxy.jpg").toExternalForm()));
        background.setFitWidth(1000);
        background.setFitHeight(800);
        background.setPreserveRatio(false);
        StackPane root = new StackPane(background, cardImageView, closeButton, indicationsLabel);
        root.setPadding(new Insets(25));
        root.setStyle(
                "-fx-border-color: #00ffff;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 18;" +
                        "-fx-background-radius: 18;" +
                        "-fx-background-color: linear-gradient(to bottom, #0e1624, #090d14);"
        );
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            popupStage.setX(event.getScreenX() - xOffset);
            popupStage.setY(event.getScreenY() - yOffset);
        });
        setAdventureCardScene(adventureCardScene, root, 1000, 800);
    }

    @FXML
    public void confirmDefence(ActionEvent actionEvent) {

        ((WarZoneDTO)bufferedCard.getDTO()).setUsingTilesWithBattery(usingTilesWithBattery);
        bufferedCard.getDTO().setPlayerId(controller.getId());

        controller.send(new ApplyWarZoneEffectRequest(controller.getId(), bufferedCard));

        usingTilesWithBattery.clear();
        for (Node node : gridPane.getChildren()){
            if (node instanceof Button) {
                ((Button)node).setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-color: transparent;");
                ((Button)node).setDisable(true);
            }
        }
        projectileWarZoneOptions.setDisable(true);
        projectileWarZoneOptions.setVisible(false);
    }

    public void switchOutOfGameScreen (float points){
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource("/gui/fxml/OutOfGame.fxml"));
        try {
            Parent root = loader.load();
            OutOfGameController outOfGameScene = loader.getController();

            outOfGameScene.setPoints(points);

            controller.setPage(outOfGameScene);
            controller.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchEndGameScreen (List<String> playersName, List<Float> points) {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource("/gui/fxml/EndGame.fxml"));
        try {
            Parent root = loader.load();
            EndGameController endGameScene = loader.getController();

            endGameScene.setLeaderboard(playersName, points);

            controller.setPage(endGameScene);
            controller.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
