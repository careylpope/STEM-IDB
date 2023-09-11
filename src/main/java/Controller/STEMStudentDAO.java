package Controller;

import Model.Student;
import java.util.TreeMap;

/**
 * Interface STEMStudentDAO, creates universal methods to interact with STEMStudents
 * Implemented by STEMStudentDAOText
 */
public interface STEMStudentDAO 
{
    public TreeMap<String,Student> getAll();
    public TreeMap<String,Student> getMap();
    public boolean addStudent(Student aStudent);
    public boolean deleteStudent(String id);
    public boolean updateStudent(String id, Student aStudent);
    public Student getStudent(String id);
    public void save();
}
