package controller;

import certificateServices.CertificateUtil;
import certificateServices.RSA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PublicKey;
import java.util.Base64;
import java.util.ResourceBundle;

import cryptographyServices.SymmetricAlgorithms;
import steganography.Steganography;

public class RequestSendingController implements Initializable {


    @FXML
    private Label qLabel;
    @FXML
    private Button noBtn;

    private File selectedFile = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        qLabel.setText("Do you want to start a conversation with " + HomeScreenController.requestToConnection.getName());
    }

    public void onClickImageBtn(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);

    }

    public void onClickNoBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();
    }

    public void onClickYesBtn(ActionEvent actionEvent) {

        if (selectedFile != null) {
            User user = HomeScreenController.requestToConnection;
            String digitalEnvelop = "";

            SymmetricAlgorithms sa = new SymmetricAlgorithms();
            try {
                sa.generateSymmetricKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                HomeScreenController.symmetricKey = sa.getSymmetricKey();
                String key = Base64.getEncoder().encodeToString(sa.getSymmetricKey().getEncoded());
                PublicKey publicKey = CertificateUtil.getPublicKey(HomeScreenController.user.getTrustStorePath(), "truststore", user.getName() + "sertifikat");

                digitalEnvelop = RSA.encrypt(key, publicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File img = Steganography.encode(selectedFile, digitalEnvelop);

            try {
                byte[] toWrite = (HomeScreenController.user.getName() + ":image#" + img.toString()).getBytes(StandardCharsets.UTF_8);
                Files.write(new File(user.getInboxDirectory() + File.separator + HomeScreenController.user.getName() + ".txt").toPath(), toWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) noBtn.getScene().getWindow();
            stage.close();
            selectedFile = null;
        } else {
            fileNotSelected("Image not selected");

        }
    }

    private void fileNotSelected(String message) {
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
}
