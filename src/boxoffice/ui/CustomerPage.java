package boxoffice.ui;

import boxoffice.database.Customer;
import boxoffice.database.FOL;
import boxoffice.models.CustomerRepository;
import boxoffice.models.FOLRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.List;

/**
 * The CustomerPage class provides a user interface for managing customer details in the box office system.
 * It allows users to view, search, filter, and add customers, as well as display a list of customers
 * who are friends of Lancaster.
 */
public class CustomerPage extends VBox {

    private TableView<Customer> customerTable;
    private ObservableList<Customer> customerData;
    private CustomerRepository customerRepo = new CustomerRepository();
    private FOLRepository folRepo = new FOLRepository();
    private CheckBox folCheckBox;
    private TextField searchField;

    /**
     * Constructs a new CustomerPage and initialises the user interface components.
     * It loads the customer data from the database.
     *
     * @throws SQLException if there is an error loading customer data from the database
     */
    CustomerPage() throws SQLException {
        initializeUI();
        loadData();
    }

    /**
     * Initialises the user interface components of the CustomerPage.
     * This includes setting up the logo, title, search field, customer table, and buttons.
     */
    private void initializeUI() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        // Setting up the logo
        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(150);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(20, 0, 0, 20));

        Label title = new Label("Customer Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;-fx-text-fill: white;");
        title.setAlignment(Pos.TOP_LEFT);
        DropShadow shadow = new DropShadow(10, Color.GRAY);
        title.setEffect(shadow);

        HBox topBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search customers...");
        folCheckBox = new CheckBox("Friends of Lancaster Only");
        folCheckBox.setStyle("-fx-text-fill: white;");
        topBox.getChildren().addAll(searchField, folCheckBox);

        customerTable = new TableView<>();
        customerTable.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");

        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        customerTable.getColumns().addAll(idCol, nameCol, phoneCol, emailCol);
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                filterData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        folCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            try {
                filterData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button addButton = new Button("Add Customer");
        addButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        addButton.setOnAction(e -> showAddCustomerDialog());
        addButton.setAlignment(Pos.CENTER_RIGHT);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setAlignment(Pos.CENTER_LEFT);

        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage();
            homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
            this.getScene().setRoot(homePage);
        });

        HBox bottomBox = new HBox(940, addButton, backButton);

        this.getChildren().addAll(logoContainer, title, topBox, customerTable, bottomBox);
    }

    /**
     * Loads customer data from the database and populates the customer table.
     *
     * @throws SQLException if there is an error retrieving customer data from the database
     */
    private void loadData() throws SQLException {
        customerData = FXCollections.observableArrayList(customerRepo.getAllCustomers());
        customerTable.setItems(customerData);
    }

    /**
     * Filters the displayed customer data based on the search query and the "Friends of Lancaster" checkbox.
     *
     * @throws SQLException if there is an error filtering the customer data from the database
     */
    private void filterData() throws SQLException {
        String searchText = searchField.getText().toLowerCase();
        boolean folOnly = folCheckBox.isSelected();

        ObservableList<Customer> filteredData = FXCollections.observableArrayList();
        List<String> folCustomers = folRepo.getAllFriendsOfLancaster().stream()
                .map(FOL::getCustomerID)
                .toList();

        for (Customer customer : customerData) {
            boolean matchesSearch = searchText.isEmpty() ||
                    customer.getCustomerName().toLowerCase().contains(searchText) ||
                    customer.getCustomerID().toLowerCase().contains(searchText);

            boolean matchesFOL = !folOnly || folCustomers.contains(customer.getCustomerID());

            if (matchesSearch && matchesFOL) {
                filteredData.add(customer);
            }
        }

        customerTable.setItems(filteredData);
    }

    /**
     * Displays a dialog for adding a new customer.
     * The dialog allows the user to input the customer's ID, name, phone number, and email address.
     */
    private void showAddCustomerDialog() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Customer newCustomer = new Customer(
                        idField.getText(),
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText()
                );
                try {
                    CustomerRepository.addCustomer(newCustomer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return newCustomer;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(customer -> {
            customerData.add(customer);
            try {
                filterData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
