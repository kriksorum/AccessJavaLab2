package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DateTime;
import sample.User;
import sample.animations.Shake;
import sample.auditlog.Audit;
import sample.database.DatabaseHandler;

public class UserSceneController {

    User user = new User();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logOutButton;

    @FXML
    private Button changePasswordButton;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Label label1;

    @FXML
    private PasswordField rePasswordField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button applyButton;

    @FXML
    private Button delUserButton;

    @FXML
    private PasswordField passwordField;


    @FXML
    void initialize() {

        logOutButton.setOnAction(event -> {
            System.out.println("Пользователь " + user.getUsername() + " вышел из системы");
            Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " вышел из системы");
            openNewScene("/sample/views/SingIn.fxml");
        });


        progressbar.setProgress(0);
        new Thread(() -> {
            while (progressbar.getProgress() <= 1.0){
                try {
                    Thread.sleep(2000);
                    if (progressbar.getProgress() >= 0.99){
                        System.out.println("Пользователь " + user.getUsername() + " вышел из системы");
                        Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " вышел из системы из за бездействий");
                        Platform.runLater(() -> openNewScene("/sample/views/SingIn.fxml"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressbar.setProgress(progressbar.getProgress() + 0.1);
            }

        }).start();


        changePasswordButton.setOnAction(event -> {
            passwordField.setVisible(true);
            rePasswordField.setVisible(true);
            applyButton.setVisible(true);
            cancelButton.setVisible(true);
            progressbar.setProgress(0);
        });

        cancelButton.setOnAction(event -> {
            passwordField.setVisible(false);
            rePasswordField.setVisible(false);
            applyButton.setVisible(false);
            cancelButton.setVisible(false);
            progressbar.setProgress(0);
        });

        applyButton.setOnAction(event -> {
            progressbar.setProgress(0);
            DatabaseHandler dbHandler = new DatabaseHandler();

            String password = passwordField.getText().trim();
            String repassword = rePasswordField.getText().trim();

            if (password.equals(repassword)){
                user.setPassword(password);
                dbHandler.changePass(user);
            }
            else {
                System.out.println("password do not match");
                Shake userPassAnim = new Shake(passwordField);
                Shake userRePassAnim = new Shake(rePasswordField);
                userPassAnim.playAnim();
                userRePassAnim.playAnim();
            }
        });

        delUserButton.setOnAction(event -> {
            progressbar.setProgress(0);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Подтверждение удаления!");
            alert.setHeaderText("Удаление аккаунта");
            alert.setContentText("Для подтверждения нажмите 'ОК'");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.deleteUser(user.getUsername());
                openNewScene("/sample/views/SingIn.fxml");
                System.out.println("Удаление");
            } else {
                // ... user chose CANCEL or closed the dialog
                System.out.println("Не удаление");
            }
        });


    }

    public void openNewScene(String window){
        logOutButton.getScene().getWindow().hide();

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
        label1.setText(user.getUsername());
    }


}
