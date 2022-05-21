package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import netty.Network;
import netty.dto.AuthRequest;
import netty.dto.BasicRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController {
    @FXML
    public TextField login;

    @FXML
    public TextField password;
    private final Network network = Network.getInstance();

    @FXML
    public void btnExitAction(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }
    @FXML
    public void btnAuth() throws InterruptedException {
        String log = login.getText();
        String pass = password.getText();
        if(log == null || log.isEmpty() || log.isBlank()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Type your login", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        BasicRequest request = new AuthRequest(log, pass);
        network.sendRequest(request);
    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        ControllerRegistry.register((Initializable) this);
    }

}

