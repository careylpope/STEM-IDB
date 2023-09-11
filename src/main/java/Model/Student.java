package Model;

import java.util.Comparator;

/**
 * Abstract Class Student holds attributes of all potential students
 *     - studentID as a String
 *         holds a students 9 digit ID
 *     - fName as a String
 *         holds student first name
 *     - mName as a String
 *         holds student middle name
 *     - lName as a String
 *         holds student last name
 *     - gender as a String
 *         holds gender identity of student
 *     - race as a String
 *         holds a students race, defaults to "Not Reported" if not given
 *     - ethnicity as a String
 *         holds a students ethnicity, defaults to "Not Reported" if not given
 *     - disability as a String
 *         holds a students disability, defaults to "Not Reported" if not given
 *     - addressZipcode as a String
 *         holds a zipcode of the students registered address
 *     - email as a String
 *         holds students email address
 *     - major as a String 
 *         holds registered Major
 *     - gpa as a double
 *         holds students cumulative gpa
 *     - careerGoal as a String
 *         holds polled careerGoal, defaults to "Not Reported" if not given
 * Create constructor that requires all attributes, getters/setters for all att's
 *     - called in child classes to simplify POJO creation 
 * Override toString to return a simple formatted Student, used further in child classes
 *     - save studentID to string variable r
 *     - if Student middle name is empty, 
 *           concat only first and last name, add to r 
 *     - else, 
 *           concat first name, middle initial and last name, add to r
 *     - return r
 */
public class Student
{
    private String StudentID;
    private String fName;
    private String mName;
    private String lName;
    private String fullName;
    private String gender;
    private String race = "Not Reported";
    private String ethnicity = "Not Reported";
    private String disability = "Not Reported";
    private String addressZip;
    private String email;
    private String major;
    private double gpa;
    private String careerGoal = "Not Reported";

    public Student(String StudentID, String fName, String mName, String lName, 
            String gender, String race, String ethnicity, String disability,
            String addressZip, String email, String major, double gpa,
            String careerGoal) 
    {
        this.StudentID = StudentID;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        if (this.mName.isEmpty())
        {
            this.fullName = this.fName + " " + this.lName;
        }
        else
        {
            this.fullName = this.fName + " " + this.mName.substring(0, 1) + " " + this.lName;
        }
        this.gender = gender;
        if (!race.isEmpty())
        {
            this.race = race;
        }
        if (!ethnicity.isEmpty())
        {
            this.ethnicity = ethnicity;
        }
        if (!disability.isEmpty())
        {
            this.disability = disability;
        }
        this.addressZip = addressZip;
        this.email = email;
        this.major = major;
        this.gpa = gpa;
        if (!careerGoal.isEmpty())
        {
            this.careerGoal = careerGoal;
        }
    }// End of Abstract Student Constructor

    // Getters/Setters for all stored Attributes
    
    // Getter/Setter: StudentID (Student's 9 Digit School ID)
    public String getStudentID() 
    {
        return StudentID;
    }
    public void setStudentID(String StudentID) 
    {
        this.StudentID = StudentID;
    }

    // Getter/Setter: fName (First Name)
    public String getfName() 
    {
        return fName;
    }
    public void setfName(String fName) 
    {
        this.fName = fName;
    }
    
    // Getter/Setter: mName (Middle Name)
    public String getmName() 
    {
        return mName;
    }
    public void setmName(String mName) 
    {
        this.mName = mName;
    }
    
    // Getter/Setter: lName (Last Name)
    public String getlName() 
    {
        return lName;
    }
    public void setlName(String lName) 
    {
        this.lName = lName;
    }

    // Getter/Setter: fullName (Students full name)
    public String getFullName()
    {
        return this.fullName;
    }
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    
    // Getter/Setter: gender (Identified Gender)
    public String getGender() 
    {
        return gender;
    }
    public void setGender(String gender) 
    {
        this.gender = gender;
    }

        // Getter/Setter: race (Student Race - Default is "Not Reported")
    public String getRace() 
    {
        return race;
    }
    public void setRace(String race) 
    {
        this.race = race;
    }

    // Getter/Setter: ethnicity (Student Ethnicity - Default is "Not Reported")
    public String getEthnicity() 
    {
        return ethnicity;
    }
    public void setEthnicity(String ethnicity) 
    {
        this.ethnicity = ethnicity;
    }

    // Getter/Setter: disability (Student Disability - Default is "Not Reported")
    public String getDisability() 
    {
        return disability;
    }
    public void setDisability(String disability) 
    {
        this.disability = disability;
    }
    
    // Getter/Setter: addressZipcode (Zipcode of Registered Address)
    public String getAddressZip() 
    {
        return addressZip;
    }
    public void setAddressZip(String addressZip) 
    {
        this.addressZip = addressZip;
    }
    
    // Getter/Setter: email (Email Address of Student)
    public String getEmail() 
    {
        return email;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }
    
    // Getter/Setter: major (Registered Major)
    public String getMajor() 
    {
        return major;
    }
    public void setMajor(String major) 
    {
        this.major = major;
    } 
    
    // Getter/Setter: gpa (Cumulative GPA)
    public double getGpa() 
    {
        return gpa;
    }
    public void setGpa(double gpa) 
    {
        this.gpa = gpa;
    }

    // Getter/Setter: careerGoal (Career Goals of Student)
    public String getCareerGoal() 
    {
        return careerGoal;
    }
    public void setCareerGoal(String careerGoal) 
    {
        this.careerGoal = careerGoal;
    }
    // End of Getters/Setters

    // Method to return a formatted String object of revelevant Student info
    @Override
    public String toString()
    {
        String r = "Student ID:   " + this.getStudentID();
        r += "\nName:          " + this.getFullName() + "\n";
        return r;
    }// End toString
}// End Abstract Student Class
