package Controller;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class Koneksi {

    public static Connection con;
    public static Statement stm;

    public void config() {
        try {
            //Variable - variable untuk koneksi
            String url = "jdbc:mysql://127.0.0.1/dbtech";
            String user = "root";
            String pass = "";
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Menggabungkan variable tersebut menjadi String Connetion
            con = DriverManager.getConnection(url, user, pass);

            //Membuat sql Statement
            stm = (Statement) con.createStatement();
            System.out.println("Koneksi Berhasil.....");
        } catch (Exception e) {
            System.out.println("Koneksi Gagal.......");
            System.out.println(e.getMessage());
        }
    }

}
