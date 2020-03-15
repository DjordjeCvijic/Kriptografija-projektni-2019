package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ChatScreenController extends Thread implements Initializable {

    public User user;
    public static User usersInConnection;

    @FXML
    private TextField textField = new TextField();
    @FXML
    private TextArea textArea = new TextArea();

    private static String message = "";
    private static Object lock1 = new Object();
    private Boolean flagForThread = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textArea.setEditable(false);

        user = HomeScreenController.user;
        usersInConnection = HomeScreenController.usersInConnection;

        start();

    }


    public void onClickSendBtn(ActionEvent actionEvent) {

        String firstMessage = textField.getText();
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        String message = "me[" + timeStamp + "] -> " + firstMessage + "\n\r\n\r";
        String curent = textArea.getText();
        String finaleMessage = message + curent;
        textArea.clear();
        textArea.setText(finaleMessage);
        textField.clear();
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(usersInConnection.getInboxDirectory() + File.separator + user.getName() + ".txt")));
            out.println(user.getName() + ":" + firstMessage);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeMessage(String tmp) {
        message = tmp;
        synchronized (lock1){
            lock1.notify();
        }

    }

    @Override
    public void run() {
        while (flagForThread) {
            try {
                synchronized (lock1) {
                    lock1.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (flagForThread) {
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                String messageTmp = usersInConnection.getName() + "[" + timeStamp + "] -> " + message + "\n\r\n\r";
                String curent = textArea.getText();
                String finaleMessage = messageTmp + curent;
                textArea.clear();
                textArea.setText(finaleMessage);
            }
        }
        System.out.println("tred u cetu zavrsio");
    }

    public void onCloseBtnClick(ActionEvent actionEvent) {
        flagForThread = false;
        synchronized (lock1) {
            lock1.notify();
        }
        Stage stage = (Stage) textArea.getScene().getWindow();
        stage.close();
    }
}
