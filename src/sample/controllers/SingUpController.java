package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.User;
import sample.animations.Shake;
import sample.database.DatabaseHandler;

public class SingUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField loginField;

    @FXML
    private Button regSingUpButton;

    @FXML
    private PasswordField rePassField;

    @FXML
    private Button loginButton;

    @FXML
    void initialize() {


        regSingUpButton.setOnAction(event -> {
            signUpNewUser();
        });

        loginButton.setOnAction(event -> {
            openNewScene("/sample/views/SingIn.fxml");
        });

    }

    private void signUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String username = loginField.getText().trim();
        String password = passField.getText().trim();

        User user = new User(username, password);
        Shake userLoginAnim = new Shake(loginField);
        Shake userPassAnim = new Shake(passField);
        Shake userRePassAnim = new Shake(rePassField);

        if (passField.getText().trim().equals(rePassField.getText().trim())){
            if (!dbHandler.checkUsers(user)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                if(dbHandler.signUpUser(user)){
                    alert.setContentText("Ваш аккаунт " + user.getUsername() + " успешно зарегистрирован!");
                } else {
                    alert.setContentText("C регистрицией что то пошло не так!");
                }
                alert.showAndWait();
                loginField.setText("");
                passField.setText("");
                rePassField.setText("");
            } else {
                System.out.println("такой логин уже есть");
                userLoginAnim.playAnim();
            }

        }
        else {
            System.out.println("Not equals");
            userPassAnim.playAnim();
            userRePassAnim.playAnim();

        }
    }

    public void openNewScene(String window){
        loginButton.getScene().getWindow().hide();

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

    }


}