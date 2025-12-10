package Controller;

import Model.Pasien;
import Utility.ComboItem;
import Utility.Koneksi;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class PasienController {
    Koneksi db;
    public Statement stm;
    public ResultSet res;
    public String sql;

    // model tabel / tabel virtual
    DefaultTableModel dtm = new DefaultTableModel();

    // Konstruktor
    public PasienController() {
    this.db = new Koneksi();   // <-- pakai this.db
    this.db.config();
    this.stm = this.db.stm;
}


    // Method 1: Membuat model / desain tabel virtual
    public DefaultTableModel createTable() {
        this.dtm.addColumn("ID Pasien");
        this.dtm.addColumn("Nama Pasien");
        this.dtm.addColumn("Keluhan");
        return this.dtm;
    }

    // Method 2: SELECT (tampilkan semua pasien)
    public void tampilkanPasien() {
        try {
            // Bersihkan isi tabel virtual
            this.dtm.getDataVector().removeAllElements();
            this.dtm.fireTableDataChanged();

            // Query
            this.sql = "SELECT * FROM tb_pasien";

            // Jalankan
            this.res = this.stm.executeQuery(sql);

            // Masukkan hasil query ke tabel virtual
            while (res.next()) {
                Object[] obj = new Object[3];

                // nama kolom harus sama dengan di database
                obj[0] = res.getInt("id_pasien");
                obj[1] = res.getString("nama_pasien");
                obj[2] = res.getString("keluhan");

                this.dtm.addRow(obj);
            }

        } catch (Exception e) {
            System.out.println("Gagal Query..");
            System.out.println(e.getMessage());
        }
    }

    // Method 3: INSERT
    public boolean tambahPasien(String nama, String keluhan) {
        // Hubungkan dengan model
        Pasien p = new Pasien();
        p.setNama_pasien(nama);
        p.setKeluhan(keluhan);

        try {
            // Query
            this.sql = "INSERT INTO tb_pasien(nama_pasien, keluhan) VALUES ('"
                    + p.getNama_pasien() + "', '"
                    + p.getKeluhan() + "')";

            // Jalankan (INSERT pakai executeUpdate)
            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Method 4: UPDATE
    public boolean ubahPasien(int id, String nama, String keluhan) {
        // Hubungkan dengan model
        Pasien p = new Pasien();
        p.setId_pasien(id);
        p.setNama_pasien(nama);
        p.setKeluhan(keluhan);

        try {
            // Query
            this.sql = "UPDATE tb_pasien SET "
                    + "nama_pasien='" + p.getNama_pasien() + "', "
                    + "keluhan='" + p.getKeluhan() + "' "
                    + "WHERE id_pasien=" + p.getId_pasien();

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Method 5: DELETE
    public boolean hapusPasien(int id) {
        // Hubungkan dengan model
        Pasien p = new Pasien();
        p.setId_pasien(id);

        try {
            // Jangan lupa pakai FROM
            this.sql = "DELETE FROM tb_pasien WHERE id_pasien=" + p.getId_pasien();

            this.stm.executeUpdate(sql);
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public ResultSet getJadwalDokter(String idDokter) {
    try {
        // id_dokter bertipe VARCHAR(10) ⇒ WAJIB pakai tanda kutip
        this.sql = "SELECT * FROM tb_jadwal WHERE id_dokter = '" + idDokter + "'";

        return this.stm.executeQuery(this.sql);

    } catch (Exception e) {
        System.out.println("Error load jadwal: " + e.getMessage());
        return null;
    }
}
public List<ComboItem> getSemuaDokterCombo() {
    List<ComboItem> list = new ArrayList<>();

    try {
        this.sql = "SELECT * FROM tb_dokter";
        ResultSet rs = this.stm.executeQuery(this.sql);

        while (rs.next()) {
            String id        = rs.getString("id_dokter");
            String nama      = rs.getString("nama_dokter");
            String spesialis = rs.getString("spesialisasi");

            list.add(new ComboItem(id, nama + " (" + spesialis + ")"));
        }

        rs.close();

    } catch (Exception e) {
        System.out.println("Error getSemuaDokterCombo: " + e.getMessage());
    }

    return list;
}

public int getUmurPasien(String idPasien) {
    int umur = -1;

    try {
        String sql = "SELECT umur FROM tb_pasien WHERE id_pasien = '" + idPasien + "'";
        ResultSet rs = stm.executeQuery(sql);

        if (rs.next()) {
            umur = rs.getInt("umur");
        }
    } catch (Exception e) {
        System.out.println("Error get umur: " + e.getMessage());
    }

    return umur;
}



}

