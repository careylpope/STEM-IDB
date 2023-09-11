/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

import Controller.STEMScholarDAO;
import Controller.STEMScholarDAOText;
import Controller.STEMStudentDAO;
import Controller.STEMStudentDAOText;
import Model.Student;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.collection.SpatialIndexFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

// TO DO: Add pop-up JDialogue (or some other notice) for when zipcode is not found
//          Ideally would display after map is presented, prevents chain of several pop-ups
//        
//        When a zipcode not stored in the .shp file is found, check against some
//          external source to add a count of student to appropriate zipcode match
//          i.e:
//              Valid Westfield zipcodes:           01085, 10186
//              Westfield zipcode in .shp file:     01085
//              
//              If a student is found to have 01086, add 1 additional count of
//              count of student to paired 01085 zipcode

public class LocationHeatMapWindow extends javax.swing.JDialog {
   
    private final STEMScholarDAO scholarDao;
    private final STEMStudentDAO studentDao;
    private final TreeMap<String,Student> scholars;
    private final TreeMap<String,Student> students;
    private final ArrayList<String> zipCodes = new ArrayList<String>();
    private final ArrayList<Integer> studentDensity = new ArrayList<Integer>();
    private final ArrayList<Rule> rules = new ArrayList<Rule>();
    private int totalStudents = 0;
    private final ImageIcon studentDistributionImage;
    private final MapContent map;
    private final STEMIDBJMapFrame mapFrame;
    
    // MAP STYLE CONSTANTS
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color LOW_DENSITY_COLOR = Color.GREEN;
    private static final Color MID_DENSITY_COLOR = Color.YELLOW;
    private static final Color HIGH_DENSITY_COLOR = Color.RED;
    private static final Color DEFAULT_FILL_COLOR = Color.GRAY;
    private static final float DEFAULT_OPACITY = 0.5f;
    
    // FEATURE ATTRIBUTE LIST POSITION CONSTANTS
    private static final int ZIP_CODE_POS = 1;  // Denotes zipcode pos in feature att list
    private static final int AREA_NAME_POS = 2;  // Denotes town/city name pos in feature att list
    
    // STYLE CREATION FACTORIES
    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    /**
     * Creates new form LocationHeatMapWindow
     */
    public LocationHeatMapWindow(java.awt.Frame parent, boolean modal) throws IOException {
        super(parent, modal);
        initComponents();
        this.scholarDao = new STEMScholarDAOText();
        this.scholars = this.scholarDao.getMap();
        this.studentDao = new STEMStudentDAOText();
        this.students = this.studentDao.getMap();
        // for loop to populate zipCode and density arrays from scholars/students
        for (Map.Entry student : this.scholars.entrySet())
        {
            totalStudents++;
            Student s = (Student) student.getValue();
            if (!zipCodes.contains(s.getAddressZip()))
            {
                zipCodes.add(s.getAddressZip());
                studentDensity.add(1);
            }
            else
            {
                int pos = zipCodes.indexOf(s.getAddressZip());
                int i = (studentDensity.get(pos) + 1);
                studentDensity.set(pos, i);
            }
        }// end for loop for scholars
        for (Map.Entry student : this.students.entrySet())
        {
            totalStudents++;
            Student s = (Student) student.getValue();
            if (!zipCodes.contains(s.getAddressZip()))
            {
                zipCodes.add(s.getAddressZip());
                studentDensity.add(1);
            }
            else
            {
                int pos = zipCodes.indexOf(s.getAddressZip());
                int i = (studentDensity.get(pos) + 1);
                studentDensity.set(pos, i);
            }
        }// end for loop for students
        
        // Create ImageIcon of stored png in resources folder
        studentDistributionImage = 
                new ImageIcon("src/main/java/Resources/StudentDistributionPercentLegend.PNG");
        
        // assign shape file to use as map content, in this case zipcode map of Massachusetts
        // FILE MUST BE A .shp FILE
        Path path = Paths.get("src/main/java/Resources/mapShapeFiles/ZIPCODES_NT_POLY.shp");
        File file = path.toFile();
        
        // use the .shp file to get a featureSource for styling map
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        // cachedSource used later to create map
        SimpleFeatureSource cachedSource =
                DataUtilities.source(
                        new SpatialIndexFeatureCollection(featureSource.getFeatures()));
        
        // Creates map content object
        map = new MapContent();
        
        // Create style to present map in
        SimpleFeatureCollection features = featureSource.getFeatures(); // grabs collection of features
        Style style = createStyle(features);
        
        // Create map from given source and style type
        Layer layer = new FeatureLayer(cachedSource, style);
        map.addLayer(layer);

        // Create and set values of STEMIDBJMapFrame from map
        mapFrame = new STEMIDBJMapFrame(map);
        mapFrame.enableStatusBar(true);
        mapFrame.enableToolBar(true);
        mapFrame.setTitle("Student Distribution by Zipcode for Massachusetts");
        mapFrame.setSize(1000, 600);
        
        // Set infoPanel to display Distribution Stats
        JPanel infoPanel = new JPanel(new BorderLayout());
        JTextArea infoBox = new JTextArea();
        infoBox.setText(DensityDistribution(features));
        infoBox.setLineWrap(true);
        infoBox.setWrapStyleWord(true);
        infoBox.setEditable(false);
        
        // Adjust width of infoBox and Title here
        //  (Should be portional to pixel width of studentDistributionImage
        int width = studentDistributionImage.getIconWidth();
        //int width = 200;
        infoBox.setPreferredSize(
                new Dimension(width, infoBox.getPreferredSize().height));
        JScrollPane scrollPane = new JScrollPane(infoBox);
        JLabel Title = new JLabel();
        Title.setText(" AREA: STUDENT DENSITY %");
        Dimension titleD = new Dimension(width, 30);
        Title.setPreferredSize(titleD);
        JLabel imageLabel = new JLabel(studentDistributionImage);
        
        // Create, add and enable infoPanel for mapFrame
        infoPanel.add(Title, BorderLayout.NORTH);
        infoPanel.add(scrollPane);
        infoPanel.add(imageLabel, BorderLayout.SOUTH);
        mapFrame.setInfoPanel(infoPanel);
        mapFrame.enableInfoPanel(true);
        
        // Finalize creation, set position of, and show mapFrame
        mapFrame.initComponents();
        mapFrame.setLocationRelativeTo(null);
        mapFrame.setVisible(true);
    }
    
