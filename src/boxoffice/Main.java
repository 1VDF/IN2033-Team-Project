package boxoffice;

import boxoffice.ui.HomePage;
import boxoffice.ui.TicketSalesPage;
import boxoffice.ui.GroupBookingsPage;
import boxoffice.ui.RefundsPage;
import boxoffice.ui.ReportsPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BoxOfficeManager boxOfficeManager = new BoxOfficeManager();

        // Initialize the main layout (BorderPane) for the home page
        BorderPane root = new BorderPane();

        // HomePage with navigation buttons
        HomePage homePage = new HomePage(boxOfficeManager);
        root.setCenter(homePage);

        // Set the stage with title, scene, and show
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Box Office System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
