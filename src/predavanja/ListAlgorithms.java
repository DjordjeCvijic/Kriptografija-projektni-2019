package predavanja;

import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class ListAlgorithms {
    public static void printSet(String setName, Set<String> algorithms) {
        System.out.println(setName + ":");
        if (algorithms.isEmpty()) {
            System.out.println("            None available.");
        } else {
            Iterator<String> it = algorithms.iterator();
            while (it.hasNext()) {
                String name = (String) it.next();

                System.out.println("            " + name);
            }
        }
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        Set<String> ciphers = new HashSet<String>();
        Set<String> keyAgreements = new HashSet<String>();
        Set<String> macs = new HashSet<String>();
        Set<String> messageDigests = new HashSet<String>();
        Set<String> signatures = new HashSet<String>();

        for (int i = 0; i != providers.length; i++) {
            Iterator<Object> it = providers[i].keySet().iterator();

            while (it.hasNext()) {
                String entry = (String) it.next();

                if (entry.startsWith("Alg.Alias.")) {
                    entry = entry.substring("Alg.Alias.".length());
                }

                if (entry.startsWith("Cipher.")) {
                    ciphers.add(entry.substring("Cipher.".length()));
                } else if (entry.startsWith("KeyAgreement.")) {
                    keyAgreements
                            .add(entry.substring("KeyAgreement.".length()));
                } else if (entry.startsWith("Mac.")) {
                    macs.add(entry.substring("Mac.".length()));
                } else if (entry.startsWith("MessageDigest.")) {
                    messageDigests.add(entry.substring("MessageDigest."
                            .length()));
                } else if (entry.startsWith("Signature.")) {
                    signatures.add(entry.substring("Signature.".length()));
                }
            }
        }

        printSet("Ciphers", ciphers);
        printSet("KeyAgreeents", keyAgreements);
        printSet("Macs", macs);
        printSet("MessageDigests", messageDigests);
        printSet("Signatures", signatures);
    }
}
