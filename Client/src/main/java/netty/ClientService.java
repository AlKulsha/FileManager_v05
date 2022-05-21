package netty;

import javafx.fxml.Initializable;
import org.example.App;
import org.example.ControllerRegistry;
import org.example.PrimaryController;
import org.example.ServerPanelController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClientService {

    public void loginSuccessful(){
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putServerFileList(List<File> serverItemsList) {
        ServerPanelController controllerObject =
                (ServerPanelController) ControllerRegistry.getControllerObject
                        ((Class<? extends Initializable>) ServerPanelController.class);
        controllerObject.renderServerFileList(serverItemsList);

    }
}
