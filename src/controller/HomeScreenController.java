package controller;

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
import main.*;
import model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public static LinkedList<User> usersInConnection = new LinkedList<>();
    public static User requestToConnection;
    private JavaInboxesListener listener = null;
    private UserInboxListener userInboxListener = null;
    public String selectedUser = null;

    private Boolean flag = true;

    public void initialize(URL location, ResourceBundle resources) {

        try {


            user = new User(LoginController.userName, new File("src" + File.separator + "resources" + File.separator + "inboxes" + File.separator + LoginController.userName + "_inbox"));


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

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/requestSending.fxml"));
            stage.setTitle(user.getName() + " sending a chat request");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void action(String message) {

        if (message.contains("request")) {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(new URL("src/view/requestReceivedScreen.fxml"));
                stage.setTitle(user.getName() + " received a chat request");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }




        }else{
            System.out.println(message);
        }



    }


    public static void userSelectYes() {
        usersInConnection.addFirst(requestToConnection);

        try{
            BufferedWriter out=new BufferedWriter(new FileWriter(requestToConnection.getInboxDirectory()+File.separator+user.getName()+".txt"));
            out.write(user.getName()+":reply=yes");
            out.close();


        }catch (Exception e){
            e.printStackTrace();
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../view/chatScreen.fxml"));
                    stage.setTitle(user.getName() + " in connection with "+usersInConnection.getFirst().getName());
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
