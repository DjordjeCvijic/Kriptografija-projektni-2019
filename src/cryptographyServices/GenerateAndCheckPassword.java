package cryptographyServices;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class GenerateAndCheckPassword {

    public static void main(String[] args) {
        //kod za generisanje sifre za korisnike
        String src = "user5";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashInBytes = md.digest(src.getBytes(StandardCharsets.UTF_8));

            FileOutputStream fos = new FileOutputStream("src" + File.separator + "resources" + File.separator + "user_accounts" + File.separator + src);
            fos.write(hashInBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean checkPassword(String userName, String enteredPass) {
        byte[] enteredPassInBytes = null;
        byte[] correctPassInBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            enteredPassInBytes = md.digest(enteredPass.getBytes(StandardCharsets.UTF_8));
            FileInputStream fis = new FileInputStream("src" + File.separator + "resources" + File.separator + "user_accounts" + File.separator + userName + File.separator + userName);
            correctPassInBytes = fis.readAllBytes();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (enteredPassInBytes.length == correctPassInBytes.length) {
            int i = 0;
            for (i = 0; i < enteredPassInBytes.length && enteredPassInBytes[i] == correctPassInBytes[i]; i++) ;
            if (i == enteredPassInBytes.length)
                return true;
        }
        return false;


    }


}
