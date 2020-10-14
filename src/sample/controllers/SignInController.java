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
import sample.animations.Shake;
import sample.auditlog.Audit;
import sample.database.DatabaseHandler;

public class SignInController {

    private int count = 0;
    private String tmpusername;

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
            Shake userLoginAnim = new Shake(loginField);
            Shake userPassAnim = new Shake(passField);


            if (loginText.equals("")  && loginPassword.equals("")){
                System.out.println("Поля логина и пароля пустые");
                userLoginAnim.playAnim();
                userPassAnim.playAnim();
            }
            else if (loginText.equals("")){
                System.out.println("Поле логина пустое");
                userLoginAnim.playAnim();
            }
            else if (loginPassword.equals("")){
                System.out.println("Поле пароля пустое");
                userPassAnim.playAnim();
            }
            else loginUser(loginText, loginPassword);
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
            if (dbHandler.checkBan(user)) {
                count = 0;
                System.out.println("Пользователь " + user.getUsername() + " вошел в систему");
                Audit.writeFile(DateTime.currentDateToStr() + " Пользователь " + user.getUsername() + " вошел в систему");
                if (dbHandler.getUserRole(user).equals("admin")){
                    user.setRole("admin");
                    openNewScene("/sample/views/AdminScene.fxml", user);
                } else {
                    user.setRole("user");
                    openNewScene("/sample/views/UserScene.fxml", user);
                }

            } else {
                System.out.println("Пользователь " + user.getUsername() + " заблокирован");
            }

        } else {
            System.out.println("Неправильно введен логин или пароль");
            Shake userLoginAnim = new Shake(loginField);
            Shake userPassAnim = new Shake(passField);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
            if (dbHandler.checkUsers(user)){
                if (count == 0){
                    count++;
                    System.out.println("count for " + user.getUsername() + " " + count);
                    tmpusername = user.getUsername();
                } else {
                    if (tmpusername.equals(user.getUsername())){
                        count++;
                        System.out.println("count for " + user.getUsername() + " " + count);
                        if (count == 3){
                            //System.out.println("blocked " + loginText);
                            dbHandler.banUser(user);
                        }
                    } else {
                        count = 0;
                    }
                }
            } else {
                count = 0;
            }

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
        if (user != null){
            if (user.getRole().equals("admin")){
                AdminSceneController adc = loader.<AdminSceneController>getController();
                adc.setUser(user);
            } else {
                UserSceneController usc = loader.<UserSceneController>getController();
                usc.setUser(user);
            }
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }



}