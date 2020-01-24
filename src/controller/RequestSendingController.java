package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;

public class RequestSendingController implements Initializable {


    @FXML
    private Label qLabel;
    @FXML
    private Button noBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        qLabel.setText("Do you want to start a conversation with "+HomeScreenController.requestToConnection.getName());
    }

    public void onClickImageBtn(ActionEvent actionEvent) {


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        try {
            System.out.println(selectedFile.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClickNoBtn(ActionEvent actionEvent) {

        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();
    }

    public void onClickYesBtn(ActionEvent actionEvent) {


        User user=HomeScreenController.requestToConnection;
        try{
            BufferedWriter out=new BufferedWriter(new FileWriter(user.getInboxDirectory()+File.separator+LoginController.userName+".txt"));
            out.write("request");
            out.close();


        }catch (Exception e){
            e.printStackTrace();
        }

        Stage stage = (Stage) noBtn.getScene().getWindow();
        stage.close();

    }
}
