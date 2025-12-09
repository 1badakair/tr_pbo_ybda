package Controller;

import Model.User;
import Utility.PasswordHash;
import Utility.Koneksi;
import java.sql.ResultSet;
import java.sql.Statement;

public class AuthController {

    public Statement stm;     // dipertahankan agar gaya serupa kode lama
    public ResultSet res;
    public String sql;

    // konstruktor
    public AuthController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    public String cekLogin(String username, String plainPassword) {
        // Dipetakan dengan model
        User usr = new User();
        usr.setUsername(username);

        // Query ke database + cek
        try {
            // SQL query
            this.sql = "SELECT * FROM tb_user WHERE username = '" + usr.getUsername() + "'";

            // Menjalankan query
            // Khusus SELECT gunakan 'executeQuery'
            this.res = this.stm.executeQuery(sql);
            
            // Pengecekan
            if (res.next()) {
                String passwordHash = res.getString("password_hash");
                boolean pw_verify = Utility.PasswordHash.verify(plainPassword, passwordHash);
                if (!pw_verify) {
                    System.out.println("Password salah!");
                    return null;
                }
                
                String role = res.getString("role");
                if (role != null) {
                    return role;
                } else {
                    return null;
                }
            } else {
                System.out.println("Data tidak ditemukan");
            }
        } catch (Exception e) {
            System.out.println("Query gagal");
        }
        return null;
    }
}
