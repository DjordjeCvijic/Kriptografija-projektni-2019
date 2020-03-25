package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main1 {
    public static void main(String[] args) {
        // File f=new File("src"+ File.separator+"resources"+File.separator+"li.jpg");
        // ImageWorker iw=new ImageWorker(f.toString());
        File f=ImageWorker.putData(new File("src"+ File.separator+"resources"+File.separator+"li.jpg"),"poruka");
        System.out.println("nova slika: "+f.toString());



        int ret = 0;
        for (int i = 0; i < 4; i++) {
            ret <<= 8;
            // ret |= (int)sizeOfDataInBytes[i] & 0xFF;
        }
        System.out.println(ret);
    }
}