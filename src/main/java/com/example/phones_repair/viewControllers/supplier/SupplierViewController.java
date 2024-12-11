package com.example.phones_repair.viewControllers.supplier;

import com.example.phones_repair.viewControllers.RoleSelectionController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/SupplierView.fxml")
public class SupplierViewController {

    private final FxWeaver fxWeaver;

    @FXML
    private Button viewOrdersButton;

    @FXML
    private Button executeOrderButton;

    @FXML
    private TextField idInputField;;

    public SupplierViewController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        viewOrdersButton.setOnAction(e -> openOrdersWindow(idInputField.getText()));
        executeOrderButton.setOnAction(e -> openOrderExecutionWindow(idInputField.getText()));
    }

    private void openOrderExecutionWindow(String id) {
        Parent executionView = fxWeaver.loadView(DetailsSendController.class);
        Stage stage = (Stage) executeOrderButton.getScene().getWindow();
        stage.setTitle("Detail orders execution");

        DetailsSendController.supplierId = Integer.parseInt(id);

        stage.setScene(new Scene(executionView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

    private void openOrdersWindow(String id) {
        Parent ordersView = fxWeaver.loadView(DetailsListController.class);
        Stage stage = (Stage) viewOrdersButton.getScene().getWindow();
        stage.setTitle("Detail orders");

        DetailsListController.supplierId = id;

        stage.setScene(new Scene(ordersView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }


}
