package boxoffice.ui;

import boxoffice.database.Staff;
import boxoffice.models.Session;
import boxoffice.models.StaffRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;

/**
 * Represents the User Management page within the Box Office application.
 * <p>
 * This page provides administrative functionality to view, add, and remove staff members.
 * Data is retrieved and managed through the {@link StaffRepository}.
 * </p>
 * <p>
 * The interface includes a table of staff members and control buttons for interacting with the data.
 * </p>
 *
 */
public class UserManagementPage extends VBox {
    private final StaffRepository staffRepository;
    private final TableView<Staff> staffTable;
    private final ObservableList<Staff> staffList;

    /**
     * Constructs a new UserManagementPage instance, setting up the UI and loading staff data.
     */
    public UserManagementPage() {
        this.staffRepository = new StaffRepository();
        this.staffList = FXCollections.observableArrayList();
        this.staffTable = new TableView<>();

        setupUI();
        loadStaffData();
    }

    /**
     * Sets up the user interface layout and styling.
     * <p>
     * Includes the title, table view for staff, and a set of control buttons for user actions.
     * </p>
     */
    private void setupUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ECF0F1;");

        Text title = new Text("User Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.WHITE);
        DropShadow shadow = new DropShadow(10, Color.GRAY);
        title.setEffect(shadow);

        setupStaffTable();

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Add User");
        addButton.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> showAddUserDialog());

        Button deleteButton = new Button("Delete User");
        deleteButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> deleteSelectedUser());

        Button backButton = new Button("Back to Home");
        backButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage();
            homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
            this.getScene().setRoot(homePage);
        });

        buttonBox.getChildren().addAll(addButton, deleteButton, backButton);

        getChildren().addAll(title, staffTable, buttonBox);
    }

    /**
     * Configures the staff table view, including its columns and bindings.
     * <p>
     * Columns are mapped to fields in the {@link Staff} class.
     * </p>
     */
    private void setupStaffTable() {
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        staffTable.setPrefHeight(400);

        TableColumn<Staff, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("staffId"));

        TableColumn<Staff, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Staff, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Staff, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Staff, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        staffTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, roleCol, emailCol);
        staffTable.setItems(staffList);
    }

    /**
     * Loads all staff members from the database and displays them in the table.
     * <p>
     * Displays an error alert if the data cannot be retrieved.
     * </p>
     */
    private void loadStaffData() {
        try {
            staffList.clear();
            staffList.addAll(staffRepository.getAllStaff());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load staff data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Opens a dialog for entering new staff details.
     * <p>
     * If confirmed, the staff member is added to the database and the list is refreshed.
     * </p>
     */
    private void showAddUserDialog() {
        Dialog<Staff> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter user details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        ComboBox<Staff.Role> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll(Staff.Role.values());
        roleCombo.setValue(Staff.Role.Staff);

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Role:"), 0, 4);
        grid.add(roleCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Staff(
                        0,
                        firstNameField.getText(),
                        lastNameField.getText(),
                        roleCombo.getValue(),
                        emailField.getText(),
                        passwordField.getText()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(staff -> {
            try {
                staffRepository.addStaff(staff);
                loadStaffData();
                showAlert("Success", "User added successfully", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Error", "Failed to add user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * Deletes the selected user from the system after confirmation.
     * <p>
     * Prevents the deletion of the currently logged-in staff member.
     * </p>
     */
    private void deleteSelectedUser() {
        Staff selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a user to delete", Alert.AlertType.WARNING);
            return;
        }

        if (selected.getStaffId() == Session.getInstance().getCurrentStaff().getStaffId()) {
            showAlert("Error", "You cannot delete your own account", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete " + selected.getFirstName() + " " + selected.getLastName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    staffRepository.deleteStaff(selected.getStaffId());
                    loadStaffData();
                    showAlert("Success", "User deleted successfully", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Displays an alert with the given title, message, and alert type.
     *
     * @param title   the title of the alert window
     * @param message the main message to be displayed
     * @param type    the style/type of the alert
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
