package Utility;

public class UserSession {

    private static Integer idUser;   // Integer -> bisa NULL
    private static String idPasien;
    private static String nama;
    private static String role;

    // Setter: Dipanggil saat Login berhasil
    public static void setUser(Integer idUser, String idPasien, String nama, String role) {
        UserSession.idUser = idUser;
        UserSession.idPasien = idPasien;
        UserSession.nama = nama;
        UserSession.role = role;
    }

    // Getter
    public static Integer getIdUser() { return idUser; }
    public static String getIdPasien() { return idPasien; }
    public static String getNama() { return nama; }
    public static String getRole() { return role; }

    // CLEAR SESSION (dipanggil saat logout)
    public static void clear() {
        idUser = null;
        idPasien = null;
        nama = null;
        role = null;
    }
}
