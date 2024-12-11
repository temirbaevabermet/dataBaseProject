package com.example.phones_repair.viewControllers.client;

import com.example.phones_repair.viewControllers.RoleSelectionController;
import com.example.phones_repair.viewControllers.master.OrderExecutionController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/ChooseOptionView.fxml")
public class ChooseOptionController {
    private final FxWeaver fxWeaver;

    @FXML
    private Button makeOrderButton;

    @FXML
    private Button viewOrdersButton;

    public ChooseOptionController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public void initialize() {
        makeOrderButton.setOnAction(event -> openMakeOrderView());
        viewOrdersButton.setOnAction(event -> openOrdersView());
    }

    private void openOrdersView() {
        Parent ordersView = fxWeaver.loadView(ClientsOrdersController.class);
        Stage stage = (Stage) viewOrdersButton.getScene().getWindow();
        stage.setTitle("My orders");
        stage.setScene(new Scene(ordersView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

    private void openMakeOrderView() {
        Parent makeOrderView = fxWeaver.loadView(MakeOrderController.class);
        Stage stage = (Stage) makeOrderButton.getScene().getWindow();
        stage.setTitle("Make order");
        stage.setScene(new Scene(makeOrderView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

}
