package Controller;

import Model.Dokter;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class DokterController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    // Model tabel virtual
    DefaultTableModel dtm = new DefaultTableModel();

    // Konstruktor
    public DokterController() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    // Method 1: Membuat desain tabel virtual
    public DefaultTableModel createTable() {
        if (this.dtm.getColumnCount() == 0) {
            this.dtm.addColumn("ID Dokter");
            this.dtm.addColumn("Nama Dokter");
            this.dtm.addColumn("Spesialisasi");
        }
        return this.dtm;
    }

    // Method 2: SELECT
    public void tampilkanDokter() {
        try {
            // Bersihkan tabel virtual
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            // Query
            this.sql = "SELECT * FROM tb_dokter";

            // Jalankan query
            this.res = this.stm.executeQuery(sql);

            // Masukkan hasil query ke tabel virtual
            while (res.next()) {
                Object[] obj = new Object[3];

                obj[0] = res.getString("id_dokter");
                obj[1] = res.getString("nama_dokter");
                obj[2] = res.getString("spesialisasi");

                this.dtm.addRow(obj);
            }

        } catch (Exception e) {
            System.out.println("Gagal Query Dokter..");
            System.out.println(e.getMessage());
        }
    }

    // Method 3: INSERT
    public boolean tambahDokter(String a, String b, String c) {
        Dokter dk = new Dokter();
        dk.setId_dokter(a);
        dk.setNama_dokter(b);
        dk.setSpesialisasi(c);

        try {
            this.sql = "INSERT INTO tb_dokter(id_dokter,nama_dokter,spesialisasi) "
                    + "VALUES ('" + dk.getId_dokter() + "','" + dk.getNama_dokter() + "','" + dk.getSpesialisasi() + "')";
            this.stm.executeUpdate(this.sql);
            System.out.println("tambahDokter: OK");
            return true;
        } catch (Exception e) {
            System.out.println("tambahDokter failed:");
            e.printStackTrace();
            System.out.println("Query that failed: " + this.sql);
            return false;
        }
    }

    // Method 4: UPDATE
    public boolean ubahDokter(String a, String b, String c) {

        Dokter dk = new Dokter();
        dk.setId_dokter(a);        // <-- TAMBAHKAN INI
        dk.setNama_dokter(b);
        dk.setSpesialisasi(c);

        try {
            // Query update
            this.sql = "UPDATE tb_dokter SET nama_dokter='" + dk.getNama_dokter()
                    + "', spesialisasi='" + dk.getSpesialisasi()
                    + "' WHERE id_dokter='" + dk.getId_dokter() + "'";

            // Jalankan query
            int hasil = this.stm.executeUpdate(sql);

            // Jika tidak ada baris terupdate => gagal
            return hasil > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Method 5: DELETE
    public boolean hapusDokter(String a) {
        Dokter dk = new Dokter();
        dk.setId_dokter(a);

        try {
            // Query delete
            this.sql = "DELETE FROM tb_dokter WHERE id_dokter='" + dk.getId_dokter() + "'";

            // Jalankan query
            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
