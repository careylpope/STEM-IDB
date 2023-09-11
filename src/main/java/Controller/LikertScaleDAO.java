package Controller;

import Model.LikertQuestion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Static class LikertScaleDAO that holds 4 static attributes
     - lines as an int
         counting attribute that holds the value of total likert-scale questions
     - filePath as a Path object
         holds the project path to the text file of questions
     - questionText as a File object
         holds the text file of questions
     - likert as an array of LikertQuestions
         holds the LikertQuestions for each question in questionText
 Contains static method getScale(), returns likert
     iterate through questionText, incrementing lines for each question read
     construct an array of LikertQuestions passed lines as its size
     iterate through questionText again,
         construct a new LikertQuestion for each question in questionText
     return likert
 */
public class LikertScaleDAO
{
    private int lines;
    private static final Path filePath = Paths.get("src/main/java/resources/questions.txt");
    private static final File questionText = filePath.toFile();
    private LikertQuestion[] likert;
    
    public LikertScaleDAO()
    {
        this.likert = createScale();
    }
    
    public LikertQuestion[] createScale()
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(LikertScaleDAO.questionText)))
        {
            lines = 0;
            while (reader.readLine() != null) 
            {
                lines++;
            }
        } 
        catch (IOException e) 
        {
            System.out.println("ERROR! Could not connect to questions text file.  "
                    + "Message: " + e.getMessage());
        }
        likert = new LikertQuestion[lines];
        
        try(BufferedReader reader = new BufferedReader(new FileReader(LikertScaleDAO.questionText)))
        {
            int pos = 0;
            String question = reader.readLine();
            while(question != null)
            {
                likert[pos] = (new LikertQuestion(question, LikertQuestion.CONSTANT));
                pos++;
                question = reader.readLine();
            }
        }
        catch(IOException e)
        {
            System.out.println("ERROR! Could not connect to questions text file.  "
                    + "Message: " + e.getMessage());
        }
        return likert;
    }
    
    public LikertQuestion[] getScale()
    {
        return likert;
    }
    
    @Override
    public String toString()
    {
        String r = "";
        for (int i = 0; i < this.likert.length; i++)
        {
            r += (i + 1) + ": " + this.likert[i].toString() + "\n";
        }
        return r;
    }
}
