package main;

import steganography.Steganography;


import java.io.File;

public class Main1 {
    public static void main(String[] args) {

        System.out.println("start");
        // File file=ImageWorker.putData(new File("src"+ File.separator+"resources"+File.separator+"li.jpg"),"poruka");
        //System.out.println("nova slika: "+file.toString());

        //String poruka=ImageWorker.getData(file);
        Steganography s=new Steganography();
        //s.encode("src" + File.separator + "resources", "li", "jpg", "copy", "ovo je prouka");
       // System.out.println(s.decode("src" + File.separator + "resources","copy"));
        File file=new File("src"+ File.separator+"resources"+File.separator+"li.jpg");
        File f=Steganography.encode(file,"poruadsasdasdazsdka");
        System.out.println(Steganography.decode(f));
        System.out.println("end");

    }
}