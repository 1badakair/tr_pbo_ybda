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
        this.dtm.addColumn("ID Jadwal");
        this.dtm.addColumn("Tanggal");
        this.dtm.addColumn("Jam Mulai");
        this.dtm.addColumn("Jam Selesai");
        this.dtm.addColumn("Kuota");
        this.dtm.addColumn("ID Dokter");
        return this.dtm;
    }

    // ==================== Method 2: SELECT semua jadwal (isi JTable) ====================
    public void tampilkanJadwal() {
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
                obj[1] = res.getDate("tanggal");      // bisa juga res.getString("tanggal")
                obj[2] = res.getTime("jam_mulai");
                obj[3] = res.getTime("jam_selesai");
                obj[4] = res.getInt("kuota");
                obj[5] = res.getInt("id_dokter");

                this.dtm.addRow(obj);
            }

        } catch (Exception e) {
            System.out.println("Query tampil jadwal gagal : " + e.getMessage());
        }
    }

    // ==================== Method 3: INSERT jadwal ====================
    public boolean tambahJadwal(Jadwal j) {
        boolean status = false;

        try {
            // konversi LocalDate & LocalTime ke java.sql.Date/Time (string)
            String tgl        = Date.valueOf(j.getTanggal()).toString();
            String jamMulai   = Time.valueOf(j.getJam_mulai()).toString();
            String jamSelesai = Time.valueOf(j.getJam_selesai()).toString();

            this.sql = "INSERT INTO tb_jadwal "
                    + "(tanggal, jam_mulai, jam_selesai, kuota, id_dokter) VALUES ("
                    + "'" + tgl + "', "
                    + "'" + jamMulai + "', "
                    + "'" + jamSelesai + "', "
                    + j.getKuota() + ", "
                    + j.getId_dokter() + ")";

            // khusus INSERT/UPDATE/DELETE pakai executeUpdate
            this.stm.executeUpdate(sql);
            status = true;
        } catch (Exception e) {
            System.out.println("Query tambah jadwal gagal : " + e.getMessage());
            status = false;
        }

        return status;
    }

    // ==================== Method 4: UPDATE jadwal ====================
    public boolean ubahJadwal(Jadwal j) {
        boolean status = false;

        try {
            String tgl        = Date.valueOf(j.getTanggal()).toString();
            String jamMulai   = Time.valueOf(j.getJam_mulai()).toString();
            String jamSelesai = Time.valueOf(j.getJam_selesai()).toString();

            this.sql = "UPDATE tb_jadwal SET "
                    + "tanggal = '" + tgl + "', "
                    + "jam_mulai = '" + jamMulai + "', "
                    + "jam_selesai = '" + jamSelesai + "', "
                    + "kuota = " + j.getKuota() + ", "
                    + "id_dokter = " + j.getId_dokter() + " "
                    + "WHERE id_jadwal = " + j.getId_jadwal();

            this.stm.executeUpdate(sql);
            status = true;
        } catch (Exception e) {
            System.out.println("Query ubah jadwal gagal : " + e.getMessage());
        }

        return status;
    }

    // ==================== Method 5: DELETE jadwal ====================
    public boolean hapusJadwal(int idJadwal) {
        boolean status = false;

        try {
            this.sql = "DELETE FROM tb_jadwal WHERE id_jadwal = " + idJadwal;
            this.stm.executeUpdate(sql);
            status = true;
        } catch (Exception e) {
            System.out.println("Query hapus jadwal gagal : " + e.getMessage());
        }

        return status;
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
