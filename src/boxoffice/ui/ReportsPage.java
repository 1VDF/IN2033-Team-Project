package boxoffice.ui;

import boxoffice.models.ReportsRepository;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * The ReportsPage class provides a UI for displaying and exporting box office reports.
 * <p>It allows the user to view reports on ticket sales or revenue in either a pie chart or bar chart format.</p>
 * <p>The reports can be filtered by different time ranges, and the data can be exported as a CSV file.</p>
 */
public class ReportsPage extends VBox {

    private Chart currentChart;
    private Text totalText;
    private String currentReportType = "Tickets Sold";
    private LocalDate currentStartDate = LocalDate.now();
    private LocalDate currentEndDate = LocalDate.now();
    private String currentChartType = "Pie Chart";

    /**
     * Constructs a ReportsPage UI with chart controls and report filters.
     */
    public ReportsPage() {
        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(190);
        logoView.setFitWidth(190);
        logoView.setPreserveRatio(true);

        Text title = new Text("Box Office Reports");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #ffffff;");
        title.setFont(Font.font("Arial", 30));
        DropShadow shadow = new DropShadow(10, Color.GRAY);
        title.setEffect(shadow);
        title.setX(470);
        title.setY(120);

        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(20, 0, 0, 20));

        ComboBox<String> chartTypeComboBox = new ComboBox<>();
        chartTypeComboBox.getItems().addAll("Pie Chart", "Bar Chart");
        chartTypeComboBox.setValue("Pie Chart");
        chartTypeComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: #ffffff; -fx-border-radius: 20px;");

        ComboBox<String> reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll("Tickets Sold", "Revenue");
        reportTypeComboBox.setValue("Tickets Sold");
        reportTypeComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: #ffffff; -fx-border-radius: 20px;");

        ComboBox<String> timeRangeComboBox = new ComboBox<>();
        timeRangeComboBox.getItems().addAll("Today", "Last 7 days", "Last 30 days");
        timeRangeComboBox.setValue("Today");
        timeRangeComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: #ffffff; -fx-border-radius: 20px;");

        StackPane chartContainer = new StackPane();
        currentChart = createPieChart(ReportsRepository.getTicketSalesByPerformanceForDateRange(LocalDate.now(), LocalDate.now()));
        chartContainer.getChildren().add(currentChart);
        chartContainer.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 20px;");
        chartContainer.setEffect(new DropShadow(10, Color.GRAY));

        totalText = new Text();
        totalText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4CAF50;");
        totalText.setFont(Font.font("Arial", 18));

        Button backButtonHome = new Button("Back");
        backButtonHome.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        backButtonHome.setLayoutX(-50);
        backButtonHome.setLayoutY(680);

        backButtonHome.setOnAction(e -> {
            HomePage homePage = new HomePage();
            homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
            this.getScene().setRoot(homePage);
        });

        HBox comboBoxContainer = new HBox(20, chartTypeComboBox, reportTypeComboBox, timeRangeComboBox);
        comboBoxContainer.setAlignment(Pos.CENTER);
        comboBoxContainer.setStyle("-fx-padding: 20px;");

        chartTypeComboBox.setOnAction(event -> {
            currentChartType = chartTypeComboBox.getValue();
            updateChart(reportTypeComboBox, timeRangeComboBox);
        });
        reportTypeComboBox.setOnAction(event -> updateChart(reportTypeComboBox, timeRangeComboBox));
        timeRangeComboBox.setOnAction(event -> updateChart(reportTypeComboBox, timeRangeComboBox));

