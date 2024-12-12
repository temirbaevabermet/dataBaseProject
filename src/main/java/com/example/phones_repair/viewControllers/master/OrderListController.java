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
@FxmlView("/view/OrdersListView.fxml")
public class OrderListController {
    @FXML
    private Button backButton;

    @FXML
    private Button categoryAllButton;

    @FXML
    private Button categoryFinishedButton;

    @FXML
    private Button categoryRepairButton;

    @FXML
    private Button categoryReplaceButton;

    @FXML
    private Button categoryCleanButton;

    @FXML
    private Button categoryLargestButton;

    @FXML
    private Button categorySmallestButton;

    @FXML
    private ListView<OrdersListsModel> listView;

    public OrderListController() {}

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> goBack());
        categoryAllButton.setOnAction(e -> loadFilteredList("all"));
        categoryFinishedButton.setOnAction(e -> loadFilteredList("finished"));
        categoryRepairButton.setOnAction(e -> loadFilteredList("repair"));
        categoryReplaceButton.setOnAction(e -> loadFilteredList("replace"));
        categoryCleanButton.setOnAction(e -> loadFilteredList("serve"));
        categoryLargestButton.setOnAction(e -> loadFilteredList("largest"));
        categorySmallestButton.setOnAction(e -> loadFilteredList("smallest"));

        listView.setCellFactory(lv -> new ListCell<OrdersListsModel>() {
            @Override
            protected void updateItem(OrdersListsModel order, boolean empty) {
                super.updateItem(order, empty);

                if (empty || order == null || order.getName() == null || order.getName().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {
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

                    container.getChildren().addAll(clientLabel, nameLabel, statusLabel, priceLabel);
                    setGraphic(container);
                }
            }
        });
    }

    private void loadFilteredList(String filter) {
        try {
            ObservableList<OrdersListsModel> items = getItemsFromApi(filter);
            listView.setItems(items);
        } catch (Exception e) {
            showAlert("Error", "Unable to load data.");
        }
    }

    private ObservableList<OrdersListsModel> getItemsFromApi(String filter) throws Exception {
        List<OrdersListsModel> items = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8012/orders/category/" + filter);
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
                String name = "", status ="", clientId = "";
                for (String field : fields) {
                    if (field.contains("\"id\":")) {
                        id = Integer.parseInt(field.split(":")[1].trim());
                    } else if (field.contains("\"clientId\":")) {
                        clientId = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"orderName\":")) {
                        name = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"status\":")) {
                        status = field.split(":")[1].replace("\"", "").trim();
                    }
                    else if (field.contains("\"price\":")) {
                        price = Integer.parseInt(field.split(":")[1].trim());
                    }
                }
                items.add(new OrdersListsModel(id,clientId, name, status, price));
            }
        }
        return items;
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
