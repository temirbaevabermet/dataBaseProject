package com.example.phones_repair.viewControllers.client;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.phones_repair.viewControllers.RoleSelectionController.sceneHistory;


@Component
@FxmlView("/view/MakeOrderView.fxml")
public class MakeOrderController {
    public static int clientId;

    @FXML
    private Button backButton;

    @FXML
    private Button repairDisplayBtn;

    @FXML
    private Button repairKeyboardBtn;

    @FXML
    private Button repairMotherboardBtn;

    @FXML
    private Button replaceBatteryBtn;

    @FXML
    private Button replaceDisplayBtn;

    @FXML
    private Button replaceProcessorBtn;

    @FXML
    private Button replaceMotherboardBtn;

    @FXML
    private Button replaceRAMBtn;

    @FXML
    private Button cleanDustBtn;

    @FXML
    private Button cleanScratchesBtn;

    public MakeOrderController() {}

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> goBack());
        repairDisplayBtn.setOnAction(event -> sendOrderIdToApi(clientId, "repair", "display"));
        repairKeyboardBtn.setOnAction(event -> sendOrderIdToApi(clientId, "repair", "keyboard"));
        repairMotherboardBtn.setOnAction(event -> {sendOrderIdToApi(clientId, "repair", "motherboard");});
        replaceBatteryBtn.setOnAction(event -> sendOrderIdToApi(clientId, "replace", "battery"));
        replaceDisplayBtn.setOnAction(event -> sendOrderIdToApi(clientId, "replace", "display"));
        replaceProcessorBtn.setOnAction(event -> sendOrderIdToApi(clientId, "replace", "processor"));
        replaceMotherboardBtn.setOnAction(event -> sendOrderIdToApi(clientId, "replace", "motherboard"));
        replaceRAMBtn.setOnAction(event -> sendOrderIdToApi(clientId, "replace", "RAM"));
        cleanDustBtn.setOnAction(event -> sendOrderIdToApi(clientId, "clean", "dust"));
        cleanScratchesBtn.setOnAction(event -> sendOrderIdToApi(clientId, "clean", "scratches"));
    }

    private void sendOrderIdToApi(int clientId, String order_option, String option_subject) {
        try {
            URL url = new URL("http://localhost:8012/orders/order");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{ \"clientId\": \"" + clientId + "\", \"order_option\": \"" + order_option + "\", \"option_subject\": \"" + option_subject + "\"}";

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
