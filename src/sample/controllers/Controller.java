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
import sample.User;
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
                System.out.println("Error");
        });


        regSingInButton.setOnAction(event -> {
            openNewScene("/sample/views/SingUp.fxml");
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
            openNewScene("/sample/views/UserScene.fxml");
            System.out.println("Success");
        } else {
            System.out.println("Error");
        }
    }

    public void openNewScene(String window){
        regSingInButton.getScene().getWindow().hide();

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
        stage.showAndWait();
    }



}