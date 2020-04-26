package controller;

import certificateServices.CertificateDetails;
import certificateServices.CertificateUtil;
import certificateServices.RSA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import listeners.JavaInboxesListener;
import listeners.UserInboxListener;
import model.User;
import steganography.Steganography;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

public class HomeScreenController extends Thread implements Initializable {

    @FXML
    private Button logoutBtn;
    @FXML
    private ImageView userImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private ChoiceBox activeUsersBox = new ChoiceBox();
    @FXML
    private Label numberLabel;


    public static ArrayList<String> activeUsers = new ArrayList<>();
    private int numOfActiveUsers = 0;
    public static User user;
    private static Object lock = new Object();
    public static User usersInConnection = null;
    public static User requestToConnection;
    private static JavaInboxesListener listener = null;
    private static UserInboxListener userInboxListener = null;
    public String selectedUser = null;
    public static SecretKey symmetricKey;

    private Boolean flag = true;

    public static void setSymmetricKey(String message) {
        int index = message.indexOf('#');
        String pathToImg=message.substring(index+1,message.length());
        String digitalEnvelop =Steganography.decode(new File(pathToImg));

        CertificateDetails cd= CertificateUtil.getCertificateDetails("src" + File.separator + "resources" + File.separator + "user_accounts" +
                File.separator + user.getName() + File.separator + user.getName() + "-store.jks",user.getName()+"store");
        PrivateKey privateKey=cd.getPrivateKey();
        String keyInString="";
        try{
            keyInString=RSA.decrypt(digitalEnvelop,privateKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        byte[] decodedKey = Base64.getDecoder().decode(keyInString);
        symmetricKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
        //System.out.println(" dobijen Kljuc: " + new String(symmetricKey.getEncoded()));


    }

    public void initialize(URL location, ResourceBundle resources) {

        try {


            user = new User(LoginController.userName, new File("src" + File.separator + "resources" + File.separator + "inboxes" + File.separator + LoginController.userName + "_inbox"));
            user.setKeyStorePath("src"+File.separator+"resources"+File.separator+"user_accounts"+File.separator+user.getName()+File.separator+user.getName()+"-store.jks");
            user.setTrustStorePath("src"+File.separator+"resources"+File.separator+"user_accounts"+File.separator+user.getName()+File.separator+"trustStore.jks");
            Image image1 = new Image(new FileInputStream("src" + File.separator + "resources" + File.separator + "homeScreenIcon.png"));
            userImage.setImage(image1);
            usernameLabel.setText(user.getName());


            activeUsers.clear();


            File[] tmp = new File("src" + File.separator + "resources" + File.separator + "inboxes").listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (!tmp[i].getName().startsWith(user.getName())) {
                    int index = tmp[i].getName().indexOf('_');
                    activeUsers.add(tmp[i].getName().substring(0, index));

                }
            }


            activeUsersBox.getItems().clear();


            for (String s : activeUsers)
                activeUsersBox.getItems().add(s);
            numOfActiveUsers = activeUsers.size();
            numberLabel.setText(Integer.toString(numOfActiveUsers));


            listener = new JavaInboxesListener(user.getName());
            listener.start();
            userInboxListener = new UserInboxListener(user.getInboxDirectory());
            userInboxListener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        start();


    }

    public void onClickLogoutBtn(ActionEvent actionEvent) {
        listener.setRunning(false);
        userInboxListener.setRunning(false);

        try {
            Stage stage1 = (Stage) logoutBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            stage1.setTitle("Login");
            stage1.getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "resources" + File.separator + "loginIcon.jpg")));
            stage1.setScene(new Scene(root));

            user.deleteUserData();
            //usersInConnection.clear();
            flag = false;
            synchronized (lock) {
                lock.notify();
            }

            stage1.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void addActiveUser(String name) {
        synchronized (lock) {
            activeUsers.add(name);
            lock.notify();
        }


    }

    public static void removeActiveUser(String name) {
        synchronized (lock) {
            activeUsers.remove(name);
            lock.notify();
        }
    }

    public void run() {

        while (flag) {
            synchronized (lock) {
                try {

                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        activeUsersBox.getItems().clear();
                        for (String s : activeUsers) {
                            activeUsersBox.getItems().add(s);
                        }

                        numOfActiveUsers = activeUsers.size();
                        numberLabel.setText(Integer.toString(numOfActiveUsers));


                    }
                });


            }
        }


    }

    public void onClickSendAMessageBtn(ActionEvent actionEvent) {
        try {

            String chosenName = activeUsersBox.getSelectionModel().getSelectedItem().toString();
            requestToConnection = new User(chosenName, new File("src" + File.separator + "resources" + File.separator + "inboxes" + File.separator + chosenName + "_inbox"));
            requestToConnection.setTrustStorePath("src"+File.separator+"resources"+File.separator+"user_accounts"+File.separator+chosenName+File.separator+"trustStore.jks");
            requestToConnection.setKeyStorePath("src"+File.separator+"resources"+File.separator+"user_accounts"+File.separator+chosenName+File.separator+chosenName+"-store.jks");
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/requestSending.fxml"));
            stage.setTitle(user.getName() + " sending a chat request");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void request(String message){
        int index = message.indexOf(':');
        String name = message.substring(0, index);
        requestToConnection = new User(name, new File("src" + File.separator + "resources" + File.separator + "inboxes" + File.separator + name + "_inbox"));

        setSymmetricKey(message);




        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        Stage stage = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("../view/requestReceivedScreen.fxml"));
                        stage.setTitle(user.getName() + " received a chat request");
                        stage.setScene(new Scene(root));
                        stage.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void replyIsYes(){
        usersInConnection=requestToConnection;


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../view/chatScreen.fxml"));
                    stage.setTitle(user.getName() + " in connection with "+usersInConnection.getName());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static void messageHasBeenRead(String message) {

            int index = message.indexOf(':');
            String tmp = message.substring(index+1,message.length());
            ChatScreenController.writeMessage(tmp);




    }


    public static void userSelectYes() {
        usersInConnection=requestToConnection;


        try{
            //BufferedWriter out=new BufferedWriter(new FileWriter(requestToConnection.getInboxDirectory()+File.separator+user.getName()+".txt"));
           // out.write(user.getName()+":reply=yes");
            //out.close();

            String tmp=user.getName()+":reply=yes";
            byte[] tmpInB=tmp.getBytes(StandardCharsets.UTF_8);
            Files.write(new File(requestToConnection.getInboxDirectory()+File.separator+user.getName()+".txt").toPath(),tmpInB);




        }catch (Exception e){
            e.printStackTrace();
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../view/chatScreen.fxml"));
                    stage.setTitle(user.getName() + " in connection with "+usersInConnection.getName());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


    public static void userSelectNo() {

        try{
            BufferedWriter out=new BufferedWriter(new FileWriter(requestToConnection.getInboxDirectory()+File.separator+user.getName()+".txt"));
            out.write(user.getName()+":reply=no");
            out.close();


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
