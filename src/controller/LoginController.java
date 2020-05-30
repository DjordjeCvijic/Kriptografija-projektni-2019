package controller;

import certificateServices.CertificateDetails;
import certificateServices.CertificateUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;

import javafx.stage.Stage;
import cryptographyServices.GenerateAndCheckPassword;


public class LoginController {

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginBtn;

    public static String userName;

    public void initialize(URL location, ResourceBundle resources) {


    }

    public void onClick(ActionEvent actionEvent) {


        userName = nameTextField.getText();
        String password = passwordTextField.getText();
        if (userName.equals(""))

            loginError("Invalid username or password");

        else {
            File userAccountsFile = new File("src" + File.separator + "resources" + File.separator + "user_accounts"+ File.separator +userName);

            File account = new File(userAccountsFile.getPath() +File.separator+userName);
            //System.out.println(account.toString());
            if (account.exists()) {
                try {


                    if (GenerateAndCheckPassword.checkPassword(userName, password)) {//provjera sifre
                        if(checkCertificate(userName))
                        showHomeScreen();
                        else{

                            loginError("Invalid Certificate");
                        }

                    } else
                        loginError("Invalid username or password");



                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else loginError("Invalid username or password");
        }

    }

    private boolean checkCertificate(String userName) {
        CertificateDetails certDetails = CertificateUtil.getCertificateDetails("src" + File.separator + "resources" + File.separator + "user_accounts"+
                File.separator+userName+File.separator+userName+"-store.jks", userName+"store");


        return certDetails.checkCertificate();

    }

    private void showHomeScreen() {
        try {

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/homeScreen.fxml"));
            stage.setTitle("Home Screen for " + userName);
            stage.getIcons().clear();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "resources" + File.separator + "homeScreenIcon.png")));
            stage.setScene(new Scene(root));

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loginError(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.show();
            }
        });


    }

    public void onCloseBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.close();
    }
}
