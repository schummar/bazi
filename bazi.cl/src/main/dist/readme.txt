BAZI - "Berechnung von Anzahlen mit Zuteilungsmethoden im Internet"
===================================================================
(Version offline.@version@)
Webpage: http://www.math.uni-augsburg.de/stochastik/bazi



Requirements
------------
BAZI requires a Java interpreter supporting Java SE and the Java Foundation Classes (JFC).
If the system does not find these programs on your machine then you can download 
    Java SE Runtime Environment, Version 6 (JRE)
from  
    http://www.java.com/ -> "Free Java Download"
.

BAZI runs with Java version 1.6 or higher.
However, it is advisable to use the current version 1.7.

Execution
---------
Windows:
Start BAZI by double clicking bazi.bat
Alternativly, you can create a link for .jar files, to start BAZI by double clicking bazi.jar:
Choose the submenu "Folder options" of the explorer menu "Extras". Then choose the tab "file types". There mark the line beginning with "JAR..." (if no entry exists, create one using the "New"-Button). Click the button "extended" to open the dialog "Edit file type". There mark the action "open" and click the button "Edit...". In the dialog now appearing input into the "Commandline" the following line (replace $JAVAHOME by your Java installation directory):
"$JAVAHOME\bin\javaw.exe" -jar "%1" %*

Linux:
Start BAZI from the console by typing
# ./bazi.sh
Perhaps this file must be marked eXecutable first, therefore type
# chmod u+x bazi.sh

On other Platforms type the command
    java -jar bazi.jar



Copyright 2000-2013 by Augsburg University
