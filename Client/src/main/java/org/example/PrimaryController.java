package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import netty.dto.AuthRequest;
import netty.dto.BasicRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrimaryController{
    @FXML
    VBox leftPanel, rightPanel;
    @FXML
    LocalPanelController leftPC;
    @FXML
    ServerPanelController rightPC;
    @FXML
    public void initialize(){
        leftPC = (LocalPanelController) leftPanel.getUserData();
        rightPC = (ServerPanelController) rightPanel.getUserData();
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void btnCopyAction(ActionEvent actionEvent) {
        LocalPanelController leftPC = (LocalPanelController) leftPanel.getProperties().get("ctrl");
        LocalPanelController rightPC = (LocalPanelController) rightPanel.getUserData();
//        LocalPanelController rightPC = (ServerPanelController) rightPanel.getUserData();

        if(leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a File", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        //определяем откуда куда копировать
        LocalPanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFileName() != null){
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFileName() != null){
            srcPC = rightPC;
            dstPC = leftPC;
        }

        //Определяем что копировать
        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "File is already exists", ButtonType.OK);
            alert.showAndWait();;
        }
    }


    public void btnMoveAction(ActionEvent actionEvent) {
        LocalPanelController leftPC = (LocalPanelController) leftPanel.getProperties().get("ctrl");
        LocalPanelController rightPC = (LocalPanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a File", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        LocalPanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFileName() != null){
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if(rightPC.getSelectedFileName() != null){
            srcPC = rightPC;
            dstPC = leftPC;
        }

        //Определяем что копировать
        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.move(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
            srcPC.updateList(Paths.get(srcPC.getCurrentPath()));

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Operation is not possible", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
    LocalPanelController leftPC = (LocalPanelController) leftPanel.getProperties().get("ctrl");
    LocalPanelController rightPC = (LocalPanelController) rightPanel.getProperties().get("ctrl");

        if(leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null){
             Alert alert = new Alert(Alert.AlertType.WARNING, "Select a File", ButtonType.OK);
             alert.showAndWait();
             return;
        }

        LocalPanelController srcPC = null;
        if(leftPC.getSelectedFileName() != null){
            srcPC = leftPC;
        }
        if(rightPC.getSelectedFileName() != null){
            srcPC = rightPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());

        try {
            Files.delete(srcPath);
            srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Operation is not possible", ButtonType.OK);
            alert.showAndWait();
        }
    }



//
//    public void btnSendToServerAction(ActionEvent actionEvent) {
//        network.sendData(msgField.getText());
//        msgField.clear;
//        msgField.requestFocus;
//    }
}

