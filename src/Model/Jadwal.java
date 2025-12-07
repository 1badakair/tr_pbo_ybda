
package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Jadwal {
    private int id_jadwal;
    private LocalDate tanggal;
    private LocalTime jam_mulai;
    private LocalTime jam_selesai;
    private int kuota;
    private int id_dokter;

    public int getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(int id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(LocalTime jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public LocalTime getJam_selesai() {
        return jam_selesai;
    }

    public void setJam_selesai(LocalTime jam_selesai) {
        this.jam_selesai = jam_selesai;
    }

    public int getKuota() {
        return kuota;
    }

    public void setKuota(int kuota) {
        this.kuota = kuota;
    }

    public int getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(int id_dokter) {
        this.id_dokter = id_dokter;
    }

}
