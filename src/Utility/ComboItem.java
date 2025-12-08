package Utility;

// Class ini agar ComboBox bisa simpan ID tersembunyi
public class ComboItem {
    private String key;   // Menyimpan ID (misal: D001)
    private String value; // Menyimpan Tampilan (misal: dr. Budi)

    public ComboItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    // Fungsi ini wajib agar yang muncul di layar adalah text value-nya
    @Override
    public String toString() {
        return value;
    }
}