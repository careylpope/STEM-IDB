package Model;

import Controller.LikertScaleDAO;

/**
 * Class Attitude that holds two attributes
     - likert as an array of LikertQuestions
         instantiated by the static method getScale() from LikertScaleDAO
     - opinions as an array of ints
         set as an array of ints passed through the constructor of Attiude
 Constructor that requires an array of ints
     iterates through passed opinions for each opinion
         sets comparative LikertQuestions value in likert to that opinions value
 Getters/Setters for attributes
 Override toString to return formatted Attitude Object
     create r as an empty String
     if value of opinion for LikertQuestions is not the constant
        append question and value from LikertQuestion in likert to r
     return r
 */
public class Attitude
{
    private LikertScaleDAO scale = new LikertScaleDAO();
    private LikertQuestion[] likert = scale.getScale();
    private int[] opinions;
    
    public Attitude(int[] opinions) 
    {       
        this.opinions = opinions;
        for (int i = 0; i < opinions.length; i++)
        {
            this.likert[i].setOpinion(opinions[i]);
        }
    }
    
    //Getter/Setter: likert array
    public LikertQuestion[] getLikert() 
    {
        return likert;
    }
    public void setLikert(LikertQuestion[] likert) 
    {
        this.likert = likert;
    }

    //Getter/Setter: opinions array
    public int[] getOpinions() 
    {
        return opinions;
    }
    public void setOpinions(int[] opinions) 
    {
        this.opinions = opinions;
    }
    
    // Method to return a formatted String object of Attitude data
    @Override
    public String toString()
    {
        String r = "";
        for (int i = 0; i < this.likert.length; i++)
        {
            if (!(this.likert[i].getOpinion() == LikertQuestion.CONSTANT))
            {
                r += (i+1) + ".)  " + this.likert[i].getQuestion() + ": " 
                    + this.likert[i].getOpinion() + "\n";
            }
        }
        return r;
    }// end toString
}// end Attitude
