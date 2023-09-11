/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.LikertQuestion;
import Model.STEMStudent;
import Model.Student;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class STEMStudentDAOExcel that creates a TreeMap from an excel workbook
 *   NOTE:  While this DAO reads from an Excel workbook, it only saves back to
 *          the local resources text file or an entirely new excel workbook.  
 *          This is done to preserve the integrity of data stored in the Excel 
 *          workbook and ensure that it does not get accidentally overwritten.  
 *          As the developer of this project I advise against changing this 
 *          functionality, and that if you do choose to, you use the utmost 
 *          caution when saving back into the Excel workbook.
 * 
 *   NOTE:  This requires the appache POI dependency to work with Excel files.
 * 
 * Attributes:
 *     - filePath1 as a Path object
 *          - stores the path to the location of the text file to save data to
 *     - stemStudentFile as a File object
 *          - stores the text file to save data back to
 *     - excelFile as a String
 *          - stores the path to the local excel workbook file that is used for
 *            for input stream
 *          NOTE: While this path could be overwritten with an excel workbook 
 *                path on your own machine, I recommend you take careful note 
 *                of the formatting of this workbook.
 *     - inputStream as a FileInputStream
 *          - stores the FileInputStream generated from the excelFile path
 *     - studentRows as an int
 *          - stores the number of rows with student data in the students sheet
 *          - NOTE:  No longer used.  Old frame work to manage still exists.  
 *                   To use later, re-add as additional condition for first 
 *                   while loop within getAll() that manages populating the
 *                   scholar treeMap.
 */
public class STEMStudentDAOExcel implements STEMStudentDAO
{
    private TreeMap<String,Student> students = new TreeMap<>();
    private final Path filePath1 = Paths.get("src/main/java/resources/stemStudents.txt");
    private final File stemStudentFile = filePath1.toFile();
    private final String excelFile = "src/main/java/resources/stemDatabaseWorkbook.xlsx";
    private final String saveExcelFile = "SavedExcelSheets/studentsSheet.xlsx";
    private static final String[] headers = {"ID", "FIRST NAME","MIDDLE NAME","LAST NAME",
        "GENDER","RACE","ETHNICITY","DISABILITY","ZIPCODE","EMAIL","MAJOR","GPA","CAREERGOAL"};
    private FileInputStream inputStream;
    private int studentRows = 10;
    
    // NOT CURRENTLY USED
    //private LikertQuestion[] likert = new LikertScaleDAO().getScale();
    //private int[] opinionsList;
    
    // CONSTANTS FOR ALL STUDENTS
    private static final String DELIMITER = "`";
    private static final int DEFCAP = 1000;
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
    
    // CONSTANT FOR SIZE OF STEMstudent DATA
        // While opinions are counted as their own attribute
        //   size is total field constants - 1
        // Opinions not in use: size = total constants
    private static final int SIZE = 13;
    
    // CONSTANT FOR WORKBOOK SHEET FOR studentS
    private static final int SHEETNUM = 1;
 
    // Constructor that connects to a student text file, calls getAll()
    public STEMStudentDAOExcel(int totalStudents)
    {
        this.studentRows = totalStudents;
        this.students = getAll();
    }
    
    public STEMStudentDAOExcel()
    {
        this(DEFCAP);
    }
    
