package boxoffice.ui;

import boxoffice.database.Customer;
import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.models.CustomerRepository;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.sql.SQLException;
import java.util.*;

public class TicketSalesPage extends VBox {
    private Performance selectedPerformance;
    private ObservableList<Seat> selectedSeats = FXCollections.observableArrayList();
    private final Set<String> bookedSeatIds = new HashSet<>();
    private List<String> accesibleSeatIDs = SeatRepository.getAccessibleSeatIDs();
    private final int maxSeatsSelectable = 11;

    public TicketSalesPage(Performance performance) throws SQLException {
        this.selectedPerformance = performance;
        initializeUI();
    }

    public void initializeUI(){
        try {
            Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitHeight(190);
            logoView.setFitWidth(190);
            logoView.setPreserveRatio(true);
            logoView.setLayoutX(-100);
            logoView.setLayoutY(-50);

            bookedSeatIds.clear();

            bookedSeatIds.addAll(TicketSaleRepository.getBookedSeats(selectedPerformance.getPerformanceId()));
            System.out.println("[DEBUG]: Loading booked seats for performance " + selectedPerformance.getPerformanceId() + ": " + bookedSeatIds);
            System.out.println("[DEBUG]: Loading the accesible seats" + accesibleSeatIDs.toString());

            // Create a pane to hold the buttons
            Pane buttonPane = new Pane();
            buttonPane.setPrefSize(getWidth(), getHeight());

            // Create balcony seats (top rows)
            createCCBalconySeats(buttonPane);

            createBBBalconySeatsV1(buttonPane);
            createBBBalconySeatsV2(buttonPane);
            createBBBalconySeatsV3(buttonPane);

            createAABalconySeatsV1(buttonPane);
            createAABalconySeatsV2(buttonPane);
            createAABalconySeatsV3(buttonPane);

            // Create stalls seats (from your image)
            createStallsSeatsRowQ(buttonPane);
            createStallsSeatsRowP(buttonPane);
            createStallsSeatsRowO(buttonPane);
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

            Rectangle stallsBorder = new Rectangle();
            stallsBorder.setFill(Color.TRANSPARENT);
            stallsBorder.setStroke(Color.BLACK);
            stallsBorder.setStrokeWidth(2);
            stallsBorder.setWidth(670);
            stallsBorder.setHeight(530);
            stallsBorder.setMouseTransparent(true);
            stallsBorder.setX(150);
            stallsBorder.setY(110);

            Label stallsLabelLeft = new Label("STALLS");
            stallsLabelLeft.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
            stallsLabelLeft.setLayoutX(200);
            stallsLabelLeft.setLayoutY(125);

            Label stallsLabelRight = new Label("STALLS");
            stallsLabelRight.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
            stallsLabelRight.setLayoutX(700);
            stallsLabelRight.setLayoutY(125);

            Label balconyLabelUpper = new Label("BALCONY");
            balconyLabelUpper.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
            balconyLabelUpper.setLayoutX(450);
            balconyLabelUpper.setLayoutY(-10);

            Label balconyLabelLeft = new Label("BALCONY");
            balconyLabelLeft.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
            balconyLabelLeft.setLayoutX(10);
            balconyLabelLeft.setLayoutY(160);
            balconyLabelLeft.setRotate(270);

            Label balconyLabelRight = new Label("BALCONY");
            balconyLabelRight.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
            balconyLabelRight.setLayoutX(885);
            balconyLabelRight.setLayoutY(160);
            balconyLabelRight.setRotate(90);

            Rectangle stageRect = new Rectangle(200, 50);
            stageRect.setFill(Color.LIGHTGRAY);
            stageRect.setStroke(Color.BLACK);
            stageRect.setStrokeWidth(2);
            stageRect.setX(380);
            stageRect.setY(620);

            Label stageLabel = new Label("STAGE");
            stageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            stageLabel.setLayoutX(455);
            stageLabel.setLayoutY(630);

            Button confirmButton = new Button("Confirm Selection");
            confirmButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                    "-fx-text-fill: white; -fx-background-radius: 5;");
            confirmButton.setDisable(true); // Disabled until seats are selected
            confirmButton.setLayoutX(850);
            confirmButton.setLayoutY(680);

            Button backButton = new Button("Back");
            backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                    "-fx-text-fill: white; -fx-background-radius: 5;");
            backButton.setLayoutX(-50);
            backButton.setLayoutY(680);

