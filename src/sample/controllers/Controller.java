package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DateTime;
import sample.User;
import sample.auditlog.Audit;
import sample.database.DatabaseHandler;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginSingInButton;

    @FXML
    private Button regSingInButton;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField loginField;

    @FXML
    void initialize() {

        loginSingInButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String loginPassword = passField.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals(""))
                loginUser(loginText, loginPassword);
            else
                System.out.println("Поля логина и пароля пустые");
        });


        regSingInButton.setOnAction(event -> {
            openNewScene("/sample/views/SingUp.fxml", null);
        });

    }

    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();

        User user = new User();
        user.setUsername(loginText);
        user.setPassword(loginPassword);

        ResultSet result = dbHandler.getUser(user);

        int counter = 0;
        try{
            while(result.next()){
                counter++;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        if (counter >= 1){
            System.out.println("Пользователь " + user.getUsername() + " вошел в систему");

            Audit.writeFile(DateTime.currentDate() + " Пользователь " + user.getUsername() + " вошел в систему");
            openNewScene("/sample/views/UserScene.fxml", user);
        } else {
            System.out.println("Неправильно введен логин или пароль");
        }
    }

    public void openNewScene(String window, User user){
        regSingInButton.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        UserSceneController usc = loader.<UserSceneController>getController();
        usc.setUser(user);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }



}