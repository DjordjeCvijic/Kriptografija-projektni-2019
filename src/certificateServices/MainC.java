package certificateServices;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class MainC {
    public static void main(String[] args) {
        /*CertificateDetails certDetails = CertificateUtil.getCertificateDetails("src" + File.separator + "resources" + File.separator + "user_accounts"+
                File.separator+"user5"+File.separator+"user5-store.jks", "user5store");
        PrivateKey privateKey = certDetails.getPrivateKey();
        PublicKey publicKey = certDetails.getPublicKey();
        X509Certificate x509Certificate = certDetails.getX509Certificate();
        Principal certIssuer =  x509Certificate.getIssuerDN();
        String certIssuerName = certIssuer.getName();
        Principal on= x509Certificate.getSubjectDN();
        try {
            x509Certificate.verify(certDetails.getRootPublicKey());

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(on.getName());

        PublicKey pk=CertificateUtil.getPublicKey(s"src" + File.separator + "resources" + File.separator + "user_accounts"+
                File.separator+"user1"+File.separator+"trustStore.jks","truststore","user2sertifikat");
        System.out.println(pk.toString());*/

        String s="djorjdje";
        byte[]sByte1=s.getBytes();
        String tmp=" ";
        for(byte b:sByte1)
            tmp+=b;
        System.out.println(tmp);
        for(byte b:sByte1)
            System.out.println(b);

    }
}