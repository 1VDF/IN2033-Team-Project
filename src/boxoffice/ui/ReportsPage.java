package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.models.ReportsRepository;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Tooltip;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;


import java.time.LocalDate;
import java.util.Map;

public class ReportsPage extends VBox {

    private final BoxOfficeManager boxOfficeManager;
    private PieChart pieChart;
    private Text totalText;

    public ReportsPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        Text title = new Text("Box Office Reports");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #2a2a2a;");
        title.setFont(Font.font("Arial", 30));
        DropShadow shadow = new DropShadow(10, Color.GRAY);
        title.setEffect(shadow);

        ComboBox<String> reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll("Tickets Sold", "Revenue");
        reportTypeComboBox.setValue("Tickets Sold");
        reportTypeComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: #ffffff; -fx-border-radius: 20px;");

        ComboBox<String> timeRangeComboBox = new ComboBox<>();
        timeRangeComboBox.getItems().addAll("Today", "Last 7 days", "Last 30 days");
        timeRangeComboBox.setValue("Today");
        timeRangeComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: #ffffff; -fx-border-radius: 20px;");

        StackPane chartContainer = new StackPane();
        pieChart = createPieChart(ReportsRepository.getTicketSalesByPerformanceForDateRange(LocalDate.now(), LocalDate.now()));  // Default chart
        chartContainer.getChildren().add(pieChart);
        chartContainer.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 20px;");
        chartContainer.setEffect(new DropShadow(10, Color.GRAY));

        totalText = new Text();
        totalText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #4CAF50;");
        totalText.setFont(Font.font("Arial", 18));

        HBox comboBoxContainer = new HBox(20, reportTypeComboBox, timeRangeComboBox);
        comboBoxContainer.setAlignment(Pos.CENTER);
        comboBoxContainer.setStyle("-fx-padding: 20px;");

        reportTypeComboBox.setOnAction(event -> updateChart(reportTypeComboBox, timeRangeComboBox));
        timeRangeComboBox.setOnAction(event -> updateChart(reportTypeComboBox, timeRangeComboBox));

        this.getChildren().addAll(title, comboBoxContainer, chartContainer, totalText);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 30px; -fx-spacing: 20px;");
    }

    private void updateChart(ComboBox<String> reportTypeComboBox, ComboBox<String> timeRangeComboBox) {
        String selectedReport = reportTypeComboBox.getValue();
        String selectedTimeRange = timeRangeComboBox.getValue();
        Map<String, ? extends Number> performanceSales;

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch (selectedTimeRange) {
            case "Today":
                startDate = LocalDate.now();
                endDate = LocalDate.now();
                break;
            case "Last 7 days":
                startDate = LocalDate.now().minusDays(7);
                endDate = LocalDate.now();
                break;
            case "Last 30 days":
                startDate = LocalDate.now().minusDays(30);
                endDate = LocalDate.now();
                break;
        }

        if (selectedReport.equals("Tickets Sold")) {
            performanceSales = ReportsRepository.getTicketSalesByPerformanceForDateRange(startDate, endDate);
        } else {
            performanceSales = ReportsRepository.getRevenueByPerformanceForDateRange(startDate, endDate);
        }

        pieChart.getData().clear();
        pieChart.getData().addAll(createPieChart(performanceSales).getData());

        double totalSales = performanceSales.values().stream()
                .mapToDouble(Number::doubleValue)
                .sum();

        String totalTextContent = selectedReport.equals("Tickets Sold")
                ? String.format("Total Tickets Sold: %d", (int) totalSales)
                : String.format("Total Revenue: £%.2f", totalSales);

        totalText.setText(totalTextContent);
    }

    // Method to create PieChart
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
}
