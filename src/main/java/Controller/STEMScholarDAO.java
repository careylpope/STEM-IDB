package Controller;

import Model.*;
import java.util.TreeMap;

/**
 * Interface STEMScholarDAO, creates universal methods to interact with STEMScholars
 * Implemented by STEMScholarDAOText
 */
public interface STEMScholarDAO 
{
    public TreeMap<String,Student> getAll();
    public TreeMap<String,Student> getMap();
    public boolean addStudent(Student aStudent);
    public boolean deleteStudent(String id);
    public boolean updateStudent(String id, Student aStudent);
    public Student getStudent(String id);
    public void save();
}
