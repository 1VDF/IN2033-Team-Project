package boxoffice;

import BoxOfficeInterface.JDBC;
import BoxOfficeInterface.SeatingConfiguration;
import boxoffice.ui.HomePage;
import boxoffice.ui.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static java.awt.Color.RED;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        // Initialize the main layout (BorderPane) for the home page
        BorderPane root = new BorderPane();

        HomePage homePage = new HomePage();

        LoginPage loginPage = new LoginPage();
        root.setCenter(homePage);

        // Set the stage with title, scene, and show
        Scene scene = new Scene(root, 1200, 800);
        loginPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");

        primaryStage.setTitle("Box Office System");
        primaryStage.setScene(scene);
        primaryStage.show();

        JDBC jdbc = new JDBC();

        List<SeatingConfiguration> restrictedSeats = jdbc.getRestrictedSeats("Meeting");
        System.out.println("Number of restricted seats: " + restrictedSeats.size());
        for(SeatingConfiguration restricted : restrictedSeats){
            System.out.println(restricted);
        }

        List<String> calendarAvailability = jdbc.getCalendarAvailability(new Date(20250504));
        System.out.println(calendarAvailability);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
