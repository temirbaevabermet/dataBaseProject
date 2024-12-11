package com.example.phones_repair.viewControllers.master;

import com.example.phones_repair.viewControllers.RoleSelectionController;
import com.example.phones_repair.viewModel.OrdersListsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.phones_repair.viewControllers.RoleSelectionController.sceneHistory;

@Component
@FxmlView("/view/OrdersExecuteListView.fxml")
public class OrderExecutionController {
    @FXML
    private ListView<OrdersListsModel> listView;

    @FXML
    private Button backButton;

    @FXML
    private Button refreshButton;

    public OrderExecutionController() {}

    @FXML
    public void initialize() {
        loadFilteredList();
        backButton.setOnAction(event -> goBack());

        listView.setCellFactory(lv -> new ListCell<OrdersListsModel>() {
            @Override
            protected void updateItem(OrdersListsModel order, boolean empty) {
                super.updateItem(order, empty);
                VBox container = new VBox(5);
                container.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

                Label clientLabel = new Label("Client's ID: " + order.getClientId());
                clientLabel.setStyle("-fx-text-fill: #666;");

                Label nameLabel = new Label("Order: " + order.getName());
                nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000;");

                Label statusLabel = new Label("Status: " + order.getStatus());
                statusLabel.setStyle("-fx-text-fill: green;");

                Label priceLabel = new Label("Price: $" + order.getPrice());
                priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff5722;");

                Button executeBtn = new Button("Execute");
                executeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                executeBtn.setOnAction(e -> {
                    try {
                        sendOrderIdToApi(order.getId());
                    } catch (Exception ex) {
                        showAlert("Error", "Failed to perform action.");
                    }
                });

                container.getChildren().addAll(clientLabel, nameLabel, statusLabel, priceLabel, executeBtn);
                setGraphic(container);
            }
        });

        refreshButton.setOnAction(e -> {loadFilteredList();});
    }

    private void loadFilteredList() {
        try {
            ObservableList<OrdersListsModel> items = getItemsFromApi();
            listView.setItems(items);
        } catch (Exception e) {
            showAlert("Error", "Unable to load data.");
        }
    }

    private ObservableList<OrdersListsModel> getItemsFromApi() throws Exception {
        List<OrdersListsModel> items = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8012/orders/category/in-progress");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    items = parseItemsFromJson(response.toString());
                }
            } else {
                throw new Exception("Failed to connect, response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return FXCollections.observableArrayList(items);
    }

    private List<OrdersListsModel> parseItemsFromJson(String json) {
        List<OrdersListsModel> items = new ArrayList<>();
        if (json.contains("[")) {
            String[] jsonObjects = json.substring(1, json.length() - 1).split("\\},\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.replace("{", "").replace("}", "");
                String[] fields = jsonObject.split(",");
                int id = 0, price = 0;
                String client = "", name = "", status ="";
                for (String field : fields) {
                    if (field.contains("\"id\":")) {
                        id = Integer.parseInt(field.split(":")[1].trim());
                    } else if (field.contains("\"clientId\":")) {
                        client = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"orderName\":")) {
                        name = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"status\":")) {
                        status = field.split(":")[1].replace("\"", "").trim();
                    }
                    else if (field.contains("\"price\":")) {
                        price = Integer.parseInt(field.split(":")[1].trim());
                    }
                }
                items.add(new OrdersListsModel(id,client, name, status, price));
            }
        }
        return items;
    }

    private void sendOrderIdToApi(int orderId) throws Exception {
        try {
            URL url = new URL("http://localhost:8012/orders/execute/" + orderId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                showAlert("Success", "Action performed for order ID: " + orderId);
            } else {
                throw new Exception("Failed to connect, response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void goBack() {
        if (!sceneHistory.isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene previousScene = sceneHistory.remove(sceneHistory.size() - 1);
            stage.setScene(previousScene);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
