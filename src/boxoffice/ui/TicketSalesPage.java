package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TicketSalesPage extends VBox {
    private Stage stage;

    public TicketSalesPage() {
        initializeUI();
    }

    public void initializeUI(){
        // Load the seating plan image
        Image seatingPlanImage = new Image("boxoffice/data/MainHall-SeatingPlan.png");
        ImageView imageView = new ImageView(seatingPlanImage);

        // Bind the image's width and height to the scene's width and height
        //imageView.setPreserveRatio(true); // Maintain aspect ratio of the image
        imageView.setSmooth(true); // Optional: Smooth scaling for better quality
        imageView.setOpacity(0.5);
        imageView.setFitWidth(1000); // Initial width, you can adjust this value
        imageView.setFitHeight(625); // Initial height, you can adjust this value

        // Create a pane to hold the buttons
        Pane buttonPane = new Pane();
        buttonPane.setPrefSize(seatingPlanImage.getWidth(), seatingPlanImage.getHeight());

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

        // Combine the image and buttons
        StackPane root = new StackPane(imageView, buttonPane);

        getChildren().addAll(root);

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
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 8; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("CC " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createBBBalconySeatsV1(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 75; // Adjust these values based on your actual image
        double startY = 250;
        double seatWidth = 30;
        double seatHeight = 30;
        double gap = 0;

        Label label = new Label("BB");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY - 140);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 5; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY - (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
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
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 6; i <= 23; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createBBBalconySeatsV3(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 865; // Adjust these values based on your actual image
        double startY = -460;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("BB");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY + 560);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 24; i <= 28; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("BB " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY + (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createAABalconySeatsV1(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 120; // Adjust these values based on your actual image
        double startY = 560;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("AA");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY - 490);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 20; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY - (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
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
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 21; i <= 33; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createAABalconySeatsV3(Pane pane) {
        // Balcony seats (BB 6 - 23)
        double startX = 825; // Adjust these values based on your actual image
        double startY = -735;
        double seatWidth = 30;
        double seatHeight = 25;
        double gap = 0;

        Label label = new Label("AA");
        label.setLayoutX(startX + 7);
        label.setLayoutY(startY + 810);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 34; i <= 53; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("AA " + i);
            seat.setLayoutX(startX);
            seat.setLayoutY(startY + (i - 1) * (seatHeight + gap));
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowQ(Pane pane) {
        // (Stall seats - Row Q)
        double startX = 340; // Adjust these values based on your actual image
        double startY = 100;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("Q");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 10; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("Q " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 10){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowP(Pane pane) {
        // (Stall seats - Row P)
        double startX = 330; // Adjust these values based on your actual image
        double startY = 120;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("P");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 11; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("P " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 11){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowO(Pane pane) {
        // (Stall seats - Row O)
        double startX = 180; // Adjust these values based on your actual image
        double startY = 140;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("O");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 20; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("O " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 20){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowN(Pane pane) {
        // (Stall seats - Row Q)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 170;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("N");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("N " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowM(Pane pane) {
        // (Stall seats - Row M)
        double startX = 240; // Adjust these values based on your actual image
        double startY = 200;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("M");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);


        for (int i = 1; i <= 16; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("M " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 16){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowL(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 230;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("L");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("L " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void createStallsSeatsRowK(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 260;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("K");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("K " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowJ(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 290;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("J");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("J " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowH(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 320;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("H");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("H " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowG(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 350;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("G");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("G " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowF(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 380;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("F");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("F " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowE(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 410;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("E");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("E " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowD(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 440;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("D");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("D " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowC(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 470;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("C");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("C " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowB(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 500;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("B");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("B " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #7a7a7a;"); // Default color is green
            pane.getChildren().add(seat);

            if(i == 1 || i == 19){
                seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            }
        }
    }

    private void createStallsSeatsRowA(Pane pane) {
        // (Stall seats - Row L)
        double startX = 200; // Adjust these values based on your actual image
        double startY = 530;
        double seatWidth = 30;
        double seatHeight = 5;
        double gap = 0;

        Label label = new Label("A");
        label.setLayoutX(startX - 20);
        label.setLayoutY(startY + 5);
        pane.getChildren().add(label);

        // Create Cc 1-8 seats
        for (int i = 1; i <= 19; i++) {
            Button seat = new Button("" + i);
            seat.setUserData("A " + i);
            seat.setLayoutX(startX + (i-1) * (seatWidth + gap));
            seat.setLayoutY(startY);
            seat.setPrefSize(seatWidth, seatHeight);
            seat.setOnAction(e -> handleSeatSelection(seat));
            seat.setStyle("-fx-background-color: #8d0293;"); // Default color is green
            pane.getChildren().add(seat);
        }
    }

    private void handleSeatSelection(Button seat) {
        // Retrieve row and seat number from button metadata (UserData)
        Object data = seat.getUserData();

        if (data instanceof String) {
            String[] parts = ((String) data).split(" "); // Example format: "A 5"

            if (parts.length == 2) {
                String row = parts[0]; // Row letter (e.g., "A")
                int seatNumber = Integer.parseInt(parts[1]); // Seat number (e.g., 5)

                // Toggle the seat color on selection
                String currentColor = seat.getStyle();

                if (currentColor.contains("-fx-background-color: #5daf24;")) {
                    // If it's green (unselected), change to red (selected)
                    seat.setStyle("-fx-background-color: #d00303;");
                    System.out.println("Selected seat: Row " + row + ", Seat " + seatNumber);
                } else {
                    // If it's red (selected), change back to green (unselected)
                    seat.setStyle("-fx-background-color: #5daf24;");
                    System.out.println("Deselected seat: Row " + row + ", Seat " + seatNumber);
                }
            }
        } else {
            System.out.println("Error: Seat data is missing.");
        }
    }
}