package controller;

import certificateServices.CertificateUtil;
import certificateServices.RSA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.security.PublicKey;
import java.util.Base64;
import java.util.ResourceBundle;

import cryptographyServises.SymmetricAlgorithms;
import steganography.Steganography;

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
            System.out.println("fajl za ubaciti poruku " + selectedFile.toString());
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
        String digitalEnvelop="";

        SymmetricAlgorithms sa = new SymmetricAlgorithms();
        try {
            sa.generateSymmetricKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("poslan Kljuc: " + new String(sa.getSymmetricKey().getEncoded()));
        try {
        HomeScreenController.symmetricKey=sa.getSymmetricKey();
        String key = Base64.getEncoder().encodeToString(sa.getSymmetricKey().getEncoded());
        PublicKey publicKey = CertificateUtil.getPublicKey(HomeScreenController.user.getTrustStorePath(),"truststore",user.getName()+"sertifikat");

            digitalEnvelop = RSA.encrypt(key, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File img=Steganography.encode(selectedFile,digitalEnvelop);


        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(user.getInboxDirectory() + File.separator + HomeScreenController.user.getName() + ".txt"));
            out.write(HomeScreenController.user.getName() + ":request#"+img.toString());
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();


    }
}
