package Controller;

import Model.Jadwal;
import Utility.Koneksi; // Pastikan ini sesuai dengan lokasi file koneksimu
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class JadwalDokterController {

    public Statement stm;
    public ResultSet res;
    public String sql;
    private String idDokter; // Menyimpan ID Dokter yang sedang login

    // model tabel / tabel virtual
    DefaultTableModel dtm = new DefaultTableModel();

    // ==================== KONSTRUKTOR ====================
    public JadwalDokterController(String idDokter) {
        this.idDokter = idDokter; // Simpan ID saat login
        
        // objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }
    
    // Getter untuk idDokter (jika dibutuhkan view)
    public String getIdDokter() {
        return this.idDokter;
    }

    // ==================== Method 1: Buat model/desain tabel Jadwal ====================
    public DefaultTableModel createTable() {
        // Reset kolom agar tidak dobel saat refresh
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID Jadwal");
        this.dtm.addColumn("ID Dokter");
        this.dtm.addColumn("Tanggal");
        this.dtm.addColumn("Jam Mulai");
        this.dtm.addColumn("Jam Selesai");
        this.dtm.addColumn("Kuota");
        return this.dtm;
    }

    // ==================== Method 2: SELECT jadwal (Spesifik Dokter Login) ====================
    public void tampilkanJadwalDokter() {
        try {
            // bersihkan isi tabel virtual
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            // ambil data jadwal HANYA milik dokter yang login
            this.sql = "SELECT * FROM tb_jadwal WHERE id_dokter = '" + this.idDokter + "'";
            this.res = this.stm.executeQuery(sql);

            // masukkan hasil query ke tabel virtual
            while (res.next()) {
                Object[] obj = new Object[6];
                obj[0] = res.getInt("id_jadwal");
                obj[1] = res.getString("id_dokter");
                obj[2] = res.getDate("tanggal");
                obj[3] = res.getTime("jam_mulai");
                obj[4] = res.getTime("jam_selesai");
                obj[5] = res.getInt("kuota");
                this.dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Query tampil jadwal gagal : " + e.getMessage());
        }
    }

    // ==================== Method 3: INSERT jadwal ====================
    public boolean tambahJadwalDokter(int k, Time jm, Time js, Date t) {
        // Hubungkan dengan model Jadwal
        Jadwal ja = new Jadwal();

        // Set nilai ke model
        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);

        try {
            // Kita harus memasukkan id_dokter juga agar tersambung ke akun dokter ini
            this.sql = "INSERT INTO tb_jadwal (id_dokter, kuota, jam_mulai, jam_selesai, tanggal) VALUES ('"
                    + this.idDokter + "', " // Masukkan ID Dokter
                    + ja.getKuota() + ", '"
                    + ja.getJam_mulai() + "', '"
                    + ja.getJam_selesai() + "', '"
                    + ja.getTanggal() + "')";
            
            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Query tambah jadwal gagal : " + e.getMessage());
            return false;
        }
    }

    // ==================== Method 4: UPDATE jadwal ====================
    public boolean ubahJadwalDokter(int k, Time jm, Time js, Date t, int ij) {
        Jadwal ja = new Jadwal();

        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);
        ja.setId_jadwal(ij);

        try {
            this.sql = "UPDATE tb_jadwal SET kuota = '" + ja.getKuota()
                    + "', jam_mulai = '" + ja.getJam_mulai()
                    + "', jam_selesai = '" + ja.getJam_selesai()
                    + "', tanggal = '" + ja.getTanggal()
                    + "' WHERE id_jadwal = " + ja.getId_jadwal(); // Pastikan nama kolom WHERE sesuai DB (biasanya id_jadwal)
            
            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Query ubah jadwal gagal : " + e.getMessage());
        }
        return false;
    }

    // ==================== Method 5: DELETE jadwal ====================
    public boolean hapusJadwalDokter(int ij) {
        try {
            this.sql = "DELETE FROM tb_jadwal WHERE id_jadwal = " + ij;
            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Query hapus jadwal gagal : " + e.getMessage());
        }
        return false;
    }

    // =========================================================================
    //              BAGIAN TAB VERIFIKASI (PENTING AGAR TIDAK ERROR)
    // =========================================================================

    // Method 6: Ambil Daftar Pasien untuk tabel Verifikasi
    public DefaultTableModel getDaftarPasien() {
    DefaultTableModel dtm = new DefaultTableModel();

    dtm.addColumn("ID Daftar");
    dtm.addColumn("Nama Pasien");
    dtm.addColumn("Tanggal");
    dtm.addColumn("Keluhan");
    dtm.addColumn("Status");

    try {
        String sql =
            "SELECT jt.id_janji_temu, p.nama_pasien, j.tanggal, jt.keluhan_pasien, jt.status " +
            "FROM tb_janji_temu jt " +
            "JOIN tb_pasien p ON jt.id_pasien = p.id_pasien " +
            "JOIN tb_jadwal j ON jt.id_jadwal = j.id_jadwal " +
            "WHERE j.id_dokter = '" + this.idDokter + "'";

        res = stm.executeQuery(sql);

        while (res.next()) {
            dtm.addRow(new Object[]{
                res.getString("id_janji_temu"),
                res.getString("nama_pasien"),
                res.getDate("tanggal"),
                res.getString("keluhan_pasien"),
                res.getString("status")
            });
        }

    } catch (Exception e) {
        System.out.println("Error getDaftarPasien : " + e.getMessage());
    }

    return dtm;
}
    
    public boolean updateStatusPasien(String idDaftar, String status){

    try {
        String sqlUpdate = 
            "UPDATE tb_janji_temu SET status = '" + status + "' WHERE id_janji_temu = '" + idDaftar + "'";
        stm.executeUpdate(sqlUpdate);
        return true;
    } catch (Exception e) {
        System.out.println("Gagal update status pasien: " + e.getMessage());
        return false;
    }
}
    



}