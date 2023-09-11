/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.LikertQuestion;
import Model.STEMScholar;
import Model.Student;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
 * Class STEMScholarDAOExcel that creates a TreeMap from an excel workbook
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
 *     - stemScholarFile as a File object
 *          - stores the text file to save data back to
 *     - excelFile as a String
 *          - stores the path to the local excel workbook file that is used for
 *            for input stream
 *          NOTE: While this path could be overwritten with an excel workbook path
 *                on your own machine, I recommend you take careful note of the
 *                formatting of this workbook.
 *     - inputStream as a FileInputStream
 *          - stores the FileInputStream generated from the excelFile path
 *     - scholarRows as an int
 *          - stores the number of rows with scholar data in the Scholars sheet
 *          - NOTE:  No longer used.  Old frame work to manage still exists.  
 *                   To use later, re-add as additional condition for first 
 *                   while loop within getAll() that manages populating the
 *                   scholar treeMap.
 *     - likert as an array of LikertQuestion's
 *          - stores the array returned from getScale of the LikertScaleDAO
 *            used to correctly fill all likert values with data from scholars
 *            and unanswered questions with the constant value in LikertQuestion
 *     - opinionsList as an array of int's
 *          - stores the responses to each likert question of each scholar
 *            length calculated by likert length, i.e. number of questions in
 *            questions text file in the resources package
 */
public class STEMScholarDAOExcel implements STEMScholarDAO
{
    private TreeMap<String,Student> students = new TreeMap<>();
    private final Path filePath1 = Paths.get("src/main/java/resources/stemScholars.txt");
    private final File stemScholarFile = filePath1.toFile();
    private final String excelFile = "src/main/java/resources/stemDatabaseWorkbook.xlsx";
    private final String saveExcelFile = "SavedExcelSheets/scholarsSheet.xlsx";
    private FileInputStream inputStream;
    private static final String[] headers = {"ID","FIRST NAME","MIDDLE NAME","LAST NAME",
        "GENDER","RACE","ETHNICITY","DISABILITY","ZIPCODE","EMAIL","MAJOR","GPA","CAREERGOAL",
        "BIRTHDATE","STREET","CITY","STATE","QUARTER START","EST GRAD YEAR","EMPLOYMENT",
        "HOURS","PHONE","LIKERT RESPONSES (RESPONSES IN ASCENDING QUESTION ORDER)"};
    private int scholarRows = 10;  // TODO:  POSSIBLY REMOVE, NO LONGER NEEDED
    private final LikertQuestion[] likert = new LikertScaleDAO().getScale();
    private int[] opinionsList = new int[likert.length];
    
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
    private static final int TOTAL_CONSTANTS = 23;
    
    // CONSTANT FOR SIZE OF STEMSCHOLAR DATA
        // While opinions are counted as their own attribute
        //   size is total field constants - 1
    private static final int SIZE = TOTAL_CONSTANTS - 1;
    
    // CONSTANT FOR WORKBOOK SHEET FOR SCHOLARS
    private static final int SHEETNUM = 0;
    
    // Constructor that connects to a student text file, calls getAll()
    public STEMScholarDAOExcel(int totalScholars)
    {
        this.scholarRows = totalScholars;
        this.students = getAll();
    }
    
