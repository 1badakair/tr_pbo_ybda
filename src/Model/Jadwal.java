
package Model;

import java.sql.Date;
import java.sql.Time;

public class Jadwal {
    private int id_jadwal;
    private String id_dokter;
    private Date tanggal;
    private Time jam_mulai;
    private Time jam_selesai;
    private int kuota;

    public int getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(int id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public String getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(String id_dokter) {
        this.id_dokter = id_dokter;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Time getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(Time jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public Time getJam_selesai() {
        return jam_selesai;
    }

    public void setJam_selesai(Time jam_selesai) {
        this.jam_selesai = jam_selesai;
    }

    public int getKuota() {
        return kuota;
    }

    public void setKuota(int kuota) {
        this.kuota = kuota;
    }
}
