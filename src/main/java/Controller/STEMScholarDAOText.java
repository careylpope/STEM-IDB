package Controller;

import Model.LikertQuestion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.Map;
import Model.Student;
import Model.STEMScholar;
import java.time.LocalDate;

/**
 * 
 */
public class STEMScholarDAOText implements STEMScholarDAO
{
    private TreeMap<String,Student> students = new TreeMap<>();
    private Path filePath1 = Paths.get("src/main/java/resources/stemScholars.txt");
    private File stemScholarFile = filePath1.toFile();
    private int[] opinionsList;
    private LikertQuestion[] likert = new LikertScaleDAO().getScale();
    
    // CONSTANST FOR ALL STUDENTS
    private static final String DELIMITER = "`";
    private static final int STUDENTID = 0;
    private static final int FNAME = 1;
    private static final int MNAME = 2;
    private static final int LNAME = 3;
    private static final int GENDER = 4;
    private static final int RACE = 5;
    private static final int ETHNICITY = 6;
    private static final int DISABILITY = 7;
    private static final int ZIP = 8;
    private static final int EMAIL = 9;
    private static final int MAJOR = 10;
    private static final int GPA = 11;
    private static final int CAREERGOAL = 12;
    
    // CONSTANTS FOR STEMSCHOLAR
    private static final int BIRTHDATE = 13;
    private static final int STREET = 14;
    private static final int CITY = 15;
    private static final int STATE = 16;
    private static final int QUARTERSTART = 17;
    private static final int ESTIMATEDGRADYEAR = 18;
    private static final int EMPLOYMENT = 19;
    private static final int EMPLOYMENTHOURS = 20;
    private static final int PHONE = 21;
    private static final int OPINIONS = 22;
    
    // Constructor that connects to a student text file, calls getAll()
    public STEMScholarDAOText()
    {
        this.students = getAll();   
    }
    
