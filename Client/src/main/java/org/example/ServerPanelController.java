package org.example;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import netty.dto.GetFileListResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerPanelController implements Initializable {
//    private Network network;
    @FXML

    TableView<FileInfo> filesTable;

    @FXML
    ComboBox<String> disksBox;
    @FXML
    TextField pathField;

    private Path upperCatalogName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>(); //создаем столбец, в котором хранитсяfileInfo, преобразованный в String
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty
                (param.getValue().getType().getName()));     //запись в таблице param узнаем ее имя F или D
        fileTypeColumn.setPrefWidth(24);  //настраиваем ширину столбца

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty
                (param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>
                (param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>(){
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item == null || empty){
                        setText(null);
                        setStyle("");
                    } else{
                        String text = String.format("%,d bytes", item);
                        if(item == -1){
                            text = "[DIR]";
                        }

                        setText(text);
                    }
                }
            };

        });
        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Date");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()
                .getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

//      в список столбцов таблицы добавили новый столбец
        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        filesTable.getSortOrder().add(fileTypeColumn); //сортировка по первому столбцу

        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()){
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    Path path = Paths.get(pathField.getText())
                            .resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
                    if(Files.isDirectory(path)){
                        updateList(path);
                    }
                }
            }
        });
        upperCatalogName = Paths.get(pathField.getText());
    }

    private void updateList(Path path) {
        try{
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        }catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Impossible update files list", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //    Метод для получения списка файлов из каталога
    public void updateServerList(Path path, List<File> serverItemsList){
        pathField.setText(path.normalize().toAbsolutePath().toString());   //прописываем абсолютный путь в тестовом поле
        filesTable.getItems().clear();
        List<FileInfo> serverFileList = serverItemsList.stream()
                .map(File::toPath)
                .map(FileInfo::new)
                .collect(Collectors.toList());
        filesTable.getItems().addAll(serverFileList);
        filesTable.sort();

    }
// метод для смены директории кнопкой вверх
    public void btnPathUpAction(ActionEvent actionEvent) {
        Path currentPath = Paths.get(pathField.getText());
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if(currentPath.equals(upperCatalogName)){
            return;
        }
        if(upperPath == null){
            updateList(upperPath);
        }
    }
// метод для выбора диска из выпадающего меню
    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
        //от источника действия получаем выбранный пункт и обновляем по нему лист
    }

    public String getSelectedFileName(){
        if(!filesTable.isFocused()){
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurrentPath(){
        return pathField.getText();
    }

    public void renderServerFileList(List<File> serverItemsList) {
        updateServerList(Paths.get(".", "server-dir"), serverItemsList);
    }
}
