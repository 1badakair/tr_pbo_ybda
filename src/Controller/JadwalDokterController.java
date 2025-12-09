package Controller;

import Model.Jadwal;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.table.DefaultTableModel;

public class JadwalDokterController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    DefaultTableModel dtm = new DefaultTableModel();

    // Tambahkan field idDokter
    private String idDokter;

    // Konstruktor harus menerima idDokter!
    public JadwalDokterController(String idDokter) {
        this.idDokter = idDokter; // simpan id dokter yang login

        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }
    
    public JadwalDokterController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    // ==================== Method 1: desain tabel ====================
    public DefaultTableModel createTable() {
        this.dtm.addColumn("ID Jadwal");
        this.dtm.addColumn("ID Dokter");
        this.dtm.addColumn("Tanggal");
        this.dtm.addColumn("Jam Mulai");
        this.dtm.addColumn("Jam Selesai");
        this.dtm.addColumn("Kuota");
        return this.dtm;
    }

    // ==================== Method 2: SELECT HANYA jadwal milik dokter login ====================
    public void tampilkanJadwalDokter() {
        try {
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            this.sql = "SELECT * FROM tb_jadwal WHERE id_dokter = '" + this.idDokter + "'";
            this.res = this.stm.executeQuery(sql);

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

    // ==================== Method 3: INSERT JADWAL DENGAN id_dokter ====================
    public boolean tambahJadwalDokter(int k, LocalTime jm, LocalTime js, LocalDate t) {
        try {
            this.sql = "INSERT INTO tb_jadwal (id_dokter, kuota, jam_mulai, jam_selesai, tanggal) " +
                       "VALUES ('" + this.idDokter + "', " + k + ", '" + jm + "', '" + js + "', '" + t + "')";

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println("Query tambah jadwal gagal : " + e.getMessage());
            return false;
        }
    }

    // ==================== Method 4: UPDATE dengan pembatas dokter ====================
    public boolean ubahJadwalDokter(int k, LocalTime jm, LocalTime js, LocalDate t, int ij) {
        try {
            this.sql = "UPDATE tb_jadwal SET kuota = " + k +
                       ", jam_mulai = '" + jm +
                       "', jam_selesai = '" + js +
                       "', tanggal = '" + t +
                       "' WHERE id_jadwal = " + ij +
                       " AND id_dokter = '" + this.idDokter + "'";

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println("Query ubah jadwal gagal : " + e.getMessage());
            return false;
        }
    }

    // ==================== Method 5: DELETE dengan pembatas dokter ====================
    public boolean hapusJadwalDokter(int ij) {
        try {
            this.sql = "DELETE FROM tb_jadwal WHERE id_jadwal = " + ij +
                       " AND id_dokter = '" + this.idDokter + "'";

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println("Query hapus jadwal gagal : " + e.getMessage());
            return false;
        }
    }
}
