package certificateServices;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class CertificateDetails {
    private PrivateKey privateKey;
    private X509Certificate x509Certificate;
    private PublicKey publicKey;
    private PublicKey rootPublicKey;

    public PublicKey getRootPublicKey() {
        return rootPublicKey;
    }

    public void setRootPublicKey(PublicKey rootPublicKey) {
        this.rootPublicKey = rootPublicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public boolean checkCertificate() {
        try {
            x509Certificate.verify(rootPublicKey);//provjera da je potpisan od strane ca tijela kojem se vjeruje
            x509Certificate.checkValidity();//provjera vremena vazenja


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}