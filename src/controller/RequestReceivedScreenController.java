package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class RequestReceivedScreenController implements Initializable {

    @FXML
    private Label messageLabel;
    @FXML
    private Button yesBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageLabel.setText("Do you want to start a conversation with "+ HomeScreenController.requestToConnection.getName());

    }


    public void onClickYesBtn(ActionEvent actionEvent) {

        HomeScreenController.userSelectYes();


    }

    public void onClickNoBtn(ActionEvent actionEvent) {
        HomeScreenController.userSelectNo();
    }




}
