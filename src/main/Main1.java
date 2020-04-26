package main;

import certificateServices.*;
import cryptographyServices.*;


import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;

public class Main1 {
    public static void main(String[] args) {
        final File file=new File("tmp.txt");


        String message="radiiiiiii";
        try {
            SymmetricAlgorithms sa=new SymmetricAlgorithms();
            sa.generateSymmetricKey();

            String encryptedMessage=sa.symmetricEncrypt(message);//enkriprovana kao string
            //System.out.println("enkriptovana poruka prije upisa:"+encryptedMessage);

            //Hesiramo enkriptovanu poruku

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashInB = md.digest(encryptedMessage.getBytes(StandardCharsets.UTF_8));//hesirana enkriptovana poruka u bajtovima
            String hashLikeString=new String(hashInB);//hesirana enkriptovana kao string

            //treba dobaviti privatni kljuv
            String keyStorePath="src"+File.separator+"resources"+File.separator+"user_accounts"+File.separator+"user1"+File.separator+"user1-store.jks";
            CertificateDetails cd=CertificateUtil.getCertificateDetails(keyStorePath,"user1store");
            PrivateKey privateKey=cd.getPrivateKey();

            //potpisujemo privatnim kljuce
            String digitalSignature=RSA.encrypt(hashLikeString,privateKey);

            //spajanje d.potpisa i enkriptovane poruke
            String finaleMessage=digitalSignature+"#terminate#"+encryptedMessage;
            byte[] finaleMessageInB=finaleMessage.getBytes(StandardCharsets.UTF_8);

            //upisano u file
            Files.write(new File("tmp.txt").toPath(),finaleMessageInB);


            //citanje iz fajla

            byte[]newMessageInB=Files.readAllBytes(new File("tmp.txt").toPath());
            //System.out.println(Arrays.equals(newMessageInB,finaleMessageInB));
            String newMessageLikeString=new String(newMessageInB);
           // System.out.println(newMessageLikeString);
            file.delete();


            //razdvajanje
            int index = newMessageLikeString.indexOf("#terminate#");
            String digitalSignature1 = newMessageLikeString.substring(0, index);
            String encryptedMessageLikeString = newMessageLikeString.substring(index + 11, newMessageLikeString.length());
            //System.out.println("poredjenje stringova:"+encryptedMessageLikeString.equals(encryptedMessage));

            //pribavljanje javnog kljuca
            PublicKey publicKey=cd.getPublicKey();

            //dekripcija digitalnog potpisa
            String hashLikeString1=RSA.decrypt(digitalSignature1,publicKey);//prvi hes za porediti


            //hesiranje prenijete enkriptovane poruke
            byte[] encryptedMessageInB=encryptedMessageLikeString.getBytes(StandardCharsets.UTF_8);

//            System.out.println(encryptedMessageLikeString.equals(encryptedMessage));
//            System.out.println(encryptedMessageLikeString);
//            System.out.println(encryptedMessage);
//            System.out.println();

//            System.out.println(encryptedMessageLikeString.length()+" "+encryptedMessage.length());
//
//            System.out.println(Arrays.equals(encryptedMessageInB,encryptedInB));
//            System.out.println(encryptedMessageInB.length+" "+encryptedInB.length);
//            String a1="";
//            for (byte b:encryptedMessageInB)
//                a1+=b+" ";
//            System.out.println(a1);
//
//            String a2="";
//            for (byte b:encryptedInB)
//                a2+=b+" ";
//            System.out.println(a2);



            byte[] hashInB2 = md.digest(encryptedMessageInB);//drugi hes za porediti
            String hashLikeString2=new String(hashInB2);

            //poredjenje
            if(!hashLikeString1.equals(hashLikeString2))
                throw new Exception("hesevi nisu jednaki");



            //dekripcija poslane poruke
            String decryptedMessage=sa.symmetricDecrypt(encryptedMessageLikeString);


            System.out.println("primljena poruka:"+new String(decryptedMessage));































//           // PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter(file)));
//            //out.write(encryptedMessage);
//            //out.close();
//            String en=sa.symmetricEncrypt(message);
//            Files.write(file.toPath(),en.getBytes(StandardCharsets.UTF_8));
//
//
//            byte[]a=Files.readAllBytes(file.toPath());
//            String b=new String (a);
//
//            String d=sa.symmetricDecrypt(b);
//            System.out.println(d);




        }catch (Exception e){
            e.printStackTrace();
        }


    }
}