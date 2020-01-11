package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;

import javafx.stage.Stage;
import main.Main;


public class LoginController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginBtn;

    public static String userName;

    public void initialize(URL location, ResourceBundle resources) {


    }

        public void onClick(ActionEvent actionEvent) {


         userName=nameTextField.getText();
        String password=passwordTextField.getText();
        if(userName.equals("")) loginError();
            else {
            File userAccountsFile = new File("src" + File.separator + "resources" + File.separator + "user_accounts");

            File account = new File(userAccountsFile.getPath() + File.separator + userName);
            if (account.exists()) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(account));
                    String pass = in.readLine();
                    if (pass.equals(password)) {
                        showHomeScreen();
                    } else
                        loginError();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else loginError();
        }
    }

    private void showHomeScreen() {
        try {

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/homeScreen.fxml"));
            stage.setTitle("Home Screen for "+userName);
            stage.getIcons().clear();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(".."+ File.separator+"resources"+File.separator+"homeScreenIcon.png")));
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void loginError(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.show();
            }
        });


    }

}
