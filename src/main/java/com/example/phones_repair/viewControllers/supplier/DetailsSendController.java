package com.example.phones_repair.viewControllers.supplier;

import com.example.phones_repair.entities.AvailableDetails;
import com.example.phones_repair.repositories.AvailableDetailsRepository;
import com.example.phones_repair.viewControllers.RoleSelectionController;
import com.example.phones_repair.viewModel.DetailOrdersListModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.phones_repair.viewControllers.RoleSelectionController.sceneHistory;

@Component
@FxmlView("/view/DetailsSendView.fxml")
public class DetailsSendController {
    public static int supplierId;

    @Autowired
    private AvailableDetailsRepository detailsRepository;

    @FXML
    private ListView<DetailOrdersListModel> listView;

    @FXML
    private Button backButton;

    @FXML
    private Button refreshButton;

    public DetailsSendController() {}

    @FXML
    public void initialize() {
        loadFilteredList();
        backButton.setOnAction(event -> goBack());

        listView.setCellFactory(lv -> new ListCell<DetailOrdersListModel>() {
            @Override
            protected void updateItem(DetailOrdersListModel order, boolean empty) {
                super.updateItem(order, empty);

                if (empty || order == null || order.getDetailName() == null || order.getDetailName().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {

                    VBox container = new VBox(5);
                    container.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

                    Label detailLabel = new Label("Detail: " + order.getDetailName());
                    detailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000;");

                    Label quantityLabel = new Label("Quantity: " + order.getQuantity());
                    quantityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

                    Label statusLabel = new Label("Status: " + order.getStatus());
                    statusLabel.setStyle("-fx-text-fill: green;");

                    Label orderDateLabel = new Label("Order date" + order.getOrderDate());
                    orderDateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff5722;");

                    Label deliveryDateLabel = new Label("Delivery date" + order.getDeliveryDate());
                    deliveryDateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff5722;");

                    Button executeBtn = new Button("Execute");
                    executeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                    executeBtn.setOnAction(e -> {
                        try {
                            sendOrderIdToApi(order.getId());
                        } catch (Exception ex) {
                            showAlert("Error", "Failed to perform action.");
                        }
                    });

                    container.getChildren().addAll(detailLabel, quantityLabel, statusLabel, orderDateLabel, deliveryDateLabel, executeBtn);
                    setGraphic(container);
                }
            }
        });

        refreshButton.setOnAction(e -> loadFilteredList());
    }

    private void loadFilteredList() {
        Platform.runLater(() -> {
            try {
                ObservableList<DetailOrdersListModel> items = getItemsFromApi();
                listView.setItems(items);
            } catch (Exception e) {
                showAlert("Error", "Unable to load data.");
            }
        });
    }

    private ObservableList<DetailOrdersListModel> getItemsFromApi() throws Exception {
        List<DetailOrdersListModel> items = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8012/details/supplier/" + supplierId + "/in-progress");
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

    private List<DetailOrdersListModel> parseItemsFromJson(String json) {
        List<DetailOrdersListModel> items = new ArrayList<>();
        if (json.contains("[")) {
            String[] jsonObjects = json.substring(1, json.length() - 1).split("\\},\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.replace("{", "").replace("}", "");
                String[] fields = jsonObject.split(",");
                int id = 0, quantity = 0;
                String detailId = "", status ="", orderDate = "", deliveryDate = "";
                for (String field : fields) {
                    if (field.contains("\"id\":")) {
                        id = Integer.parseInt(field.split(":")[1].trim());
                    } else if (field.contains("\"detailId\":")) {
                        detailId = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"quantity\":")) {
                        quantity = Integer.parseInt(field.split(":")[1].trim());
                    } else if (field.contains("\"status\":")) {
                        status = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"orderDate\":")) {
                        orderDate = field.split(":")[1].replace("\"", "").trim();
                    } else if (field.contains("\"deliveryDate\":")) {
                        deliveryDate = field.split(":")[1].replace("\"", "").trim();
                    }
                }
                String detailName = findDetailById(detailId);
                items.add(new DetailOrdersListModel(id,detailName, quantity, status, orderDate, deliveryDate));
            }
        }
        return items;
    }

    private String findDetailById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        else {
            Optional<AvailableDetails> availableDetailsOpt = detailsRepository.findById(Long.valueOf(id));
            if (availableDetailsOpt.isPresent()) {
                AvailableDetails availableDetails = availableDetailsOpt.get();
                return availableDetails.getName();
            } else {
                return null;
            }
        }
    }

    private void sendOrderIdToApi(int orderId) throws Exception {
        try {
            URL url = new URL("http://localhost:8012/details/sendDetail/" + Long.valueOf(orderId));
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
