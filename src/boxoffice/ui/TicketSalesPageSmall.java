package boxoffice.ui;

import boxoffice.database.Customer;
import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.models.CustomerRepository;
import boxoffice.models.RestrictedRepository;
import boxoffice.models.SeatRepository;
import boxoffice.models.TicketSaleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.sql.SQLException;
import java.util.*;


/**
 * Provides a compact graphical interface for ticket sales and seat management.
 * This class handles seat selection, booking validation, and restriction management
 * for a specific performance.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Interactive seat map with visual status indicators</li>
 *   <li>Wheelchair accessible seat handling with companion seat management</li>
 *   <li>Restriction management mode for staff</li>
 *   <li>Customer information collection and validation</li>
 * </ul>
 *
 * <p>Visual indicators:
 * <ul>
 *   <li>Red: Booked seats</li>
 *   <li>Green: Selected seats</li>
 *   <li>Blue: Wheelchair accessible seats</li>
 *   <li>Orange: Fully restricted seats</li>
 *   <li>Yellow: Partially restricted seats</li>
 * </ul>
 */
public class TicketSalesPageSmall extends VBox {
    private Performance selectedPerformance;
    private ObservableList<Seat> selectedSeats = FXCollections.observableArrayList();
    private final Set<String> bookedSeatIds = new HashSet<>();
    private List<String> accesibleSeatIDs = SeatRepository.getAccessibleSeatIDs();
    private final int maxSeatsSelectable = 11;
    private ComboBox<String> restrictionTypeComboBox;
    private boolean isRestrictedManagementMode = false;
    private final Set<String> fullRestrictedSeats = new HashSet<>();
    private final Set<String> partialRestrictedSeats = new HashSet<>();

    /**
     * Constructs a new ticket sales interface for the specified performance.
     *
     * @param performance the performance to sell tickets for
     * @throws SQLException if database access fails during initialization
     */
    public TicketSalesPageSmall(Performance performance) throws SQLException {
        this.selectedPerformance = performance;
        initializeUI();

        updateAllSeatColors();
    }

    /**
     * Initializes the user interface components including:
     * <ul>
     *   <li>Seat layout visualization</li>
     *   <li>Management controls</li>
     *   <li>Booking confirmation system</li>
     *   <li>Visual key for seat statuses</li>
     * </ul>
     */
    public void initializeUI(){
        // Load already booked seats for this performance
        try {
            Map<String, String> restrictedSeats = RestrictedRepository.getRestrictedSeats(selectedPerformance.getPerformanceId());
            for (Map.Entry<String, String> entry : restrictedSeats.entrySet()) {
                if ("full".equals(entry.getValue())) {
                    fullRestrictedSeats.add(entry.getKey());
                } else if ("partial".equals(entry.getValue())) {
                    partialRestrictedSeats.add(entry.getKey());
                }
            }

            Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitHeight(190);
            logoView.setFitWidth(190);
            logoView.setPreserveRatio(true);
            logoView.setLayoutX(-100);
            logoView.setLayoutY(-50);

            bookedSeatIds.clear();

            bookedSeatIds.addAll(TicketSaleRepository.getBookedSeats(selectedPerformance.getPerformanceId()));

            Pane buttonPane = new Pane();
            buttonPane.setPrefSize(getWidth(), getHeight());

            createStallsSeatsRowN(buttonPane);
            createStallsSeatsRowM(buttonPane);
            createStallsSeatsRowL(buttonPane);
            createStallsSeatsRowK(buttonPane);
            createStallsSeatsRowJ(buttonPane);
            createStallsSeatsRowH(buttonPane);
            createStallsSeatsRowG(buttonPane);
            createStallsSeatsRowF(buttonPane);
            createStallsSeatsRowE(buttonPane);
            createStallsSeatsRowD(buttonPane);
            createStallsSeatsRowC(buttonPane);
            createStallsSeatsRowB(buttonPane);
            createStallsSeatsRowA(buttonPane);

            CheckBox restrictedManagementCheckbox = new CheckBox("Restricted Management");
            restrictedManagementCheckbox.setStyle("-fx-text-fill: white;");

            restrictionTypeComboBox = new ComboBox<>();
            restrictionTypeComboBox.getItems().addAll("full", "partial");
            restrictionTypeComboBox.setValue("full");
            restrictionTypeComboBox.setDisable(true);

            Button saveRestrictionsButton = new Button("Save Restrictions");
            saveRestrictionsButton.setDisable(true);
            saveRestrictionsButton.setOnAction(e -> saveSeatRestrictions());

            HBox managementControls = new HBox(10, restrictedManagementCheckbox, restrictionTypeComboBox, saveRestrictionsButton);
            managementControls.setAlignment(Pos.CENTER_RIGHT);
            managementControls.setLayoutX(600);
            managementControls.setLayoutY(20);

            restrictedManagementCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                isRestrictedManagementMode = newVal;
                restrictionTypeComboBox.setDisable(!newVal);
                saveRestrictionsButton.setDisable(!newVal);
                updateAllSeatColors();
            });

            restrictionTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            });

            Rectangle keyRect = new Rectangle(180,180);
            keyRect.setStroke(Color.BLACK);
            keyRect.setFill(Color.TRANSPARENT);
            keyRect.setStrokeWidth(2);
            keyRect.setX(-100);
            keyRect.setY(300);

            Label keyLabel = new Label("Key Table");
            keyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-text-fill: white;");
            keyLabel.setLayoutX(-30);
            keyLabel.setLayoutY(310);

            Rectangle orangeRect = new Rectangle(10,10);
            orangeRect.setFill(Color.ORANGE);
            orangeRect.setStroke(Color.BLACK);
            orangeRect.setStrokeWidth(1);
            orangeRect.setX(-80);
            orangeRect.setY(350);

            Label orangeRectLabel = new Label("- Fully restricted seats");
            orangeRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            orangeRectLabel.setLayoutX(-70);
            orangeRectLabel.setLayoutY(348);

            Rectangle yellowRect = new Rectangle(10,10);
            yellowRect.setFill(Color.YELLOW);
            yellowRect.setStroke(Color.BLACK);
            yellowRect.setStrokeWidth(1);
            yellowRect.setX(-80);
            yellowRect.setY(370);

            Label yellowRectLabel = new Label("- Partially restricted seats");
            yellowRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            yellowRectLabel.setLayoutX(-70);
            yellowRectLabel.setLayoutY(368);

            Rectangle redRect = new Rectangle(10,10);
            redRect.setFill(Color.RED);
            redRect.setStroke(Color.BLACK);
            redRect.setStrokeWidth(1);
            redRect.setX(-80);
            redRect.setY(390);

            Label redRectLabel = new Label("- Booked seats");
            redRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            redRectLabel.setLayoutX(-70);
            redRectLabel.setLayoutY(388);

            Rectangle greenRect = new Rectangle(10,10);
            greenRect.setFill(Color.GREEN);
            greenRect.setStroke(Color.BLACK);
            greenRect.setStrokeWidth(1);
            greenRect.setX(-80);
            greenRect.setY(410);

            Label greenRectLabel = new Label("- Selected seats");
            greenRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            greenRectLabel.setLayoutX(-70);
            greenRectLabel.setLayoutY(408);

            Rectangle blueRect = new Rectangle(10,10);
            blueRect.setFill(Color.BLUE);
            blueRect.setStroke(Color.BLACK);
            blueRect.setStrokeWidth(1);
            blueRect.setX(-80);
            blueRect.setY(430);

            Label blueRectLabel = new Label("- Wheelchair seats");
            blueRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            blueRectLabel.setLayoutX(-70);
            blueRectLabel.setLayoutY(428);

            Rectangle greyRect = new Rectangle(10,10);
            greyRect.setFill(Color.LIGHTGREY);
            greyRect.setStroke(Color.BLACK);
            greyRect.setStrokeWidth(1);
            greyRect.setX(-80);
            greyRect.setY(450);

            Label greyRectLabel = new Label("- Standard seats");
            greyRectLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: white;");
            greyRectLabel.setLayoutX(-70);
            greyRectLabel.setLayoutY(448);

            Pane colourKey = new Pane(keyRect,keyLabel,orangeRectLabel,orangeRect,yellowRectLabel,yellowRect,redRectLabel,
                    redRect,greenRectLabel,greenRect,blueRectLabel,blueRect,greyRectLabel,greyRect);

            Rectangle stageRect = new Rectangle(400, 70);
            stageRect.setFill(Color.LIGHTGRAY);
            stageRect.setStroke(Color.BLACK);
            stageRect.setStrokeWidth(2);
            stageRect.setX(245);
            stageRect.setY(550);

            Label stageLabel = new Label("STAGE");
            stageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 21px;");
            stageLabel.setLayoutX(410);
            stageLabel.setLayoutY(570);

            Line leftWall = new Line(245,550,245,100);
            Line leftWallStairs = new Line(345,437,345,100);
            Line rightWallLower = new Line(645,550,645,408);
            Line rightWallLower2 = new Line(645,408,612,408);
            Line rightWall = new Line(612,408,612,100);
            Line upperWall = new Line(612,100,330,100);
            Line upperWall2 = new Line(245,100,255,100);

            Line greySeatLine = new Line(345,438,645,438);
            greySeatLine.setStroke(Color.GRAY);

            Line greySeatLine2 = new Line(345,408,612,408);
            greySeatLine2.setStroke(Color.GRAY);

            Line greySeatLine3 = new Line(345,378,612,378);
            greySeatLine3.setStroke(Color.GRAY);

            Line greySeatLine4 = new Line(345,348,612,348);
            greySeatLine4.setStroke(Color.GRAY);

            Line greySeatLine5 = new Line(345,318,612,318);
            greySeatLine5.setStroke(Color.GRAY);

            Line greySeatLine6 = new Line(345,288,612,288);
            greySeatLine6.setStroke(Color.GRAY);

            Line greySeatLine7 = new Line(345,258,612,258);
            greySeatLine7.setStroke(Color.GRAY);

            Line greySeatLine8 = new Line(345,228,612,228);
            greySeatLine8.setStroke(Color.GRAY);

            Line greySeatLine9 = new Line(345,198,612,198);
            greySeatLine9.setStroke(Color.GRAY);

            Line greySeatLine10 = new Line(345,168,612,168);
            greySeatLine10.setStroke(Color.GRAY);

            Line greySeatLine11 = new Line(345,138,522,138);
            greySeatLine11.setStroke(Color.GRAY);

            Label soundLabel = new Label("SOUND");
            soundLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-text-fill: white;");
            soundLabel.setLayoutX(540);
            soundLabel.setLayoutY(110);

            Label deskLabel = new Label("DESK");
            deskLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-text-fill: white;");
            deskLabel.setLayoutX(546);
            deskLabel.setLayoutY(130);

            Label aisleLabel = new Label("AISLE");
            aisleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;-fx-text-fill: white;");
            aisleLabel.setLayoutX(270);
            aisleLabel.setLayoutY(320);
            aisleLabel.setRotate(270);

            Button confirmButton = new Button("Confirm Booking");
            confirmButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                    "-fx-text-fill: white; -fx-background-radius: 5;");
            confirmButton.setDisable(true);
            confirmButton.setLayoutX(850);
            confirmButton.setLayoutY(680);

            Button backButton = new Button("Back");
            backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                    "-fx-text-fill: white; -fx-background-radius: 5;");
            backButton.setLayoutX(-50);
            backButton.setLayoutY(680);

            backButton.setOnAction(e -> {
                HomePage homePage = new HomePage();
                homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                this.getScene().setRoot(homePage);
            });

            // Combine the image and buttons
            Pane seatingPlanPane = new Pane(buttonPane,stageRect, stageLabel,leftWall,leftWallStairs,
                    rightWallLower,rightWallLower2,rightWall,upperWall,upperWall2,greySeatLine,greySeatLine2,
                    greySeatLine3,greySeatLine4,greySeatLine5,greySeatLine6,greySeatLine7,
                    greySeatLine8,greySeatLine9,greySeatLine10,greySeatLine11,
                    soundLabel,deskLabel,aisleLabel,confirmButton,backButton,logoView,managementControls,colourKey);

            setMargin(seatingPlanPane,new Insets(40,0,0,100));

            confirmButton.setOnAction(e -> {
                if(validateSeatSelection()) {
                    showCustomerDialog();
                }
            });

            selectedSeats.addListener((ListChangeListener<Seat>) change -> {
                confirmButton.setDisable(selectedSeats.isEmpty());
            });

            this.getChildren().addAll(
                    seatingPlanPane
            );

            this.setSpacing(10);
            this.setPadding(new Insets(20));
            this.setAlignment(Pos.TOP_CENTER);

            updateAllSeatColors();

        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load seat data").show();
        }
    }

    /**
     * Creates and configures the seats for Row N.
     *
     * @param pane the parent pane to add seats to
     */
    private void createStallsSeatsRowN(Pane pane) {
        double startX = 370;
        double startY = 110;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("N");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        for (int i = 1; i <= 4; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("N " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHN" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1 || i == 19){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row M.
     *
     * @param pane the parent pane to add seats to
     */
    private void createStallsSeatsRowM(Pane pane) {
        // (Stall seats - Row M)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 140;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("M");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);


        for (int i = 1; i <= 4; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("M " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHM" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }


    /**
     * Creates and configures the seats for Row L in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowL(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 170;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("L");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("L " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHL" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;"); // Default color is green
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row K in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowK(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 200;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("K");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("K " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHK" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row J in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowJ(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 230;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("J");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("J " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHJ" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row H in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowH(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 260;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("H");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("H " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHH" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }

            pane.getChildren().add(seat);

        }
    }

    /**
     * Creates and configures the seats for Row G in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowG(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400;
        double startY = 290;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("G");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        //temp for starting value

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("G " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth,seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHG" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row F in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowF(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 320;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("F");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("F " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHF" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row E in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowE(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 350;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("E");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("E " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHE" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row D in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowD(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 380;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("D");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 7; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("D " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHD" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row C in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowC(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 410;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("C");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 8; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("C " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHC" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row B in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowB(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 440;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("B");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 8; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("B " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("SHB" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Creates and configures the seats for Row A in the stalls section.
     *
     * @param pane the parent pane to which the seat buttons will be added
     */
    private void createStallsSeatsRowA(Pane pane) {
        double startX = 400;
        double startY = 470;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("A");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 8; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("A " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);

            seat.setOnAction(e -> handleSeatSelection(seat));
            if (bookedSeatIds.contains("SHA" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #067bc4;");
                seat.setDisable(false);
            }
            pane.getChildren().add(seat);
        }
    }

    /**
     * Updates visual styling for all seats based on current state.
     * Handles color coding for different seat statuses including:
     * <ul>
     *   <li>Booked</li>
     *   <li>Selected</li>
     *   <li>Accessible</li>
     *   <li>Restricted</li>
     * </ul>
     */
    private void updateAllSeatColors() {
        Pane seatingPlanPane = (Pane) this.getChildren().getFirst();
        for (Node node : seatingPlanPane.getChildren()) {
            if (node instanceof Pane) {
                Pane buttonPane = (Pane) node;
                for (Node buttonNode : buttonPane.getChildren()) {
                    if (buttonNode instanceof Button) {
                        Button seatButton = (Button) buttonNode;
                        Object userData = seatButton.getUserData();
                        if (userData instanceof String) {
                            String[] parts = ((String) userData).split(" ");
                            if (parts.length == 2) {
                                String row = parts[0];
                                int seatNumber = Integer.parseInt(parts[1]);
                                String seatId = "SH" + row + seatNumber;
                                if (fullRestrictedSeats.contains(seatId)) {
                                    seatButton.setStyle("-fx-background-color: #ffa500;");
                                    seatButton.setDisable(!isRestrictedManagementMode);
                                }
                                else if (partialRestrictedSeats.contains(seatId)) {
                                    seatButton.setStyle("-fx-background-color: #ffff00;");
                                    seatButton.setDisable(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Persists current seat restrictions to the database.
     * Saves both full and partial restrictions for the performance.
     *
     * @throws SQLException if database access fails during save operation
     */

    private void saveSeatRestrictions() {
        try {
            RestrictedRepository.clearPerformanceRestrictions(selectedPerformance.getPerformanceId());

            for (String seatId : fullRestrictedSeats) {
                RestrictedRepository.setSeatRestriction(selectedPerformance.getPerformanceId(), seatId, "full");
            }

            for (String seatId : partialRestrictedSeats) {
                RestrictedRepository.setSeatRestriction(selectedPerformance.getPerformanceId(), seatId, "partial");
            }

            new Alert(Alert.AlertType.INFORMATION, "Seat restrictions saved successfully").showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save seat restrictions").showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Handles seat selection/deselection based on current mode.
     * In normal mode, selects seats for booking.
     * In management mode, applies/removes seat restrictions.
     *
     * @param seat the seat button that was clicked
     */
    private void handleSeatSelection(Button seat) {
        Object data = seat.getUserData();
        if (data instanceof String) {
            String[] parts = ((String) data).split(" ");
            if (parts.length == 2) {
                String row = parts[0];
                int seatNumber = Integer.parseInt(parts[1]);
                String seatId = "SH" + row + seatNumber;

                if (isRestrictedManagementMode) {
                    String restrictionType = restrictionTypeComboBox.getValue();

                    if (fullRestrictedSeats.contains(seatId) || partialRestrictedSeats.contains(seatId)) {
                        fullRestrictedSeats.remove(seatId);
                        partialRestrictedSeats.remove(seatId);
                        if (accesibleSeatIDs.contains(seatId)) {
                            seat.setStyle("-fx-background-color: #067bc4;");
                        } else {
                            seat.setStyle("-fx-background-color: #cccccc;");
                        }
                    } else {
                        if ("full".equals(restrictionType)) {
                            fullRestrictedSeats.add(seatId);
                            partialRestrictedSeats.remove(seatId);
                            seat.setStyle("-fx-background-color: #ffa500;");
                        } else {
                            partialRestrictedSeats.add(seatId);
                            fullRestrictedSeats.remove(seatId);
                            seat.setStyle("-fx-background-color: #ffff00;");
                        }
                    }
                } else {
                    boolean isSelected = selectedSeats.stream()
                            .anyMatch(s -> s.getSeatID().equals(seatId));

                    if (isSelected) {
                        handleWheelchairPairDeselection(seat, seatId);
                        selectedSeats.removeIf(s -> s.getSeatID().equals(seatId));
                        if (partialRestrictedSeats.contains(seatId)) {
                            seat.setStyle("-fx-background-color: #ffff00;");
                        } else if (accesibleSeatIDs.contains(seatId)) {
                            seat.setStyle("-fx-background-color: #067bc4;");
                        } else {
                            seat.setStyle("-fx-background-color: #cccccc;");
                        }
                    } else {
                        if (accesibleSeatIDs.contains(seatId)) {
                            showAccessibleSeatConfirmation(seat, row, seatNumber, seatId);
                        } else {
                            selectedSeats.add(new Seat(seatId, row, seatNumber, false));
                            seat.setStyle("-fx-background-color: #14b904;");
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines if a seat is marked as a companion to a wheelchair space.
     *
     * @param seatId the seat ID to check
     * @return true if the seat is a companion seat, false otherwise
     */
    private boolean isCompanionSeat(String seatId) {
        return selectedSeats.stream()
                .anyMatch(s -> s.isAccesible() &&
                        getAdjacentSeatId(s.getSeatID(), getAllSeatsInVenue()) != null &&
                        getAdjacentSeatId(s.getSeatID(), getAllSeatsInVenue()).equals(seatId));
    }

    /**
     * Shows confirmation dialog for selecting an accessible seat.
     *
     * @param seat the seat button being selected
     * @param row the row of the seat
     * @param seatNumber the seat number
     * @param seatId the full seat ID
     */
    private void showAccessibleSeatConfirmation(Button seat, String row, int seatNumber, String seatId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Accessible Seat");
        alert.setHeaderText("Is this booking for a wheelchair user?");
        alert.setContentText("Selecting 'Yes' will reserve the adjacent seat as well");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectAccessibleSeatWithAdjacent(seat, row, seatNumber, seatId);
        } else {
            selectedSeats.add(new Seat(seatId, row, seatNumber, false));
            seat.setStyle("-fx-background-color: #14b904;");
        }
    }


    /**
     * Validates the current seat selection against business rules.
     *
     * @return true if selection is valid (within limits), false otherwise
     */
    public boolean validateSeatSelection() {
        long regularSeatCount = selectedSeats.stream().filter(s -> !isCompanionSeat(s.getSeatID())).count();
        if (regularSeatCount > maxSeatsSelectable) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Too Many Seats");
            alert.setHeaderText("Seat limit exceeded");
            alert.setContentText(String.format(
                    "You have selected %d regular seats (limit is %d). " +
                            "Please remove some seats before proceeding.",
                    regularSeatCount, maxSeatsSelectable
            ));
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Selects an accessible seat and its adjacent companion seat.
     *
     * @param seat the accessible seat button
     * @param row the row of the seat
     * @param seatNumber the seat number
     * @param seatId the full seat ID
     */
    private void selectAccessibleSeatWithAdjacent(Button seat, String row, int seatNumber, String seatId) {
        selectedSeats.add(new Seat(seatId, row, seatNumber, true));
        seat.setStyle("-fx-background-color: #14b904;");

        // Find available adjacent seat
        String adjacentSeatId = findAvailableAdjacentSeat(
                seatId,
                getAllSeatsInVenue(),
                bookedSeatIds,
                selectedSeats
        );

        if (adjacentSeatId != null) {
            Button adjacentButton = findButtonBySeatId((Pane)seat.getParent(), adjacentSeatId);
            if (adjacentButton != null) {
                String adjIdWithoutPrefix = adjacentSeatId.substring(2);
                String adjRow = adjIdWithoutPrefix.replaceAll("[0-9]", "");
                int adjNumber = Integer.parseInt(adjIdWithoutPrefix.substring(adjRow.length()));

                selectedSeats.add(new Seat(adjacentSeatId, adjRow, adjNumber, false));
                adjacentButton.setStyle("-fx-background-color: #14b904;");
            }
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Cannot book as wheelchair space - no available adjacent seat found.\n" +
                            "Please select a different accessible seat or book as standard seat.")
                    .showAndWait();

            selectedSeats.removeIf(s -> s.getSeatID().equals(seatId));
            seat.setStyle(accesibleSeatIDs.contains(seatId) ?
                    "-fx-background-color: #067bc4;" : "-fx-background-color: #cccccc;");
        }
    }

    /**
     * Finds the adjacent seat ID for a given seat.
     *
     * @param seatId the seat ID to find adjacent for
     * @param allSeats list of all seats in the venue
     * @return adjacent seat ID or null if none exists
     */
    private String getAdjacentSeatId(String seatId, List<Seat> allSeats) {
        try {
            String prefix = seatId.substring(0, 2);
            String rowAndNumber = seatId.substring(2);

            String row = rowAndNumber.replaceAll("\\d+", "");
            String numberStr = rowAndNumber.substring(row.length());
            int number = Integer.parseInt(numberStr);

            String forwardSeatId = prefix + row + (number + 1);
            boolean forwardExists = allSeats.stream()
                    .anyMatch(s -> s.getSeatID().equals(forwardSeatId));

            if (forwardExists) {
                return forwardSeatId;
            }


            if (number > 1) {
                String backwardSeatId = prefix + row + (number - 1);
                boolean backwardExists = allSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(backwardSeatId));

                if (backwardExists) {
                    return backwardSeatId;
                }
            }

            return null;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing seat ID: " + seatId);
            return null;
        }
    }

    /**
     * Finds an available adjacent seat for wheelchair companion.
     *
     * @param seatId the seat ID to find adjacent for
     * @param allSeats list of all seats in the venue
     * @param bookedSeatIds set of booked seat IDs
     * @param currentlySelectedSeats list of currently selected seats
     * @return available adjacent seat ID or null if none available
     */
    private String findAvailableAdjacentSeat(String seatId, List<Seat> allSeats, Set<String> bookedSeatIds, List<Seat> currentlySelectedSeats) {
        try {
            String prefix = seatId.substring(0, 2);
            String rowAndNumber = seatId.substring(2);


            String row = rowAndNumber.replaceAll("\\d+", "");
            String numberStr = rowAndNumber.substring(row.length());
            int number = Integer.parseInt(numberStr);


            String[] directions = {String.valueOf(number + 1), String.valueOf(number - 1)};

            for (String dirNumber : directions) {
                String adjacentSeatId = prefix + row + dirNumber;


                boolean seatExists = allSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(adjacentSeatId));

                if (!seatExists) continue;


                boolean isBooked = bookedSeatIds.contains(adjacentSeatId);

                boolean isFullyRestricted = fullRestrictedSeats.contains(adjacentSeatId);

                boolean isSelectedAsCompanion = currentlySelectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(adjacentSeatId) &&
                                selectedSeats.stream().anyMatch(sel ->
                                        sel.isAccesible() &&
                                                getAdjacentSeatId(sel.getSeatID(), allSeats).equals(adjacentSeatId)));

                if (!isBooked && !isSelectedAsCompanion && !isFullyRestricted) {
                    return adjacentSeatId;
                }
            }

            return null;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing seat ID: " + seatId);
            return null;
        }
    }

    /**
     * Finds a seat button by its ID.
     *
     * @param parent the parent pane containing seat buttons
     * @param seatId the seat ID to find
     * @return the matching button or null if not found
     */
    private Button findButtonBySeatId(Pane parent, String seatId) {
        String seatIdWithoutPrefix = seatId.substring(2);
        String row = seatIdWithoutPrefix.replaceAll("[0-9]", "");
        String numberStr = seatIdWithoutPrefix.substring(row.length());

        for (Node node : parent.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button)node;
                Object buttonData = button.getUserData();
                if (buttonData instanceof String) {
                    String[] parts = ((String) buttonData).split(" ");
                    if (parts.length == 2 && parts[0].equals(row) && parts[1].equals(numberStr)) {
                        return button;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Handles deselection of wheelchair seat pairs.
     *
     * @param seat the seat button being deselected
     * @param seatId the seat ID being deselected
     */
    private void handleWheelchairPairDeselection(Button seat, String seatId) {
        Optional<Seat> accessibleSeat = selectedSeats.stream()
                .filter(s -> s.isAccesible() && s.getSeatID().equals(seatId))
                .findFirst();

        Optional<Seat> companionSeat = selectedSeats.stream()
                .filter(s -> !s.isAccesible() && s.getSeatID().equals(seatId))
                .findFirst();

        if (accessibleSeat.isPresent()) {
            String companionId = getAdjacentSeatId(seatId, getAllSeatsInVenue());
            if (companionId != null) {
                selectedSeats.removeIf(s -> s.getSeatID().equals(companionId));
                resetSeatButtonStyle(companionId);
            }
        }
        else if (companionSeat.isPresent()) {
            String accessibleId = selectedSeats.stream()
                    .filter(s -> s.isAccesible() &&
                            getAdjacentSeatId(s.getSeatID(), getAllSeatsInVenue()).equals(seatId))
                    .findFirst()
                    .map(Seat::getSeatID)
                    .orElse(null);

            if (accessibleId != null) {
                selectedSeats.removeIf(s -> s.getSeatID().equals(accessibleId));
                resetSeatButtonStyle(accessibleId);
            }
        }

        selectedSeats.removeIf(s -> s.getSeatID().equals(seatId));
        resetSeatButtonStyle(seatId);
    }

    /**
     * Resets a seat button to its default style.
     *
     * @param seatId the seat ID to reset
     */
    private void resetSeatButtonStyle(String seatId) {
        Pane seatingPlanPane = (Pane) this.getChildren().get(0);

        Pane buttonPane = null;
        for (Node node : seatingPlanPane.getChildren()) {
            if (node instanceof Pane && node.getId() != null && node.getId().equals("buttonPane")) {
                buttonPane = (Pane) node;
                break;
            }
        }
        if (buttonPane == null) {
            for (Node node : seatingPlanPane.getChildren()) {
                if (node instanceof Pane) {
                    buttonPane = (Pane) node;
                    break;
                }
            }
        }
        if (buttonPane != null) {
            Button button = findButtonBySeatId(buttonPane, seatId);
            if (button != null) {
                button.setStyle(accesibleSeatIDs.contains(seatId) ?
                        "-fx-background-color: #067bc4;" : "-fx-background-color: #cccccc;");
            }
        }
    }

    /**
     * Retrieves all seats in the venue for adjacency calculations.
     *
     * @return complete list of seats in the venue
     */
    private List<Seat> getAllSeatsInVenue() {
        List<Seat> allSeats = new ArrayList<>();
        String[] rows = {"N", "M", "L", "K", "J", "H", "G", "F", "E", "D", "C", "B", "A"};
        int[] counts = {4, 4, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8};

        for (int r = 0; r < rows.length; r++) {
            String row = rows[r];
            int count = counts[r];
            for (int i = 1; i <= count; i++) {
                String seatId = "SH" + row + i;
                allSeats.add(new Seat(seatId, row, i, accesibleSeatIDs.contains(seatId)));
            }
        }
        return allSeats;
    }

    /**
     * Shows the customer information dialog to complete booking.
     * Handles both new and existing customer scenarios.
     */
    private void showCustomerDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Customer Information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,0,0,0));

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");
        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);
        customerIdField.setDisable(true);


        Button searchButton = new Button("Search");
        grid.add(searchButton, 2, 0);


        TextField customerNameField = new TextField();
        customerNameField.setPromptText("Full Name");
        grid.add(new Label("Full Name:"), 0, 1);
        grid.add(customerNameField, 1, 1, 2, 1);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.setDisable(true);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2, 2, 1);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setDisable(true);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3, 2, 1);

        Label statusLabel = new Label();
        grid.add(statusLabel, 0, 4, 3, 1);


        Button confirmButton = new Button("Confirm Booking");
        confirmButton.setDisable(true);
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, 5, 3, 1);


        searchButton.setOnAction(e -> {
            try {
                Customer customer = CustomerRepository.getCustomerByName(customerNameField.getText());
                if (customer != null) {
                    // Existing customer
                    customerNameField.setText(customer.getCustomerName());
                    customerIdField.setText(customer.getCustomerID());
                    phoneField.setText(customer.getPhoneNumber());
                    emailField.setText(customer.getEmail());
                    statusLabel.setText("Existing customer found");
                    confirmButton.setDisable(false);
                } else {
                    // New customer
                    customerIdField.setDisable(false);
                    phoneField.setDisable(false);
                    emailField.setDisable(false);
                    statusLabel.setText("New customer - please enter details");
                    confirmButton.setDisable(false);
                }
            } catch (SQLException ex) {
                statusLabel.setText("Error accessing database");
                ex.printStackTrace();
            }
        });

        // Confirm booking
        confirmButton.setOnAction(e -> {
            try {
                String customerId = customerIdField.getText();
                String name = customerNameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();

                // Check if we need to create a new customer
                if (CustomerRepository.getCustomerById(customerId) == null) {
                    Customer newCustomer = new Customer(customerId, name, phone, email);
                    CustomerRepository.addCustomer(newCustomer);
                }

                // Process payment through the new class
                boolean paymentSuccessful = PaymentPage.processPayment(
                        dialog, // parent stage
                        selectedSeats,
                        customerId,
                        name,
                        selectedPerformance
                );

                if (paymentSuccessful) {
                    selectedSeats.clear();

                    this.getChildren().clear();
                    initializeUI();
                    dialog.close();
                }
            } catch (Exception ex) {
                statusLabel.setText("Error processing booking");
                ex.printStackTrace();
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

}