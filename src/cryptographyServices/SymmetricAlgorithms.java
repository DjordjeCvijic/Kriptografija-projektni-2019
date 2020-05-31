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

    public String symmetricEncrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte output[];
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        output = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(output);
    }

    public String symmetricDecrypt(String cipherText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        return new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
    }

    public SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(SecretKey sk) {
        symmetricKey = sk;
    }

}
