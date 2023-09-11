package View;

import Controller.LikertScaleDAO;
import Controller.STEMScholarDAO;
import Controller.STEMScholarDAOExcel;
import Controller.STEMScholarDAOText;
import Controller.STEMStudentDAO;
import Controller.STEMStudentDAOExcel;
import Controller.STEMStudentDAOText;
import Model.LikertQuestion;
import Model.STEMScholar;
import Model.Student;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


/**
 * Graphs implemented using the publicly available JFreeChart jar
 *     - runs off the stored files packaged with this project in the "resources" folder
 *     - limits counted majors and races for graphing purposes to only those represented
 *         by currently stored data in "stemScholars.txt" and "stemStudents.txt"
 * Implements a tabbed pane that displays three different graphs
 *     - GPAMajorRaceBarChart
 *         - displays the average GPA of every race of scholars and students
 *             studying under each major as a Bar Chart
 *     - LikertAvgBarChart
 *         ---------------------------------------------------------------------
 *        | TODO: Implement easier upkeep method within main program            |
 *        | Current Suggest Max Questions: 25                                   |
 *         ---------------------------------------------------------------------
 *         - displays the average response of scholars to stored likert questions
 *             as a 3D Bar Chart.  
 *             (STEMScholars ONLY, as of the current version STEMStudents do not
 *              have the ability to store data on Likert opinions through an Attitude.)
 *         - questions can be found and updated through the "questions.txt" file
 *             in the resources folder.  This file does require manual upkeep if 
 *             questions or question order changes.  One question per line
 *         - "GRAPH" button implemented to create a new chart based on a specific
 *             race of students as opposed to the default option of "All"
 *         - Too many questions have the potential to make the graph too full to
 *             be readable.  Suggested fix as of now is good upkeep of 
 *             interested questions and stored response values.
 *     - MajorRacePieChart
 *         - displays the distribution of scholars and students across all majors
 *             as a 3D Pie Chart
 *         - "GRAPH" button implemented to create a new chart based on a specific
 *             race of students as opposed to the default option of "All"
 * All graphing windows are resizeable as necessary
 */
public class Graphs extends javax.swing.JDialog {

    private STEMScholarDAO scholarDao;
    private STEMStudentDAO studentDao;
    private TreeMap<String,Student> scholars;
    private TreeMap<String,Student> students;
    private ArrayList<String> majors = new ArrayList<String>();
    private ArrayList<String> races = new ArrayList<String>();
    private LikertScaleDAO scale = new LikertScaleDAO();
    private LikertQuestion[] likert = scale.getScale();
    private JComboBox raceComboBoxLikert;
    private JComboBox raceComboBoxMajor;

    public Graphs(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.scholarDao = new STEMScholarDAOText();
        this.scholars = this.scholarDao.getMap();
        this.studentDao = new STEMStudentDAOText();
        this.students = this.studentDao.getMap();
        this.setTitle("Graphs");
        // Set lists of majors and races from current maps of scholars and students
        for (Map.Entry student : this.scholars.entrySet())
        {
            Student s = (Student) student.getValue();
            if (!majors.contains(s.getMajor()))
            {
                majors.add(s.getMajor());
            }
            if ((!s.getRace().equalsIgnoreCase("Not Reported")) 
                    && !races.contains(s.getRace()))
            {
                races.add(s.getRace());
            }
        }// end for loop for scholars
        for (Map.Entry student : this.students.entrySet())
        {
            Student s = (Student) student.getValue();
            if (!majors.contains(s.getMajor()))
            {
                majors.add(s.getMajor());
            }
            if ((!s.getRace().equalsIgnoreCase("Not Reported")) 
                    && !races.contains(s.getRace()))
            {
                races.add(s.getRace());
            }
        }// end for loop for students
        this.raceComboBoxLikert = getComboBoxRace();
        this.raceComboBoxMajor = getComboBoxRace();
        this.setGPAMajorRaceBarChart();
        this.setLikertAvgBarChart();
        this.setMajorRacePieChart();
    }// end Constructor
    
