# STEM-IDB
---------------------------
 STEM Interactive Database
---------------------------
Maven Java software developed as an all-purpose tool for analyzing &amp; visualizing STEM student data.

*Lead Developer, Planner & Author: Carey Pope*

-----------
WHAT IS IT?
-----------
  “STEM Interactive Database” – software designed specifically to help offer the greatest level of support to marginalized students in STEM.  With this goal in mind, I wanted to create a user-friendly program that would be able to easily and efficiently collect, store and use various student data to display clean and clear tables, charts or even a student distribution heat-map.  Through these charts, trends could be derived on which areas student groups were performing well in – and in which areas they required additional support.  Through the user-friendly GUI, users can view or enter new data for individual STEM students, view a table of all students, read or write data back from excel spreadsheets, display several different charts comparing various data fields, or display a student distribution heatmap across the different zip code areas in Massachusetts.  This software was designed initially for use by the NSF funded SCoRE (STEM Cohorts for Research & Engagement) program at HCC (Holyoke Community College), but could likely be repurposed with little issue for any similar program within Massachusetts (or other states, accepting the heat-distribution map will not be supported for them).  Current data in the program was fabricated randomly, and is not based upon any real individuals, for use in examples and tests.

Currently, supported charts are: 
  *  Bar charts comparing responses from Likert surveys across different groups of students
  *  Pie charts comparing distribution of majors across all STEM students, or different groups of the student body
  *  Bar charts comparing student gpa average across major and race.

-----
NOTES
-----

  *  To update likert questions, please update manually in the questions text file in the Resources folder.
     *  Project File path:  STEMInteractiveDatabase\src\main\java\Resources
     
     *  *Expected formatting is one question per line*

  *  To update scholars and students directly, please update the STEMDatabaseWorkbook excel file in the Resources folder
     *  Project File path:  STEMInteractiveDatabase\src\main\java\Resources
	
     *  *Once workbook is updated, internal files can be updated through "Load" in the main program window*

  *  Data is saved locally, i.e there exist no servers.  For real data, it is required you have your own access to relevant data and
      supply it for use with the program on your own machine
