//package Controller;
//
//import Model.Dokter;
//import Utility.Koneksi;
//import java.sql.Statement;
//import java.sql.ResultSet;
//import javax.swing.table.DefaultTableModel;
//
//public class DokterController {
//    public Statement stm;
//    public ResultSet res;
//    public String sql;
//
//    // Model tabel / tabel virtual sebelum diaplikasikan ke view
//    DefaultTableModel dtm = new DefaultTableModel();
//
//    // Konstruktor
//    public DokterController() {
//        Koneksi db = new Koneksi();
//        db.config();
//        this.stm = db.stm;
//    }
//
//    // Method 1: Membuat desain tabel virtual untuk jadwal
//    public DefaultTableModel createTable() {
//        this.dtm = new DefaultTableModel(); // reset jika sebelumnya ada
//        this.dtm.addColumn("ID Dokter");
//        this.dtm.addColumn("Nama");
//        this.dtm.addColumn("Spesialisasi");
//        return this.dtm;
//    }
//
//    // Method 2: SELECT semua jadwal dan masukkan ke tabel virtual
//    public void tampilkanDokter() {
//        try {
//            this.dtm.getDataVector().removeAllElements();
//            this.dtm.fireTableDataChanged();
//
//            // query
//            this.sql = "SELECT * FROM tb_dokter";
//
//            // jalankan
//            this.res = this.stm.executeQuery(sql);
//
//            // masukkan hasil ke tabel virtual
//            while (res.next()) {
//                Object[] obj = new Object[3];
//                obj[0] = res.getInt("id_dokter");
//                obj[1] = res.getString("nama_dokter");
//                obj[2] = res.getString("spesialisasi");
//                this.dtm.addRow(obj);
//            }
//            System.out.println("tampilkanDokter: rows loaded = " + this.dtm.getRowCount());
//        } catch (Exception e) {
//            System.out.println("Gagal query tampil jadwal: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}

package Controller;

import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class DokterController {
    public Statement stm;
    public ResultSet res;
    public String sql;
    private DefaultTableModel dtm;

    public DokterController() {
        System.out.println("DokterController(): constructor START");
        Koneksi db = new Koneksi();
        db.config();
        this.stm = Koneksi.stm; // gunakan static seperti di Koneksi
        System.out.println("DokterController(): after Koneksi.config(), stm = " + this.stm);
    }

    public DefaultTableModel createTable() {
        System.out.println("DokterController.createTable() called");
        this.dtm = new DefaultTableModel();
        this.dtm.addColumn("ID Dokter");
        this.dtm.addColumn("Nama");
        this.dtm.addColumn("Spesialisasi");
        return this.dtm;
    }

    public DefaultTableModel tampilkanDokter() {
        System.out.println("DokterController.tampilkanDokter() called");
        if (this.dtm == null) createTable();
        this.dtm.setRowCount(0);
        try {
            if (this.stm == null) {
                System.out.println("tampilkanDokter: stm IS NULL");
                return this.dtm;
            }
            this.sql = "SELECT * FROM tb_dokter";
            System.out.println("tampilkanDokter: executing -> " + this.sql);
            this.res = this.stm.executeQuery(sql);
            int count = 0;
            while (res.next()) {
                Object[] obj = new Object[3];
                obj[0] = res.getObject("id_dokter");
                obj[1] = res.getObject("nama_dokter");
                obj[2] = res.getObject("spesialisasi");
                this.dtm.addRow(obj);
                count++;
            }
            System.out.println("tampilkanDokter: rows loaded = " + count);
        } catch (Exception e) {
            System.err.println("tampilkanDokter: exception -> " + e.getMessage());
        }
        return this.dtm;
    }
}
