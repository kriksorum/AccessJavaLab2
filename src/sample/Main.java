package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.auditlog.Audit;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/SingIn.fxml"));
        primaryStage.setTitle("manzarovApp");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();

        System.out.println("Application run");
        Audit.writeFile( DateTime.currentDateToStr() + " Application run");
    }


    public static void main(String[] args) {
        launch(args);
    }
}


