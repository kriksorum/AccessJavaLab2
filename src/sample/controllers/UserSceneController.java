package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import sample.DateTime;
import sample.User;
import sample.auditlog.Audit;

public class UserSceneController {

    User user = new User();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logOutButton;

    @FXML
    private Button userFormbutton;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Label label1;


    @FXML
    void initialize() {

        logOutButton.setOnAction(event -> {
            System.out.println("Пользователь " + user.getUsername() + " вышел из системы");
            Audit.writeFile(DateTime.currentDate() + " Пользователь " + user.getUsername() + " вышел из системы");
            openNewScene("/sample/views/SingIn.fxml");
        });


        progressbar.setProgress(0);
        new Thread(() -> {
            while (progressbar.getProgress() <= 1.0){
                try {
                    Thread.sleep(1000);
                    if (progressbar.getProgress() >= 0.9){
                        System.out.println("Пользователь " + user.getUsername() + " вышел из системы");
                        Audit.writeFile(DateTime.currentDate() + " Пользователь " + user.getUsername() + " вышел из системы");
                        Platform.runLater(() -> openNewScene("/sample/views/SingIn.fxml"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressbar.setProgress(progressbar.getProgress() + 0.1);
            }

        }).start();


        userFormbutton.setOnAction(event -> {
            progressbar.setProgress(0);
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
