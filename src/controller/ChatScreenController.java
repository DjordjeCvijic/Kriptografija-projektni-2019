package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ChatScreenController implements Initializable{


    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setEditable(false);

    }

    public void onClickSendBtn(ActionEvent actionEvent) {
        String firstMessage=textField.getText();
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        String message="user["+timeStamp+"] -> "+firstMessage+"\n\r\n\r";
        String curent=textArea.getText();
        String finaleMessage=message+curent;
        textArea.clear();
        textArea.setText(finaleMessage);
        textField.clear();






    }
}
