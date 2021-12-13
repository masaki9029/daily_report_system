package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class EncryptUtil {
    public static String getPasswordEncrypt(String plainPass, String pepper) {
        String ret = "";

        if (plainPass != null && !plainPass.equals("")) {
            byte[] bytes;
            String password = plainPass + pepper;
            try {
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
                ret = DatatypeConverter.printHexBinary(bytes);
            } catch (NoSuchAlgorithmException ex) {
            }
        }

        return ret;
    }

}
