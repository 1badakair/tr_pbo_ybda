package Controller;

import Model.User;
import Utility.PasswordHash;
import Utility.Koneksi;
import java.sql.ResultSet;
import java.sql.Statement;

public class AuthController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    // simpan id_user sementara agar bisa diambil setelah login
    private int loggedUserId = 0;

    public AuthController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    public String cekLogin(String username, String plainPassword) {
        User usr = new User();
        usr.setUsername(username);   

        try {
            this.sql = "SELECT * FROM tb_user WHERE username = '" + usr.getUsername() + "'";
            this.res = this.stm.executeQuery(sql);

            if (res.next()) {
                String passwordHash = res.getString("password_hash");
                boolean pw_verify = PasswordHash.verify(plainPassword, passwordHash);

                if (!pw_verify) {
                    System.out.println("Password salah!");
                    return null;
                }

                // SIMPAN id_user agar bisa dipakai getIdDokterByUserId()
                this.loggedUserId = res.getInt("id_user");

                String role = res.getString("role");
                return role != null ? role : null;
            } else {
                System.out.println("Data tidak ditemukan");
            }

        } catch (Exception e) {
            System.out.println("Query gagal: " + e.getMessage());
        }

        return null;
    }

    // Getter untuk mengambil id_user setelah login
    public int getLoggedUserId() {
        return this.loggedUserId;
    }

    // --- BAGIAN DOKTER ---
    
    // cari id_dokter dari id_user
    public String getIdDokterByUserId(int id_user) {
        try {
            this.sql = "SELECT id_dokter FROM tb_dokter WHERE id_user = " + id_user;
            this.res = this.stm.executeQuery(sql);
            if (res.next()) {
                return res.getString("id_dokter");
            }
        } catch (Exception e) {
            System.out.println("Error getIdDokter: " + e.getMessage());
        }
        return null;
    }

    public String getNamaDokterByUserId(int idUser) {
        try {
            this.sql = "SELECT nama_dokter FROM tb_dokter WHERE id_user = " + idUser;
            this.res = this.stm.executeQuery(sql);
            if (res.next()) {
                return res.getString("nama_dokter");
            }
        } catch (Exception e) {
            System.out.println("Error getNamaDokter: " + e.getMessage());
        }
        return null;
    }

    // --- BAGIAN PASIEN (INI YANG BARU DITAMBAHKAN) ---
    
    // 1. Ambil ID Pasien berdasarkan id_user
    public String getIdPasienByUserId(int id_user) {
        try {
            this.sql = "SELECT id_pasien FROM tb_pasien WHERE id_user = " + id_user;
            this.res = this.stm.executeQuery(sql);
            if (res.next()) {
                return res.getString("id_pasien");
            }
        } catch (Exception e) {
            System.out.println("Error getIdPasien: " + e.getMessage());
        }
        return null;
    }

    // 2. Ambil Nama Pasien berdasarkan id_user
    public String getNamaPasienByUserId(int id_user) {
        try {
            // Pastikan kolom di database namanya 'nama_pasien' atau hanya 'nama'
            this.sql = "SELECT nama_pasien FROM tb_pasien WHERE id_user = " + id_user;
            this.res = this.stm.executeQuery(sql);
            if (res.next()) {
                return res.getString("nama_pasien");
            }
        } catch (Exception e) {
            System.out.println("Error getNamaPasien: " + e.getMessage());
        }
        return null;
    }
    
    // --- TAMBAHAN UNTUK AMBIL UMUR ---
    public String getUmurPasienByUserId(int id_user) {
        try {
            // Pastikan nama kolom di database kamu adalah 'umur'
            this.sql = "SELECT umur FROM tb_pasien WHERE id_user = " + id_user;
            this.res = this.stm.executeQuery(sql);
            if (res.next()) {
                return res.getString("umur");
            }
        } catch (Exception e) {
            System.out.println("Error getUmur: " + e.getMessage());
        }
        return "-"; // Default jika tidak ditemukan
    }
}