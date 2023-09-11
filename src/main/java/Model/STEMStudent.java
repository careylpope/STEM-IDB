package Model;

/**
 * Class STEMStudent that is a child of Student with additional attributes
 *     - attitude as a String
 *         holds a STEM Student's reported attitude about personal learning,
 *         studying, teaching and mentoring experience at HCC within their major
 *             defaults to "Not Reported" if not given
 * Constructor that builds upon Student Constructor, getter/setter for attitude
 * Override toString of Abstract Student Class
 *     - call toString of Student, save to r
 *     - append remaining attributes of STEMStudent to r
 *     - return r
 */
public class STEMStudent extends Student
{
    public STEMStudent(String StudentID, String fName, String mName, String lName, 
            String gender, String race, String ethnicity, String disability, 
            String addressZipcode, String email, String major, double gpa, 
            String careerGoal) 
    {
        super(StudentID, fName, mName, lName, gender, race, ethnicity, disability, 
                addressZipcode, email, major, gpa, careerGoal);
    }// End of STEMStudent Constructor
    
    // Method to return a formatted String object of revelevant STEM Student info
    @Override
    public String toString()
    {
        String r = super.toString();
        r += "\nGender:        " + this.getGender();
        r += "\nRace:           " + this.getRace();
        r += "\nEthnicity:      " + this.getEthnicity();
        r += "\nDisability:     " + this.getDisability();
        r += "\n\nZip Code:      " + this.getAddressZip();
        r += "\nEmail:           " + this.getEmail();
        r += "\n\nIntended Major:     " + this.getMajor();
        r += "\nCumulative GPA:   " + this.getGpa();
        r += "\n\nCareer Goal:   " + this.getCareerGoal();
        return r;
    }// End of toString
}// End of STEMStudent Class
