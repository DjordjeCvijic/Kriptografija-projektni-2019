package controller;

import certificateServices.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import cryptographyServices.SymmetricAlgorithms;
import steganography.Steganography;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ChatScreenController extends Thread implements Initializable {

    public User user;
    public static User usersInConnection;

    @FXML
    private TextField textField = new TextField();
    @FXML
    private TextArea textArea = new TextArea();
    @FXML
    private Button sendBtn = new Button();

    private static String message = "";
    private static Object lock1 = new Object();
    private static boolean flagForThread = true;
    private static SecretKey symmetricKey;
    private SymmetricAlgorithms symmetricAlgorithms = new SymmetricAlgorithms();
    private File file = new File("pomoc.txt");
    private static boolean flag = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        flag = false;
        flagForThread = true;
        textArea.setEditable(false);

        user = HomeScreenController.user;
        usersInConnection = HomeScreenController.usersInConnection;
        symmetricKey = HomeScreenController.symmetricKey;

        symmetricAlgorithms.setSymmetricKey(symmetricKey);

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
            String messageToWrite = makeMessageToWrite(firstMessage);

            //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(usersInConnection.getInboxDirectory() + File.separator + user.getName() + ".txt")));
            //out.println(user.getName() + ":" + messageToWrite);
            //out.close();
            byte[] messageToWriteInB = (user.getName() + ":" + messageToWrite).getBytes(StandardCharsets.UTF_8);
            Files.write(new File(usersInConnection.getInboxDirectory() + File.separator + user.getName() + ".txt").toPath(), messageToWriteInB);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String metoda(byte[] bytes) {
        String tmp = " ";
        for (byte b : bytes)
            tmp += b;
        return tmp;
    }

    private String makeMessageToWrite(String firstMessage) {
        String messageToWrite = "";
        try {

            //String encryptedMessageInBytes =symmetricAlgorithms.symmetricEncrypt(firstMessage);

            //enkripcija poruke
            String encryptedMessage = symmetricAlgorithms.symmetricEncrypt(firstMessage);

            //hesiranje poruke
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] encryptedMessageDigest = md.digest(encryptedMessage.getBytes(StandardCharsets.UTF_8));

            //izvlacenje privatnog kljuca
            CertificateDetails certificateDetails = CertificateUtil.getCertificateDetails(user.getKeyStorePath(), user.getName() + "store");
            PrivateKey privateKey = certificateDetails.getPrivateKey();

            //digitalno potpisivanje
            String digitalSignature = RSA.encrypt(new String(encryptedMessageDigest), privateKey);

            messageToWrite += digitalSignature + "#terminate#" + encryptedMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messageToWrite;
    }


    public static void writeMessage(String tmp) {
        message = tmp;
        synchronized (lock1) {
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
                try {
                    if (flag) {


                        sendBtn.setVisible(false);


                    } else {
                        //razdvajanje poruke
                        int index = message.indexOf("#terminate#");
                        String digitalSignature = message.substring(0, index);
                        String encryptedMessage = message.substring(index + 11, message.length());

                        //izdvajanje javnog kljuca
                        PublicKey publicKey = CertificateUtil.getPublicKey(user.getTrustStorePath(), "truststore", usersInConnection.getName() + "sertifikat");

                        //dekriptovanje digitalnog potpisa
                        String firstDigest = RSA.decrypt(digitalSignature, publicKey);


                        //hesiranje enkriptovane prenesene poruke
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        byte[] secondDigestInBytes = md.digest(encryptedMessage.getBytes(StandardCharsets.UTF_8));
                        String secondDigest = new String(secondDigestInBytes);


                        if (!firstDigest.equals(secondDigest))
                            throw new Exception("Secure communication is compromised");


                        String decriyptedMessage = symmetricAlgorithms.symmetricDecrypt(encryptedMessage);

                        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                        String messageTmp = usersInConnection.getName() + " [" + timeStamp + "] -> " + decriyptedMessage + "\n\r\n\r";
                        String curent = textArea.getText();
                        String finaleMessage = messageTmp + curent;
                        textArea.clear();
                        textArea.setText(finaleMessage);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // System.out.println("tred u cetu zavrsio");
    }

    public void onCloseBtnClick(ActionEvent actionEvent) {
        flagForThread = false;
        synchronized (lock1) {
            lock1.notify();
        }
        if (!flag) {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            String endMessage = "end of communication";
            File img = Steganography.encode(selectedFile, endMessage);
            try {
                byte[] toWrite = (HomeScreenController.user.getName() + ":image#" + img.toString()).getBytes(StandardCharsets.UTF_8);
                Files.write(new File(usersInConnection.getInboxDirectory() + File.separator + user.getName() + ".txt").toPath(), toWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Stage stage = (Stage) textArea.getScene().getWindow();
        stage.close();

    }

    public static void lastMessage() {
        flag = true;
        synchronized (lock1) {
            lock1.notify();
        }
    }
}
