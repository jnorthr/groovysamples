// refactor as a class then add main method witha args, first one being name external command line name of file to open
import groovy.swing.SwingBuilder 
import javax.swing.WindowConstants as WC 
import java.awt.FlowLayout
import java.awt.*
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.border.*
import java.io.*
import java.awt.event.*;
import javax.swing.event.CaretEvent
import java.awt.GridBagConstraints.*
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.awt.Toolkit
import java.awt.Toolkit.*
import javax.swing.JTextArea.AccessibleJTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import java.awt.image.BufferedImage
import javax.imageio.*
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import TextUtilities;
import GTextArea;
import javax.swing.text.NumberFormatter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



swing = new SwingBuilder()
log="";	
home = System.getProperty("user.home")
logName = home+"/.TextEditor.log"
String story = "";
rowcol = new JLabel("xxx");

gta = new GTextArea();
gta.setStory(story);
this.getTextAreaCursorPosition();    

gta.addKeyListener(new KeyListener()
{
	@Override 
	public void keyPressed(KeyEvent ke)
    {
		boolean f = false

		if (ke.isShiftDown()) 
		{
			f = true
		} // end of if

		switch (ke.getKeyCode()) 
		{
			case KeyEvent.VK_F3:  // move x coordinate left
			if (f)
			{
				println "F15 key pressed"
			} // end of shift

			else
			{
				println "F3 key pressed"
				exitText(ke);
			} // end of 
			break;
		}
    }

			@Override
            public void keyReleased(KeyEvent e)
          	{          	
	        	this.getTextAreaCursorPosition();    
	        	//println "keyReleased rowcol="+rowcol;
            }
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

});

boolean saveflag = false;
logCount=-1;
lastedited="";
def logEntries=[]
gui=null;


// Apple Mac bits follow ---
System.setProperty("apple.laf.useScreenMenuBar", "true")
System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TextEditor")
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
def createShortcutWithModifier = { key, modifier -> KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | modifier) }
def createShortcut = { key -> createShortcutWithModifier(key, 0) }
def isMac = { System.getProperty("mrj.version") != null }


GridBagConstraints c = new GridBagConstraints();
// the external padding of the component 
//c.insets = new Insets(0, 1, 0, 1);

// Weights are used to determine how to distribute space among columns (weightx) and among rows (weighty);
// this is important for specifying resizing behavior. Larger numbers indicate that the component's 
// row or column should get more space. Range is 0.0 - 1.0
c.weighty = 1d;
c.weightx = 1d;
//c.gridx = 0; // column number where zero is first column
//c.gridy = 0; // row number where zero is first row

// direction of drift 4 this component when smaller than window 
//c.anchor = GridBagConstraints.WEST; 

// rule to let a component expand both ways when more space is available on resize
// fill. Used when the component's display area is larger than the component's requested size 
c.fill = GridBagConstraints.BOTH; 
//c.gridwidth = GridBagConstraints.RELATIVE

// declare text area panel for bottom of split screen
JPanel panelx = new JPanel(new GridBagLayout());

String fileName = "";
jtf = new JTextField(30)
jtf.text = fileName
chooser = new JFileChooser()
chooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
//------------
// jcombobox tests
            String[] ls = [ "strawberry", "chocolate", "vanilla","red","green","blue" ];
            JComboBox flavour = new JComboBox( ls );
            // ensure all three choices will be displayed without scrolling.
            flavour.setMaximumRowCount( 3 );
            flavour.setForeground( Color.RED );
            flavour.setBackground( Color.WHITE );
            flavour.setFont( new Font( "Dialog", Font.PLAIN, 12 ) );
            // turn off the write-in feature
            flavour.setEditable( false );
            // setting the selection
            flavour.setSelectedIndex( 0 );
            // alternatively, by value.

            // Compares against defined items with. .equals, not ==.
            // For custom objects will want a custom equals method.
            // Selected items work best with enums.
            flavour.setSelectedItem( "chocolate" );

            // add components
            //flavour.addItemListener( theListener );


donew = swing.action(
  name:'New     ',
  closure:this.&doNew,
  mnemonic:'n',
  accelerator: createShortcut(KeyEvent.VK_N)
)

// Uses Cmd-O/Ctrl+O
openAction = swing.action(
    name:'Open        ', closure: this.&openText, mnemonic: 'O', accelerator: createShortcut(KeyEvent.VK_O)
)

