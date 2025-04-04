package boxoffice;

import boxoffice.database.Staff;
import boxoffice.ui.HomePage;
import boxoffice.ui.LoginPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private final BoxOfficeManager boxOfficeManager = new BoxOfficeManager();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);

        // Set to true for development to skip login
        boolean skipLogin = false;

        if (skipLogin) {
            Staff mockStaff = new Staff(
                    1, "Dev", "User", "Manager", "dev@boxoffice.com", "password"
            );
            boxOfficeManager.setCurrentStaff(mockStaff);
            showHomePage();
        } else {
            showLoginPage();
        }
    }

    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(primaryStage, this);
        Scene scene = new Scene(loginPage);
        primaryStage.setTitle("Box Office System - Login");
        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(550);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public void showHomePage() {
        HomePage homePage = new HomePage(boxOfficeManager);
        Scene scene = new Scene(homePage, 1200, 800);
        primaryStage.setTitle("Box Office System");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }



    public BoxOfficeManager getBoxOfficeManager() {
        return boxOfficeManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

