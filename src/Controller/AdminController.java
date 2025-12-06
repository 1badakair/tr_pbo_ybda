package Controller;

import Model.Admin;
import java.sql.Statement;
import java.sql.ResultSet;

public class AdminController {

    public Statement stm;
    public ResultSet res;
    public String sql;

    //konstruktor
    public AdminController() {
        //objek koneksi
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    //method cekLogin(select)
    public boolean cekLogin(String un, String pw) {
        //dipetakan dengan model
        Admin adm = new Admin();
        adm.setUsername(un);
        adm.setPasswordHash(pw);
        boolean status = false;

        //query ke database + cek
        try {
            //sql query
            this.sql = "SELECT * FROM tbadmin "
                    + "WHERE username = '" + adm.getUsername() + "' "
                    + "AND password = '" + adm.getPasswordHash() + "'";

            //menjalankan query
            //khusus SELECT gunakan 'executeQuery'
            this.res = this.stm.executeQuery(sql);

            //pengecekan
            if (res.next()) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            System.out.println("Query gagal");
        }
        return status;
    }
}
