package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class ImageWorker {
    private String path = "";
    //private BufferedImage imageCopy;
    private int p = 0;

    public ImageWorker(String p) {
        path = p;
        try {
            //imageCopy = ImageIO.read(new File("src" + File.separator + "resources" + File.separator + "loginIcon1.jpg"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
        int a = alpha;
        int r = red;
        int g = green;
        int b = 0;
        p = (a << 24) | (r << 16) | (g << 8) | b;

    }

    private void marchThrougahImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                printPixelARGB(pixel);
                image.setRGB(j, i, p);
                printPixelARGB(p);
                System.out.println("");


            }
        }

        try {
            File f = new File("src" + File.separator + "resources" + File.separator + "new.jpg");
            ImageIO.write(image, "jpg", f);

        } catch (Exception e) {
            System.out.println(e);


        }
    }

    public void worker() {
        try {

            BufferedImage image =
                    ImageIO.read(new File(path));


            // marchThroughImage(image);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static File putData(File path, String data) {


        int sizeOfData = data.length();

        byte[] sizeOfDataInBytes = new byte[4];
        sizeOfDataInBytes[0] = (byte) ((sizeOfData & 0xFF000000) >>> 24);
        sizeOfDataInBytes[1] = (byte) ((sizeOfData & 0x00FF0000) >>> 16);
        sizeOfDataInBytes[2] = (byte) ((sizeOfData & 0x0000FF00) >>> 8);
        sizeOfDataInBytes[3] = (byte) ((sizeOfData & 0x000000FF));

        byte[] tmp = data.getBytes(StandardCharsets.UTF_8);
        byte[] dataInByte = new byte[sizeOfDataInBytes.length + tmp.length];
        dataInByte[0] = sizeOfDataInBytes[0];
        dataInByte[1] = sizeOfDataInBytes[1];
        dataInByte[2] = sizeOfDataInBytes[2];
        dataInByte[3] = sizeOfDataInBytes[3];
        for (int i = 4; i < dataInByte.length; i++) {
            dataInByte[i] = tmp[i - 4];
        }
        System.out.println("velicina u bajtovima "+dataInByte.length);
        return marchThroughImage(path, dataInByte);


    }

    private static File marchThroughImage(File path, byte[] dataInByte) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = img.getWidth();
        int h = img.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        int k = 0;

        for (int i = 0; i < h && k < dataInByte.length*8; i++) {
            for (int j = 0; j < w && k < dataInByte.length*8; j++) {

                System.out.println("x,y: " + j + ", " + i + " , " + k);
                int pixel = img.getRGB(j, i);
                System.out.println("bajt i bit u bajtu  "+k / 8+" "+ k%8);
                int newPixel = makeNewPixel(pixel, dataInByte[k / 8], k % 8);
                k++;
                img.setRGB(j, i, newPixel);
            }
        }

        try {
            String tmp=path.toString();

            int index=tmp.indexOf(".");
            String newImgPath=tmp.substring(0,index)+"1.jpg";

            File f = new File(newImgPath);
            ImageIO.write(img, "jpg", f);
            return f;

        } catch (Exception e) {
            System.out.println(e);


        }
        return null;


    }

    private static int makeNewPixel(int pixel, byte b, int n) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);

        int bitInByte = (b >> 7 - n) & 0x00000001;
        int lastInBlue = blue & 0x00000001;
        int newBlue = 0;
        if (lastInBlue == bitInByte) {
            newBlue = blue;
        } else {
            if(bitInByte==0){
                newBlue=blue & 0xfffffffe;
            }
            else{
                newBlue=blue |bitInByte;
            }

        }

        int rez = (alpha << 24) | (red << 16) | (green << 8) | newBlue;
        System.out.println("bit in byte "+bitInByte);
        System.out.println("new blue "+newBlue);
        System.out.println();
        return rez;

    }

}



