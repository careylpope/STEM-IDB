package View;

import Controller.Validate;
import Model.STEMStudent;
import javax.swing.JOptionPane;

/**
 *
 */
public class AddUpdateStudentWindow extends javax.swing.JDialog {

    private String[] raceOptions = {"Not Reported", "American Indian / Alaska Native", "Asian", "Black or African American", "Native Hawaiian / Other Pacific Islander", "White", "Hispanic or Latino"};
    private String[] ethnicityOptions = {"Not Reported", "Hispanic or Latino", "Not Hispanic or Latino"};
    private String[] disabilityOptions = {"Not Reported", "Yes", "No"};
    private STEMStudent student = null;
    // Possible use to restore student fields on error when updating, may not use
    private STEMStudent updatingStudent = null;
    
    private static final int MONTH = 0;
    private static final int DAY = 1;
    private static final int YEAR = 2;
    
    /**
     * Creates new form AddScholarWindow
     */
    public AddUpdateStudentWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Add / Update Student");
        for (String x : raceOptions)
        {
            this.jComboBoxRace.addItem(x);
        }
        for (String x : ethnicityOptions)
        {
            this.jComboBoxEthnicity.addItem(x);
        }
        for (String x : disabilityOptions)
        {
            this.jComboBoxDisability.addItem(x);
        }
    }

    public STEMStudent getStudent()
    {
        return this.student;
    }
    
    public void submit()
    {
        // Variable Declaration, handles scope issues
        // Error tracking variables
        int error = 0;
        boolean idError = false;
        boolean fNameError = false;
        boolean lNameError = false;
        boolean genderError = false;
        boolean zipError = false;
        boolean majorError = false;
        boolean gpaError = false;
        boolean emailError = false;
        
        // Student attribute tracking variables
        String id = this.jTextFieldID.getText();
        String fName = this.jTextFieldfName.getText();
        String mName = this.jTextFieldmName.getText();
        String lName = this.jTextFieldlName.getText();
        String gender = this.jTextFieldGender.getText();
        String race = (String) this.jComboBoxRace.getSelectedItem();
        String ethnicity = (String) this.jComboBoxEthnicity.getSelectedItem();
        String disability = (String) this.jComboBoxDisability.getSelectedItem();
        String zip = this.jTextFieldZip.getText();
        String major = this.jTextFieldMajor.getText();
        double gpa = 0;
        String goal = this.jTextAreaGoal.getText();
        String email = this.jTextFieldEmail.getText();
        
        // Validation on input from Validate class methods
        // If any attribute is not set correctly, increment error and set att's error to true
        if (!Validate.isID(id)) {error++; idError = true;}
        if (fName.isEmpty()) {error++; fNameError = true;}
        if (lName.isEmpty()) {error++; lNameError = true;}
        if (gender.isEmpty()) {error++; genderError = true;}
        if (zip.length() != 5 || !Validate.isInt(zip)) {error++; zipError = true;}
        if (major.isEmpty()) {error++; majorError = true;}
        if (Validate.isDouble(this.jTextFieldGpa.getText(), 0.1, 4.5))
        {
            gpa = Double.parseDouble(this.jTextFieldGpa.getText());
        } else {error++; gpaError = true;}
        if (email.isEmpty()) {error++; emailError = true;}
        if (error == 0)
        {   
            int[] i = {};
            this.student = new STEMStudent(id, fName, mName, lName, gender, race, ethnicity, 
                    disability, zip, email, major, gpa, goal);
            this.dispose();
        }
        else
        {
            // Open error window, display errors, reset error
            String[] errorMessages = {"\"ID\" is required.  Must be a 9 digit ID.\n\n",
                    "\"First Name\" is required.\n\n","\"Last Name\" is required.\n\n",
                    "\"Gender\" is required.\n\n",
                    "\"Zipcode\" is required. Must be a 5 digit zipcode.\n\n",
                    "\"Email\" is required.\n\n","\"Major\" is required.\n\n",
                    "\"GPA\" is required.  Must be a two digit decimal between 0.1 and 4.5.\n\n"};
            String message = "";
            if (idError) {message += errorMessages[0];}
            if (fNameError) {message += errorMessages[1];}
            if (lNameError) {message += errorMessages[2];}
            if (genderError) {message += errorMessages[3];}
            if (zipError) {message += errorMessages[4];}
            if (emailError) {message += errorMessages[5];}
            if (majorError) {message += errorMessages[6];}
            if (gpaError) {message += errorMessages[7];}
            JOptionPane.showMessageDialog(rootPane, message, "Error!", 0);
            error = 0;
        }
    }
    
    public void updateStudent(STEMStudent s)
    {
        this.updatingStudent = s;
        // Disable Reset buttons to stop fields from being accidently erased
        this.jButtonResetBasic.setEnabled(false);
        this.jButtonResetDemo.setEnabled(false);
        this.jButtonResetDegree.setEnabled(false);
        
        // Set each Student attribute to its appropriate component
        this.jTextFieldID.setText(s.getStudentID());
        this.jTextFieldfName.setText(s.getfName());
        this.jTextFieldmName.setText(s.getmName());
        this.jTextFieldlName.setText(s.getlName());
        this.jTextFieldGender.setText(s.getGender());
        // Find student set race, set to race combo box
        for (String x : this.raceOptions)
        {
            if (s.getRace().equals(x))
            {
                this.jComboBoxRace.setSelectedItem(x);
            }  
        }
        // Find student set ethnicity, set to ethnicity combo box
        for (String x : this.ethnicityOptions)
        {
            if (s.getEthnicity().equals(x))
            {
                this.jComboBoxEthnicity.setSelectedItem(x);
            }
        }
        // Find student set disability, set to disability combo box
        for (String x : this.disabilityOptions)
        {
            if (s.getDisability().equals(x))
            {
                this.jComboBoxDisability.setSelectedItem(x);
            }
        }
        this.jTextFieldZip.setText(s.getAddressZip());
        this.jTextFieldMajor.setText(s.getMajor());
        this.jTextFieldGpa.setText(Double.toString(s.getGpa()));
        this.jTextAreaGoal.setText(s.getCareerGoal());
        this.jTextFieldEmail.setText(s.getEmail());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScholarTabbedPane = new javax.swing.JTabbedPane();
        jPanelBasic = new javax.swing.JPanel();
        jLabelID = new javax.swing.JLabel();
        jLabelfName = new javax.swing.JLabel();
        jLabelmName = new javax.swing.JLabel();
        jLabellName = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jTextFieldfName = new javax.swing.JTextField();
        jTextFieldmName = new javax.swing.JTextField();
        jTextFieldlName = new javax.swing.JTextField();
        jLabelBasic = new javax.swing.JLabel();
        jButtonResetBasic = new javax.swing.JButton();
        jLabelMessage = new javax.swing.JLabel();
        jButtonSubmit4 = new javax.swing.JButton();
        jPanelDemo = new javax.swing.JPanel();
        jLabelGender = new javax.swing.JLabel();
        jLabelRace = new javax.swing.JLabel();
        jLabelEthnicity = new javax.swing.JLabel();
        jLabelDisability = new javax.swing.JLabel();
        jLabelZip = new javax.swing.JLabel();
        jLabelIdentity = new javax.swing.JLabel();
        jLabelAddress = new javax.swing.JLabel();
        jTextFieldGender = new javax.swing.JTextField();
        jTextFieldZip = new javax.swing.JTextField();
        jComboBoxRace = new javax.swing.JComboBox<>();
        jComboBoxEthnicity = new javax.swing.JComboBox<>();
        jButtonResetDemo = new javax.swing.JButton();
        jComboBoxDisability = new javax.swing.JComboBox<>();
        jLabelEmail = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelContact = new javax.swing.JLabel();
        jButtonSubmit3 = new javax.swing.JButton();
        jPanelScholar = new javax.swing.JPanel();
        jLabelMajor = new javax.swing.JLabel();
        jLabelGpa = new javax.swing.JLabel();
        jLabelGoal = new javax.swing.JLabel();
        jLabelProgramInfo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaGoal = new javax.swing.JTextArea();
        jTextFieldMajor = new javax.swing.JTextField();
        jTextFieldGpa = new javax.swing.JTextField();
        jButtonResetDegree = new javax.swing.JButton();
        jButtonSubmit2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelID.setText("*ID:");

        jLabelfName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelfName.setText("*First Name:");

        jLabelmName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelmName.setText("Middle Name:");

        jLabellName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabellName.setText("*Last Name:");

        jLabelBasic.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelBasic.setText(" Student Identification ");
        jLabelBasic.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonResetBasic.setText("Reset");
        jButtonResetBasic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetBasicActionPerformed(evt);
            }
        });

        jLabelMessage.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabelMessage.setText(" Fields in Bold Must be Entered! ");

        jButtonSubmit4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmit4.setText("Submit");
        jButtonSubmit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmit4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBasicLayout = new javax.swing.GroupLayout(jPanelBasic);
        jPanelBasic.setLayout(jPanelBasicLayout);
        jPanelBasicLayout.setHorizontalGroup(
            jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBasicLayout.createSequentialGroup()
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelBasicLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetBasic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit4))
                    .addGroup(jPanelBasicLayout.createSequentialGroup()
                        .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelBasicLayout.createSequentialGroup()
                                .addGap(204, 204, 204)
                                .addComponent(jLabelBasic))
                            .addGroup(jPanelBasicLayout.createSequentialGroup()
                                .addGap(143, 143, 143)
                                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelMessage)
                                    .addGroup(jPanelBasicLayout.createSequentialGroup()
                                        .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabellName)
                                            .addComponent(jLabelmName, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelfName)
                                            .addComponent(jLabelID))
                                        .addGap(33, 33, 33)
                                        .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldfName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldlName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldmName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 190, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelBasicLayout.setVerticalGroup(
            jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBasicLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabelBasic)
                .addGap(29, 29, 29)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelID))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelfName)
                    .addComponent(jTextFieldfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldmName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmName))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldlName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabellName))
                .addGap(31, 31, 31)
                .addComponent(jLabelMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetBasic)
                    .addComponent(jButtonSubmit4))
                .addContainerGap())
        );

        jScholarTabbedPane.addTab("Basic Information", jPanelBasic);

        jLabelGender.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelGender.setText("*Gender:");

        jLabelRace.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelRace.setText("Race:");

        jLabelEthnicity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelEthnicity.setText("Ethnicity:");

        jLabelDisability.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDisability.setText("Disability:");

        jLabelZip.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelZip.setText("*Zipcode:");

        jLabelIdentity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelIdentity.setText(" Identity ");
        jLabelIdentity.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelAddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelAddress.setText(" Address ");
        jLabelAddress.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxRace.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jComboBoxEthnicity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jButtonResetDemo.setText("Reset");
        jButtonResetDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetDemoActionPerformed(evt);
            }
        });

        jComboBoxDisability.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelEmail.setText("*Email:");

        jLabelContact.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelContact.setText(" Contact Information ");
        jLabelContact.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonSubmit3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmit3.setText("Submit");
        jButtonSubmit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmit3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDemoLayout = new javax.swing.GroupLayout(jPanelDemo);
        jPanelDemo.setLayout(jPanelDemoLayout);
        jPanelDemoLayout.setHorizontalGroup(
            jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDemoLayout.createSequentialGroup()
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetDemo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit3))
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                                .addComponent(jLabelGender)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextFieldGender, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(58, 58, 58)
                                                .addComponent(jLabelRace))
                                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                                .addGap(216, 216, 216)
                                                .addComponent(jLabelIdentity))
                                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                                .addComponent(jLabelDisability)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBoxDisability, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabelEthnicity))))
                                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                                        .addGap(136, 136, 136)
                                        .addComponent(jLabelAddress)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxEthnicity, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxRace, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDemoLayout.createSequentialGroup()
                                        .addComponent(jLabelContact)
                                        .addGap(63, 63, 63))))
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabelZip)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldZip, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(67, 67, 67)
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelDemoLayout.setVerticalGroup(
            jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDemoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelIdentity)
                .addGap(18, 18, 18)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGender)
                    .addComponent(jTextFieldGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRace)
                    .addComponent(jComboBoxRace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDisability)
                    .addComponent(jLabelEthnicity)
                    .addComponent(jComboBoxEthnicity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxDisability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAddress)
                    .addComponent(jLabelContact))
                .addGap(19, 19, 19)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelZip)
                    .addComponent(jTextFieldZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEmail)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetDemo)
                    .addComponent(jButtonSubmit3))
                .addContainerGap())
        );

        jScholarTabbedPane.addTab("Demographic Data", jPanelDemo);

        jLabelMajor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelMajor.setText("*Major:");

        jLabelGpa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelGpa.setText("*GPA:");

        jLabelGoal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelGoal.setText("Career Goal:");

        jLabelProgramInfo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelProgramInfo.setText(" Degree Information ");
        jLabelProgramInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextAreaGoal.setColumns(20);
        jTextAreaGoal.setLineWrap(true);
        jTextAreaGoal.setRows(5);
        jTextAreaGoal.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaGoal);

        jTextFieldGpa.setText("0.0");

        jButtonResetDegree.setText("Reset");
        jButtonResetDegree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetDegreeActionPerformed(evt);
            }
        });

        jButtonSubmit2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmit2.setText("Submit");
        jButtonSubmit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelScholarLayout = new javax.swing.GroupLayout(jPanelScholar);
        jPanelScholar.setLayout(jPanelScholarLayout);
        jPanelScholarLayout.setHorizontalGroup(
            jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelScholarLayout.createSequentialGroup()
                .addContainerGap(225, Short.MAX_VALUE)
                .addComponent(jLabelProgramInfo)
                .addGap(221, 221, 221))
            .addGroup(jPanelScholarLayout.createSequentialGroup()
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addGap(223, 223, 223)
                        .addComponent(jLabelGpa)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldGpa, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabelGoal)
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jLabelMajor)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMajor, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelScholarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonResetDegree)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSubmit2)
                .addContainerGap())
        );
        jPanelScholarLayout.setVerticalGroup(
            jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScholarLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelProgramInfo)
                .addGap(18, 18, 18)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMajor)
                    .addComponent(jTextFieldMajor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldGpa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelGpa))
                .addGap(28, 28, 28)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonResetDegree)
                            .addComponent(jButtonSubmit2)))
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addComponent(jLabelGoal)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScholarTabbedPane.addTab("Degree", jPanelScholar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScholarTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScholarTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSubmit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit2ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit2ActionPerformed

    private void jButtonResetDegreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetDegreeActionPerformed
        this.jTextFieldMajor.setText("");
        this.jTextFieldGpa.setText("");
        this.jTextAreaGoal.setText("");
    }//GEN-LAST:event_jButtonResetDegreeActionPerformed

    private void jButtonSubmit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit3ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit3ActionPerformed

    private void jButtonResetDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetDemoActionPerformed
        this.jTextFieldGender.setText("");
        this.jComboBoxRace.setSelectedIndex(0);
        this.jComboBoxEthnicity.setSelectedIndex(0);
        this.jComboBoxDisability.setSelectedIndex(0);
        this.jTextFieldZip.setText("");
        this.jTextFieldEmail.setText("");
    }//GEN-LAST:event_jButtonResetDemoActionPerformed

    private void jButtonSubmit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit4ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit4ActionPerformed

    private void jButtonResetBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetBasicActionPerformed
        this.jTextFieldID.setText("");
        this.jTextFieldfName.setText("");
        this.jTextFieldmName.setText("");
        this.jTextFieldlName.setText("");
    }//GEN-LAST:event_jButtonResetBasicActionPerformed

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
            java.util.logging.Logger.getLogger(AddUpdateStudentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUpdateStudentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUpdateStudentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUpdateStudentWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddUpdateStudentWindow dialog = new AddUpdateStudentWindow(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonResetBasic;
    private javax.swing.JButton jButtonResetDegree;
    private javax.swing.JButton jButtonResetDemo;
    private javax.swing.JButton jButtonSubmit2;
    private javax.swing.JButton jButtonSubmit3;
    private javax.swing.JButton jButtonSubmit4;
    private javax.swing.JComboBox<String> jComboBoxDisability;
    private javax.swing.JComboBox<String> jComboBoxEthnicity;
    private javax.swing.JComboBox<String> jComboBoxRace;
    private javax.swing.JLabel jLabelAddress;
    private javax.swing.JLabel jLabelBasic;
    private javax.swing.JLabel jLabelContact;
    private javax.swing.JLabel jLabelDisability;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEthnicity;
    private javax.swing.JLabel jLabelGender;
    private javax.swing.JLabel jLabelGoal;
    private javax.swing.JLabel jLabelGpa;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelIdentity;
    private javax.swing.JLabel jLabelMajor;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelProgramInfo;
    private javax.swing.JLabel jLabelRace;
    private javax.swing.JLabel jLabelZip;
    private javax.swing.JLabel jLabelfName;
    private javax.swing.JLabel jLabellName;
    private javax.swing.JLabel jLabelmName;
    private javax.swing.JPanel jPanelBasic;
    private javax.swing.JPanel jPanelDemo;
    private javax.swing.JPanel jPanelScholar;
    private javax.swing.JTabbedPane jScholarTabbedPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaGoal;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldGender;
    private javax.swing.JTextField jTextFieldGpa;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldMajor;
    private javax.swing.JTextField jTextFieldZip;
    private javax.swing.JTextField jTextFieldfName;
    private javax.swing.JTextField jTextFieldlName;
    private javax.swing.JTextField jTextFieldmName;
    // End of variables declaration//GEN-END:variables
}
