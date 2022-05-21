package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class App extends Application {
    private static Scene scene;
//    private static final FXMLLoader SERVER_PANEL_LOADER;
//    private static final Parent SERVER_PANEL_LOADER_PARENT;
//    static {
//        try {
//            SERVER_PANEL_LOADER = new FXMLLoader(App.class.getResource("primary.fxml"));
//            SERVER_PANEL_LOADER_PARENT = SERVER_PANEL_LOADER.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("auth"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
//
//    public static void setServerFileList(List<File> serverFileList){
//        ServerPanelController controller = SERVER_PANEL_LOADER.getController();
//        controller.renderServerFileList(serverFileList);
//    }


    public static void main(String[] args) {
        launch();
    }

}