package Model;

import java.time.LocalDate;

/**
 * Class STEMScholar that is a child of Student with additional attributes
 *     - birthDate as a LocalDate object
 *         holds students birth date as a LocalDate object, work with using LocalDate methods
 *     - addressStreet as a String
 *         holds student registered street address
 *     - addressCity as a String
 *         holds city of students registered address
 *     - addressState as a String
 *         holds state of students registered address
 *     - quarterStart as a String
 *         holds period student joined S-STEM program in format (season semester/yyyy)
 *     - estimatedGradYear as an int
 *         holds students estimated year of graduation
 *     - employment as a String
 *         holds students place/type of employment, defaults to "Not Reported"
 *     - employmentHours as a String
 *         holds students employment hours per week, defaults to "Not Reported"
 *     - phone as a String
 *         holds student phone number
 * Constructor that builds upon Student Constructor, getters/setters for new attributes
 * Override toString of Abstract Student Class
 *     - call toString of Student, save to r
 *     - append remaining attributes of STEMScholar to r
 *     - return r
 */
public class STEMScholar extends Student
{  
    private final int TOTAL_FIELDS = 23;
    private LocalDate birthDate;
    private String addressStreet;
    private String addressCity;
    private String addressState;
    private String quarterStart;
    private int estimatedGradYear;
    private String employment = "Not Reported";
    private String employmentHours = "Not Reported";
    private String phone;
    private Attitude attitude;
    
    public STEMScholar(String StudentID, String fName, String mName, String lName, 
            String gender, String race, String ethnicity, String disability,
            String addressZipcode, String email, String major, double gpa, 
            String careerGoal, LocalDate birthDate, String addressStreet, 
            String addressCity, String addressState, String quarterStart, 
            int estimatedGradYear, String employment, String employmentHours, 
            String phone, int[] opinions) 
    {
        super(StudentID, fName, mName, lName, gender, race, ethnicity, disability, 
                addressZipcode, email, major, gpa, careerGoal);
        this.birthDate = birthDate;
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.quarterStart = quarterStart;
        this.estimatedGradYear = estimatedGradYear;
        if (!employment.isEmpty())
        {
            this.employment = employment;
        }
        if (!employment.isEmpty())
        {
            this.employmentHours = employmentHours;
        }
        this.phone = phone;
        this.attitude = new Attitude(opinions);
    }// End of STEMScholar Constructor

    // Getters/Setters for all stored Attributes
    
    // Getter/Setter: birthDate (Students Birth Date in Format "mm/dd/yyyy")
    public LocalDate getBirthDate() 
    {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) 
    {
        this.birthDate = birthDate;
    }
    // Return birthDate in the form of a string as mm/dd/yyyy
    public String getStringBirthDate()
    {
        String r = "";
        r += this.getBirthDate().getMonthValue() + "/"
                + this.getBirthDate().getDayOfMonth() + "/"
                + this.getBirthDate().getYear();
        return r;
    }
    
    // Getter/Setter: addressStreet (Registered Street Address)
    public String getAddressStreet() 
    {
        return addressStreet;
    }
    public void setAddressStreet(String addressStreet) 
    {
        this.addressStreet = addressStreet;
    }

    // Getter/Setter: addressCity (City of Registered Address)
    public String getAddressCity() 
    {
        return addressCity;
    }
    public void setAddressCity(String addressCity) 
    {
        this.addressCity = addressCity;
    }

    // Getter/Setter: addressState (State of Registered Address)
    public String getAddressState() 
    {
        return addressState;
    }
    public void setAddressState(String addressState) 
    {
        this.addressState = addressState;
    }

    // Getter/Setter: quarterStart (Quarter Joined S-STEM Progam, i.e. "Fall 2021")
    public String getQuarterStart() 
    {
        return quarterStart;
    }
    public void setQuarterStart(String quarterStart) 
    {
        this.quarterStart = quarterStart;
    }

    // Getter/Setter: estimatedGradYear (Students Estimated Year of Graduation)
    public int getEstimatedGradYear() 
    {
        return estimatedGradYear;
    }
    public void setEstimatedGradYear(int estimatedGradYear) 
    {
        this.estimatedGradYear = estimatedGradYear;
    }

    // Getter/Setter: employment (Students Current Employment - Default is "Not Reported")
    public String getEmployment() 
    {
        return employment;
    }
    public void setEmployment(String employment) 
    {
        this.employment = employment;
    }

    // Getter/Setter: employmentHours (Employment Hours/Week - Default is "Not Reported")
    public String getEmploymentHours() 
    {
        return employmentHours;
    }
    public void setEmploymentHours(String employmentHours) 
    {
        this.employmentHours = employmentHours;
    }

    // Getter/Setter: phone (Students Phone Number)
    public String getPhone() 
    {
        return phone;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }
    
    // Getter/Setter: attitude (Students stored Likert-scale survey responses)
    public Attitude getAttitude() 
    {
        return attitude;
    }
    public void setAttitude(Attitude attitude) 
    {
        this.attitude = attitude;
    }
    // End of Getters/Setters  
    
    // Method to return a formatted String object of revelevant STEM Scholar info
    @Override
    public String toString()
    {
        String border1 = "\n---------------------------------------"
                + "-------------------------------------";
        String border2 = "\n==================================================\n";
        String r = super.toString();
        r += "Birth Date:    " + this.getStringBirthDate();
        r += border1;
        r += "\nGender:       " + this.getGender();
        r += "\nRace:          " + this.getRace();
        r += "\nEthnicity:     " + this.getEthnicity();
        r += "\nDisability:    " + this.getDisability();
        r += border1;
        r += "\nAddress:      " + this.getAddressStreet() + ", " + this.getAddressCity()  + ", " + this.getAddressState() + " " + this.getAddressZip();
        r += "\nEmail:          " + this.getEmail();
        r += "\nPhone:         " + this.getPhone();
        r+= border1;
        r += "\nIntended Major:               " + this.getMajor();
        r += "\nCumulative GPA:            " + this.getGpa();
        r += "\nEstimated Grad Year:      " + this.getEstimatedGradYear();
        r += "\nS-STEM Start Quarter:   " + this.getQuarterStart();
        r += border1;
        r += "\nEmployment:   " + this.getEmployment();
        r += "\nHours/Week:   " + this.getEmploymentHours();
        r += "\n\nCareer Goal:   " + this.getCareerGoal() + "\n";
        r += border2;
        r += this.attitude;
        return r;
    }// End of toString   
}// End of STEMScholar Class
