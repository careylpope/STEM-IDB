package Controller;

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
import Model.STEMStudent;

/**
 * TODO: Catch somewhere if nothing is passed to Year/Month/Day for Attitude
 */
public class STEMStudentDAOText implements STEMStudentDAO
{
    private TreeMap<String,Student> students = new TreeMap<>();
    private Path filePath1 = Paths.get("src/main/java/resources/stemStudents.txt");
    private File stemStudentFile = filePath1.toFile();
    private int[] opinionsList; // not currently used for students
    
    // CONSTANTS FOR ALL STUDENTS
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
    
    // Constructor that connects to a student text file, calls getAll()
    public STEMStudentDAOText()
    {
        this.students = getAll();
    }
    
    // Method to return a TreeMap of Students within a text file
    @Override
    public final TreeMap<String,Student> getAll() 
    {
        TreeMap<String,Student> students = new TreeMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(this.stemStudentFile)))
        {
            //-- move through the file
            String line = reader.readLine();
            while(line != null)
            {
                //-----for each line, break it into parts on the delimit
                String[] student = line.split(DELIMITER);
                String id = student[STUDENTID];
                String fName = student[FNAME];
                String mName = student[MNAME];
                String lName = student[LNAME];
                String gender = student[GENDER];
                String race = student[RACE];
                String ethnicity = student[ETHNICITY];
                String disability = student[DISABILITY];
                String zip = student[ZIP];
                String email = student[EMAIL];
                String major = student[MAJOR];
                double gpa = Double.parseDouble(student[GPA]);
                String careerGoal = student[CAREERGOAL];
                // Call STEMStudent constructor, pass attributes
                STEMStudent stemStudent = new STEMStudent(id, fName, mName, lName, 
                        gender, race, ethnicity, disability, zip, email, major, gpa, 
                        careerGoal);
                // Add to TreeMap, set Student ID as key
                students.put(id, stemStudent);
                line = reader.readLine();
            }// End while loop
        }
        catch(IOException e)
        {
            System.out.println("ERROR! Could not connect to stemStudent file.  Message: " + e.getMessage());
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
        // If Student ID was unchanged
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
        // Else, check new ID doesn't belong to another student
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
    
    // Method to save STEMStudents back to STEMStudents file
    @Override
    public void save() 
    {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this.stemStudentFile))))
        {
            // Move through student key/value pairs in students
            for(Map.Entry curStudent : students.entrySet())
            {
                // Assign current student value to a new Student variable
                Student student = (Student) curStudent.getValue();
                // Check if student is a STEMStudent
                if (student instanceof STEMStudent)
                {
                    // Append each attribute to a String variable in the files format
                    STEMStudent s = (STEMStudent) student;
                    String result = "";
                    result += s.getStudentID() + DELIMITER + s.getfName() 
                            + DELIMITER + s.getmName() + DELIMITER + s.getlName() 
                            + DELIMITER + s.getGender() + DELIMITER + s.getRace()
                            + DELIMITER + s.getEthnicity() + DELIMITER + s.getDisability() 
                            + DELIMITER + s.getAddressZip() + DELIMITER + s.getEmail() 
                            + DELIMITER + s.getMajor() + DELIMITER + Double.toString(s.getGpa()) 
                            + DELIMITER + s.getCareerGoal() + "\n";
                    // Write the STEMStudent to the file
                    writer.write(result);
                }// End if statement checking students object type
            }// End for loop iterating through Students
        }// End try block for stemStudentFile
        catch(IOException e)
        {
            System.out.println("ERROR! Could not update stemStudent file.  Message: " + e.getMessage());
        }
    }// End Save Method
} // End SSTEMDAOText
