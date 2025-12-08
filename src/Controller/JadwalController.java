package Controller;

import Model.Jadwal;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;

public class JadwalController {

    public Statement stm;
    public ResultSet res;         
    public String sql;

    // konstruktor
    public JadwalController() {
        // objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    // ==================== SELECT semua jadwal ====================
    public ResultSet getDataJadwal() {
        try {
            // ambil semua data jadwal
            this.sql = "SELECT * FROM tb_jadwal";
            this.res = this.stm.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Query tampil jadwal gagal : " + e.getMessage());
        }
        return this.res;
    }

    // ==================== INSERT jadwal ====================
    public boolean tambahJadwal(Jadwal j) {
        boolean status = false;

        try {
            // konversi LocalDate & LocalTime ke java.sql.Date/Time (string)
            String tgl       = Date.valueOf(j.getTanggal()).toString();
            String jamMulai  = Time.valueOf(j.getJam_mulai()).toString();
            String jamSelesai= Time.valueOf(j.getJam_selesai()).toString();

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

    // ==================== UPDATE jadwal ====================
    public boolean ubahJadwal(Jadwal j) {
        boolean status = false;

        try {
            String tgl       = Date.valueOf(j.getTanggal()).toString();
            String jamMulai  = Time.valueOf(j.getJam_mulai()).toString();
            String jamSelesai= Time.valueOf(j.getJam_selesai()).toString();

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

    // ==================== DELETE jadwal ====================
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

    // ==================== SELECT jadwal by dokter (opsional) ====================
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
