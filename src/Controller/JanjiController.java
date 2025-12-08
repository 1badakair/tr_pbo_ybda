package Controller;

import Utility.Koneksi;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class JanjiController {
    
    public Statement stm;
    public ResultSet res;
    public String sql;
    Koneksi db;
    
    // Model tabel virtual
    DefaultTableModel dtm = new DefaultTableModel();

    // Konstruktor
    public JanjiController() {
        db = new Koneksi();
        db.config(); // Buka koneksi
        
        // --- PERBAIKAN PENTING (Agar tidak NullPointer) ---
        try {
            // Jika di class Koneksi.java statement belum dibuat, kita buat paksa disini
            if (db.stm == null) {
                this.stm = db.con.createStatement();
            } else {
                this.stm = db.stm;
            }
        } catch (Exception e) {
            System.out.println("Error membuat koneksi di Controller: " + e.getMessage());
        }
    }

    // 1. Setup Header Tabel
    public DefaultTableModel createTable() {
        // Kita reset kolom dulu biar bersih
        dtm = new DefaultTableModel();
        
        dtm.addColumn("No");
        dtm.addColumn("Nama Pasien"); // Ambil dari tb_pasien
        dtm.addColumn("Nama Dokter"); // Ambil dari tb_dokter
        dtm.addColumn("Tanggal");     // Ambil dari tb_jadwal
        dtm.addColumn("Jam");         // Ambil dari tb_jadwal
        dtm.addColumn("Keluhan");     // Ambil dari tb_janji_temu
        dtm.addColumn("Status");      // Ambil dari tb_janji_temu
        
        return dtm;
    }

    // 2. Tampilkan Data (READ dengan JOIN 4 Tabel)
    public void tampilkanRiwayat() {
        try {
            // Bersihkan baris lama
            dtm.setRowCount(0);

            // --- QUERY SQL BARU (Sesuai db_techospital) ---
            // Kita hubungkan: 
            // tb_janji_temu -> tb_pasien (buat ambil nama pasien)
            // tb_janji_temu -> tb_jadwal (buat ambil tanggal)
            // tb_jadwal -> tb_dokter (buat ambil nama dokter)
            
            this.sql = "SELECT " +
                       "  jt.id_janji_temu, " +
                       "  p.nama_pasien, " +
                       "  d.nama_dokter, " +
                       "  j.tanggal, " +
                       "  j.jam_mulai, " +
                       "  jt.keluhan_pasien, " +
                       "  jt.status " +
                       "FROM tb_janji_temu jt " +
                       "JOIN tb_pasien p ON jt.id_pasien = p.id_pasien " +
                       "JOIN tb_jadwal j ON jt.id_jadwal = j.id_jadwal " +
                       "JOIN tb_dokter d ON j.id_dokter = d.id_dokter " +
                       "ORDER BY jt.waktu_buat_janji DESC"; 

            // Eksekusi query
            this.res = this.stm.executeQuery(sql);

            // Masukkan ke tabel
            int no = 1;
            while (res.next()) {
                Object[] row = new Object[7];
                row[0] = no++;
                row[1] = res.getString("nama_pasien");
                row[2] = res.getString("nama_dokter");
                row[3] = res.getString("tanggal");
                row[4] = res.getString("jam_mulai");
                row[5] = res.getString("keluhan_pasien");
                row[6] = res.getString("status"); 

                dtm.addRow(row);
            }
        } catch (Exception e) {
            System.out.println("Gagal load riwayat: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean tambahJanjiTemu(int idJadwal, String idPasien, String keluhan) {
    try {
        String idJanji = generateIdJanji();

        String sql = "INSERT INTO tb_janji_temu(id_janji_temu, id_jadwal, id_pasien, status, keluhan_pasien) "
                   + "VALUES ('" + idJanji + "', " + idJadwal + ", '" + idPasien + "', "
                   + "'menunggu', '" + keluhan + "')";

        stm.executeUpdate(sql);
        return true;

    } catch (Exception e) {
        System.out.println("Error tambahJanjiTemu: " + e.getMessage());
        return false;
    }
}

    
    private String generateIdJanji() {
    String newId = "JT001";
    try {
        String sql = "SELECT id_janji_temu FROM tb_janji_temu ORDER BY id_janji_temu DESC LIMIT 1";
        ResultSet rs = stm.executeQuery(sql);

        if (rs.next()) {
            String lastId = rs.getString("id_janji_temu").substring(2); // ambil angka
            int number = Integer.parseInt(lastId) + 1;
            newId = String.format("JT%03d", number);
        }
        rs.close();
    } catch (Exception e) {
        System.out.println("Error generate ID: " + e.getMessage());
    }
    return newId;
}


}