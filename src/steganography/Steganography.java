package steganography;


import java.io.File;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class Steganography {


    public Steganography() {
    }

    public static File encode(File file, String message) {
        BufferedImage image_orig = getImage(file);
        BufferedImage image = user_space(image_orig);
        image = add_text(image, message);
        String tmp = file.toString();

        int index = tmp.indexOf(".");
        String newImgPath = tmp.substring(0, index) + "-copy.png";
        File newFile = new File(newImgPath);
        setImage(image, newFile, "png");
        return newFile;
    }

    public static String decode(File f) {
        byte[] decode;
        try {
            BufferedImage image = user_space(getImage(f));
            decode = decode_text(get_byte_data(image));
            return (new String(decode));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There is no hidden message in this image!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private static BufferedImage getImage(File f) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Image could not be read!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }

    private static boolean setImage(BufferedImage image, File file, String ext) {
        try {
            file.delete();
            ImageIO.write(image, ext, file);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "File could not be saved!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static BufferedImage add_text(BufferedImage image, String text) {
        byte img[] = get_byte_data(image);
        byte msg[] = text.getBytes();
        byte len[] = bit_conversion(msg.length);
        try {
            encode_text(img, len, 0); //0 first positiong
            encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Target File cannot hold message!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }

    private static BufferedImage user_space(BufferedImage image) {
        BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return new_img;
    }

    private static byte[] get_byte_data(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    private static byte[] bit_conversion(int i) {

        byte byte3 = (byte) ((i & 0xFF000000) >>> 24); //0
        byte byte2 = (byte) ((i & 0x00FF0000) >>> 16); //0
        byte byte1 = (byte) ((i & 0x0000FF00) >>> 8); //0
        byte byte0 = (byte) ((i & 0x000000FF));
        return (new byte[]{byte3, byte2, byte1, byte0});
    }

    private static byte[] encode_text(byte[] image, byte[] addition, int offset) {
        if (addition.length + offset > image.length) {
            throw new IllegalArgumentException("File not long enough!");
        }
        for (int i = 0; i < addition.length; ++i) {
            int add = addition[i];
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
        return image;
    }

    private static byte[] decode_text(byte[] image) {
        int length = 0;
        int offset = 32;

        for (int i = 0; i < 32; ++i) {
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];

        for (int b = 0; b < result.length; ++b) {

            for (int i = 0; i < 8; ++i, ++offset) {
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }
}
