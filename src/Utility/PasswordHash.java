package Utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    // Verifikasi password plain terhadap hash BCrypt
    public static boolean verify(String plainPassword, String passwordHashed) {
        if (plainPassword == null || passwordHashed == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, passwordHashed);
        } catch (Exception e) {
            return false;
        }
    }

//    // Hash password (untuk registrasi / migrasi)
//    public static String hash(String plainPassword) {
//        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
//    }
}