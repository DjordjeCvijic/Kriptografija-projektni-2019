package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.Main;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable{

    @FXML
    private Button logoutBtn;
    @FXML
    private ImageView userImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private ChoiceBox activeUsersBox=new ChoiceBox();
    @FXML
    private Label numberLabel;

    private  ArrayList<String> activeUsers=new ArrayList<>();


    public void initialize(URL location, ResourceBundle resources) {

        try {
            Image image1 = new Image(new FileInputStream("src" + File.separator + "resources" + File.separator + "homeScreenIcon.png"));
            userImage.setImage(image1);
            usernameLabel.setText(LoginController.userName);



            File[] tmp=new File("src" + File.separator + "resources" + File.separator + "inboxes").listFiles();
            //ArrayList<String> activeUsers=new ArrayList<>();
            for(int i=0;i<tmp.length;i++){
                if(!tmp[i].getName().startsWith(usernameLabel.getText())) {
                    int index=tmp[i].getName().indexOf('_');
                    activeUsers.add(tmp[i].getName().substring(0,index));
                }
            }
            for(String s:activeUsers)
                activeUsersBox.getItems().add(s);

            numberLabel.setText(Integer.toString(activeUsers.size()));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void onClickLogoutBtn(ActionEvent actionEvent) {
        //Stage stage = (Stage) closeAppBtn.getScene().getWindow();
        //stage.close();
        try {
            Stage stage = Main.primaryStage1;
            Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            stage.setTitle("Login");
            stage.getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "resources" + File.separator + "loginIcon.jpg")));
            stage.setScene(new Scene(root));

            LoginController.inboxFile.delete();

            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
