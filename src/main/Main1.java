package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main1 extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../view/chatScreen.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(".."+ File.separator+"resources"+File.separator+"loginIcon.jpg")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();



        /*try {
            Stage stage = new Stage();
            root = FXMLLoader.load(getClass().getResource("../view/chatScreen.fxml"));
            //stage.setTitle(user.getName() + " received a chat request");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }


    public static void main(String[] args) {
        launch(args);
    }
}
