package Controller;

import Model.Jadwal;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Time;
import javax.swing.table.DefaultTableModel;

public class JadwalDokterController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    private DefaultTableModel dtm = new DefaultTableModel();

    // dokter yang login
    private String idDokter;

    // =============== Konstruktor ===============
    public JadwalDokterController(String idDokter) {
        this.idDokter = idDokter;
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    public JadwalDokterController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    public String getIdDokter() { return this.idDokter; }

    // =============== TABLE MODEL ===============
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

    // =============== SELECT (HANYA JADWAL MILIK DOKTER INI) ===============
    public void tampilkanJadwalDokter() {
        try {
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            if (this.idDokter == null || this.idDokter.isBlank()) {
                System.out.println("ERROR: idDokter belum diset.");
                return;
            }

            this.sql = "SELECT * FROM tb_jadwal WHERE id_dokter = '" + this.idDokter +
                       "' ORDER BY tanggal, jam_mulai";

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
            System.out.println("Query tampil jadwal gagal: " + e.getMessage());
        }
    }

    // =============== INSERT JADWAL ===============
    public boolean tambahJadwalDokter(int k, Time jm, Time js, Date t) {

        // MASUKKAN KE MODEL TERLEBIH DAHULU
        Jadwal ja = new Jadwal();
        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);
        ja.setId_dokter(this.idDokter); // penting!

        try {
            this.sql =
                "INSERT INTO tb_jadwal (id_dokter, kuota, jam_mulai, jam_selesai, tanggal) VALUES ('"
                + ja.getId_dokter() + "', "
                + ja.getKuota() + ", '"
                + ja.getJam_mulai() + "', '"
                + ja.getJam_selesai() + "', '"
                + ja.getTanggal() + "')";

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println("Query tambah jadwal gagal: " + e.getMessage());
            return false;
        }
    }

    // =============== UPDATE JADWAL ===============
    public boolean ubahJadwalDokter(int k, Time jm, Time js, Date t, int ij) {

        // PAKAI MODEL
        Jadwal ja = new Jadwal();
        ja.setKuota(k);
        ja.setJam_mulai(jm);
        ja.setJam_selesai(js);
        ja.setTanggal(t);
        ja.setId_jadwal(ij);
        ja.setId_dokter(this.idDokter);

        try {
            this.sql =
                "UPDATE tb_jadwal SET kuota = " + ja.getKuota()
                + ", jam_mulai = '" + ja.getJam_mulai()
                + "', jam_selesai = '" + ja.getJam_selesai()
                + "', tanggal = '" + ja.getTanggal()
                + "' WHERE id_jadwal = " + ja.getId_jadwal()
                + " AND id_dokter = '" + ja.getId_dokter() + "'";

            int updated = this.stm.executeUpdate(sql);
            return updated > 0;

        } catch (Exception e) {
            System.out.println("Query ubah jadwal gagal: " + e.getMessage());
            return false;
        }
    }

    // =============== DELETE JADWAL ===============
    public boolean hapusJadwalDokter(int ij) {

        Jadwal ja = new Jadwal();
        ja.setId_jadwal(ij);
        ja.setId_dokter(this.idDokter);

        try {
            this.sql =
                "DELETE FROM tb_jadwal WHERE id_jadwal = "
                + ja.getId_jadwal()
                + " AND id_dokter = '" + ja.getId_dokter() + "'";

            int deleted = this.stm.executeUpdate(sql);
            return deleted > 0;

        } catch (Exception e) {
            System.out.println("Query hapus jadwal gagal: " + e.getMessage());
            return false;
        }
    }

}
