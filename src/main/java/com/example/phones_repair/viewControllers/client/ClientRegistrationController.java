package com.example.phones_repair.viewControllers.client;

import com.example.phones_repair.viewControllers.RoleSelectionController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@FxmlView("/view/ClientRegistrationWindow.fxml")
public class ClientRegistrationController {
    private String clientId;

    @FXML
    private final FxWeaver fxWeaver;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button registerButton;

    public ClientRegistrationController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public void initialize() {
        registerButton.setOnAction(event -> registerClient(emailField.getText(), fullNameField.getText(), phoneNumberField.getText()));
    }

    private void registerClient(String email, String fullName, String phoneNumber) {
        try {
            String existingClientId = checkExistingClient(email);
            if (existingClientId != null && !existingClientId.isEmpty()) {
                clientId = existingClientId;
            } else {
                URL url = new URL("http://localhost:8012/clients/add");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonInputString = "{ \"email\": \"" + email + "\", \"fullName\": \"" + fullName + "\", \"phoneNumber\": \"" + phoneNumber + "\"}";

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
                        String responseBody = response.toString();
                        clientId = responseBody;
                    }
                } else {
                    throw new Exception("Failed to connect, response code: " + responseCode);
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Unable to connect to the server.");
        }
        openClientView(clientId);
    }


    private String checkExistingClient(String email) {
        try {
            URL url = new URL("http://localhost:8012/clients/findByEmail?email=" + email);
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
                    String responseBody = response.toString();
                    return parseClientIdFromJson(responseBody);
                }
            } else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            } else {
                throw new Exception("Failed to connect, response code: " + responseCode);
            }

        } catch (Exception e) {
            showAlert("Error", "Unable to check existing client.");
            return null;
        }
    }

    private String parseClientIdFromJson(String json) {
        String id = "";
        if (json.contains("{")) {
            String jsonObject = json.substring(1, json.length() - 1);
            String[] fields = jsonObject.split(",");

            for (String field : fields) {
                if (field.contains("\"id\":")) {
                    id = field.split(":")[1].replace("\"", "").trim();
                    break;
                }
            }
        }
        return id;
    }

    private void openClientView(String id) {
        Parent clientsView = fxWeaver.loadView(ChooseOptionController.class);

        ClientsOrdersController.clientId = id;
        if (clientId != null && !clientId.isEmpty()) {
            MakeOrderController.clientId = Integer.parseInt(id);
        }

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setTitle("Clients options");
        stage.setScene(new Scene(clientsView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}