reo = swing.action(
  name:'Open Recent    ',
  closure:this.&reopenText,
  mnemonic:'r',
  accelerator: createShortcut(KeyEvent.VK_R)
)

save = swing.action(
  name:'Save    ',
  //closure:{},
  enabled:saveflag,
  closure:this.&saveText,
  mnemonic:'s',
  accelerator: createShortcut(KeyEvent.VK_S)
)

saveAs = swing.action(
  name:'SaveAs   ',
  enabled:true,
  closure:this.&saveAsText,
  mnemonic:'d',
  accelerator: createShortcut(KeyEvent.VK_D)
)

closeup = swing.action(
  name:'Close',
  closure:this.&doClose,
  mnemonic:'w',
  accelerator: createShortcut(KeyEvent.VK_W)
)

exit = swing.action(
  name:'Exit    ',
  closure:this.&exitText,
  mnemonic:'e',
  accelerator: createShortcut(KeyEvent.VK_E)
)

// text and background color settings
dopickbg = swing.action(
  name:'BG Color    ',
  closure:this.&doPick,
  mnemonic:'b',
  accelerator: createShortcut(KeyEvent.VK_B)
)

dopickfg = swing.action(
  name:'Text Color    ',
  closure:this.&doPickText,
  mnemonic:'t',
  accelerator: createShortcut(KeyEvent.VK_T)
)

dofont = swing.action(
  name:'Change Text',
  closure:this.&doFont,
  mnemonic:'u',
  accelerator: createShortcut(KeyEvent.VK_U)
)


JScrollPane scrollPane = new JScrollPane(gta)
scrollPane.setMinimumSize(new java.awt.Dimension(300,500))
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED)
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
TextLineNumber tln = new TextLineNumber(gta);
scrollPane.setRowHeaderView( tln );

BufferedImage myPicture = ImageIO.read(new File("resources/help.png"));
JLabel picLabel = new JLabel(new ImageIcon(myPicture));

//pane.add(flavour);
def boxone = swing.hbox {
	widget(picLabel)
	widget(new JLabel(' File : '))
	widget(jtf)
	widget(rowcol)
	widget(flavour)
}


// add constraints to lower pane
panelx.add(scrollPane, c);

//Create a split pane with the two scroll panes in it.
splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boxone, panelx);
splitPane.setResizeWeight(0.1); // <-- here
splitPane.setOneTouchExpandable(true);
splitPane.setDividerLocation(30);

//Provide minimum sizes for the two components in the split pane
Dimension minimumSize = new Dimension(600, 360);
panelx.setMinimumSize(minimumSize);

//         frame.setPreferredSize(new Dimension(300, 300));


JFrame.setDefaultLookAndFeelDecorated(true)
gui = swing.frame(id:'FR',title:fileName, layout:new BorderLayout(),minimumSize:[500,300], preferredSize:[700,500], pack:false, show:false, location:[200,200], defaultCloseOperation:WC.EXIT_ON_CLOSE)
{
        menuBar {
                menu(text:'File',  mnemonic:'F')   //,accelerator: createShortcut(KeyEvent.VK_F))   //, accelerator:'ctrl F' ) 
                {
                         menuItem() 
                         {
                             action(donew)
                         }
                         menuItem() 
                         {
			     action(openAction)
                         }
                         menuItem() 
                         {
                             action(reo)
                         }
                         separator()
                         menuItem() 
                         {
                             action(closeup)
                         }
                         menuItem(id:'mi1') 
                         {
                             action(save,enabled:saveflag)
                         }
                         menuItem(id:'mi2') 
                         {
                             action(saveAs)
                         }
                         separator()
                         menuItem() 
                         {
                             action(exit)
                         }
                     
                     } // end of menu

                menu(text:'Settings') 
                {
                         menuItem() 
                         {
                             action(dopickfg)
                         }
                         menuItem() 
                         {
                             action(dopickbg)
                         }
                         menuItem() 
                         {
                             action(dofont)
                         }
                } // end of menu

        } // end of menubar


        //spinner1 = spinner(model: sModel);  //size:[10, 10], model: sModel)
	    container(splitPane);

} // end of gui

gui.pack()
gui.show()

getList();
println "\n\n--> Last Edited was '${lastedited}' logCount="+logCount
def fi = new File(lastedited);
if (fi.exists())
{
	getText(fi)
} // end of if

// ====================================

