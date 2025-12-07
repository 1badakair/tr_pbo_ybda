package Controller;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
    // Verifikasi password plain terhadap hash BCrypt
    public static boolean verify(String plainPassword, String hashed) {
        if (plainPassword == null || hashed == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashed);
        } catch (Exception e) {
            return false;
        }
    }

    // Hash password (untuk registrasi / migrasi)
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
}