    public STEMScholarDAOExcel()
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
            int rowsTravelled = 0; // keeps track of how many rows until all scholars read
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                rowsTravelled++;
                boolean skipRow = false;
                // checks for empty row, avoids null scholars if more given than
                //   available in sheet
                if (row.getCell(0).getCellType() == CellType.BLANK)
                {
                    skipRow = true;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                String[] scholar = new String[SIZE];
                LocalDate birthDate = LocalDate.EPOCH;
                int responsePos = 0;
                int listPos = -1;
                while (cellIterator.hasNext() && !skipRow)
                {
                    Cell cell = cellIterator.next();
                    listPos++;
                    // if current cell is ID, format correctly as 9 digit ID lead by 0's
                    if (listPos == STUDENTID)
                    {
                        if (cell.getCellType() == CellType.STRING)
                        {
                            scholar[listPos] = cell.getStringCellValue();
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
                                scholar[listPos] = startID + tempID;
                            }
                            else 
                            {
                                scholar[listPos] = Integer.toString((int) cell.getNumericCellValue());
                            }
                        }// end if check for numeric ID
                    }
                    // if current cell is GPA or Grad Year, get its numeric value
                    else if (listPos == GPA || listPos == ESTIMATEDGRADYEAR)
                    {
                        scholar[listPos] = Double.toString(cell.getNumericCellValue());
                    }
                    // if current cell is a date object
                    else if (listPos == BIRTHDATE)
                    {
                        scholar[listPos] = "";
                        birthDate = cell.getLocalDateTimeCellValue().toLocalDate();
                    }
                    // if current cell is a likert response cell, get its numeric value
                    else if (listPos >= scholar.length)
                    {
                        if (cell.getCellType() == CellType.STRING)
                        {
                            this.opinionsList[responsePos] = Integer.parseInt(cell.getStringCellValue());
                        }
                        else if(cell.getCellType() == CellType.NUMERIC)
                        {
                            this.opinionsList[responsePos] = (int) cell.getNumericCellValue();
                        }
                        responsePos++;
                    }
                    // get current cells string value
                    else
                    {
                        scholar[listPos] = cell.getStringCellValue();
                    }
                }// end while loop for cells in row
                // if row was not "empty" (had no student ID)
                if (!skipRow)
                {
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
                    String street = scholar[STREET];
                    String city = scholar[CITY];
                    String state = scholar[STATE];
                    String quarterStart = scholar[QUARTERSTART];
                    int estGradYear = (int) Double.parseDouble(scholar[ESTIMATEDGRADYEAR]);
                    String employment = scholar[EMPLOYMENT];
                    String employmentHours = scholar[EMPLOYMENTHOURS];
                    String phone = scholar[PHONE];
                    // Fills in extra space from unanswered likert responses with
                    //   LikertQuestions constant value
                    while (responsePos < likert.length)
                    {
                        this.opinionsList[responsePos] = LikertQuestion.CONSTANT;
                        responsePos++;
                    }
                    // throw an error if birthDate was never overwritten
                    if (birthDate.isEqual(LocalDate.EPOCH))
                    {
                        throw new BirthDateException("Birth Date for student #" + id
                                + " was unreadable.  Cancelling operation.");
                    }
                    // call STEMScholar constructor, pass attributes
                    STEMScholar stemScholar = new STEMScholar(id, fName, mName, lName, 
                            gender, race, ethnicity, disability, zip, email, major, 
                            gpa, careerGoal, birthDate, street, city, state, quarterStart, 
                            estGradYear, employment, employmentHours, phone, opinionsList);
                    // add to TreeMap, set Student ID as the key
                    students.put(id, stemScholar);
                    // empty/reset current opinionsList
                    this.opinionsList = new int[this.likert.length];
                }// end if check for skipping empty row
            }// end while loop for rows in sheet
            this.inputStream.close();
        }// end of try
        catch(IOException e)
        {
            System.out.println("ERROR! Could not connect to stemScholar file.  Message: " + e.getMessage());
        }
        catch(BirthDateException e)
        {
            System.out.println("ERROR! " + e);
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
                    int[] opinions = s.getAttitude().getOpinions();
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
    
    public void saveNewExcelSheet(TreeMap<String, Student> scholars) 
            throws SaveScholarToSheetException, IOException
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet scholarSheet = workbook.createSheet("Scholars");
        int rowCount = 0;
        int colCount = 0;
        
        // Populate row of header labels
        Row row = scholarSheet.createRow(rowCount++);
        for(String label : headers)
        {
            Cell cell = row.createCell(colCount++);
            cell.setCellValue(label);
        }
        
        for (Map.Entry curScholar : scholars.entrySet())
        {
            row = scholarSheet.createRow(rowCount++);
            colCount = 0; // Reset column position to 0 for each row
            STEMScholar scholar = (STEMScholar) curScholar.getValue();
            
            // Create object array of scholar data, size of SIZE + how many opinions scholar has stored
            Object[] scholarData = new Object[SIZE + scholar.getAttitude().getOpinions().length];
            scholarData[STUDENTID] = scholar.getStudentID();
            scholarData[FNAME] = scholar.getfName();
            scholarData[MNAME] = scholar.getmName();
            scholarData[LNAME] = scholar.getlName();
            scholarData[GENDER] = scholar.getGender();
            scholarData[RACE] = scholar.getRace();
            scholarData[ETHNICITY] = scholar.getEthnicity();
            scholarData[DISABILITY] = scholar.getDisability();
            scholarData[ZIP] = scholar.getAddressZip();
            scholarData[EMAIL] = scholar.getEmail();
            scholarData[MAJOR] = scholar.getMajor();
            scholarData[GPA] = scholar.getGpa();
            scholarData[CAREERGOAL] = scholar.getCareerGoal();
            scholarData[BIRTHDATE] = scholar.getBirthDate();
            scholarData[STREET] = scholar.getAddressStreet();
            scholarData[CITY] = scholar.getAddressCity();
            scholarData[STATE] = scholar.getAddressState();
            scholarData[QUARTERSTART] = scholar.getQuarterStart();
            scholarData[ESTIMATEDGRADYEAR] = scholar.getEstimatedGradYear();
            scholarData[EMPLOYMENT] = scholar.getEmployment();
            scholarData[EMPLOYMENTHOURS] = scholar.getEmploymentHours();
            scholarData[PHONE] = scholar.getPhone();
            int opinionPos = 0;
            for (int dataPos = SIZE; dataPos < scholarData.length; dataPos++)
            {
                int response = scholar.getAttitude().getOpinions()[opinionPos];
                if (response != LikertQuestion.CONSTANT)
                {
                    scholarData[dataPos] = response;
                }
                opinionPos++;
            }
            
            for (Object data : scholarData)
            {
                Cell cell = row.createCell(colCount++);
                if (data instanceof String)
                {
                    cell.setCellValue((String) data);
                }
                else if(data instanceof Integer)
                {
                    cell.setCellValue((Integer) data);
                }
                else if(data instanceof Double)
                {
                    cell.setCellValue((Double) data);
                }
                else if(data instanceof LocalDate)
                {
                    // Create cell style formatted for date
                    CellStyle cellStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    cellStyle.setDataFormat(
                        createHelper.createDataFormat().getFormat("m/d/yyyy"));
                    
                    cell.setCellValue((LocalDate) data);
                    cell.setCellStyle(cellStyle);
                }
                else if(data == null)
                {
                    // purposefully do nothing
                }
                else
                {
                    String message = "Error!  Could not determine type of " + data 
                            + "to be added to row/column " + rowCount + colCount
                            + "for scholar ID: " + scholar.getStudentID();
                    throw new SaveScholarToSheetException(message);
                }
            }// end for loop for populating columns of scholarData in curRow   
        }// end for loop for populating rows of scholars within given TreeMap
        try (FileOutputStream outputStream = new FileOutputStream(saveExcelFile)) 
        {
            workbook.write(outputStream);
        }
        catch(IOException e)
        {
            throw new IOException("ERROR! Could not save stemScholar file.  Message: " + e.getMessage());
        }
    }// end saveNewExcelSheet
    
    
    // Getter/Setter: scholarRows
    public int getScholarRows()
    {
        return this.scholarRows;
    }
    public void setScholarRows(int rows)
    {
        this.scholarRows = rows;
    }
    // Private helper class for throwing BirthDate errors specific to this class
    private class BirthDateException extends Exception
    {
        public BirthDateException(String errorMessage)
        {
            super(errorMessage);
        }
    }
    
    // Private helper class for throwing saveScholarToSheetException errors specific to this class
    private class SaveScholarToSheetException extends Exception
    {
        public SaveScholarToSheetException(String errorMessage)
        {
            super(errorMessage);
        }
    }
}// End STEMStudentDAOExcel
