package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DateTime;
import sample.User;
import sample.auditlog.Audit;
import sample.database.DatabaseHandler;

public class AdminSceneController {

    User user = new User();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<?> listView;

    @FXML
    private Button exitButton;

    @FXML
    private Button openAuditButton;

    @FXML
    private Button listUserButton;

    @FXML
    private Button zipAuditButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button unZipButton;

    @FXML
    void initialize() {
        String value;
        exitButton.setOnAction(event -> {
            System.out.println("Пользователь " + user.getUsername() + " вышел из системы");
            Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " вышел из системы");
            openNewScene("/sample/views/SingIn.fxml");
        });

        openAuditButton.setOnAction(event -> {
            listView.getItems().clear();
            listView.getItems().addAll(Audit.readFile("auditlog.txt"));
            deleteUserButton.setVisible(false);
        });

        listUserButton.setOnAction(event -> {
            DatabaseHandler dbHandler = new DatabaseHandler();
            listView.getItems().clear();
            listView.getItems().addAll(dbHandler.getAllUsers());
            listView.getItems().remove(0);
            deleteUserButton.setVisible(true);

        });





        deleteUserButton.setOnAction(event -> {

            String selectedItems = listView.getSelectionModel().getSelectedItems().toString();
            if (!selectedItems.equals("[]")) {
                selectedItems = selectedItems.substring(1, selectedItems.length() - 1);
                if (selectedItems.contains(" ")) {
                    selectedItems = selectedItems.substring(0, selectedItems.indexOf(" "));
                }
                System.out.println(selectedItems);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтверждение удаления!");
                alert.setHeaderText("Удаление аккаунта");
                alert.setContentText("Для подтверждения нажмите 'ОК'");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    DatabaseHandler dbHandler = new DatabaseHandler();
                    dbHandler.deleteUser(selectedItems);
                    System.out.println("Удаление");
                } else {
                    // ... user chose CANCEL or closed the dialog
                    System.out.println("Не удаление");
                }
            } else {
                System.out.println("Выберите пользователя");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Не выбран пользователь для удаления!");

                alert.showAndWait();
            }
        });

        zipAuditButton.setOnAction(event -> {
            deleteUserButton.setVisible(false);
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Архивация");
            dialog.setHeaderText("Внимание! При ахривации текущий журнал будет очищен!");
            dialog.setContentText("Введите имя для архива:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                Audit.zipAudit(result.get());
                Audit.cleanAudit();
                Audit.writeFile(DateTime.currentDateToStr() + " Журнал очищен");
            }
        });

        unZipButton.setOnAction(event -> {
            deleteUserButton.setVisible(false);
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Распаковка");
            dialog.setContentText("Введите имя архива:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                if (Audit.unZip(result.get())){
                    listView.getItems().clear();
                    listView.getItems().addAll(Audit.readFile("unzip/auditlog.txt"));
                    Audit.deleteFile();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Файл не найден!");
                    alert.setContentText(null);

                    alert.showAndWait();
                }

            }
        });

    }

    public void openNewScene(String window){
        exitButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }



    public void setUser(User user){
        this.user = user;
    }


}

