package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public static Stage primaryStage1;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage1=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(".."+ File.separator+"resources"+File.separator+"loginIcon.jpg")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
