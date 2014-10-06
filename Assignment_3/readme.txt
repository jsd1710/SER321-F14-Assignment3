Author: Tim Lindquist (Tim.Lindquist@asu.edu)
        Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
Version: September 2014

See http://pooh.poly.asu.edu/Cst420/Assigns/Assign2/assign2.html

Purpose: Sample Java program demonstrating Ant builds and a simple
javax.swing GUI

This program is executable on Mac OSX, Linux and Windows running Cygwin.

Classes in Java to demonstrate defining a simple GUI with javax.swing
components, and controlling that GUI. This program represents the
View (WaypointGUI class) and Control (SampleAssign2 class) components.

The Assign2 project includes an Ant build file, with the following
targets: prepare, clean, build, execute, tasks.
To execute ant using the build.xml in this directory, you will need to
copy the file: antlibs.jar from the lib directory to your home directory:
cp lib/antlibs.jar ~
or
cp lib/antlibs.jar $HOME/
Note that ~ (tilde) is a shortcut for $HOME
then extract the antlibs.jar file:
pushd ~
jar xvf antlibs.jar
pushd -0
The pushd commands manipulate a stack of directories for switching your
bash's current directory. The first pushd pushes home onto the stack and
switches the current directory to home. The second pushd takes you
back to whatever directory you were in before the first.

To run the example, from a bash shell in the project directory, execute the
command:
ant execute

To clean the project (remove the .class files) execute:
ant clean


