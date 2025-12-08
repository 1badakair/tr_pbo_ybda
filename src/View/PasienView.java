/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.JanjiController;
import Controller.PasienController;
import Utility.ComboItem; // Wajib: Pastikan file ComboItem.java ada di package Utility
import Utility.UserSession;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class PasienView extends javax.swing.JFrame {
    PasienController pasienController;
    JanjiController janjiController;
   
    /**
     * Creates new form DashboardUserView
     */
    public PasienView() {
        initComponents();
        
        // --- SIMULASI SESSION (HAPUS JIKA SUDAH ADA LOGIN) ---
        // Jika belum login, kita paksa login sebagai user ID 4 (Pasien P001)
        if (UserSession.getIdPasien() == null) {
             UserSession.setUser(4, "P001", "Dhika Primanda", "pasien");
        }
        // -----------------------------------------------------
        
        // Inisialisasi Controller
        pasienController = new PasienController();
        janjiController = new JanjiController();
        
        // Jalankan setup awal
        myCustomInit();
    }    
      
    private void myCustomInit() {
        // 1. Tampilkan Nama User di Label
        lblTampilNama.setText("Halo, " + UserSession.getNama()); 
        
        // 2. Load Data Dokter ke ComboBox
        loadDokterCombo(); 
        
        // 3. Load Data Tabel Status (Tab Sebelah)
        refreshTabelStatus(); 
        
        // 4. Posisikan layar di tengah
        setLocationRelativeTo(null); 
        
        // --- EVENT LISTENER UNTUK COMBOBOX DOKTER ---
        // Saat dokter dipilih, otomatis cari jadwal beliau
        cbPilihDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Ambil item dokter yang dipilih
                Object selected = cbPilihDokter.getSelectedItem();
                
                // Pastikan yang dipilih adalah object ComboItem (bukan string null)
                if (selected instanceof ComboItem) {
                    ComboItem item = (ComboItem) selected;
                    // Load jadwal berdasarkan ID Dokter (key)
                    loadJadwalCombo(item.getKey());
                }
            }
        });
    }
    
    // --- METHOD: ISI COMBOBOX DOKTER ---