        Button exportButton = createExportButton();
        HBox buttonContainer = new HBox(20, exportButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setStyle("-fx-padding: 20px;");

        this.getChildren().addAll(logoContainer, title, comboBoxContainer, chartContainer, totalText, buttonContainer, backButtonHome);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Updates the chart based on selected report type and time range.
     * <p>This method listens for changes in the report type and time range, updates the start and end dates accordingly,</p>
     * <p>and generates the appropriate chart (either pie or bar) with the relevant data.</p>
     *
     * @param reportTypeComboBox ComboBox containing report types.
     * @param timeRangeComboBox ComboBox containing time ranges.
     */
    private void updateChart(ComboBox<String> reportTypeComboBox, ComboBox<String> timeRangeComboBox) {
        currentReportType = reportTypeComboBox.getValue();
        String selectedTimeRange = timeRangeComboBox.getValue();
        Map<String, ? extends Number> chartData;

        currentStartDate = LocalDate.now();
        currentEndDate = LocalDate.now();

        switch (selectedTimeRange) {
            case "Today":
                currentStartDate = LocalDate.now();
                currentEndDate = LocalDate.now();
                break;
            case "Last 7 days":
                currentStartDate = LocalDate.now().minusDays(6);
                currentEndDate = LocalDate.now();
                break;
            case "Last 30 days":
                currentStartDate = LocalDate.now().minusDays(29);
                currentEndDate = LocalDate.now();
                break;
        }

        StackPane chartContainer = (StackPane) this.getChildren().get(3);
        chartContainer.getChildren().clear();

        if (currentChartType.equals("Pie Chart")) {
            if (currentReportType.equals("Tickets Sold")) {
                chartData = ReportsRepository.getTicketSalesByPerformanceForDateRange(currentStartDate, currentEndDate);
            } else {
                chartData = ReportsRepository.getRevenueByPerformanceForDateRange(currentStartDate, currentEndDate);
            }
            currentChart = createPieChart(chartData);
        } else {
            if (currentReportType.equals("Tickets Sold")) {
                chartData = ReportsRepository.getDailyTicketSales(currentStartDate, currentEndDate);
            } else {
                chartData = ReportsRepository.getDailyRevenue(currentStartDate, currentEndDate);
            }
            currentChart = createBarChart(chartData);
        }

        chartContainer.getChildren().add(currentChart);

        double total = chartData.values().stream()
                .mapToDouble(Number::doubleValue)
                .sum();

        String totalTextContent = currentReportType.equals("Tickets Sold")
                ? String.format("Total Tickets Sold: %d", (int) total)
                : String.format("Total Revenue: £%.2f", total);

        totalText.setText(totalTextContent);
    }

    /**
     * Creates a BarChart based on daily report data.
     * <p>This method takes in daily data and creates a bar chart representation of it,</p>
     * <p>including formatting the x and y axes and adding tooltips for each bar.</p>
     *
     * @param dailyData Map containing the data for the bar chart.
     * @return A BarChart object displaying the daily data.
     */
    private BarChart<String, Number> createBarChart(Map<String, ? extends Number> dailyData) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Daily " + currentReportType);
        barChart.setLegendVisible(false);
        barChart.setAnimated(true);

        xAxis.setLabel("Date");
        yAxis.setLabel(currentReportType);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        dailyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String date = entry.getKey();
                    Number value = entry.getValue();

                    XYChart.Data<String, Number> data = new XYChart.Data<>(date, value);
                    series.getData().add(data);

                    data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            Tooltip tooltip = new Tooltip(String.format("%s\n%s: %s",
                                    date,
                                    currentReportType,
                                    currentReportType.equals("Tickets Sold") ?
                                            value.intValue() :
                                            String.format("£%.2f", value.doubleValue())));
                            Tooltip.install(newValue, tooltip);
                            newValue.setStyle("-fx-bar-fill: #3498DB; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0.0, 0, 1);");
                        }
                    });
                });

        barChart.getData().add(series);

        barChart.setStyle("-fx-background-color: white;");
        xAxis.setStyle("-fx-font-size: 12px;");
        yAxis.setStyle("-fx-font-size: 12px;");

        return barChart;
    }

    /**
     * Creates a PieChart based on performance report data.
     * <p>This method generates a pie chart that represents the sales for different performances.</p>
     * <p>Each segment of the pie chart corresponds to a performance, and the size of the segment is proportional to the sales.</p>
     *
     * @param performanceSales Map containing the performance sales data.
     * @return A PieChart object displaying the performance sales data.
     */
    private PieChart createPieChart(Map<String, ? extends Number> performanceSales) {
        PieChart pieChart = new PieChart();
        double totalSales = performanceSales.values().stream()
                .mapToDouble(Number::doubleValue)
                .sum();

        for (Map.Entry<String, ? extends Number> entry : performanceSales.entrySet()) {
            String performanceTitle = entry.getKey();
            Number sales = entry.getValue();

            double percentage = (sales.doubleValue() / totalSales) * 100;

            PieChart.Data data = new PieChart.Data(performanceTitle + " (" + sales + ")", sales.doubleValue());
            pieChart.getData().add(data);

            data.getNode().setOnMouseEntered(event -> {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-pie-label-style: -fx-font-size 14px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0.9, 0, 0);");
                    Tooltip tooltip = new Tooltip(String.format("%.2f%%", percentage));
                    Tooltip.install(data.getNode(), tooltip);
                }
            });
        }

        pieChart.setAnimated(true);
        return pieChart;
    }

    /**
     * Creates the export button used to trigger data export.
     * <p>This button allows the user to export the report data to a CSV file.</p>
     *
     * @return A Button object for exporting the report data.
     */
    private Button createExportButton() {
        Button exportButton = new Button("Export Data");
        exportButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold;");
        exportButton.setOnAction(e -> exportDataToCSV());
        return exportButton;
    }

    /**
     * Exports the current report data to a CSV file.
     * <p>This method writes the report data to a CSV file based on the selected report type and time range.</p>
     */
    private void exportDataToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report Data");
        fileChooser.setInitialFileName(generateDefaultFilename());

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(this.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                Map<String, ? extends Number> data;

                if (currentChartType.equals("Pie Chart")) {
                    // For pie chart, export performance based data
                    writer.write("Performance, " + currentReportType + "\n");

                    if (currentReportType.equals("Tickets Sold")) {
                        data = ReportsRepository.getTicketSalesByPerformanceForDateRange(currentStartDate, currentEndDate);
                    } else {
                        data = ReportsRepository.getRevenueByPerformanceForDateRange(currentStartDate, currentEndDate);
                    }
                } else {
                    // For bar chart, exporting daily data
                    writer.write("Date, " + currentReportType + "\n");

                    if (currentReportType.equals("Tickets Sold")) {
                        data = ReportsRepository.getDailyTicketSales(currentStartDate, currentEndDate);
                    } else {
                        data = ReportsRepository.getDailyRevenue(currentStartDate, currentEndDate);
                    }
                }

                for (Map.Entry<String, ? extends Number> entry : data.entrySet()) {
                    writer.write(String.format("\"%s\", %s\n",
                            entry.getKey().replace("\"", "\"\""),
                            entry.getValue()));
                }

                double total = data.values().stream()
                        .mapToDouble(Number::doubleValue)
                        .sum();

                if (currentReportType.equals("Tickets Sold")) {
                    writer.write(String.format("\"Total\", %d\n", (int) total));
                } else {
                    writer.write(String.format("\"Total\", %.2f\n", total));
                }

                showAlert("Export Successful",
                        "Data has been successfully exported to:\n" + file.getAbsolutePath());

            } catch (IOException ex) {
                showAlert("Export Error",
                        "An error occurred while exporting data:\n" + ex.getMessage());
            }
        }
    }

    /**
     * Generates a default filename for the exported CSV file based on the selected report and time range.
     * <p>This method generates the default filename based on the report type and date range.</p>
     *
     * @return The generated default filename for the CSV.
     */
    private String generateDefaultFilename() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateRange = currentStartDate.equals(currentEndDate) ?
                currentStartDate.format(formatter) :
                currentStartDate.format(formatter) + "_to_" + currentEndDate.format(formatter);
        return currentReportType.replace(" ", "_") + "_" + dateRange + ".csv";
    }

    /**
     * Shows an alert with a given title and message.
     *
     * @param title The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