def doPick(event)
{
        	ColorPicker cp = new ColorPicker();
        	String nc = cp.getColor();
        	println "===> new color:"+nc.toString();

	        def rgb = null;
        	if (nc!=null)
        	{
            	rgb = cp.hex2Rgb(nc); 
	            println "===> new color:"+nc.toString()+" as "+rgb;
        	    if (rgb != null) 
            	{
                	gta.setBackground(rgb);
            	} // end of if
        	} // end of if

} // end of pick

// get foreground text color
def doPickText(event)
{
        	ColorPicker cp = new ColorPicker();
			cp.setTitle("Choose Text Color");
        	String nc = cp.getColor();
        	println "===> new color:"+nc.toString();

	        def rgb = null;
        	if (nc!=null)
        	{
            	rgb = cp.hex2Rgb(nc); 
	            println "===> new color:"+nc.toString()+" as "+rgb;
        	    if (rgb != null) 
                {
                	gta.setForeground(rgb);
        	    } // end of if
        	} // end of if
} // end of pick


// change text 
def doFont(event)
{
			JFontChooser fontChooser = new JFontChooser();
			fontChooser.setSelectedFont(gta.getFont());
			int result = fontChooser.showDialog(null);
			if (result == JFontChooser.OK_OPTION)
			{
				this.font = fontChooser.getSelectedFont(); 
				System.out.println("Selected New Font : " + font); 
                gta.setFont(font);
			} // end of if            
} // end of change font


def doNew(event)
{
	saveflag=false;
	swing.mi1.enabled=saveflag;

	def getProperty = System.&getProperty  
	def home = getProperty("user.home")
	fileName = ""
	jtf.text = ""
	gui.title = ""
	gta.setText("")
	gta.setCaretPosition(0)
	gta.requestFocus();
	gta.requestFocusInWindow();
}


def doClose(event)
{
	saveflag=false;
	swing.mi1.enabled=saveflag;

	def getProperty = System.&getProperty  
	def home = getProperty("user.home")
	fileName = ""
	jtf.text = ""
	gui.title = ""
	rowcol.setText("");
	gta.setText("")
	gta.setCaretPosition(0)
	gta.requestFocus();
	gta.requestFocusInWindow();
}


def reopenText(event) {
	String[] choices = getList();
	choices.each{ e-> println "-->$e";} 

	String s = (String)JOptionPane.showInputDialog(
                    null,
                    "Choose a file to re=open or ESC",
                    "Re-Open Dialog",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choices,
                    choices[0]);
  
	if (s==null) return;

	def fh2 = new File(s);
	if ( !( fh2.exists() ) )
	{
		JOptionPane.showMessageDialog(null, "Cannot find ${s} to re-open", "Missing File", JOptionPane.ERROR_MESSAGE);
		return;
	} // end of if


		getText(fh2);
		gui.repaint();
	return;


	// chooser.setFileFilter(filter1);
	int returnValue = chooser.showOpenDialog(gui);
	if ((returnValue == javax.swing.JFileChooser.APPROVE_OPTION)) 
	{  
		def fh = chooser.getSelectedFile();
		getText(fh);
		gui.repaint();
	} // end of if
} // end of reopenText


def int setCursor(fn)
{
	int at = 0;
	def fnlc = fn.toLowerCase();
	log = new File(logName).text;
	log.eachLine{ln ->
		int i = ln.indexOf(":=");
		if (i>-1)
		{
			def na = ln.substring(0,i);
			def nalc = na.toLowerCase();
			def va = ln.substring(i+2).toLowerCase();
			
			int j = va.indexOf(';');
			if (j>-1)
			{
				def v = va.substring(0,j);
				if (fnlc.equals(nalc)) { println "... they match "+v; 
				try{at = v as int;} catch(Exception f) {}  
				} // end of if
			} //end if
			
		} // end of if
		
	} // end of log
	
	return ( at < 1 ) ? 0 : at;
} // end of


def getText(def fh)
{
		saveflag=true;
		swing.mi1.enabled=saveflag;

		fileName = fh.toString()
		int at = setCursor(fileName);
		jtf.text = fileName
		gui.title = getMetadata(fh)
		story = fh.text;            //getText('UTF-8')
		gta.setText(story)
		gta.setCaretPosition(at);
	        this.getTextAreaCursorPosition();    

		gta.requestFocus();
		gta.requestFocusInWindow();
} // end of getText


