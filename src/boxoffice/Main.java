package boxoffice;

import BoxOfficeInterface.JDBC;
import BoxOfficeInterface.SeatingConfiguration;
import boxoffice.ui.HomePage;
import boxoffice.ui.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * The entry point for the Box Office System JavaFX application.
 * <p>
 * This class extends {@link javafx.application.Application} and is responsible
 * for initializing and displaying the GUI. It begins with a login page and
 * demonstrates back-end interaction by calling methods from the {@link JDBC} class
 * to retrieve and print restricted and reserved seat data, as well as calendar availability.
 * </p>
 *
 * <p>
 * GUI elements are styled using CSS and displayed within a {@link BorderPane} layout.
 * </p>
 *
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by initializing the main UI components.
     * <p>
     * This method sets up a login screen as the starting interface, configures the scene,
     * and displays the main application window. It also establishes a connection to the database
     * through the {@link JDBC} class and performs sample data retrieval actions including:
     * <ul>
     *     <li>Fetching restricted seating configurations for "Small Hall"</li>
     *     <li>Fetching reserved seats for "Small Hall"</li>
     *     <li>Checking calendar availability for a specific date</li>
     * </ul>
     * </p>
     *
     * @param primaryStage the primary window for this application.
     * @throws SQLException if a database error occurs during data retrieval.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    @Override
    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException {
        BorderPane root = new BorderPane();

        LoginPage loginPage = new LoginPage();
        root.setCenter(loginPage);

        Scene scene = new Scene(root, 1200, 800);
        loginPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");

        primaryStage.setTitle("Box Office System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Backend data retrieval and demonstration
        JDBC jdbc = new JDBC();

        List<SeatingConfiguration> restrictedSeats = jdbc.getRestrictedSeats("Small Hall");
        System.out.println("Number of restricted seats: " + restrictedSeats.size());
        for (SeatingConfiguration restricted : restrictedSeats) {
            System.out.println(restricted);
        }

        List<SeatingConfiguration> reservedSeats = jdbc.getReservedSeats("Small Hall");
        System.out.println("Number of reserved seats: " + reservedSeats.size());
        for (SeatingConfiguration reserved : reservedSeats) {
            System.out.println(reserved);
        }

        List<String> calendarAvailability = jdbc.getCalendarAvailability(new Date(20250504));
        System.out.println(calendarAvailability);
    }

    /**
     * The main entry point for launching the Box Office System application.
     * <p>
     * This method calls {@link #launch(String...)}, which initializes the JavaFX runtime
     * and triggers the {@code start} method.
     * </p>
     *
     * @param args command-line arguments passed to the application (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
