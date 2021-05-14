package ua.app.classroom.util;

import org.apache.log4j.Logger;
import ua.app.classroom.db.UserDB;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoding {

    private static final Logger LOG = Logger.getLogger(UserDB.class);

    public static String encodingPassword(String password) {
        if (password == null) {
            return null;
        }
        String encryptedPassword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            encryptedPassword = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("There was an error encrypting the password: ", e);
        }
        return encryptedPassword;
    }
}
