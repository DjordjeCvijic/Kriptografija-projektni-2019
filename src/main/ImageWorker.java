package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageWorker {
    private String path = "";
    private BufferedImage imageCopy;
    private int p = 0;

    public ImageWorker(String p) {
        path = p;
        try {
            imageCopy =
                    ImageIO.read(new File("src" + File.separator + "resources" + File.separator + "loginIcon1.jpg"));

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
        int a=alpha;
        int r=red;
        int g=green;
        int b=0;
        p=(a<<24) | (r<<16) | (g<<8) | b;

    }

    private void marchThroughImage(BufferedImage image) {
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
            File f = new File("src" + File.separator + "resources" + File.separator + "loginIcon1.jpg");
            ImageIO.write(image, "jpg", f);
        } catch (Exception e) {
            System.out.println(e);


        }
    }

        public void worker () {
            try {

                BufferedImage image =
                        ImageIO.read(new File(path));


                marchThroughImage(image);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