def getMetadata(def fh)
{
	String sz = fh.length();
	String r = (fh.canRead()) ? 'read' : '';
	String w = (fh.canWrite()) ? '/write' : '';
	long timestamp = fh.lastModified();
	Date when = new Date(timestamp);
	SimpleDateFormat sdf = new SimpleDateFormat( "EEEE yyyy/MM/dd hh:mm:ss aa zz" );
	sdf.setTimeZone(TimeZone.getDefault()); // local time
	String display = sdf.format(when)+"  ${sz} bytes ($r$w)";
	return display;
}

def openText(event) 
{
	//         chooser.setFileFilter(filter1);
	int returnValue = chooser.showOpenDialog(gui);
	if ((returnValue == javax.swing.JFileChooser.APPROVE_OPTION)) 
	{  
   		def fh = chooser.getSelectedFile();
		//File f = fh.getSelectedFile();
		def path = fh.getAbsolutePath();
		println "openText chose file "+fh.toString()+" in folder "+path;

		getText(fh);
		gui.repaint();
	} // end of if
} // end of openText


def saveText(event) 
{ 
	fileName = jtf.text
	def f = new File(fileName);
	def path = f.getAbsolutePath();
	println "saveText chose to keep file "+fileName+" in folder "+path;

	dosave( gta.getText(), fileName ); 
}

def saveAsText(event) 
{ 
	fileName = jtf.text
        def fc = new JFileChooser()
	fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
	fc.setSelectedFile(new File(fileName));

	int returnVal = fc.showSaveDialog(null);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
		fileName = fc.getSelectedFile().toString();
		File f = fc.getSelectedFile();
		def path = f.getAbsolutePath();
		println "saveAsText saved file "+fileName+" into folder "+path;
		jtf.text = fileName
		saveflag=true;
		swing.mi1.enabled=saveflag;
	} // end of if
	dosave( gta.getText(), fileName ); 
}

def dosave(String story, String outName)
{
	def getProperty = System.&getProperty  
	def home = getProperty("user.home")
	def thisName = home+"/Desktop/sample.txt"
	int n = 0;
	if (new File(outName).exists() )  
	{
		//default icon, custom title
		n = JOptionPane.showConfirmDialog(null,
	    	"Would you like to replace this file ?\n${outName}",
    		"An Existing File Will Be Over-written",
    		JOptionPane.YES_NO_OPTION);
		println "JOptionPane gave $n as the answer"
	} // end of if

	if (n==0)
	{
		int j = gta.getCaretPosition()
		thisName=outName;
		def out = new File(thisName)
		println "===> Writing ${thisName} with caret at $j"

		try{
			//out.delete()
			saveflag=false;
			out.write(story)
			keeper(thisName, j);
		} catch(Exception xx)
		{
			JOptionPane.showMessageDialog(null, "Cannot save this :\n${thisName}\nPlease correct path or filename ", "Failed to Save", JOptionPane.ERROR_MESSAGE);
		}
	} // end of if
} // end of dosave


def keeper(String outName, int at)
{
	def getProperty = System.&getProperty  
	def home = getProperty("user.home")
	def log = new File(logName)

	today = new Date() 
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ssz");
	String formattedDate = formatter.format(today);
	log.append("${outName}:=${at}; ${formattedDate}"+"\n")

	getList();
	println "--> Last Edited (keeper) was "+ lastedited
} // end of keeper


// pull up all prior edited files
def String[] getList()
{
	def getProperty = System.&getProperty  
	def home = getProperty("user.home")
	def logName = home+"/.TextEditor.log"

	// global vars
	lastedited="";
	logCount = 0;
	logEntries=[]

	if ( !( new File( logName).exists() ) ) return logEntries;

	log = new File(logName).text;
	log.eachLine{ln ->
		println "--->"+ln; 
		int i = ln.indexOf(":=");
		if (i>-1)
		{
			lastedited=ln.substring(0,i);
			logEntries.push(lastedited);
       			++logCount; 
		}
	} // end of log

	int ct = (logCount > 20) ? 20  : 0 ;
	int rm = logCount - ct;
	ct=0;
	if (rm > 0) {logEntries.drop(rm);}
	logEntries.reverse(true);
	logEntries.each{e->
		//println "logEntries(${++ct}).each=${e}"; 
	} // end of each
	println "---------\n"
	return logEntries;
} // end of keeper


def exitText(event) 
{
	System.exit(0);
}


	def getTextAreaCursorPosition() 
	{
 		int c = TextUtilities.getColumnAtCaret(gta); 
 		int r = TextUtilities.getLineAtCaret(gta);  
 		rowcol.setText("row:$r  col:$c");
 		//println rc;
	} // end of method



