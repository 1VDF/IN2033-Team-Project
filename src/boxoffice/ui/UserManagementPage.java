package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.database.Staff;
import boxoffice.models.StaffRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class UserManagementPage extends VBox {
    private final BoxOfficeManager boxOfficeManager;
    private final StaffRepository staffRepository;
    private final TableView<Staff> staffTable;
    private final ObservableList<Staff> staffList;

    public UserManagementPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;
        this.staffRepository = new StaffRepository();
        this.staffList = FXCollections.observableArrayList();
        this.staffTable = new TableView<>();

        setupUI();
        loadStaffData();
    }

    private void setupUI() {
        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ECF0F1;");

        Text title = new Text("User Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#2C3E50"));

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
        backButton.setOnAction(e -> getScene().setRoot(new HomePage(boxOfficeManager)));

        buttonBox.getChildren().addAll(addButton, deleteButton, backButton);

        getChildren().addAll(title, staffTable, buttonBox);
    }

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

    private void loadStaffData() {
        try {
            staffList.clear();
            staffList.addAll(staffRepository.getAllStaff());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load staff data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

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
        ComboBox<String> roleCombo = new ComboBox<>();

        // Set available roles based on current user's role
        if (boxOfficeManager.getCurrentStaff().getRole().equals("Manager")) {
            roleCombo.getItems().addAll("Staff", "Deputy Manager", "Manager");
        } else if (boxOfficeManager.getCurrentStaff().getRole().equals("Deputy Manager")) {
            roleCombo.getItems().addAll("Staff", "Deputy Manager");
        }
        roleCombo.setValue("Staff");

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

    private void deleteSelectedUser() {
        Staff selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a user to delete", Alert.AlertType.WARNING);
            return;
        }

        if (selected.getStaffId() == boxOfficeManager.getCurrentStaff().getStaffId()) {
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}