private void loadDokterCombo() {

    cbPilihDokter.removeAllItems();

    List<ComboItem> dokterList = pasienController.getSemuaDokterCombo();

    for (ComboItem d : dokterList) {
        cbPilihDokter.addItem(d);
    }

    if (cbPilihDokter.getItemCount() > 0) {
        cbPilihDokter.setSelectedIndex(0);
    }
}


    
    // --- METHOD: ISI COMBOBOX JADWAL ---
    // Method ini dipanggil otomatis saat cbPilihDokter berubah
    private void loadJadwalCombo(String idDokter) {
    cbPilihJadwal.removeAllItems();   // bersihkan dulu

    try {
        ResultSet rs = pasienController.getJadwalDokter(idDokter);

        boolean adaJadwal = false;

        if (rs != null) {
            while (rs.next()) {
                adaJadwal = true;

                String idJadwal = rs.getString("id_jadwal");
                String tgl      = rs.getString("tanggal");
                String jam      = rs.getString("jam_mulai");
                String kuota    = rs.getString("kuota");

                String tampilan = tgl + " | Jam " + jam + " | Sisa: " + kuota;

                cbPilihJadwal.addItem(new ComboItem(idJadwal, tampilan));
            }
        }

        if (!adaJadwal) {
            cbPilihJadwal.addItem(new ComboItem("0", "-- Tidak ada jadwal --"));
        }

    } catch (Exception e) {
        System.out.println("Error load jadwal: " + e.getMessage());
        cbPilihJadwal.addItem(new ComboItem("0", "-- Error load jadwal --"));
    }
}


    // --- LOGIKA UTAMA: TOMBOL KIRIM ---
    public void aksiSimpanJanji() {
    // 1. Ambil ID Pasien dari Session
    String idPasien = UserSession.getIdPasien(); 
        
    // 2. Ambil Inputan
    String nama = lblTampilNama.getText();   // <-- TAMBAHKAN INI
    String keluhan = txtKeluhan.getText(); 
        
    // 3. Ambil ID Jadwal dari ComboBox
    ComboItem selectedJadwal = (ComboItem) cbPilihJadwal.getSelectedItem();
        
    // --- VALIDASI ---
    if (keluhan.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Mohon isi keluhan Anda!");
        return;
    }
        
    if (selectedJadwal == null || selectedJadwal.getKey().equals("0")) {
         JOptionPane.showMessageDialog(this, "Jadwal tidak tersedia, silakan pilih dokter lain.");
         return;
    }

    // Konversi ID Jadwal ke Integer
    int idJadwalTarget = Integer.parseInt(selectedJadwal.getKey());

    // 4. SIMPAN KE DATABASE
    boolean berhasil = janjiController.tambahJanjiTemu(idJadwalTarget, idPasien, keluhan);


    if (berhasil) {
        JOptionPane.showMessageDialog(this, "Berhasil! Janji Temu telah dibuat.");
        bersihkanForm();
        refreshTabelStatus();
    } else {
        JOptionPane.showMessageDialog(this, "Gagal menyimpan. Silakan coba lagi.");
    }
}

    // Method untuk membersihkan form input
    private void bersihkanForm() {
        txtKeluhan.setText("");
        if (cbPilihDokter.getItemCount() > 0) cbPilihDokter.setSelectedIndex(0);
        // cbPilihJadwal akan otomatis reset karena trigger dari dokter
    }
    
    // Method untuk me-refresh tabel status (Tab 2)
    private void refreshTabelStatus() {
        // Pastikan variabel tabel kamu namanya 'tabelStatus'
        // Jika error merah disini, ganti nama variabel tabelmu di Design
        if (tabelStatus != null) {
            tabelStatus.setModel(janjiController.createTable());
            janjiController.tampilkanRiwayat();
        }
    }
    
    private void clearStatusTable() {
    DefaultTableModel model = (DefaultTableModel) tabelStatus.getModel();
    model.setRowCount(0); // hapus semua baris
}

    
    // --- EVENT HANDLER TOMBOL DARI DESIGN ---
    



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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeluhan = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbPilihDokter = new javax.swing.JComboBox<>();
        btnClear = new javax.swing.JButton();
        btnKirimPermintaan = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTampilNama = new javax.swing.JLabel();
        cbPilihJadwal = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelStatus = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        btnClearTbl = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(72, 202, 228));

        jLabel1.setBackground(new java.awt.Color(0, 119, 182));
        jLabel1.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DASHBOARD PASIEN");

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 153));
        jTabbedPane1.setForeground(new java.awt.Color(102, 102, 102));

        jPanel2.setBackground(new java.awt.Color(144, 224, 239));

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("FORMULIR KELUHAN & PILIH DOKTER");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Tuliskan Keluhan Anda :");

        txtKeluhan.setBackground(new java.awt.Color(204, 255, 255));
        txtKeluhan.setColumns(20);
        txtKeluhan.setRows(5);
        jScrollPane1.setViewportView(txtKeluhan);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("DD/MM/YYYY, HOUR");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Pilih Dokter yang tersedia :");

        cbPilihDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPilihDokterActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(255, 255, 153));
        btnClear.setForeground(new java.awt.Color(51, 51, 51));
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnKirimPermintaan.setBackground(new java.awt.Color(255, 255, 153));
        btnKirimPermintaan.setForeground(new java.awt.Color(51, 51, 51));
        btnKirimPermintaan.setText("Kirim Permintaan");
        btnKirimPermintaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKirimPermintaanActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Pilih Jadwal :");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Selamat Datang,");

        lblTampilNama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTampilNama.setForeground(new java.awt.Color(51, 51, 51));
        lblTampilNama.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblTampilNama))
                                            .addComponent(jLabel3))))
                                .addGap(0, 722, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbPilihDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnKirimPermintaan, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cbPilihJadwal, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblTampilNama))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbPilihJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnKirimPermintaan, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbPilihDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        jTabbedPane1.addTab("Buat Janji Temu", jPanel2);

        jPanel3.setBackground(new java.awt.Color(144, 224, 239));

        tabelStatus.setBackground(new java.awt.Color(204, 255, 255));
        tabelStatus.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabelStatus);

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Riwayat Pengajuan Janji Temu");

        btnClearTbl.setBackground(new java.awt.Color(255, 255, 153));
        btnClearTbl.setForeground(new java.awt.Color(0, 0, 0));
        btnClearTbl.setText("Clear");
        btnClearTbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearTblActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnClearTbl, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClearTbl, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Status Janji Temu", jPanel3);

        btnLogout.setBackground(new java.awt.Color(255, 51, 51));
        btnLogout.setForeground(new java.awt.Color(0, 0, 0));
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLogout)))
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnKirimPermintaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKirimPermintaanActionPerformed
        aksiSimpanJanji();
    }//GEN-LAST:event_btnKirimPermintaanActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        bersihkanForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cbPilihDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPilihDokterActionPerformed
        ComboItem item = (ComboItem) cbPilihDokter.getSelectedItem();
    if (item != null) {
        loadJadwalCombo(item.getKey());  // key = id_dokter (D001, D002, ...)
    }
    }//GEN-LAST:event_cbPilihDokterActionPerformed

    private void btnClearTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearTblActionPerformed
        clearStatusTable();
    }//GEN-LAST:event_btnClearTblActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        UserSession.clear();  // kosongkan session

    this.dispose();       // tutup dashboard

    new LoginView().setVisible(true);  // kembali ke login
    }//GEN-LAST:event_btnLogoutActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PasienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PasienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PasienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PasienView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PasienView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearTbl;
    private javax.swing.JButton btnKirimPermintaan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<ComboItem> cbPilihDokter;
    private javax.swing.JComboBox<ComboItem> cbPilihJadwal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTampilNama;
    private javax.swing.JTable tabelStatus;
    private javax.swing.JTextArea txtKeluhan;
    // End of variables declaration//GEN-END:variables
}
