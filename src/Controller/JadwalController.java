package Controller;

import Model.Jadwal;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;
import javax.swing.table.DefaultTableModel;

public class JadwalController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    // model tabel / tabel virtual
    DefaultTableModel dtm = new DefaultTableModel();

    // konstruktor
    public JadwalController() {
        // objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    // ==================== Method 1: Buat model/desain tabel ====================
    public DefaultTableModel createTable() {
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID Jadwal");
        this.dtm.addColumn("ID Dokter");
        this.dtm.addColumn("Tanggal");
        this.dtm.addColumn("Jam Mulai");
        this.dtm.addColumn("Jam Selesai");
        this.dtm.addColumn("Kuota");
        return this.dtm;
    }

    // ==================== Method 2: SELECT semua jadwal (isi JTable) ====================
    public void tampilkanJadwalDokter() {
        try {
            // bersihkan isi tabel virtual
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            // ambil semua data jadwal
            this.sql = "SELECT * FROM tb_jadwal";
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
    public boolean tambahJadwalDokter(String id, int k, Time jm, Time js, Date t) {
        // Hubungkan dengan model Jadwal
        Jadwal ja = new Jadwal();

        // Set nilai ke model
        ja.setId_dokter(id);
        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);

        try {
            this.sql = "INSERT INTO tb_jadwal (id_dokter, kuota , jam_mulai, jam_selesai, tanggal) VALUES ('"
                    + ja.getId_dokter() + "', "
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
    public boolean ubahJadwalDokter(int k, Time jm, Time js, Date t, int ij,String id) {
        Jadwal ja = new Jadwal();

        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);
        ja.setId_jadwal(ij);
        ja.setId_dokter(id);

        try {
            this.sql = "UPDATE tb_jadwal SET kuota = '" + ja.getKuota()
                    + "', jam_mulai = '" + ja.getJam_mulai()
                    + "', jam_selesai = '" + ja.getJam_selesai()
                    + "', tanggal = '" + ja.getTanggal()
                    + "', id_dokter = '" + ja.getId_dokter()
                    + "' WHERE id_jadwal = " + ja.getId_jadwal();
            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Query ubah jadwal gagal : " + e.getMessage());
        }
        return false;
    }

    // ==================== Method 5: DELETE jadwal ====================
    public boolean hapusJadwalDokter(int ij) {
        Jadwal ja = new Jadwal();
        ja.setId_jadwal(ij);
        try {
            this.sql = "DELETE FROM tb_jadwal WHERE id_jadwal = " + ja.getId_jadwal();
            this.stm.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("Query hapus jadwal gagal : " + e.getMessage());
        }
        return false;
    }

    // ==================== Method 6: SELECT jadwal by dokter (opsional) ====================
    public ResultSet cariJadwalByDokter(int idDokter) {
        try {
            this.sql = "SELECT * FROM tb_jadwal WHERE id_dokter = " + idDokter;
            this.res = this.stm.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Query cari jadwal by dokter gagal : " + e.getMessage());
        }
        return this.res;
    }
}
