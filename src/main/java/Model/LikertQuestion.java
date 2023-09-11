package Model;

/**
 * Class LikertQuestion holds two attributes
 *     - CONSTANT as an int
 *         holds a constant value to assign as equaling an unanswered question
 *     - question as a String
 *         holds a likert-scale question
 *     - opinion as an int
 *         holds the paired value for the likert-scale question
 * Default constructor that creates a LikertQuestion of default question and negative opinion
 * Constructor that creates a LikertQuestion with the given question and opinion value
 *     calls default constructor
 *     if passed question is not "Q", sets question and opinion to passed values
 * Getters and Setters for both attributes
 *     
 */
public class LikertQuestion 
{
    public final static int CONSTANT = 0;  // Constant value to equal unanswered questions
    public final static String EMPTY_QUESTION = "Q";  // Constant to expect for empty question
    private String question;                          // lines in text file (questions.txt)
    private int opinion;
    
    // Defualt Constructor
    public LikertQuestion()
    {
        this.question = "No Question Available";
        this.opinion = CONSTANT;
    }
    
    public LikertQuestion(String question, int opinion)
    {
        this();
        if (!question.equals(EMPTY_QUESTION))
        {
            this.question = question;
            this.opinion = opinion;
        }
    }

    public String getQuestion() 
    {
        return question;
    }
    public void setQuestion(String question) 
    {
        this.question = question;
    }

    public int getOpinion() 
    {
        return opinion;
    }
    public void setOpinion(int opinion) 
    {
        this.opinion = opinion;
    }
    
    @Override
    public String toString()
    {
        String r = question + "\n";
        return r;
    }
}
