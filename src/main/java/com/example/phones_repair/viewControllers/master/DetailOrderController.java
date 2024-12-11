package com.example.phones_repair.viewControllers.master;

import com.example.phones_repair.entities.AvailableDetails;
import com.example.phones_repair.entities.Supplier;
import com.example.phones_repair.repositories.SupplierRepository;
import com.example.phones_repair.viewControllers.RoleSelectionController;
import com.example.phones_repair.viewModel.AvailableDetailsListModel;
import com.example.phones_repair.viewModel.DetailOrdersListModel;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.phones_repair.viewControllers.RoleSelectionController.sceneHistory;

@Component
@FxmlView("/view/DetailOrderView.fxml")
public class DetailOrderController {
    @FXML
    private Button backButton;

    @FXML
    private ListView<AvailableDetailsListModel> listView;

    @FXML
    private TextField quantityField;

    public DetailOrderController() {}

    @FXML
    public void initialize() {
        loadFilteredList();

        listView.setCellFactory(lv -> new ListCell<AvailableDetailsListModel>() {
            @Override
            protected void updateItem(AvailableDetailsListModel order, boolean empty) {
                super.updateItem(order, empty);

                if (empty || order == null || order.getDetailName() == null || order.getDetailName().isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(5);
                    container.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #ccc;");

                    Label detailLabel = new Label("Detail: " + order.getDetailName());
                    detailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000;");

                    Label priceLabel = new Label("Price: " + order.getPrice());
                    priceLabel.setStyle("-fx-text-fill: green;");

                    quantityField = new TextField("1");
                    int quantity = Integer.parseInt(quantityField.getText());

                    Button orderBtn = new Button("Order");
                    orderBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                    orderBtn.setOnAction(e -> {
                        try {
                            sendOrderIdToApi(order.getId(), quantity);
                        } catch (Exception ex) {
                            showAlert("Error", "Failed to perform action.");
                        }
                    });

                    container.getChildren().addAll(detailLabel, priceLabel, quantityField, orderBtn);
                    setGraphic(container);
                }
            }
        });
        backButton.setOnAction(e -> goBack());
    }

    private void loadFilteredList() {
        try {
            ObservableList<AvailableDetailsListModel> items = getItemsFromApi();
            listView.setItems(items);
        } catch (Exception e) {
            showAlert("Error", "Unable to load data.");
        }
    }

    private ObservableList<AvailableDetailsListModel> getItemsFromApi() throws Exception {
        List<AvailableDetailsListModel> items = new ArrayList<>();

        try {
            URL url = new URL("http://localhost:8012/details/showAvailable");
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

    private List<AvailableDetailsListModel> parseItemsFromJson(String json) {
        List<AvailableDetailsListModel> items = new ArrayList<>();
        if (json.contains("[")) {
            String[] jsonObjects = json.substring(1, json.length() - 1).split("\\},\\{");
            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.replace("{", "").replace("}", "");
                String[] fields = jsonObject.split(",");
                int id = 0, price = 0;
                String detailName = "", supplier ="";
                for (String field : fields) {
                    if (field.contains("\"id\":")) {
                        id = Integer.parseInt(field.split(":")[1].trim());
                    } else if (field.contains("\"name\":")) {
                        detailName = field.split(":")[1].replace("\"", "").trim();
                    }  else if (field.contains("\"price\":")) {
                        price = Integer.parseInt(field.split(":")[1].trim());
                    }
                }
                items.add(new AvailableDetailsListModel(id,detailName, price));
            }
        }
        return items;
    }

    private void sendOrderIdToApi(int detailId, int quantity) throws Exception {
        try {
            URL url = new URL("http://localhost:8012/details/orderDetail");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{ \"detailId\": \"" + detailId + "\", \"quantity\": \"" + quantity + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }
            } else {
                throw new Exception("Failed to connect, response code: " + responseCode);
            }
        } catch (Exception e) {
            showAlert("Error", "Unable to connect to the server.");
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
