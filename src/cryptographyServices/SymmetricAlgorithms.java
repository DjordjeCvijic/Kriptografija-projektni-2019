package cryptographyServices;

import get_properties.GetConfigPropertyValues;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;

public class SymmetricAlgorithms {

    private String symmetricAlgorithm = "";
    private SecretKey symmetricKey;


    public SymmetricAlgorithms() {
        symmetricAlgorithm = GetConfigPropertyValues.getPropValue("symmetric_algorithm");
    }


    public void generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(symmetricAlgorithm);
        symmetricKey = keygen.generateKey();
    }

    //primao je niz bajtova i vrcao niz bajtova
    public String symmetricEncrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        System.out.println("alg je:" + symmetricAlgorithm);
        byte output[] = null;
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        System.out.println("enkripicja kljuc je:" + symmetricKey + " " + symmetricKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        //output = cipher.doFinal(input);
        output = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        //return output;
        return Base64.getEncoder().encodeToString(output);
    }

    //primao i vracao niz bajtova
    public String symmetricDecrypt(String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] bytes = Base64.getDecoder().decode(cipherText);
        //byte output[] = null;
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        System.out.println("alg je:" + symmetricAlgorithm);
        System.out.println("dekripcija kljuc je:" + symmetricKey + " " + symmetricKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        //output = cipher.doFinal(input);
        //return output;
        return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
    }

    public SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(SecretKey sk) {
        symmetricKey = sk;
    }

}