            // Combine the image and buttons
            Pane seatingPlanPane = new Pane(buttonPane,stallsBorder,stallsLabelLeft,stallsLabelRight,
                    balconyLabelUpper, balconyLabelLeft, balconyLabelRight,stageRect,stageLabel,confirmButton,backButton,logoView);
            setMargin(seatingPlanPane,new Insets(40,0,0,100));


            backButton.setOnAction(e -> {
                HomePage homePage = new HomePage();
                homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                this.getScene().setRoot(homePage);
            });

            // Action when confirm button is clicked
            confirmButton.setOnAction(e -> {
                if(validateSeatSelection()) {
                    showCustomerDialog(); // Your existing code
                }
            });

            // Enable/disable button based on seat selection
            selectedSeats.addListener((ListChangeListener<Seat>) change -> {
                confirmButton.setDisable(selectedSeats.isEmpty());
            });

            // Add components to the main VBox
            this.getChildren().addAll(
                    seatingPlanPane
            );

            this.setSpacing(10);
            this.setPadding(new Insets(20));
            this.setAlignment(Pos.TOP_CENTER);

        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load seat data").show();
        }
    }

    private void createCCBalconySeats(Pane pane) {
        // Balcony seats (Cc 1-8)
        double startX = 375; // Adjust these values based on your actual image
        double startY = 20;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("CC");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 8; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("CC " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            // Single point for click handling
            if (bookedSeatIds.contains("MHCC" + i)) {
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createBBBalconySeatsV1(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 60; // Adjust these values based on your actual image
        double startY = 220;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("BB");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY - 120);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 5; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY - (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHBB" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createBBBalconySeatsV2(Pane pane){
        // Balcony seats (BB 6 - 23)
        double startX = 75; // Adjust these values based on your actual image
        double startY = 45;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("BB");
        label.setLayoutX(startX + 130);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 6; i <= 23; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHBB" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createBBBalconySeatsV3(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 880; // Adjust these values based on your actual image
        double startY = -460;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("BB");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY + 560);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 24; i <= 28; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY + (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHBB" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createAABalconySeatsV1(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 100; // Adjust these values based on your actual image
        double startY = 560;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("AA");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY - 490);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 20; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY - (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHAA" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createAABalconySeatsV2(Pane pane){
        // Balcony seats (AA 21 - 33)
        double startX = -300; // Adjust these values based on your actual image
        double startY = 70;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("AA");
        label.setLayoutX(startX + 575);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 21; i <= 33; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHAA" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }

            pane.getChildren().add(seat);
        }
    }

    private void createAABalconySeatsV3(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 840; // Adjust these values based on your actual image
        double startY = -735;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("AA");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY + 810);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 34; i <= 53; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY + (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHAA" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);
            }
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowQ(Pane pane) {
        // (Stall seats - Row Q)
        double startX = 340; // Adjust these values based on your actual image
        double startY = 120;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("Q");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 10; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("Q " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHQ" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1 || i == 10){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowP(Pane pane) {
        // (Stall seats - Row P)
        double startX = 330; // Adjust these values based on your actual image
        double startY = 150;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("P");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 11; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("P " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            if (bookedSeatIds.contains("MHP" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1 || i == 11){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);

        }
    }

    private void createStallsSeatsRowO(Pane pane) {
        // (Stall seats - Row O)
        double startX = 180; // Adjust these values based on your actual image
        double startY = 180;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("O");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 20; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("O " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHO" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1 || i == 20){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowN(Pane pane) {
        // (Stall seats - Row Q)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 210;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("N");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("N " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHN" + i)){
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

    private void createStallsSeatsRowM(Pane pane) {
        // (Stall seats - Row M)
        double startX = 240; // Adjust these values based on your actual image
        double startY = 240;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("M");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);


        for (int i = 1; i <= 16; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("M " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHM" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #cccccc;");// Gray;
                seat.setDisable(false);

                if(i == 1 || i == 16){
                    seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                }
            }
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowL(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 270;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("L");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("L " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHL" + i)){
                seat.setStyle("-fx-background-color: #ff0000;"); // Red
                seat.setOpacity(1);
                seat.setDisable(true);
            } else {
                seat.setStyle("-fx-background-color: #067bc4;"); // Default color is green
                seat.setDisable(false);
            }
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowK(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 300;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("K");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("K " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHK" + i)){
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

    private void createStallsSeatsRowJ(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 330;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("J");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("J " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHJ" + i)){
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

    private void createStallsSeatsRowH(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 360;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("H");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("H " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHH" + i)){
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

    private void createStallsSeatsRowG(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 390;
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
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("G " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth,seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHG" + i)){
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

    private void createStallsSeatsRowF(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 420;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("F");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("F " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHF" + i)){
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

    private void createStallsSeatsRowE(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 450;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("E");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("E " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHE" + i)){
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

    private void createStallsSeatsRowD(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 480;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("D");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("D " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHD" + i)){
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

    private void createStallsSeatsRowC(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 510;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("C");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("C " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHC" + i)){
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

    private void createStallsSeatsRowB(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 540;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("B");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("B " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));

            if (bookedSeatIds.contains("MHB" + i)){
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

    private void createStallsSeatsRowA(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 570;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("A");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        label.setStyle("-fx-text-fill: white;");
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("A " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);

            seat.setOnAction(e -> handleSeatSelection(seat));
            if (bookedSeatIds.contains("MHA" + i)){
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

    private void handleSeatSelection(Button seat) {
        Object data = seat.getUserData();
        if (data instanceof String) {
            String[] parts = ((String) data).split(" ");
            if (parts.length == 2) {
                String row = parts[0];
                int seatNumber = Integer.parseInt(parts[1]);
                String seatId = "MH" + row + seatNumber;

                // Check if already selected
                boolean isSelected = selectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(seatId));

                if (isSelected) {
                    handleWheelchairPairDeselection(seat, seatId);
                } else {
                    if (accesibleSeatIDs.contains(seatId)) {
                        // Show confirmation for accessible seat
                        showAccessibleSeatConfirmation(seat, row, seatNumber, seatId);
                    } else {
                        // Normal seat selection
                        selectedSeats.add(new Seat(seatId, row, seatNumber, false));
                        seat.setStyle("-fx-background-color: #14b904;");
                    }
                }
            }
        }
    }

    private boolean isCompanionSeat(String seatId) {
        return selectedSeats.stream()
                .anyMatch(s -> s.isAccesible() &&
                        getAdjacentSeatId(s.getSeatID(), getAllSeatsInVenue()) != null &&
                        getAdjacentSeatId(s.getSeatID(), getAllSeatsInVenue()).equals(seatId));
    }

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

    private void selectAccessibleSeatWithAdjacent(Button seat, String row, int seatNumber, String seatId) {
        // Add the accessible seat
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
                // Extract row and number from adjacent seat ID
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

            // Deselect the accessible seat since we can't complete the booking
            selectedSeats.removeIf(s -> s.getSeatID().equals(seatId));
            seat.setStyle(accesibleSeatIDs.contains(seatId) ?
                    "-fx-background-color: #067bc4;" : "-fx-background-color: #cccccc;");
        }
    }

    private String getAdjacentSeatId(String seatId, List<Seat> allSeats) {
        try {
            String prefix = seatId.substring(0, 2); // "MH"
            String rowAndNumber = seatId.substring(2); // "A1" or "AA1"

            // Separate row letters from seat number
            String row = rowAndNumber.replaceAll("\\d+", "");
            String numberStr = rowAndNumber.substring(row.length());
            int number = Integer.parseInt(numberStr);

            // Try forward adjacent seat first (higher number)
            String forwardSeatId = prefix + row + (number + 1);
            boolean forwardExists = allSeats.stream()
                    .anyMatch(s -> s.getSeatID().equals(forwardSeatId));

            if (forwardExists) {
                return forwardSeatId;
            }

            // If no forward seat, try backward adjacent (lower number)
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

    private String findAvailableAdjacentSeat(String seatId, List<Seat> allSeats, Set<String> bookedSeatIds, List<Seat> currentlySelectedSeats) {
        try {
            String prefix = seatId.substring(0, 2); // "MH"
            String rowAndNumber = seatId.substring(2); // "A1" or "AA1"

            // Separate row letters from seat number
            String row = rowAndNumber.replaceAll("\\d+", "");
            String numberStr = rowAndNumber.substring(row.length());
            int number = Integer.parseInt(numberStr);

            // Check both directions and return first available
            String[] directions = {String.valueOf(number + 1), String.valueOf(number - 1)};

            for (String dirNumber : directions) {
                String adjacentSeatId = prefix + row + dirNumber;

                // Check if seat exists in venue
                boolean seatExists = allSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(adjacentSeatId));

                if (!seatExists) continue;

                // Check if seat is already booked
                boolean isBooked = bookedSeatIds.contains(adjacentSeatId);

                // Check if seat is already selected as a companion
                boolean isSelectedAsCompanion = currentlySelectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(adjacentSeatId) &&
                                selectedSeats.stream().anyMatch(sel ->
                                        sel.isAccesible() &&
                                                getAdjacentSeatId(sel.getSeatID(), allSeats).equals(adjacentSeatId)));

                if (!isBooked && !isSelectedAsCompanion) {
                    return adjacentSeatId;
                }
            }

            return null;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing seat ID: " + seatId);
            return null;
        }
    }

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

    private void handleWheelchairPairDeselection(Button seat, String seatId) {
        // Check if this is an accessible seat with companion
        Optional<Seat> accessibleSeat = selectedSeats.stream()
                .filter(s -> s.isAccesible() && s.getSeatID().equals(seatId))
                .findFirst();

        // Check if this is a companion seat
        Optional<Seat> companionSeat = selectedSeats.stream()
                .filter(s -> !s.isAccesible() && s.getSeatID().equals(seatId))
                .findFirst();

        if (accessibleSeat.isPresent()) {
            // Deselecting an accessible seat - find and remove its companion too
            String companionId = getAdjacentSeatId(seatId, getAllSeatsInVenue());
            if (companionId != null) {
                selectedSeats.removeIf(s -> s.getSeatID().equals(companionId));
                resetSeatButtonStyle(companionId);
            }
        }
        else if (companionSeat.isPresent()) {
            // Deselecting a companion seat - find and remove its accessible seat
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

        // Always remove the clicked seat and reset its style
        selectedSeats.removeIf(s -> s.getSeatID().equals(seatId));
        resetSeatButtonStyle(seatId);
    }

    private void resetSeatButtonStyle(String seatId) {
        // Get the main seating plan pane from your VBox
        Pane seatingPlanPane = (Pane) this.getChildren().get(0); // Adjust index if needed

        // Find the button pane that contains all seat buttons
        Pane buttonPane = null;
        for (Node node : seatingPlanPane.getChildren()) {
            if (node instanceof Pane && node.getId() != null && node.getId().equals("buttonPane")) {
                buttonPane = (Pane) node;
                break;
            }
        }
        if (buttonPane == null) {
            // Fallback - search all panes if ID isn't set
            for (Node node : seatingPlanPane.getChildren()) {
                if (node instanceof Pane) {
                    buttonPane = (Pane) node;
                    break;
                }
            }
        }
        if (buttonPane != null) {
            // Now search for the specific seat button
            Button button = findButtonBySeatId(buttonPane, seatId);
            if (button != null) {
                button.setStyle(accesibleSeatIDs.contains(seatId) ?
                        "-fx-background-color: #067bc4;" : "-fx-background-color: #cccccc;");
            }
        }
    }
    private List<Seat> getAllSeatsInVenue() {
        List<Seat> allSeats = new ArrayList<>();

        // Balcony Seats - CC
        for (int i = 1; i <= 8; i++) {
            allSeats.add(new Seat("MHCC" + i, "CC", i, accesibleSeatIDs.contains("MHCC" + i)));
        }

        // Balcony Seats - BB
        for (int i = 1; i <= 28; i++) {
            allSeats.add(new Seat("MHBB" + i, "BB", i, accesibleSeatIDs.contains("MHBB" + i)));
        }

        // Balcony Seats - AA
        for (int i = 1; i <= 53; i++) {
            allSeats.add(new Seat("MHAA" + i, "AA", i, accesibleSeatIDs.contains("MHAA" + i)));
        }

        // Stalls Seats - Rows Q to A
        String[] rows = {"Q", "P", "O", "N", "M", "L", "K", "J", "H", "G", "F", "E", "D", "C", "B", "A"};
        int[] counts = {10, 11, 20, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19};

        for (int r = 0; r < rows.length; r++) {
            String row = rows[r];
            int count = counts[r];
            for (int i = 1; i <= count; i++) {
                String seatId = "MH" + row + i;
                allSeats.add(new Seat(seatId, row, i, accesibleSeatIDs.contains(seatId)));
            }
        }
        return allSeats;
    }

    private void showCustomerDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Customer Information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,0,0,0));

        // Customer ID field
        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");
        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);
        customerIdField.setDisable(true);

        // Search button
        Button searchButton = new Button("Search");
        grid.add(searchButton, 2, 0);

        // Customer details fields
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

        // Buttons
        Button confirmButton = new Button("Confirm Booking");
        confirmButton.setDisable(true);
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 0, 5, 3, 1);

        // Search functionality
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