    private Style createStyle(SimpleFeatureCollection features) 
    {
        // Create rules for all stored zipCodes
        for (int i = 0; i < zipCodes.size(); i ++)
        {
            rules.add(createRule(features, zipCodes.get(i)));
        }
        
        // Default rule for non-held zipCodes
        Stroke stroke = sf.createStroke(ff.literal(LINE_COLOR), ff.literal(DEFAULT_OPACITY), ff.literal(0.5));
        Fill fill = sf.createFill(ff.literal(DEFAULT_FILL_COLOR), ff.literal(DEFAULT_OPACITY));
        Rule otherRule = sf.createRule();
        otherRule.setElseFilter(true);
        Symbolizer symbolizer = sf.createPolygonSymbolizer(stroke, fill, null);
        otherRule.symbolizers().add(symbolizer);

        // Add all non-null rules to a FeatureTypeStyle object
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        for (Rule r : rules)
        {
            if (r != null)
            {
                fts.rules().add(r);
            }
        }
        fts.rules().add(otherRule);
        
        // Create and return style from FeatureTypeStyle
        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }// end createStyle
    
    private Rule createRule(SimpleFeatureCollection features, String zipCode)
    {
        Rule tempRule = null;
        
        SimpleFeatureIterator iter = features.features();
        
        while (iter.hasNext())
        {
            SimpleFeature feature = iter.next();
            if (feature.getAttributes().get(ZIP_CODE_POS).equals(zipCode))
            {
                int zipPos = zipCodes.indexOf(zipCode);
                FeatureId ID = feature.getIdentifier();
                float density = (float)studentDensity.get(zipPos) / (float)totalStudents;
                Stroke stroke = sf.createStroke(ff.literal(LINE_COLOR), ff.literal(1), ff.literal(0.5));
                Fill fill;
                
                if (density <= 0.33f)
                {
                    // create fill determined by density
                    density = (density / 0.33f);
                    fill = sf.createFill(ff.literal(LOW_DENSITY_COLOR), ff.literal(density));
                }
                else if (density <= 0.66f)
                {
                    //create fill determined by density
                    density = (density / 0.66f);
                    fill = sf.createFill(ff.literal(MID_DENSITY_COLOR), ff.literal(density));
                }
                else
                {
                    // create fill determined by density
                    density = (density / 1.00f);
                    fill = sf.createFill(ff.literal(HIGH_DENSITY_COLOR), ff.literal(density));
                }
                // create symbolizer from stroke and fill
                Symbolizer symbolizer = sf.createPolygonSymbolizer(stroke, fill, null);
                // create rule using filter of this feature ID and symbolizer
                tempRule = sf.createRule();
                tempRule.setFilter(ff.id(ID)); 
                tempRule.symbolizers().add(symbolizer);
                iter.close();
                return tempRule;
            }// end if for rule created
        }// end while for iterator
        iter.close();
        return tempRule;
    }// end createRule
    
    private String DensityDistribution(SimpleFeatureCollection features) 
    {
        String r = "";
        
        String percentBuffer = "\n   ..............................   ";
        String endLine = "\n------------------------------------------\n";
        
        // Formatting object for displaying density percentages
        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMaximumFractionDigits(2);
        
        // ArrayList to keep track of added zipCodes
        // Prevents occasional reinclusion of zipcodes
        ArrayList<String> addedZips = new ArrayList<String>();
        
        // Iterator to move through features
        SimpleFeatureIterator iter = features.features();
        while (iter.hasNext())
        {
            SimpleFeature feature = iter.next();
            
            // If feature is of a zipcode included in zipCodes, and not yet included
            if (zipCodes.contains((String)feature.getAttributes().get(ZIP_CODE_POS))
                    && !addedZips.contains((String)feature.getAttributes().get(ZIP_CODE_POS)))
            {
                int zipPos = zipCodes.indexOf(feature.getAttributes().get(ZIP_CODE_POS));
                FeatureId ID = feature.getIdentifier();
                float density = (float)studentDensity.get(zipPos) / (float)totalStudents;
                
                // "\u2022" ---> unicode encoding for bullet character
                r += "\u2022 " + feature.getAttributes().get(AREA_NAME_POS) + " ";
                r += percentBuffer + pf.format(density);
                r += endLine;
                addedZips.add((String) feature.getAttributes().get(ZIP_CODE_POS));
            }
        }
        iter.close();
        return r;
    }// end DensityDistribution
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(LocationHeatMapWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LocationHeatMapWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LocationHeatMapWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LocationHeatMapWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LocationHeatMapWindow dialog = null;
                try {
                    dialog = new LocationHeatMapWindow(new javax.swing.JFrame(), true);
                } catch (IOException ex) {
                    Logger.getLogger(LocationHeatMapWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    // End of variables declaration//GEN-END:variables
}
