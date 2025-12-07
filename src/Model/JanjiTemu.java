
package Model;

import java.time.LocalDateTime;

public class JanjiTemu {
    private int id_janji_temu;
    private String status;
    private LocalDateTime waktu_janji_dibuat;
    private String keluhan_pasien;
    private int id_jadwal;
    private int id_pasien;
    private int id_dokter;

    public int getId_janji_temu() {
        return id_janji_temu;
    }

    public void setId_janji_temu(int id_janji_temu) {
        this.id_janji_temu = id_janji_temu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getWaktu_janji_dibuat() {
        return waktu_janji_dibuat;
    }

    public void setWaktu_janji_dibuat(LocalDateTime waktu_janji_dibuat) {
        this.waktu_janji_dibuat = waktu_janji_dibuat;
    }

    public String getKeluhan_pasien() {
        return keluhan_pasien;
    }

    public void setKeluhan_pasien(String keluhan_pasien) {
        this.keluhan_pasien = keluhan_pasien;
    }

    public int getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(int id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public int getId_pasien() {
        return id_pasien;
    }

    public void setId_pasien(int id_pasien) {
        this.id_pasien = id_pasien;
    }

    public int getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(int id_dokter) {
        this.id_dokter = id_dokter;
    }
    
}


