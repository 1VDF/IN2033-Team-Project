package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.database.Customer;
import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.database.TicketSale;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.sql.Types.NULL;

public class TicketSalesPageSmall extends VBox {
    private Performance selectedPerformance;
    private ObservableList<Seat> selectedSeats = FXCollections.observableArrayList();
    private final Set<String> bookedSeatIds = new HashSet<>();
    private List<String> accesibleSeatIDs = SeatRepository.getAccessibleSeatIDs();
    private final int maxSeatsSelectable = 11;

    public TicketSalesPageSmall(Performance performance) throws SQLException {
        this.selectedPerformance = performance;
        initializeUI();
    }

    public TicketSalesPageSmall() throws SQLException {
    }

    public void initializeUI(){
        // Load already booked seats for this performance
        try {
            bookedSeatIds.clear();

            bookedSeatIds.addAll(TicketSaleRepository.getBookedSeats(selectedPerformance.getPerformanceId()));
            System.out.println("[DEBUG]: Loading booked seats for performance " + selectedPerformance.getPerformanceId() + ": " + bookedSeatIds);
            System.out.println("[DEBUG]: Loading the accesible seats" + accesibleSeatIDs.toString());

            // Create a pane to hold the buttons
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
            soundLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            soundLabel.setLayoutX(540);
            soundLabel.setLayoutY(110);

            Label deskLabel = new Label("DESK");
            deskLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            deskLabel.setLayoutX(546);
            deskLabel.setLayoutY(130);

            Label aisleLabel = new Label("AISLE");
            aisleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
            aisleLabel.setLayoutX(270);
            aisleLabel.setLayoutY(320);
            aisleLabel.setRotate(270);

            Button confirmButton = new Button("Confirm Selection");
            confirmButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
            confirmButton.setDisable(true); // Disabled until seats are selected
            confirmButton.setLayoutX(-50);
            confirmButton.setLayoutY(680);

            // Combine the image and buttons
            Pane seatingPlanPane = new Pane(buttonPane,stageRect, stageLabel,leftWall,leftWallStairs,
                    rightWallLower,rightWallLower2,rightWall,upperWall,upperWall2,greySeatLine,greySeatLine2,
                    greySeatLine3,greySeatLine4,greySeatLine5,greySeatLine6,greySeatLine7,
                    greySeatLine8,greySeatLine9,greySeatLine10,greySeatLine11,
                    soundLabel,deskLabel,aisleLabel,confirmButton);

            setMargin(seatingPlanPane,new Insets(40,0,0,100));


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

    private void createStallsSeatsRowN(Pane pane) {
        // (Stall seats - Row Q)
        double startX = 370; // Adjust these values based on your actual image
        double startY = 110;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("N");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
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
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
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

    private void createStallsSeatsRowG(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 290;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("G");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
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

    private void createStallsSeatsRowA(Pane pane) {
        // (Stall seats - Row L)
        double startX = 400; // Adjust these values based on your actual image
        double startY = 470;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("A");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
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

    private void handleSeatSelection(Button seat) {
        Object data = seat.getUserData();
        if (data instanceof String) {
            String[] parts = ((String) data).split(" ");
            if (parts.length == 2) {
                String row = parts[0];
                int seatNumber = Integer.parseInt(parts[1]);
                String seatId = "SH" + row + seatNumber;

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
            String prefix = seatId.substring(0, 2);
            String rowAndNumber = seatId.substring(2);

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

        // Stalls Seats - Rows Q to A
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