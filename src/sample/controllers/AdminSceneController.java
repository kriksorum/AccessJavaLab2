package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
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
    private Button setAdminButton;

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
            listView.getItems().addAll(Audit.readFile());
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
            selectedItems = selectedItems.substring(1, selectedItems.length() - 1);
            if (selectedItems.contains(" ")){
                selectedItems = selectedItems.substring(0, selectedItems.indexOf(" "));
            }
            System.out.println(selectedItems);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления!");
            alert.setHeaderText("Удаление аккаунта");
            alert.setContentText("Для подтверждения нажмите 'ОК'");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.deleteUser(selectedItems);
                System.out.println("Удаление");
            } else {
                // ... user chose CANCEL or closed the dialog
                System.out.println("Не удаление");
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

