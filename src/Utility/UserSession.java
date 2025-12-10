package Utility;

public class UserSession {
    private static int idUser;
    private static String idPasien; 
    private static String nama;
    private static String role;
    // Variabel umur DIHAPUS

    // Balikkan ke 4 parameter saja (HAPUS parameter String umur)
    public static void setUser(int idUser, String idRef, String nama, String role) {
        UserSession.idUser = idUser;
        UserSession.idPasien = idRef;
        UserSession.nama = nama;
        UserSession.role = role;
        // Baris this.umur = umur DIHAPUS
    }

    public static int getIdUser() { return idUser; }
    public static String getIdPasien() { return idPasien; }
    public static String getNama() { return nama; }
    public static String getRole() { return role; }
    
    // Getter getUmur() DIHAPUS

    public static void clear() {
        idUser = 0;
        idPasien = null;
        nama = null;
        role = null;
        // Pembersihan umur DIHAPUS
    }
}