    // Method to return a TreeMap of Students within a text file
    @Override
    public final TreeMap<String,Student> getAll() 
    {
        TreeMap<String,Student> students = new TreeMap<>();
        try
        {
            this.inputStream = new FileInputStream(this.excelFile);
            Workbook workbook = new XSSFWorkbook(this.inputStream);
            Sheet sheet = workbook.getSheetAt(SHEETNUM);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();  // skips over header row
            int rowsTravelled = 0; // keeps track of how many rows until all students read
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                rowsTravelled++;
                boolean skipRow = false;
                if (row.getCell(0).getCellType() == CellType.BLANK)
                {
                    skipRow = true;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                String[] student = new String[SIZE];
                int listPos = -1;
                while (cellIterator.hasNext() && !(listPos >= student.length) && !skipRow)
                {
                    Cell cell = cellIterator.next();
                    listPos++;
                    // if current cell is ID, format correctly as 9 digit ID lead by 0's
                    if (listPos == STUDENTID)
                    {
                        if (cell.getCellType() == CellType.STRING)
                        {
                            student[listPos] = cell.getStringCellValue();
                        }
                        else if (cell.getCellType() == CellType.NUMERIC)
                        {
                            String tempID = Integer.toString((int) cell.getNumericCellValue());
                            if (tempID.length() < 9)
                            {
                                int toAppend = 9 - tempID.length();
                                String startID = "";
                                for (int i = 0; i < toAppend; i++)
                                {
                                    startID += "0";
                                }
                                student[listPos] = startID + tempID;
                            }
                            else 
                            {
                                student[listPos] = Integer.toString((int) cell.getNumericCellValue());
                            }
                        }// end if check for numeric ID
                    }
                    else if (listPos == GPA)
                    {
                        student[listPos] = Double.toString(cell.getNumericCellValue());
                    }
                    else
                    {
                        student[listPos] = cell.getStringCellValue();
                    }
                }// end while loop for cells in row
                // if row was "empty" (had no student ID)
                if (!skipRow)
                {
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
                } // end if check for skipping empty row
            }// end while loop for rows in sheet
        }// end of try
        catch(IOException e)
        {
            System.out.println("ERROR! Could not connect to stemStudent file.  Message: " + e.getMessage());
        }
        // Return TreeMap
        return students;
    }
    
    // Method to return a TreeMap of students
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
    
    public void saveNewExcelSheet(TreeMap<String, Student> students) 
            throws SaveStudentToSheetException
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet studentSheet = workbook.createSheet("Students");
        int rowCount = 0;
        int colCount = 0;
        
        // Populate row of header labels
        Row row = studentSheet.createRow(rowCount++);
        for(String label : headers)
        {
            Cell cell = row.createCell(colCount++);
            cell.setCellValue(label);
        }
        
        for (Map.Entry curStudent : students.entrySet())
        {
            row = studentSheet.createRow(rowCount++);
            colCount = 0; // Reset column position to 0 for each row
            STEMStudent student = (STEMStudent) curStudent.getValue();
            
            // Create object array of student data, size of SIZE
            Object[] studentData = new Object[SIZE];
            studentData[STUDENTID] = student.getStudentID();
            studentData[FNAME] = student.getfName();
            studentData[MNAME] = student.getmName();
            studentData[LNAME] = student.getlName();
            studentData[GENDER] = student.getGender();
            studentData[RACE] = student.getRace();
            studentData[ETHNICITY] = student.getEthnicity();
            studentData[DISABILITY] = student.getDisability();
            studentData[ZIP] = student.getAddressZip();
            studentData[EMAIL] = student.getEmail();
            studentData[MAJOR] = student.getMajor();
            studentData[GPA] = student.getGpa();
            studentData[CAREERGOAL] = student.getCareerGoal();

            // Fill current row's columns with student data
            for (Object data : studentData)
            {
                Cell cell = row.createCell(colCount++);
                if (data instanceof String)
                {
                    cell.setCellValue((String) data);
                }
                else if(data instanceof Double)
                {
                    cell.setCellValue((Double) data);
                }
                else if(data == null)
                {
                    // Purposefully do nothing, cannot add null data to cell
                    //     nature of loop passes over cell slot, no need to add
                    //     empty string or something similar
                }
                else
                {
                    String message = "Error!  Could not determine type of " + data 
                            + " to be added to row/column " + rowCount + "/" + colCount
                            + " for student ID: " + student.getStudentID();
                    throw new SaveStudentToSheetException(message);
                }
            }// end for loop for populating columns of studentData in curRow   
        }// end for loop for populating rows of students within given TreeMap
        try (FileOutputStream outputStream = new FileOutputStream(saveExcelFile)) 
        {
            workbook.write(outputStream);
        }
        catch(IOException e)
        {
            System.out.println("ERROR! Could not save stemStudent file.  Message: " + e.getMessage());
        }
    }// end saveNewExcelSheet
    
    // Private helper class for throwing saveStudentToSheetException errors specific to this class
    private class SaveStudentToSheetException extends Exception
    {
        public SaveStudentToSheetException(String errorMessage)
        {
            super(errorMessage);
        }
    }
    
    // Getter/Setter: studentRows
    public int getStudentRows()
    {
        return this.studentRows;
    }
    public void setStudentRows(int rows)
    {
        this.studentRows = rows;
    }
}// end STEMStudentDAOExcel
