package com.example.phones_repair.viewControllers;

import com.example.phones_repair.viewControllers.client.ClientRegistrationController;
import com.example.phones_repair.viewControllers.master.MasterViewController;
import com.example.phones_repair.viewControllers.supplier.SupplierViewController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
@FxmlView("/view/RoleSelectionView.fxml")
public class RoleSelectionController {
    public static final List<Scene> sceneHistory = new ArrayList<>();

    private final FxWeaver fxWeaver;

    @FXML
    private Button clientButton;

    @FXML
    private Button masterButton;

    @FXML
    private Button supplierButton;

    public RoleSelectionController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        clientButton.setOnAction(e -> openClientRegistrationWindow());
        masterButton.setOnAction(e -> openMasterWindow());
        supplierButton.setOnAction(e -> openSupplierRegistrationWindow());
    }

    private void openClientRegistrationWindow() {
        Parent clientView = fxWeaver.loadView(ClientRegistrationController.class);
        Stage stage = (Stage) clientButton.getScene().getWindow();
        stage.setTitle("Client Registration");
        stage.setScene(new Scene(clientView));
        stage.show();
        sceneHistory.add(stage.getScene());
    }

    private void openMasterWindow() {
        Parent masterView = fxWeaver.loadView(MasterViewController.class);
        Stage stage = (Stage) masterButton.getScene().getWindow();
        stage.setTitle("Master");
        stage.setScene(new Scene(masterView));
        stage.show();
        sceneHistory.add(stage.getScene());
    }

    private void openSupplierRegistrationWindow() {
        Parent clientView = fxWeaver.loadView(SupplierViewController.class);
        Stage stage = (Stage) clientButton.getScene().getWindow();
        stage.setTitle("Client Registration");
        stage.setScene(new Scene(clientView));
        stage.show();
        sceneHistory.add(stage.getScene());
    }

}
