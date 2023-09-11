package Controller;

/**
 *  Validation class, checks that certain fields are accepted before being
 *      passed to constructors in the view
 */
public class Validate 
{
    
    public static boolean isID(String id)
    {
        if (id.length() == 9)
        {
            if (id.matches("[0-9]+"))
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isDelimited(String string, String delim, int amount)
    {
        if (!string.isEmpty())
        {
            int count = 0;
            for (int i = 0; i < string.length(); i++)
            {
                if (string.substring(i, (i + 1)).equals(delim))
                {
                    count++;
                }
            }
            if (count == amount)
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isInt(String string)
    {
        if (string.matches("[0-9]+"))
        {
            return true;
        }
        return false;
    }
    
    public static boolean isInt(String string, int min)
    {
        if (string.matches("[0-9]+"))
        {
            int i = Integer.parseInt(string);
            if (i >= min)
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isInt(String string, int min, int max)
    {
        if (string.matches("[0-9]+"))
        {
            int i = Integer.parseInt(string);
            if (i >= min && i <= max)
            {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isDouble(String string, double min, double max)
    {
        try
        {
            double d = Double.parseDouble(string);
            if (d >= min && d <= max)
            {
                return true;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return false;
    }
}
