package View;

import Controller.LikertScaleDAO;
import Controller.Validate;
import Model.LikertQuestion;
import Model.STEMScholar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

/**
 *
 */
public class AddUpdateScholarWindow extends javax.swing.JDialog {

    private String[] raceOptions = {"Not Reported", "American Indian / Alaska Native", "Asian", "Black or African American", "Native Hawaiian / Other Pacific Islander", "White", "Hispanic or Latino"};
    private String[] ethnicityOptions = {"Not Reported", "Hispanic or Latino", "Not Hispanic or Latino"};
    private String[] disabilityOptions = {"Not Reported", "Yes", "No"};
    private String[] stateOptions = {"MA", "CT", "RI", "NH", "NY", "VT", "NJ", "ME", "PA"};
    private STEMScholar scholar = null;
    private LikertScaleDAO scale = new LikertScaleDAO();
    private LikertQuestion[] likert = scale.getScale();
    private int[] opinions = new int[likert.length];
    // Possible use to restore student fields on error when updating, may not use
    private STEMScholar updatingStudent = null;
    
    /**
     * Creates new form AddScholarWindow
     */
    public AddUpdateScholarWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Add / Update Scholar");
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
        for (String x : stateOptions)
        {
            this.jComboBoxState.addItem(x);
        }
        for (int i = 0; i < likert.length; i++)
        {
            this.jComboBoxQuestionSelection.addItem(Integer.toString(i+1));
        }
    }

    public STEMScholar getScholar()
    {
        return this.scholar;
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
        boolean birthError = false;
        boolean zipError = false;
        boolean streetError = false;
        boolean cityError = false;
        boolean stateError = false;  // May or may not implement stateError later
        boolean majorError = false;  // --- Set first list pos to empty, if choice is 0 set true
        boolean gpaError = false;
        boolean startError = false;
        boolean estGradError = false;
        boolean emailError = false;
        boolean phoneError = false;

        // Student attribute tracking variables
        String id = this.jTextFieldID.getText();
        String fName = this.jTextFieldfName.getText();
        String mName = this.jTextFieldmName.getText();
        String lName = this.jTextFieldlName.getText();
        String birth = this.jTextFieldBirthdate.getText();
        LocalDate birthDate = null;
        String gender = this.jTextFieldGender.getText();
        String race = (String) this.jComboBoxRace.getSelectedItem();
        String ethnicity = (String) this.jComboBoxEthnicity.getSelectedItem();
        String disability = (String) this.jComboBoxDisability.getSelectedItem();
        String zip = this.jTextFieldZip.getText();
        String street = this.jTextFieldStreet.getText();
        String city = this.jTextFieldCity.getText();
        String state = (String) this.jComboBoxState.getSelectedItem();
        String major = this.jTextFieldMajor.getText();
        double gpa = 0;
        String start = this.jTextFieldStart.getText();
        int estGrad = 0;
        String goal = this.jTextAreaGoal.getText();
        String employ = this.jTextFieldEmployment.getText();
        String hours = this.jTextFieldWeekHours.getText();
        String email = this.jTextFieldEmail.getText();
        String phone = this.jTextFieldPhone.getText();
        // If a new scholar is being added, set likert opinions to defaults values
        if (this.opinions.length == 0)
        {
            this.opinions = new int[this.likert.length];
            for (int i = 0; i < this.likert.length; i++)
            {
                this.opinions[i] = LikertQuestion.CONSTANT;
            }
        }
        // Validation on input from Validate class methods
        // If any attribute is not set correctly, increment error and set att's error to true
        if (!Validate.isID(id)) {error++; idError = true;}
        if (fName.isEmpty()) {error++; fNameError = true;}
        if (lName.isEmpty()) {error++; lNameError = true;}
        if (birth.isEmpty() || !Validate.isDelimited(birth, "/", 2))
        {
            error++; 
            birthError = true;
        }
        else
        {
            String[] birthTest = birth.split("/");
            if (!Validate.isInt(birthTest[0], 1, 12) || !Validate.isInt(birthTest[1], 1, 31)
                    || !Validate.isInt(birthTest[2], 1900, LocalDate.now().getYear() - 15))
            {
                error++; birthError = true;
            }
            else
            {
                birthDate = LocalDate.of(Integer.parseInt(birthTest[2]),
                        Integer.parseInt(birthTest[0]), 
                        Integer.parseInt(birthTest[1]));
            }
        }
        if (gender.isEmpty()) {error++; genderError = true;}
        if (zip.length() != 5 || !Validate.isInt(zip)) {error++; zipError = true;}
        if (street.isEmpty()) {error++; streetError = true;}
        if (city.isEmpty()) {error++; cityError = true;}
        if (major.isEmpty()) {error++; majorError = true;}
        if (Validate.isDouble(this.jTextFieldGpa.getText(), 0.1, 4.5))
        {
            gpa = Double.parseDouble(this.jTextFieldGpa.getText());
        } else {error++; gpaError = true;}
        if (!Validate.isDelimited(start, " ", 1) || start.isEmpty())
        {
            error++; 
            startError = true;
        } 
        else
        {
            String[] startTest = start.split(" ");
            String semester = startTest[0];
            String year = startTest[1];
            if (!semester.equals("Fall") && !semester.equals("Spring") || !Validate.isInt(year, 2019))
            {
                error++; 
                startError = true;
            }
        }
        if (Validate.isInt(this.jTextFieldEstGrad.getText(), 2015))
        {
            estGrad = Integer.parseInt(this.jTextFieldEstGrad.getText());
        } else {error++; estGradError = true;}
        if (email.isEmpty()) {error++; emailError = true;}
        if (Validate.isDelimited(phone, "-", 2))
        {
            String[] phoneTest = phone.split("-");
            if (phone.equals("555-555-5555"))
            {
                error++; 
                phoneError = true;
            }
            else if (!Validate.isInt(phoneTest[0]) || !Validate.isInt(phoneTest[1]) || !Validate.isInt(phoneTest[2]))
            {
                error++; 
                phoneError = true;
            }
        }
        else if (Validate.isInt(phone) && (phone.length() == 10))
        {
            String formattedPhone = "";
            formattedPhone += phone.substring(0,3) + "-" + phone.substring(3,6) + "-" + phone.substring(6,10);           
            phone = formattedPhone;
        } else {error++; phoneError = true;}
        // Check for errors.  If none create Scholar, else display errors
        if (error == 0)
        {    
            this.scholar = new STEMScholar(id, fName, mName, lName, gender, race, ethnicity, 
                    disability, zip, email, major, gpa, goal, birthDate, street, city, state, 
                    start, estGrad, employ, hours, phone, opinions);
            this.dispose();
        }
        else
        {
            // Open error window, display errors, reset error
            String[] errorMessages = {"\"ID\" is required.  Must be a 9 digit ID.\n\n",
                    "\"First Name\" is required.\n\n","\"Last Name\" is required.\n\n",
                    "\"Birthdate\" is required.  Must be a valid date in the form mm/dd/yyyy.\n\n",
                    "\"Gender\" is required.\n\n","\"Zipcode\" is required.  Must be a 5 digit zipcode.\n\n",
                    "\"Street\" is required.\n\n","\"City\" is required.\n\n",
                    "\"Major\" is required.\n\n","\"GPA\" is required.  Must be a two digit decimal between 0.1 and 4.5.\n\n",
                    "\"Quarter-Year Start\" is required.  Must be in the form \"Fall \" or \"Spring \" followed by a year past 2019.\n\n",
                    "\"Estimated Grad Year\" is required.  Must be a year past 2000.\n\n",
                    "\"Email\" is required.\n\n", "\"Phone\" is required in the form 555-555-5555 or as a 10 digit number.\n\n"};
            String message = "";
            if (idError) {message += errorMessages[0];}
            if (fNameError) {message += errorMessages[1];}
            if (lNameError) {message += errorMessages[2];}
            if (birthError) {message += errorMessages[3];}
            if (genderError) {message += errorMessages[4];}
            if (zipError) {message += errorMessages[5];}
            if (streetError) {message += errorMessages[6];}
            if (cityError) {message += errorMessages[7];}
            if (majorError) {message += errorMessages[8];}
            if (gpaError) {message += errorMessages[9];}
            if (startError) {message += errorMessages[10];}
            if (estGradError) {message += errorMessages[11];}
            if (emailError) {message += errorMessages[12];}
            if (phoneError) {message += errorMessages[13];}
            JOptionPane.showMessageDialog(rootPane, message, "Error!", 0);
            error = 0;
        }
    }
            
    public void updateScholar(STEMScholar s)
    {
        // Store saved Likert-scale responses for use at end of update
        this.opinions = s.getAttitude().getOpinions();
        // Set default selected Likert button to position of first stored response: (0)
        this.setLikertRadioButton(0);
        // Disable Reset buttons to stop fields from being accidently erased
        this.jButtonResetBasic.setEnabled(false);
        this.jButtonResetDemo.setEnabled(false);
        this.jButtonResetScholar.setEnabled(false);
        this.jButtonResetEmployment.setEnabled(false);
        this.jButtonResetContact.setEnabled(false);
        this.jButtonResetLikertResponses.setEnabled(false);
        
        // Set each Scholar attribute to its appropriate component
        this.jTextFieldID.setText(s.getStudentID());
        this.jTextFieldfName.setText(s.getfName());
        this.jTextFieldmName.setText(s.getmName());
        this.jTextFieldlName.setText(s.getlName());
        this.jTextFieldBirthdate.setText(s.getStringBirthDate());
        this.jTextFieldGender.setText(s.getGender());
        // Find scholars set race, set to race combo box
        for (String x : this.raceOptions)
        {
            if (s.getRace().equals(x))
            {
                this.jComboBoxRace.setSelectedItem(x);
            }  
        }
        // Find scholars set ethnicity, set to ethnicity combo box
        for (String x : this.ethnicityOptions)
        {
            if (s.getEthnicity().equals(x))
            {
                this.jComboBoxEthnicity.setSelectedItem(x);
            }
        }
        // Find scholars set disability, set to disability combo box
        for (String x : this.disabilityOptions)
        {
            if (s.getDisability().equals(x))
            {
                this.jComboBoxDisability.setSelectedItem(x);
            }
        }
        this.jTextFieldZip.setText(s.getAddressZip());
        this.jTextFieldStreet.setText(s.getAddressStreet());
        this.jTextFieldCity.setText(s.getAddressCity());
        // Find scholars set state, set to state combo box
        for (String x : this.stateOptions)
        {
            if (s.getAddressState().equals(x))
            {
                this.jComboBoxState.setSelectedItem(x);
            }
            
        }
        this.jTextFieldMajor.setText(s.getMajor());
        this.jTextFieldGpa.setText(Double.toString(s.getGpa()));
        this.jTextFieldStart.setText(s.getQuarterStart());
        this.jTextFieldEstGrad.setText(Integer.toString(s.getEstimatedGradYear()));
        this.jTextAreaGoal.setText(s.getCareerGoal());
        this.jTextFieldEmployment.setText(s.getEmployment());
        this.jTextFieldWeekHours.setText(s.getEmploymentHours());
        this.jTextFieldEmail.setText(s.getEmail());
        this.jTextFieldPhone.setText(s.getPhone());
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScholarTabbedPaneLikertResponse = new javax.swing.JTabbedPane();
        jPanelBasic = new javax.swing.JPanel();
        jLabelID = new javax.swing.JLabel();
        jLabelfName = new javax.swing.JLabel();
        jLabelmName = new javax.swing.JLabel();
        jLabellName = new javax.swing.JLabel();
        jLabelBirthdate = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jTextFieldfName = new javax.swing.JTextField();
        jTextFieldmName = new javax.swing.JTextField();
        jTextFieldlName = new javax.swing.JTextField();
        jTextFieldBirthdate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButtonResetBasic = new javax.swing.JButton();
        jLabelMessage = new javax.swing.JLabel();
        jButtonSubmit4 = new javax.swing.JButton();
        jPanelDemo = new javax.swing.JPanel();
        jLabelGender = new javax.swing.JLabel();
        jLabelRace = new javax.swing.JLabel();
        jLabelEthnicity = new javax.swing.JLabel();
        jLabelDisability = new javax.swing.JLabel();
        jLabelZip = new javax.swing.JLabel();
        jLabelStreet = new javax.swing.JLabel();
        jLabelCity = new javax.swing.JLabel();
        jLabelState = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldGender = new javax.swing.JTextField();
        jTextFieldCity = new javax.swing.JTextField();
        jTextFieldStreet = new javax.swing.JTextField();
        jTextFieldZip = new javax.swing.JTextField();
        jComboBoxRace = new javax.swing.JComboBox<>();
        jComboBoxEthnicity = new javax.swing.JComboBox<>();
        jComboBoxState = new javax.swing.JComboBox<>();
        jButtonResetDemo = new javax.swing.JButton();
        jComboBoxDisability = new javax.swing.JComboBox<>();
        jButtonSubmit3 = new javax.swing.JButton();
        jPanelScholar = new javax.swing.JPanel();
        jLabelMajor = new javax.swing.JLabel();
        jLabelGpa = new javax.swing.JLabel();
        jLabelStart = new javax.swing.JLabel();
        jLabelGoal = new javax.swing.JLabel();
        jLabelEstGrad = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaGoal = new javax.swing.JTextArea();
        jTextFieldMajor = new javax.swing.JTextField();
        jTextFieldGpa = new javax.swing.JTextField();
        jTextFieldStart = new javax.swing.JTextField();
        jTextFieldEstGrad = new javax.swing.JTextField();
        jButtonResetScholar = new javax.swing.JButton();
        jButtonSubmit2 = new javax.swing.JButton();
        jPanelEmployment = new javax.swing.JPanel();
        jLabelEmployment = new javax.swing.JLabel();
        jLabelWeekHours = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldEmployment = new javax.swing.JTextField();
        jTextFieldWeekHours = new javax.swing.JTextField();
        jButtonResetEmployment = new javax.swing.JButton();
        jButtonSubmit1 = new javax.swing.JButton();
        jPanelContact = new javax.swing.JPanel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelPhone = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldPhone = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jButtonSubmit = new javax.swing.JButton();
        jButtonResetContact = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelLikertResponses = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jComboBoxQuestionSelection = new javax.swing.JComboBox<>();
        jLabelQuestion = new javax.swing.JLabel();
        jRadioButtonResponse1 = new javax.swing.JRadioButton();
        jRadioButtonResponse2 = new javax.swing.JRadioButton();
        jRadioButtonResponse3 = new javax.swing.JRadioButton();
        jRadioButtonResponse4 = new javax.swing.JRadioButton();
        jRadioButtonResponse5 = new javax.swing.JRadioButton();
        jRadioButtonNoResponse = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaQuestionDisplay = new javax.swing.JTextArea();
        jButtonSubmitLikertResponses = new javax.swing.JButton();
        jButtonResetLikertResponses = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelID.setText("*ID:");

        jLabelfName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelfName.setText("*First Name:");

        jLabelmName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelmName.setText("Middle Name:");

        jLabellName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabellName.setText("*Last Name:");

        jLabelBirthdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelBirthdate.setText("*Birthdate:");

        jTextFieldBirthdate.setText("mm/dd/yyyy");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText(" Student Identification ");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
                        .addGap(143, 143, 143)
                        .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelMessage)
                            .addGroup(jPanelBasicLayout.createSequentialGroup()
                                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabellName)
                                    .addComponent(jLabelBirthdate)
                                    .addComponent(jLabelmName, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelfName)
                                    .addComponent(jLabelID))
                                .addGap(33, 33, 33)
                                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldmName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldfName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldlName, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelBasicLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(jLabel3)))
                        .addGap(0, 190, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelBasicLayout.setVerticalGroup(
            jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBasicLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelID)
                    .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelfName))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldmName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelmName))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldlName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabellName))
                .addGap(18, 18, 18)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBirthdate))
                .addGap(18, 18, 18)
                .addComponent(jLabelMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetBasic)
                    .addComponent(jButtonSubmit4))
                .addContainerGap())
        );

        jScholarTabbedPaneLikertResponse.addTab("Basic Information", jPanelBasic);

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

        jLabelStreet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelStreet.setText("*Street:");

        jLabelCity.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCity.setText("*City:");

        jLabelState.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelState.setText("*State:");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText(" Identity ");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText(" Address ");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxRace.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jComboBoxEthnicity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jComboBoxState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

        jButtonResetDemo.setText("Reset");
        jButtonResetDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetDemoActionPerformed(evt);
            }
        });

        jComboBoxDisability.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {}));

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
                        .addGap(21, 21, 21)
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addComponent(jLabelGender)
                                .addGap(16, 16, 16)
                                .addComponent(jTextFieldGender, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addComponent(jLabelDisability)
                                .addGap(14, 14, 14)
                                .addComponent(jComboBoxDisability, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelZip)
                            .addComponent(jLabelCity))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCity, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldZip, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDemoLayout.createSequentialGroup()
                                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelRace, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelEthnicity, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBoxRace, javax.swing.GroupLayout.Alignment.TRAILING, 0, 230, Short.MAX_VALUE)
                                    .addComponent(jComboBoxEthnicity, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(23, 23, 23))
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addComponent(jLabelState)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxState, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelDemoLayout.createSequentialGroup()
                                .addComponent(jLabelStreet)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(68, Short.MAX_VALUE))))
            .addGroup(jPanelDemoLayout.createSequentialGroup()
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelDemoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetDemo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit3)))
                .addContainerGap())
        );
        jPanelDemoLayout.setVerticalGroup(
            jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDemoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelGender)
                    .addComponent(jTextFieldGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRace)
                    .addComponent(jComboBoxRace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDisability)
                    .addComponent(jLabelEthnicity)
                    .addComponent(jComboBoxEthnicity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxDisability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelStreet)
                    .addComponent(jTextFieldStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelZip))
                .addGap(26, 26, 26)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelState)
                    .addComponent(jTextFieldCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanelDemoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetDemo)
                    .addComponent(jButtonSubmit3))
                .addContainerGap())
        );

        jScholarTabbedPaneLikertResponse.addTab("Demographic Data", jPanelDemo);

        jLabelMajor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelMajor.setText("*Major:");

        jLabelGpa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelGpa.setText("*GPA:");

        jLabelStart.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelStart.setText("*Program Quarter-Year Start:");

        jLabelGoal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelGoal.setText("Career Goal:");

        jLabelEstGrad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelEstGrad.setText("*Estimated Graduation Year:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText(" Program Information ");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextAreaGoal.setColumns(20);
        jTextAreaGoal.setLineWrap(true);
        jTextAreaGoal.setRows(5);
        jTextAreaGoal.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaGoal);

        jTextFieldGpa.setText("0.0");

        jTextFieldStart.setText("(ex. Fall 2020)");

        jTextFieldEstGrad.setText("(ex. 2022)");

        jButtonResetScholar.setText("Reset");
        jButtonResetScholar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetScholarActionPerformed(evt);
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
            .addGroup(jPanelScholarLayout.createSequentialGroup()
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetScholar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit2))
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelScholarLayout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                                        .addComponent(jLabelGoal)
                                        .addGap(31, 31, 31)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelScholarLayout.createSequentialGroup()
                                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabelMajor)
                                            .addComponent(jLabelGpa))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldMajor, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldGpa, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelStart)
                                            .addComponent(jLabelEstGrad))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldEstGrad, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                            .addComponent(jTextFieldStart)))))
                            .addGroup(jPanelScholarLayout.createSequentialGroup()
                                .addGap(213, 213, 213)
                                .addComponent(jLabel4)))
                        .addGap(0, 24, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelScholarLayout.setVerticalGroup(
            jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScholarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addGap(24, 24, 24)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelStart)
                    .addComponent(jTextFieldStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMajor)
                    .addComponent(jTextFieldMajor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstGrad)
                    .addComponent(jTextFieldEstGrad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldGpa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelGpa))
                .addGap(28, 28, 28)
                .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanelScholarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonResetScholar)
                            .addComponent(jButtonSubmit2)))
                    .addGroup(jPanelScholarLayout.createSequentialGroup()
                        .addComponent(jLabelGoal)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScholarTabbedPaneLikertResponse.addTab("Scholar Data", jPanelScholar);

        jLabelEmployment.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelEmployment.setText("Employment:");

        jLabelWeekHours.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelWeekHours.setText("Employment Weekly Hours:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText(" Employment Information ");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonResetEmployment.setText("Reset");
        jButtonResetEmployment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetEmploymentActionPerformed(evt);
            }
        });

        jButtonSubmit1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmit1.setText("Submit");
        jButtonSubmit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmit1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEmploymentLayout = new javax.swing.GroupLayout(jPanelEmployment);
        jPanelEmployment.setLayout(jPanelEmploymentLayout);
        jPanelEmploymentLayout.setHorizontalGroup(
            jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetEmployment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit1))
                    .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                        .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                                .addGap(205, 205, 205)
                                .addComponent(jLabel5))
                            .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelEmployment)
                                    .addComponent(jLabelWeekHours))
                                .addGap(33, 33, 33)
                                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldWeekHours, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(jTextFieldEmployment))))
                        .addGap(0, 153, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelEmploymentLayout.setVerticalGroup(
            jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEmploymentLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addGap(60, 60, 60)
                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmployment)
                    .addComponent(jTextFieldEmployment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelWeekHours)
                    .addComponent(jTextFieldWeekHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addGroup(jPanelEmploymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetEmployment)
                    .addComponent(jButtonSubmit1))
                .addContainerGap())
        );

        jScholarTabbedPaneLikertResponse.addTab("Employment", jPanelEmployment);

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelEmail.setText("*Email:");

        jLabelPhone.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelPhone.setText("*Phone:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText(" Contact Information ");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextFieldPhone.setText("555-555-5555");

        jButtonSubmit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmit.setText("Submit");
        jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitActionPerformed(evt);
            }
        });

        jButtonResetContact.setText("Reset");
        jButtonResetContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetContactActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelContactLayout = new javax.swing.GroupLayout(jPanelContact);
        jPanelContact.setLayout(jPanelContactLayout);
        jPanelContactLayout.setHorizontalGroup(
            jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContactLayout.createSequentialGroup()
                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelContactLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonResetContact)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSubmit))
                    .addGroup(jPanelContactLayout.createSequentialGroup()
                        .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelContactLayout.createSequentialGroup()
                                .addGap(210, 210, 210)
                                .addComponent(jLabel6))
                            .addGroup(jPanelContactLayout.createSequentialGroup()
                                .addGap(178, 178, 178)
                                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelEmail)
                                    .addComponent(jLabelPhone))
                                .addGap(34, 34, 34)
                                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 180, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelContactLayout.setVerticalGroup(
            jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContactLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addGap(57, 57, 57)
                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmail)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPhone)
                    .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addGroup(jPanelContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetContact)
                    .addComponent(jButtonSubmit))
                .addContainerGap())
        );

        jScholarTabbedPaneLikertResponse.addTab("Contact", jPanelContact);

        jLabelLikertResponses.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelLikertResponses.setText(" Likert Responses ");
        jLabelLikertResponses.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxQuestionSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxQuestionSelectionActionPerformed(evt);
            }
        });

        jLabelQuestion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelQuestion.setText(" Question: ");

        buttonGroup2.add(jRadioButtonResponse1);
        jRadioButtonResponse1.setText("1");
        jRadioButtonResponse1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonResponse1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButtonResponse2);
        jRadioButtonResponse2.setText("2");
        jRadioButtonResponse2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonResponse2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButtonResponse3);
        jRadioButtonResponse3.setText("3");
        jRadioButtonResponse3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonResponse3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButtonResponse4);
        jRadioButtonResponse4.setText("4");
        jRadioButtonResponse4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonResponse4ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButtonResponse5);
        jRadioButtonResponse5.setText("5");
        jRadioButtonResponse5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonResponse5ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButtonNoResponse);
        jRadioButtonNoResponse.setText("No Response ");
        jRadioButtonNoResponse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonNoResponseActionPerformed(evt);
            }
        });

        jTextAreaQuestionDisplay.setEditable(false);
        jTextAreaQuestionDisplay.setColumns(20);
        jTextAreaQuestionDisplay.setLineWrap(true);
        jTextAreaQuestionDisplay.setRows(5);
        jTextAreaQuestionDisplay.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextAreaQuestionDisplay);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jRadioButtonNoResponse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonResponse1)
                        .addGap(12, 12, 12)
                        .addComponent(jRadioButtonResponse2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonResponse3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonResponse4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonResponse5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelQuestion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jComboBoxQuestionSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxQuestionSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelQuestion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonNoResponse)
                    .addComponent(jRadioButtonResponse1)
                    .addComponent(jRadioButtonResponse2)
                    .addComponent(jRadioButtonResponse3)
                    .addComponent(jRadioButtonResponse4)
                    .addComponent(jRadioButtonResponse5))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jButtonSubmitLikertResponses.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButtonSubmitLikertResponses.setText("Submit");
        jButtonSubmitLikertResponses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitLikertResponsesActionPerformed(evt);
            }
        });

        jButtonResetLikertResponses.setText("Reset");
        jButtonResetLikertResponses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetLikertResponsesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(233, 233, 233)
                .addComponent(jLabelLikertResponses)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonResetLikertResponses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSubmitLikertResponses)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabelLikertResponses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSubmitLikertResponses, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonResetLikertResponses)))
        );

        jScholarTabbedPaneLikertResponse.addTab("Likert Responses", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScholarTabbedPaneLikertResponse, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScholarTabbedPaneLikertResponse)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonResetBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetBasicActionPerformed
        this.jTextFieldID.setText("");
        this.jTextFieldfName.setText("");
        this.jTextFieldmName.setText("");
        this.jTextFieldlName.setText("");
        this.jTextFieldBirthdate.setText("mm/dd/yyyy");
    }//GEN-LAST:event_jButtonResetBasicActionPerformed

    private void jButtonResetDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetDemoActionPerformed
        this.jTextFieldGender.setText("");
        this.jComboBoxRace.setSelectedIndex(0);
        this.jComboBoxEthnicity.setSelectedIndex(0);
        this.jComboBoxDisability.setSelectedIndex(0);
        this.jTextFieldZip.setText("");
        this.jTextFieldCity.setText("");
        this.jComboBoxState.setSelectedIndex(0);
        this.jTextFieldStreet.setText("");
    }//GEN-LAST:event_jButtonResetDemoActionPerformed

    private void jButtonResetScholarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetScholarActionPerformed
        this.jTextFieldMajor.setText("");
        this.jTextFieldGpa.setText("");
        this.jTextFieldStart.setText("");
        this.jTextFieldEstGrad.setText("");
        this.jTextAreaGoal.setText("");
    }//GEN-LAST:event_jButtonResetScholarActionPerformed

    private void jButtonResetEmploymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetEmploymentActionPerformed
        this.jTextFieldEmployment.setText("");
        this.jTextFieldWeekHours.setText("");
    }//GEN-LAST:event_jButtonResetEmploymentActionPerformed

    private void jButtonResetContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetContactActionPerformed
        this.jTextFieldEmail.setText("");
        this.jTextFieldPhone.setText("");
    }//GEN-LAST:event_jButtonResetContactActionPerformed

    private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmitActionPerformed

    private void jButtonSubmit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit1ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit1ActionPerformed

    private void jButtonSubmit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit2ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit2ActionPerformed

    private void jButtonSubmit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit3ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit3ActionPerformed

    private void jButtonSubmit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmit4ActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmit4ActionPerformed

    private void jRadioButtonResponse4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonResponse4ActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = 4;
    }//GEN-LAST:event_jRadioButtonResponse4ActionPerformed

    private void jRadioButtonResponse5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonResponse5ActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = 5;
    }//GEN-LAST:event_jRadioButtonResponse5ActionPerformed

    private void jRadioButtonNoResponseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonNoResponseActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = LikertQuestion.CONSTANT;
    }//GEN-LAST:event_jRadioButtonNoResponseActionPerformed

    private void jButtonResetLikertResponsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetLikertResponsesActionPerformed
        this.jComboBoxQuestionSelection.setSelectedIndex(0);
        // Reset each question's response to the default value constant
        for(int i = 0; i < this.opinions.length; i++)
        {
            this.opinions[i] = LikertQuestion.CONSTANT;
        }
    }//GEN-LAST:event_jButtonResetLikertResponsesActionPerformed

    private void jComboBoxQuestionSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxQuestionSelectionActionPerformed
        int pos = this.jComboBoxQuestionSelection.getSelectedIndex();
        this.jTextAreaQuestionDisplay.setText(
                likert[pos].getQuestion());
        setLikertRadioButton(pos);
    }//GEN-LAST:event_jComboBoxQuestionSelectionActionPerformed

    private void setLikertRadioButton(int pos)
    {
        // Look at current opinions array given position, set selected button to appropriate stored response
        switch(this.opinions[pos])
        {
            case (LikertQuestion.CONSTANT):
                this.buttonGroup2.setSelected(this.jRadioButtonNoResponse.getModel(), true);
                break;
            case 1:
                this.buttonGroup2.setSelected(this.jRadioButtonResponse1.getModel(), true);
                break;
            case 2:
                this.buttonGroup2.setSelected(this.jRadioButtonResponse2.getModel(), true);
                break;
            case 3:
                this.buttonGroup2.setSelected(this.jRadioButtonResponse3.getModel(), true);
                break;
            case 4:
                this.buttonGroup2.setSelected(this.jRadioButtonResponse4.getModel(), true);
                break;
            case 5:
                this.buttonGroup2.setSelected(this.jRadioButtonResponse5.getModel(), true);
                break;
            default:
                String message = "Error auto-selecting button based on stored response value.";
                JOptionPane.showMessageDialog(rootPane, message, "Error!", 0);
        }// end switch/case for Likert buttonGroup selection
    }// end setLikertRadioButton
    
    private void jButtonSubmitLikertResponsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSubmitLikertResponsesActionPerformed
        submit();
    }//GEN-LAST:event_jButtonSubmitLikertResponsesActionPerformed

    private void jRadioButtonResponse3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonResponse3ActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = 3;
    }//GEN-LAST:event_jRadioButtonResponse3ActionPerformed

    private void jRadioButtonResponse2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonResponse2ActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = 2;
    }//GEN-LAST:event_jRadioButtonResponse2ActionPerformed

    private void jRadioButtonResponse1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonResponse1ActionPerformed
        this.opinions[this.jComboBoxQuestionSelection.getSelectedIndex()] = 1;
    }//GEN-LAST:event_jRadioButtonResponse1ActionPerformed

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
            java.util.logging.Logger.getLogger(AddUpdateScholarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUpdateScholarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUpdateScholarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUpdateScholarWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddUpdateScholarWindow dialog = new AddUpdateScholarWindow(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonResetBasic;
    private javax.swing.JButton jButtonResetContact;
    private javax.swing.JButton jButtonResetDemo;
    private javax.swing.JButton jButtonResetEmployment;
    private javax.swing.JButton jButtonResetLikertResponses;
    private javax.swing.JButton jButtonResetScholar;
    private javax.swing.JButton jButtonSubmit;
    private javax.swing.JButton jButtonSubmit1;
    private javax.swing.JButton jButtonSubmit2;
    private javax.swing.JButton jButtonSubmit3;
    private javax.swing.JButton jButtonSubmit4;
    private javax.swing.JButton jButtonSubmitLikertResponses;
    private javax.swing.JComboBox<String> jComboBoxDisability;
    private javax.swing.JComboBox<String> jComboBoxEthnicity;
    private javax.swing.JComboBox<String> jComboBoxQuestionSelection;
    private javax.swing.JComboBox<String> jComboBoxRace;
    private javax.swing.JComboBox<String> jComboBoxState;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelBirthdate;
    private javax.swing.JLabel jLabelCity;
    private javax.swing.JLabel jLabelDisability;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEmployment;
    private javax.swing.JLabel jLabelEstGrad;
    private javax.swing.JLabel jLabelEthnicity;
    private javax.swing.JLabel jLabelGender;
    private javax.swing.JLabel jLabelGoal;
    private javax.swing.JLabel jLabelGpa;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelLikertResponses;
    private javax.swing.JLabel jLabelMajor;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelPhone;
    private javax.swing.JLabel jLabelQuestion;
    private javax.swing.JLabel jLabelRace;
    private javax.swing.JLabel jLabelStart;
    private javax.swing.JLabel jLabelState;
    private javax.swing.JLabel jLabelStreet;
    private javax.swing.JLabel jLabelWeekHours;
    private javax.swing.JLabel jLabelZip;
    private javax.swing.JLabel jLabelfName;
    private javax.swing.JLabel jLabellName;
    private javax.swing.JLabel jLabelmName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBasic;
    private javax.swing.JPanel jPanelContact;
    private javax.swing.JPanel jPanelDemo;
    private javax.swing.JPanel jPanelEmployment;
    private javax.swing.JPanel jPanelScholar;
    private javax.swing.JRadioButton jRadioButtonNoResponse;
    private javax.swing.JRadioButton jRadioButtonResponse1;
    private javax.swing.JRadioButton jRadioButtonResponse2;
    private javax.swing.JRadioButton jRadioButtonResponse3;
    private javax.swing.JRadioButton jRadioButtonResponse4;
    private javax.swing.JRadioButton jRadioButtonResponse5;
    private javax.swing.JTabbedPane jScholarTabbedPaneLikertResponse;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextAreaGoal;
    private javax.swing.JTextArea jTextAreaQuestionDisplay;
    private javax.swing.JTextField jTextFieldBirthdate;
    private javax.swing.JTextField jTextFieldCity;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldEmployment;
    private javax.swing.JTextField jTextFieldEstGrad;
    private javax.swing.JTextField jTextFieldGender;
    private javax.swing.JTextField jTextFieldGpa;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldMajor;
    private javax.swing.JTextField jTextFieldPhone;
    private javax.swing.JTextField jTextFieldStart;
    private javax.swing.JTextField jTextFieldStreet;
    private javax.swing.JTextField jTextFieldWeekHours;
    private javax.swing.JTextField jTextFieldZip;
    private javax.swing.JTextField jTextFieldfName;
    private javax.swing.JTextField jTextFieldlName;
    private javax.swing.JTextField jTextFieldmName;
    // End of variables declaration//GEN-END:variables
}
