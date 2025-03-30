package boxoffice.ui;

import boxoffice.database.Performance;
import boxoffice.models.PerformanceRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class PerformancePage extends VBox {
    private final PerformanceRepository performanceRepository;
    // Callback for when "Book a ticket" is clicked

    public PerformancePage() {
        this.performanceRepository = new PerformanceRepository();

        initializeUI();
    }

    private void initializeUI() {
        try {
            // Create table view
            TableView<Performance> table = new TableView<>();
            ObservableList<Performance> performances = FXCollections.observableArrayList(
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
            bookTicketButton.setOnAction(event -> {
                Performance selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    TicketSalesPage ticketSalesPage = new TicketSalesPage();
                    this.getScene().setRoot(ticketSalesPage);;
                }
            });

            // Add components to VBox
            this.getChildren().addAll(table, bookTicketButton);
            this.setSpacing(10);

        } catch (SQLException e) {
            e.printStackTrace();
            // You might want to add error handling UI here
        }
    }
}