package boxoffice.ui;

import boxoffice.database.Performance;
import boxoffice.models.PerformanceRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PerformancePage extends VBox {
    private final PerformanceRepository performanceRepository;

    private   ObservableList<Performance> performances;

    public PerformancePage() {
        this.performanceRepository = new PerformanceRepository();

        initializeUI();
    }

    private void initializeUI() {
        try {
            // Create table view
            TableView<Performance> table = new TableView<>();
            performances = FXCollections.observableArrayList(
                    performanceRepository.getAllPerformances()
            );

            // Create columns
            TableColumn<Performance, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("performanceId"));

            TableColumn<Performance, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<Performance, String> typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("performanceType"));

            TableColumn<Performance, String> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getDate().format(formatter)
                );
            });

            TableColumn<Performance, String> timeColumn = new TableColumn<>("Time");
            timeColumn.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                return javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getStartTime().format(formatter)
                );
            });

            TableColumn<Performance, Integer> durationColumn = new TableColumn<>("Duration (min)");
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));

            // Add columns to table
            table.getColumns().addAll(idColumn, titleColumn, typeColumn, dateColumn, timeColumn, durationColumn);
            table.setItems(performances);

            // Create book ticket button
            Button bookTicketButton = new Button("Book a ticket");

            // In PerformancePage.java
            bookTicketButton.setOnAction(event -> {
                Performance selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if(selected.getVenueID() == 1) {
                        TicketSalesPage ticketSalesPage = null; // Make sure selected is passed
                        try {
                            ticketSalesPage = new TicketSalesPage(selected);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        this.getScene().setRoot(ticketSalesPage);
                    }
                    else{
                        TicketSalesPageSmall ticketSalesPageSmall = null;
                        try {
                            ticketSalesPageSmall = new TicketSalesPageSmall(selected);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        this.getScene().setRoot(ticketSalesPageSmall);
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Please select a performance first").showAndWait();
                }
            });

            Button addPerformanceButton = new Button("Add Performance");
            addPerformanceButton.setOnAction(event -> showAddPerformanceDialog());

            // Create button container
            HBox buttonBox = new HBox(10, bookTicketButton, addPerformanceButton);
            buttonBox.setStyle("-fx-padding: 10;");

            // Add components to VBox
            this.getChildren().addAll(table, buttonBox);
            this.setSpacing(10);

        } catch (SQLException e) {
            e.printStackTrace();
            // You might want to add error handling UI here
        }
    }

    private void showAddPerformanceDialog() {
        // Create a new stage for the dialog
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Add New Performance");

        // Create form fields
        TextField titleField = new TextField();
        TextField typeField = new TextField();
        TextArea descriptionField = new TextArea();
        descriptionField.setPrefRowCount(3);
        DatePicker datePicker = new DatePicker();

        // Improved time input with validation
        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm (e.g., 14:30)");

        TextField durationField = new TextField();
        TextField venueIdField = new TextField();

        // Create form grid (same as before)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Title:"), titleField);
        grid.addRow(1, new Label("Type:"), typeField);
        grid.addRow(2, new Label("Description:"), descriptionField);
        grid.addRow(3, new Label("Date:"), datePicker);
        grid.addRow(4, new Label("Time (HH:mm):"), timeField);
        grid.addRow(5, new Label("Duration (minutes):"), durationField);
        grid.addRow(6, new Label("Venue ID:"), venueIdField);

        // Create buttons
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            try {
                // Validate time format
                String timeText = timeField.getText().trim();
                if (!timeText.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                    showAlert("Format Error", "Time must be in HH:mm format (e.g., 14:30)");
                    return;
                }

                // Parse input
                LocalDate date = datePicker.getValue();
                if (date == null) {
                    showAlert("Input Error", "Please select a date");
                    return;
                }

                LocalTime time = LocalTime.parse(timeText);
                int duration = Integer.parseInt(durationField.getText());
                int venueId = Integer.parseInt(venueIdField.getText());

                // Create new performance
                Performance performance = new Performance(
                        0, // ID will be auto-generated by database
                        titleField.getText(),
                        typeField.getText(),
                        descriptionField.getText(),
                        date,
                        time,
                        duration,
                        venueId
                );

                // Add to database
                boolean success = performanceRepository.addPerformance(performance);
                if (success) {
                    // Refresh table
                    performances.setAll(performanceRepository.getAllPerformances());
                    dialog.close();
                } else {
                    showAlert("Error", "Failed to add performance to database.");
                }
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Duration and Venue ID must be numbers");
            } catch (Exception e) {
                showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            }
        });

        // Rest of the method remains the same...
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> dialog.close());

        HBox buttonBox = new HBox(10, submitButton, cancelButton);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center-right;");

        // Create dialog content
        VBox dialogVBox = new VBox(10, grid, buttonBox);
        dialogVBox.setStyle("-fx-padding: 20;");

        // Show dialog
        Scene dialogScene = new Scene(dialogVBox);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}