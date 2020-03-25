package controller;

import certificateService.CertificateUtil;
import certificateService.RSA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import controller.LoginController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.Buffer;
import java.security.PublicKey;
import java.util.ResourceBundle;

public class RequestSendingController implements Initializable {


    @FXML
    private Label qLabel;
    @FXML
    private Button noBtn;

    private File selectedFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        qLabel.setText("Do you want to start a conversation with " + HomeScreenController.requestToConnection.getName());
    }

    public void onClickImageBtn(ActionEvent actionEvent) {


        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        try {
            System.out.println(selectedFile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickNoBtn(ActionEvent actionEvent) {

        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();
    }

    public void onClickYesBtn(ActionEvent actionEvent) {
        User user = HomeScreenController.requestToConnection;
        //u selectedFile treba ubaciti link sa slikom u kojoj je ubacea envelopa
        String tmpString = "Ja sam djordje";

        PublicKey publicKey = CertificateUtil.getPublicKey("src" + File.separator + "resources" + File.separator + "user_accounts" +
                File.separator + user.getName() + File.separator + user.getName() + "-store.jks", user.getName() + "store", user.getName());
        try {
            String digitalEnvelop = RSA.encrypt(tmpString, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(user.getInboxDirectory() + File.separator + HomeScreenController.user.getName() + ".txt"));
            out.write(HomeScreenController.user.getName() + ":request");
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();


    }
}
