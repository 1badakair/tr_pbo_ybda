
package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Jadwal {
    private int id;
    private int dokterId;
    private LocalDate tanggal;
    private LocalTime mulai;
    private LocalTime selesai;
    private int kapasitas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDokterId() {
        return dokterId;
    }

    public void setDokterId(int dokterId) {
        this.dokterId = dokterId;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getMulai() {
        return mulai;
    }

    public void setMulai(LocalTime mulai) {
        this.mulai = mulai;
    }

    public LocalTime getSelesai() {
        return selesai;
    }

    public void setSelesai(LocalTime selesai) {
        this.selesai = selesai;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
    
}