    // Create and add a barchart to Graphs window of avg GPA from Major and Race
    private void setGPAMajorRaceBarChart()
    {
        // Create Dataset  
        CategoryDataset dataset = createGPAMajorRaceBarChartDataset();   
        // Create chart  
        JFreeChart chart = ChartFactory.createBarChart(  
            "GPA Avg by Major & Race", //Chart Title  
            "Major", // Category axis  
            "GPA", // Value axis  
            dataset,  
            PlotOrientation.HORIZONTAL,  
            true,true,false
        );
        // Add chart to TabbedPane
        ChartPanel panel = new ChartPanel(chart);
        this.jTabbedPane1.add("GPA by Major/Race", panel);   
    }// end setGPAMajorRaceChart
    
    // Create a dataset for a Bar Chart of avg GPA using Major and Race
    private CategoryDataset createGPAMajorRaceBarChartDataset() 
    {  
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
        // Create values for dataset
        for (int i = 0; i < majors.size(); i++)
        {
            for (int j = 0; j < races.size(); j++)
            {
                double sum = 0;
                int totalStudents = 0;
                for (Map.Entry student : this.scholars.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getRace().equals(races.get(j)))
                    {
                        if (s.getMajor().equals(majors.get(i)))
                        {
                            sum += s.getGpa();
                            totalStudents++;
                        }
                    }
                }// end for loop for scholars TreeMap
                for (Map.Entry student : this.students.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getRace().equals(races.get(j)))
                    {
                        if (s.getMajor().equals(majors.get(i)))
                        {
                            sum += s.getGpa();
                            totalStudents++;
                        }
                    }
                }// end for loop for students TreeMap
                // If students of current examined race are registered for this major,
                // calculate avg gpa and add to dataset
                if (totalStudents != 0)
                {
                    double avgGpa = sum / totalStudents;
                    dataset.addValue(avgGpa, races.get(j), majors.get(i));
                }
            }// end for loop for races
        }// end for loop for majors
        return dataset;  
    }// end createGPAMajorRaceBarChartDataset
    
    // Create and add a Bar Chart to Graphs window of avg Likert questions response
    private void setLikertAvgBarChart()
    {
        // Create Dataset 
        CategoryDataset dataset = createLikertAvgBarChartDataset(
                (String)this.raceComboBoxLikert.getSelectedItem()); 
        // Create chart 
        JFreeChart chart = ChartFactory.createBarChart3D(  
            "Likert Response Avg Value", //Chart Title  
            "Question", // Category axis  
            "Avg Value", // Value axis  
            dataset,  
            PlotOrientation.VERTICAL,  
            false,true,false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Create panels that will display LikertAvgBarChart tab 
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel subPanel = new JPanel(new BorderLayout());
        JPanel interactPanel = new JPanel(new BorderLayout());
        
        // Create swing var's for interactPanel
        JButton graphLikert = new JButton();
        graphLikert.setText("GRAPH");
        
        // Set action of graphMajorPie button to private graphMajorPie method
        graphLikert.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
                graphLikert(chartPanel);
            } 
        });
        JTextArea questionsBox = new JTextArea();
        questionsBox.setText(scale.toString());
        questionsBox.setLineWrap(true);
        questionsBox.setWrapStyleWord(true);
        questionsBox.setEditable(false);
        // Adjust width of questionsBox here
        int width = 200;
        questionsBox.setPreferredSize(
                new Dimension(width, questionsBox.getPreferredSize().height));
        JScrollPane scrollPane = new JScrollPane(questionsBox);
        
        // Set display of LikertAvgChart tab
        interactPanel.add(this.raceComboBoxLikert, BorderLayout.NORTH);
        interactPanel.add(graphLikert, BorderLayout.SOUTH);
        subPanel.add(interactPanel, BorderLayout.NORTH);
        subPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(subPanel, BorderLayout.EAST);
        this.jTabbedPane1.add("Likert Response Avg", mainPanel);   
    }// end setLikertAvgBarChart
    
    // Create a dataset for a Bar Chart of avg Likert questions response
    private CategoryDataset createLikertAvgBarChartDataset(String race) 
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Create values for dataset
        if (race.equalsIgnoreCase((String) this.raceComboBoxLikert.getItemAt(0)))
        {
            for (int i = 0; i < this.likert.length; i++)
            {
                double sum = 0;
                int totalStudents = 0;
                for (Map.Entry student : this.scholars.entrySet())
                {
                    STEMScholar s = (STEMScholar) student.getValue();
                    // If student gave input for this likert question
                    // count input towards avg response
                    if (s.getAttitude().getOpinions()[i] > LikertQuestion.CONSTANT)
                    {
                        sum += s.getAttitude().getOpinions()[i];
                        totalStudents++;
                    }
                }// end for loop for scholars TreeMap
                // If any student input available for this question,
                // calculate avg response value and add to dataset
                if (totalStudents != 0)
                {
                    double avgResponse = sum / totalStudents;
                    dataset.addValue(avgResponse, "Question", Integer.toString(i+1));
                }
            }// end for loop for all races and all questions
        }
        else
        {
            for (int i = 0; i < this.likert.length; i++)
            {
                double sum = 0;
                int totalStudents = 0;
                for (Map.Entry student : this.scholars.entrySet())
                {
                    STEMScholar s = (STEMScholar) student.getValue();
                    // If student gave input for this likert question
                    // count input towards avg response
                    if (s.getRace().equalsIgnoreCase(race) && 
                            s.getAttitude().getOpinions()[i] > LikertQuestion.CONSTANT)
                    {
                        sum += s.getAttitude().getOpinions()[i];
                        totalStudents++;
                    }
                }// end for loop for scholars TreeMap
                // If any student input available for this question,
                // calculate avg response value and add to dataset
                if (totalStudents != 0)
                {
                    double avgResponse = sum / totalStudents;
                    dataset.addValue(avgResponse, "Question", Integer.toString(i+1));
                }
            }// end for loop for specific race and all questions
        }
        return dataset;    
    }// end createLikertAvgBarChartDataset
    
    // Create and add a Pie Chart of Majors by Race
    private void setMajorRacePieChart()
    {
        // Create Dataset 
        PieDataset dataset = createMajorRacePieChartDataset(
                (String)this.raceComboBoxMajor.getSelectedItem()); 
        // Create chart  
        JFreeChart chart = ChartFactory.createPieChart3D(
            "Major Distribution by Race", // Chart Title
            dataset, 
            false, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Create panels that will display MajorPieChart tab 
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel subPanel = new JPanel(new BorderLayout());
        JPanel interactPanel = new JPanel(new BorderLayout());
        
        // Create swing var's for interactPanel
        JButton graphMajorPie = new JButton();
        graphMajorPie.setText("GRAPH");
        
        // Set action of graphMajorPie button to private graphMajorPie method
        graphMajorPie.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            { 
                graphMajorPie(chartPanel);
            } 
        });
        
        // Set display of MajorPieChart tab
        interactPanel.add(this.raceComboBoxMajor, BorderLayout.NORTH);
        interactPanel.add(graphMajorPie, BorderLayout.SOUTH);
        subPanel.add(interactPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(subPanel, BorderLayout.EAST);
        this.jTabbedPane1.add("Major Distribution", mainPanel); 
    }// end setMajorRacePieChart
    
    // Create a dataset for a Pie Chart of Majors by Race
    private PieDataset createMajorRacePieChartDataset(String race)
    {
        DefaultPieDataset dataset = new DefaultPieDataset();  
        // Create values for dataset
        if (race.equalsIgnoreCase((String)this.raceComboBoxMajor.getItemAt(0)))
        {
            for (int i = 0; i < majors.size(); i++)
            {
                int totalStudents = 0;
                for (Map.Entry student : this.scholars.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getMajor().equals(majors.get(i)))
                    {
                        totalStudents++;
                    }
                }// end for loop for scholars TreeMap
                for (Map.Entry student : this.students.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getMajor().equals(majors.get(i)))
                    {
                        totalStudents++;
                    }
                }// end for loop for students TreeMap
                // If students are registered for this major,
                // set value of major to totalStudents in dataset map
                if (totalStudents != 0)
                {
                    dataset.setValue(majors.get(i), totalStudents);
                }
            }// end for loop for all races
        }// end if check for all races
        else
        {
            for (int i = 0; i < majors.size(); i++)
            {
                int totalStudents = 0;
                for (Map.Entry student : this.scholars.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getRace().equals(race))
                    {
                        if (s.getMajor().equals(majors.get(i)))
                        {
                            totalStudents++;
                        }
                    }
                }// end for loop for scholars TreeMap
                for (Map.Entry student : this.students.entrySet())
                {
                    Student s = (Student) student.getValue();
                    if (s.getRace().equals(race))
                    {
                        if (s.getMajor().equals(majors.get(i)))
                        {
                            totalStudents++;
                        }
                    }
                }// end for loop for students TreeMap
                // If students of current examined race are registered for this major,
                // set value of major to totalStudents in dataset map
                if (totalStudents != 0)
                {
                    dataset.setValue(majors.get(i), totalStudents);
                }
            }// end for loop for specific race
        }// end else for specific race
        return dataset; 
    }// end createMajorRacePieChartDataset
    
    // Creates a JComboBox of all races included in data, plus "All"
    private JComboBox getComboBoxRace()
    {
        // Appends "All" as a selectable race option at start of raceComboBox list
        String[] raceOptions = new String[this.races.size() + 1];
        for (int i = 0; i < this.races.size() + 1; i++)
        {
            if (i == 0)
            {
                raceOptions[i] = "All";
            }
            else
            {
                raceOptions[i] = this.races.get(i - 1);
            }
        }
        JComboBox raceComboBox = new JComboBox(raceOptions);
        raceComboBox.setFont(new Font("Arial", Font.BOLD, 12));
        return raceComboBox;
    }// end of getComboBoxRace
    
    // Creates a JComboBox of all majors included in data, plus "All"
    // ---- NOT CURRENTLY IN USE ----
    private JComboBox getComboBoxMajor()
    {
        /**
         * Appends "All" as a selectable major option at start of majorComboBox list
        String[] majorOptions = new String[this.majors.size() + 1];
        for (int i = 0; i (less than) this.majors.size() + 1; i++)
        {
            if (i == 0)
            {
                majorOptions[i] = "All";
            }
            else
            {
                majorOptions[i] = this.majors.get(i - 1);
            }
        }
        */
        JComboBox majorComboBox = new JComboBox(this.majors.toArray());
        majorComboBox.setFont(new Font("Arial", Font.BOLD, 12));
        return majorComboBox;
    }// end of getComboBoxMajor
    
    // Updates passed ChartPanel with new chart data, for use with LikertAvgChart
    private void graphLikert(ChartPanel panel) 
    {                                                       
        CategoryDataset dataset = createLikertAvgBarChartDataset(
                (String)this.raceComboBoxLikert.getSelectedItem());
        // Create chart  
        JFreeChart chart = ChartFactory.createBarChart3D(  
            "Likert Response Avg Value", //Chart Title  
            "Question", // Category axis  
            "Avg Value", // Value axis  
            dataset,  
            PlotOrientation.VERTICAL,  
            false,true,false
        );
        panel.setChart(chart);
    }// end of graphLikert
    
    // Updates passed ChartPanel with new chart data, for use with MajorPieChart
    private void graphMajorPie(ChartPanel panel)
    {
        PieDataset dataset = createMajorRacePieChartDataset(
                (String)this.raceComboBoxMajor.getSelectedItem());
        // Create chart  
        JFreeChart chart = ChartFactory.createPieChart3D(  
            "Major Distribution by Race", //Chart Title  
            dataset,  
            false,true,false
        );
        panel.setChart(chart);
    }//end of graphMajorPie
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Graphs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Graphs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Graphs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graphs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Graphs dialog = new Graphs(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
