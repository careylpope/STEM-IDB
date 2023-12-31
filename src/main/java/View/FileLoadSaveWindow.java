/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

/**
 * FileManager window:  Takes in user given values of which fields to update and
 *   how many scholars/students are in each linked excel sheet.  Input validation
 *   taken care of in DAO's and updateFile action method of STEMIDBMainWindow
 */
public class FileLoadSaveWindow extends javax.swing.JDialog {

    private static int loadSave;
    public static final int LOAD = 0;
    public static final int SAVE = 1;
    
    private boolean actionScholars;
    private boolean actionStudents;
    
    // NOT CURRENTLY USED
    private String TotalScholars;
    private String totalStudents;
    
    /**
     * Creates new form FileManager
     */
    public FileLoadSaveWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("File Manager");
        if(loadSave == LOAD)
        {
            this.jInstructionsLabel1.setText(" Select Fields to update from Excel Spreadsheet ");
            this.jLoadSaveFilesButton.setText(" Update Files ");
        }
        else if(loadSave == SAVE)
        {
            this.jInstructionsLabel1.setText(" Select Fields to save to new Excel Spreadsheet ");
            this.jLoadSaveFilesButton.setText(" Save Files ");
        }
        this.setLocationRelativeTo(null);
    }

    public boolean isActionScholars() 
    {
        return actionScholars;
    }
    public boolean isActionStudents() 
    {
        return actionStudents;
    }
    
    public String getTotalScholars()
    {
        return this.TotalScholars;
    }
    public String getTotalStudents()
    {
        return this.totalStudents;
    }
    
    // Function to determine whether load or save window is desired
    public static void setLoadSave(int option)
    {
        if(option == LOAD || option == SAVE)
        {
            loadSave = option;
        }
        else
        {
            //TODO: throw error, close window through additional bool, something of that nature
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMainPanel = new javax.swing.JPanel();
        jScholarCheckBox = new javax.swing.JCheckBox();
        jStudentCheckBox = new javax.swing.JCheckBox();
        jInstructionsLabel1 = new javax.swing.JLabel();
        jLoadSaveFilesButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScholarCheckBox.setText("Scholars");
        jScholarCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jScholarCheckBoxActionPerformed(evt);
            }
        });

        jStudentCheckBox.setText("Students");

        jInstructionsLabel1.setText(" Select Fields to Update from Excel Spreadsheet ");
        jInstructionsLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLoadSaveFilesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLoadSaveFilesButton.setText("Update Files");
        jLoadSaveFilesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLoadSaveFilesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(jMainPanel);
        jMainPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jScholarCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jStudentCheckBox)
                .addGap(40, 40, 40))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                        .addComponent(jInstructionsLabel1)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jMainPanelLayout.createSequentialGroup()
                        .addComponent(jLoadSaveFilesButton)
                        .addGap(102, 102, 102))))
        );
        jMainPanelLayout.setVerticalGroup(
            jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jInstructionsLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jScholarCheckBox)
                    .addComponent(jStudentCheckBox))
                .addGap(18, 18, 18)
                .addComponent(jLoadSaveFilesButton)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLoadSaveFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLoadSaveFilesButtonActionPerformed
        this.actionScholars = this.jScholarCheckBox.isSelected();
        this.actionStudents = this.jStudentCheckBox.isSelected();
        this.dispose();
    }//GEN-LAST:event_jLoadSaveFilesButtonActionPerformed

    private void jScholarCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jScholarCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScholarCheckBoxActionPerformed

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
            java.util.logging.Logger.getLogger(FileLoadSaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileLoadSaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileLoadSaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileLoadSaveWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FileLoadSaveWindow dialog = new FileLoadSaveWindow(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jInstructionsLabel1;
    private javax.swing.JButton jLoadSaveFilesButton;
    private javax.swing.JPanel jMainPanel;
    private javax.swing.JCheckBox jScholarCheckBox;
    private javax.swing.JCheckBox jStudentCheckBox;
    // End of variables declaration//GEN-END:variables
}
