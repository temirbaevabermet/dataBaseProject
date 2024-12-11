package com.example.phones_repair.viewControllers.master;

import com.example.phones_repair.viewControllers.RoleSelectionController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/view/MasterWindow.fxml")
public class MasterViewController {

    private final FxWeaver fxWeaver;
    
    @FXML
    private Button viewOrdersButton;
    
    @FXML
    private Button executeOrderButton;

    @FXML
    private Button orderDetailButton;
    
    public MasterViewController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize() {
        viewOrdersButton.setOnAction(e -> openOrdersWindow());
        executeOrderButton.setOnAction(e -> openOrderExecutionWindow());
        orderDetailButton.setOnAction(e -> openDetailOrderView());
    }

    private void openDetailOrderView() {
        Parent orderView = fxWeaver.loadView(DetailOrderController.class);
        Stage stage = (Stage) executeOrderButton.getScene().getWindow();
        stage.setTitle("Detail Order");
        stage.setScene(new Scene(orderView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

    private void openOrderExecutionWindow() {
        Parent executionView = fxWeaver.loadView(OrderExecutionController.class);
        Stage stage = (Stage) executeOrderButton.getScene().getWindow();
        stage.setTitle("Orders execution");
        stage.setScene(new Scene(executionView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }

    private void openOrdersWindow() {
        Parent ordersView = fxWeaver.loadView(OrderListController.class);
        Stage stage = (Stage) viewOrdersButton.getScene().getWindow();
        stage.setTitle("Orders");
        stage.setScene(new Scene(ordersView));
        stage.show();
        RoleSelectionController.sceneHistory.add(stage.getScene());
    }


}
