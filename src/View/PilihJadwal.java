/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Controller.JadwalController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**
 * PilihJadwal - dialog modal untuk memilih jadwal.
 * Gunakan callback untuk menerima id jadwal + ringkasan di PasienView.
 */
public class PilihJadwal extends JDialog {

    public interface PilihCallback {
        void onJadwalDipilih(int idJadwal, String ringkasan);
    }

    private final JadwalController jadwalController;
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private JTextField txtCari;
    private PilihCallback callback;

    public PilihJadwal(Frame owner, PilihCallback callback) {
        super(owner, "Pilih Jadwal", true);
        this.callback = callback;
        this.jadwalController = new JadwalController();

        initUI();
        loadData();

        setPreferredSize(new Dimension(900, 420));
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    
    private void initUI() {

    // 1️⃣ Buat model tabel (kolom) SEKALI SAJA
    DefaultTableModel model = jadwalController.createTable();

    // 2️⃣ Buat JTable
    table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setFillsViewportHeight(true);

    // 3️⃣ RowSorter untuk search
    rowSorter = new TableRowSorter<>(model);
    table.setRowSorter(rowSorter);

    // 4️⃣ Panel atas (search + refresh)
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(new JLabel("Cari:"));
    txtCari = new JTextField(25);
    topPanel.add(txtCari);

    JButton btnRefresh = new JButton("Refresh");
    topPanel.add(btnRefresh);

    // 5️⃣ ScrollPane
    JScrollPane sp = new JScrollPane(table);

    // 6️⃣ Panel bawah
    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnPilih = new JButton("Pilih");
    JButton btnBatal = new JButton("Batal");
    bottom.add(btnPilih);
    bottom.add(btnBatal);

    // 7️⃣ Pasang ke frame
    setLayout(new BorderLayout(8, 8));
    add(topPanel, BorderLayout.NORTH);
    add(sp, BorderLayout.CENTER);
    add(bottom, BorderLayout.SOUTH);

    // 8️⃣ ACTIONS
    btnRefresh.addActionListener(e -> jadwalController.tampilkanJadwalDokter());
    btnPilih.addActionListener(e -> pilihAction());
    btnBatal.addActionListener(e -> dispose());

    // 9️⃣ SEARCH FILTER
    txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void filter() {
            String text = txtCari.getText().trim();
            if (text.isEmpty()) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
    });

    // 🔟 Atur lebar kolom
    TableColumnModel cm = table.getColumnModel();
    if (cm.getColumnCount() >= 6) {
        cm.getColumn(0).setPreferredWidth(70);   // ID Jadwal
        cm.getColumn(1).setPreferredWidth(70);   // ID Dokter
        cm.getColumn(2).setPreferredWidth(100);  // Tanggal
        cm.getColumn(3).setPreferredWidth(80);   // Jam Mulai
        cm.getColumn(4).setPreferredWidth(90);   // Jam Selesai
        cm.getColumn(5).setPreferredWidth(50);   // Kuota
    }
}



    private void loadData() {
        // agar model diisi
        jadwalController.tampilkanJadwalDokter();

        // jika ingin isi cbFilterDokter dari DB, tambahkan method di controller yang mengembalikan daftar dokter
        // contoh:
        // cbFilterDokter.removeAllItems();
        // cbFilterDokter.addItem("Semua Dokter");
        // for (String d : jadwalController.getDaftarNamaDokter()) cbFilterDokter.addItem(d);
    }

    private void pilihAction() {
        int selView = table.getSelectedRow();
        if (selView < 0) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu jadwal dulu.", "Pilih Jadwal", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // jika menggunakan sorter, konversi view->model index
        int selModel = table.convertRowIndexToModel(selView);

        // kolom sesuai createTable() di JadwalController:
        // 0 = id_jadwal, 1 = tanggal, 2 = jam_mulai, 3 = jam_selesai, ...
        Object valId = jadwalController.createTable().getValueAt(selModel, 0);
        int idJadwal;
        try {
            idJadwal = Integer.parseInt(valId.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ID jadwal invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tgl = jadwalController.createTable().getValueAt(selModel, 1).toString();
        String jamMulai = jadwalController.createTable().getValueAt(selModel, 2).toString();
        String jamSelesai = jadwalController.createTable().getValueAt(selModel, 3).toString();
        String ringkasan = tgl + " | " + jamMulai + " - " + jamSelesai;

        if (callback != null) callback.onJadwalDipilih(idJadwal, ringkasan);
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCariNama = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableJadwal = new javax.swing.JTable();
        btnBatal = new javax.swing.JButton();
        btnPilih = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PILIH JADWAL");

        jLabel2.setText("Cari :");

        tableJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableJadwal);

        btnBatal.setText("Batal");

        btnPilih.setText("Pilih");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPilih)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatal))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCariNama)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCariNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal)
                    .addComponent(btnPilih)
                    .addComponent(btnRefresh))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPilihActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(() -> {
        // panggil dialog dengan owner null dan callback sederhana untuk testing
        PilihJadwal dlg = new PilihJadwal((Frame) null, (id, ringkasan) -> {
            System.out.println("Terpilih: " + id + " -> " + ringkasan);
        });
        dlg.setVisible(true);
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnPilih;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableJadwal;
    private javax.swing.JTextField txtCariNama;
    // End of variables declaration//GEN-END:variables
}
