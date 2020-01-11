package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RequestSendingController implements Initializable {


    @FXML
    private Label qLabel;
    @FXML
    private Button noBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        qLabel.setText("Do you want to start a conversation with "+HomeScreenController.usersInConnection.getFirst().getName());
    }

    public void onClickImageBtn(ActionEvent actionEvent) {


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        System.out.println(selectedFile.toString());
    }

    public void onClickNoBtn(ActionEvent actionEvent) {

        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();
    }

    public void onClickYesBtn(ActionEvent actionEvent) {
    }
}
