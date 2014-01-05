import groovy.util.ConfigSlurper
import java.util.Properties;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import  javax.swing.JOptionPane;

public class Support
{
	def config
	def paths
	def env = [:]
	Properties props

	// non-OS specific parameters for business issues
	String propertyfile = '../editordata/menu.properties'  

	// non-OS specific parameters for business issues
	String pathfile = '../editordata/path.properties'  

	// class constructor - loads configuration, get system environmental variables;
	public Support()
	{
		// Get all system properties
  		props = System.getProperties()
	       	paths = new ConfigSlurper().parse(new File(pathfile).toURL() )

		// get non-path related static values
	       	config = new ConfigSlurper().parse(new File(propertyfile).toURL())	
       		env = System.getenv()
	} // end of constructor

	public void showAll()
	{
		println "System Properties"
		props.each{k,v-> println "key:${k} and value:${v}"}

		println "Config Properties"
		config.each{k,v-> println "key:${k} and value:${v}"}

		println "Path Properties"
		paths.each{k,v-> println "key:${k} and value:${v}"}

		println "Env Properties"
		env.each{k,v-> println "key:${k} and value:${v}"}
	}

	// describe dialog
	public void showDialog(String ti, String tx)
	{
		int messageType = JOptionPane.WARNING_MESSAGE; // no standard icon
		JOptionPane.showMessageDialog(null, "$tx", "${ti}", messageType);
	} // end of showDialog

	public void writeProperties()
	{
		// make new props file from the 'props'  = System.getProperties()
		File file = new File("../editordata/test2.properties");
		FileOutputStream fileOut = new FileOutputStream(file);
		props.store(fileOut, "Favorite Things"); // adds a title and date to start of text file;			
		fileOut.close();

		// since this call only gets 'development' leg, the output test file has all non-environment props plus
		// only the dev leg; remarks are lost too
                def config = new ConfigSlurper("development").parse(new File(pathfile).toURL())
		new File("../editordata/pathdev.properties").withWriter { writer ->
     			config.writeTo(writer)
		}
                config = new ConfigSlurper().parse(new File(pathfile).toURL())
		new File("../editordata/pathall.properties").withWriter { writer ->
     			config.writeTo(writer)
		}
	} // end of method

	// ---------------------------------
	// pull up all prior edited files; the most recently edited file is last, earliest is first
	/*
		--->/Users/jim/core:=21; 2013-11-06 14:59:46CET
		--->/Users/jim/audit2x.txt:=171; 2013-11-06 15:01:14CET
		--->/Users/jim/DerbyClientSession.txt:=410; 2013-11-06 15:10:47CET	
	*/
	public static String[] getListOfEditedFiles()
	{
    	def getProperty = System.&getProperty  
    	def home = getProperty("user.home")
    	def logName = home+"/.TextEditor.log"
    	def lastedited
    	def logEntries=[]
    	def logCount = 0;
    
	    if ( !( new File( logName).exists() ) ) return logEntries;

	    def log = new File(logName).text;
    	log.eachLine{ln ->
        	println "--->"+ln; 
        	int i = ln.indexOf(":=");
        	if (i>-1)
        	{
            	lastedited=ln.substring(0,i);
            	logEntries.push(lastedited);
        	} // end of if	
	    } // end of log

		// reorder list so latest edited file is first, oldest is last
    	logEntries.reverse(true);
    
    	println "most recently edited file of ${logEntries.size()} files was "+lastedited;

    	if (logEntries.size() < 2) { return logEntries; }
    	
	    // remove duplicates
    	def flag = true
    	int ix = 0;
    	int jx = 1;
    	int kx = logEntries.size()
    	def lc, nc;
    	while(flag)
    	{
    		lc = logEntries[ix].toLowerCase()
    		kx = logEntries.size();
    		
    		// need jx loop here 	
    		jx = ix + 1;
    		def flag2 = (jx>kx) ? false : true;
    		if (flag2)
    		{
    			// add more code here
    		} // end of if
    		
    		nc = logEntries[jx].toLowerCase()
    		if( lc.equals(nc) ) { logEntries.remove(jx); } 	
    		
    		if (ix >= kx) 
    		{
    			flag = false;
    		} 
    		else
    		{
    			ix += 1;
        	    ++logCount; 
    		}
	    } // end of while

    	return logEntries;
	} // end of getListOfEditedFiles()
    
    
	// test harness for this class
	public static void main(String[] args)
	{	
		println "... started"
		Support su = new Support()
		su.showDialog("This Is A Title", "This is some text.");
		su.showAll()
		su.writeProperties();
		println "\n----------------\nList of recently edited files"
		def log = Support.getListOfEditedFiles();
		println "\nThere are ${log.size()} edited files"
		println "... ended"
	} // end of main

} // end of class

/*
from: https://bowerstudios.com/node/1066

Create the properties object, and load it from the file system:
Properties props = new Properties()
File propsFile = new File('/usr/local/etc/test.properties')
props.load(propsFile.newDataInputStream())

Take a peek:
println props.getProperty('porcupine')

Write a new random value and persist it to the file system:
Integer rand = new Random().next(4)
props.setProperty('porcupine', rand.toString())
props.store(propsFile.newWriter(), null)

Peek again:
props.load(propsFile.newDataInputStream())
println props.getProperty('porcupine')

//---------------------
// also see: http://groovy.codehaus.org/ConfigSlurper


*/
