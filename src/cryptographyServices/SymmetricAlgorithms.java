package cryptographyServices;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.*;

public class SymmetricAlgorithms {

    private String symmetricAlgorithm = "DES";
    private SecretKey symmetricKey;

    private static ArrayList<String> algs = new ArrayList<String>();

    static {
        algs.add("AES");
        algs.add("DES");
        algs.add("RC4");
        algs.add("RC2");
        algs.add("Blowfish");
    }

    public SymmetricAlgorithms() {
        super();
    }

    public SymmetricAlgorithms(String symmetricAlgorithm) throws NoSuchAlgorithmException {
        if (!SymmetricAlgorithms.algs.contains(symmetricAlgorithm))
            throw new NoSuchAlgorithmException(
                    "Specified symmetric algorithm " + symmetricAlgorithm + " not supported!!!");
        this.symmetricAlgorithm = symmetricAlgorithm;
    }

    public void generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(symmetricAlgorithm);
        symmetricKey = keygen.generateKey();
    }

    public byte[] symmetricEncrypt(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte output[] = null;
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        output = cipher.doFinal(input);
        return output;
    }

    public byte[] symmetricDecrypt(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte output[] = null;
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        output = cipher.doFinal(input);
        return output;
    }

    public SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(SecretKey sk){
        symmetricKey=sk;
    }
    /*public static void main(String[] args) throws Exception {
        // int index = (int) Math.round(Math.random() * (algs.size()-1));
        for (int index = 0; index < algs.size(); index++) {
            Crypto cu = new Crypto(Crypto.algs.get(index));
            cu.generateSymmetricKey();
            String originalString = "Kriptografija i racunarska zastita";
            byte encrypted[] = cu.symmetricEncrypt(originalString.getBytes());
            String encryptedString = new String(encrypted);
            byte decrypted[] = cu.symmetricDecrypt(encrypted);
            String decryptedString = new String(decrypted);
            System.out.println("Originalni string: " + originalString);
            System.out.println("Algoritam: " + cu.symmetricAlgorithm);
            System.out.println("Kljuc: " + new String(cu.symmetricKey.getEncoded()));
            System.out.println("Duzina kljuca: " + cu.symmetricKey.getEncoded().length * 8);
            System.out.println("Kriptovani string: " + encryptedString);
            System.out.println("Dekriptovani string: " + decryptedString);
            System.out.println("====================");
            System.out.println("====================");
        }
    }*/
}