    // Method to return a TreeMap of Students within a text file
    @Override
    public final TreeMap<String,Student> getAll() 
    {
        TreeMap<String,Student> students = new TreeMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(this.stemScholarFile)))
        {
            String line = reader.readLine();
            while(line != null)
            {
                // Break each line into scholar attributes on the delimiter
                String[] scholar = line.split(DELIMITER);
                String id = scholar[STUDENTID];
                String fName = scholar[FNAME];
                String mName = scholar[MNAME];
                String lName = scholar[LNAME];
                String gender = scholar[GENDER];
                String race = scholar[RACE];
                String ethnicity = scholar[ETHNICITY];
                String disability = scholar[DISABILITY];
                String zip = scholar[ZIP];
                String email = scholar[EMAIL];
                String major = scholar[MAJOR];
                Double gpa = Double.parseDouble(scholar[GPA]);
                String careerGoal = scholar[CAREERGOAL];
                // string parsed must be formatted as "yyyy-mm-dd"
                LocalDate birthDate = LocalDate.parse(scholar[BIRTHDATE]);
                String street = scholar[STREET];
                String city = scholar[CITY];
                String state = scholar[STATE];
                String quarterStart = scholar[QUARTERSTART];
                int estGradYear = Integer.parseInt(scholar[ESTIMATEDGRADYEAR]);
                String employment = scholar[EMPLOYMENT];
                String employmentHours = scholar[EMPLOYMENTHOURS];
                String phone = scholar[PHONE];
                String[] opinions = scholar[OPINIONS].split(",");
                this.opinionsList = new int[likert.length];
                int responsePos = 0;
                // Builds opinionsList with stored scholar likert responses
                for (int i = 0; i < opinions.length; i++)
                {
                    responsePos++;
                    this.opinionsList[i] = Integer.parseInt(opinions[i]);
                }
                // Fills in extra space from unanswered likert responses with
                //   LikertQuestions constant value
                while (responsePos < likert.length)
                {
                    this.opinionsList[responsePos] = LikertQuestion.CONSTANT;
                    responsePos++;
                }
                // call STEMScholar constructor, pass attributes
                STEMScholar stemScholar = new STEMScholar(id, fName, mName, lName, 
                        gender, race, ethnicity, disability, zip, email, major, 
                        gpa, careerGoal, birthDate, street, city, state, quarterStart, 
                        estGradYear, employment, employmentHours, phone, opinionsList);
                // add to TreeMap, set Student ID as the key
                students.put(id, stemScholar);
                line = reader.readLine();
            }// End while loop
        }
        catch(IOException e)
        {
            System.out.println("ERROR! Could not connect to stemScholar file.  Message: " + e.getMessage());
        }
        // Return TreeMap
        return students;
    }
    
    // Method to return a TreeMap of Scholars
    @Override
    public TreeMap<String,Student> getMap()
    {
        return this.students;
    }
    
    // Method to add a new Student with assigned Student ID
    @Override
    public boolean addStudent(Student s) 
    {
        for (Map.Entry student: this.students.entrySet())
        {
            // If student ID matches given ID, do not add and return false
            if(student.getKey().equals(s.getStudentID()))
            {
                return false;
            }  
        }
        // If for loop is succesfully completed, add student, save and return true
        students.put(s.getStudentID(), s);
        this.save();
        return true;
    }

    // Method to delete a Student with matching Student ID
    @Override
    public boolean deleteStudent(String id) 
    {
       for (Map.Entry student: this.students.entrySet())
        {
            // If student ID matches given ID, delete key/value pair and return true
            if(student.getKey().equals(id))
            {
                students.remove(id);
                this.save();
                return true;
            }  
        }
        // Return false if a student with matching ID could not be found
        return false;
    }

    // Method to update a Student with matching Student ID
    @Override
    public boolean updateStudent(String id, Student s) 
    {
        // If student ID was unchanged
        if (id.equals(s.getStudentID()))
        {
            for (Map.Entry student: this.students.entrySet())
            {
                // If student ID matches given ID, overwrite value and return true
                if(student.getKey().equals(id))
                {
                    student.setValue(s);
                    this.save();
                    return true;
                }  
            }
        }
        // Else, check new ID doesn't belond to another student
        else
        {
            boolean check = true;
            for (Map.Entry student: this.students.entrySet())
            {
                // If student ID matches given ID, overwrite value and return true
                if(student.getKey().equals(s.getStudentID()))
                {
                    check = false;
                }
            }
            if (check)
            {
                for (Map.Entry student: this.students.entrySet())
                {   
                    // If student ID matches given ID, overwrite value and return true
                    if(student.getKey().equals(id))
                    {
                        student.setValue(s);
                        this.save();
                        return true;
                    }  
                }
            }
        }
        // Return false if a student with matching ID could not be found
        return false;
    }

    // Method to return a Student with matching provided Student ID
    @Override
    public Student getStudent(String id) 
    {
        for (Map.Entry student: this.students.entrySet())
        {
            // If student ID matches given ID, return Student
            if(student.getKey().equals(id))
            {
                return (Student) student.getValue();
            }    
        }
        // Return null if Student was not found 
        return null;
    }
    
    // Method to save STEMScholars back into STEMScholars text file
    @Override
    public void save() 
    {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this.stemScholarFile))))
        {
            // Move through student key/value pairs in students
            for(Map.Entry curStudent : students.entrySet())
            {
                // Assign current student value to a new Student variable
                Student student = (Student) curStudent.getValue();
                // Check if student is a STEMScholar
                if (student instanceof STEMScholar)
                {
                    // Append each attribute to a String variable in the files format
                    STEMScholar s = (STEMScholar) student;
                    int[] opinions = new int[s.getAttitude().getOpinions().length];
                    for (int i = 0; i < opinions.length; i++)
                    {
                        opinions[i] = s.getAttitude().getOpinions()[i];
                    }
                    String result = "";
                    result += s.getStudentID() + DELIMITER + s.getfName() 
                            + DELIMITER + s.getmName() + DELIMITER + s.getlName() 
                            + DELIMITER + s.getGender() + DELIMITER + s.getRace()
                            + DELIMITER + s.getEthnicity() + DELIMITER + s.getDisability() 
                            + DELIMITER + s.getAddressZip() + DELIMITER + s.getEmail() 
                            + DELIMITER + s.getMajor() + DELIMITER + Double.toString(s.getGpa()) 
                            + DELIMITER + s.getCareerGoal() + DELIMITER + s.getBirthDate() 
                            + DELIMITER + s.getAddressStreet() + DELIMITER + s.getAddressCity()
                            + DELIMITER + s.getAddressState() + DELIMITER + s.getQuarterStart()
                            + DELIMITER + Integer.toString(s.getEstimatedGradYear())
                            + DELIMITER + s.getEmployment() + DELIMITER + s.getEmploymentHours() 
                            + DELIMITER + s.getPhone() + DELIMITER;
                    for (int i = 0; i < opinions.length; i++)
                    {
                        if (i == (opinions.length - 1))
                        {
                            result += opinions[i] + "\n";
                        }
                        else
                        {
                            result += opinions[i] + ",";
                        }
                    }
                    // Write the STEMScholar to the file
                    writer.write(result);
                }// End if statement checking students object type
            }// End for loop iterating through students
        }// End try block for stemScholarFile
        catch(IOException e)
        {
            System.out.println("ERROR! Could not update stemScholar file.  Message: " + e.getMessage());
        }
    }// End Save Method
} // End STEMScholarDAOText
