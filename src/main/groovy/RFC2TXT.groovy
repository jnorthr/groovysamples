import javax.swing.JOptionPane;
/*
class MetaLine
{
    int leadingSpaces
    boolean emptyLine
    int pageAt
}
*/


// for the local output file which is a copy of the online version
def file3
def fn
def payload
int rfc = 0
linecount = 0

// non-blank line count
lines = 0


// how many leading blanks on this line ?
def i=0;

// holds text forming a paragraph
paragraph = ""
// this address returns the full index of rfc's
def index() { return "http://www.ietf.org/rfc/rfc-index.txt"; }

def addr(at) { return "http://www.ietf.org/rfc/rfc${at}.txt"  }

// pull back the STD-index.txt page similar to the RFC pages
// ??? http://www.rfc-editor.org/rfc/std-index.txt diff or same ???
def std(at) {return "http://www.rfc-editor.org/rfc/std/std-index.txt";}

// http://www.rfc-editor.org/rfc/fyi/fyi-index.txt gives index of FYI's
def fyi(at) {return "http://www.rfc-editor.org/rfc/fyi/fyi-index.txt";}

// ien ??? http://www.rfc-editor.org/rfc/ien/ien-index.txt
def ien(at) {return "http://www.rfc-editor.org/rfc/ien/ien-index.txt";}

// zips and .tar.gz files for a range of RFC's are here: http://www.rfc-editor.org/rfc/tar/
// so pdfrfc1001-1500.tar.gz holds a range of RFC text files as zipped PDF's




// the PDF versions of RFCxxx.txt files are here:
// http://www.rfc-editor.org/rfc/pdfrfc/

// index of Best Current Practice: http://www.rfc-editor.org/rfc/bcp-index.txt

Integer.metaClass.getSeconds = { ->
    delegate * 1000
}

def paragraphStarted()
{
    return ( paragraph.size() > 0 ) ? true : false
}

def documentStarted()
{
    return (lines > 0) ? true : false
}

def pageEnd(txt)
{
    return (txt.indexOf("[Page ")> 0) ? true : false
}


def leadingSpaces(txt)
{
    flag = true
    i = 0
    
    while(flag)
    {
   
        // turn off flag once a non-blank char. is found on this line
        if (!(txt[i]==' '))
        { 
            flag = false; 
        } // end of if
        
        // if our pointer is after the end of this line, fall out of loop
        if ( i > txt.size() ) 
        {
            flag = false
        } // end of if
        
        // since this char. was a blank, bump our local pointer, which is alo a count of blanks at the start of this line
        if (flag) { i+=1 }

    } // end of while

    if (i>0) i-= 1;
    return i
} // end of



// handle each line of text
def doLine(txt, writer)
{
    // count lines, even blank ones
    linecount += 1;
    
    // if line has at least one non-space char. 
    if (txt.trim().size()>0)
    {
        lines += 1;
        i = leadingSpaces(txt)
        if ( paragraphStarted() )
        {
            //para += '\n'
            paragraph += txt.substring(i)  //.trim()
        }
        else
        {
            paragraph = txt
        } // end of else

    } // end of if
    
    // handle case when line has no content
    else
    {  
        // only do this if 1st line of text in document has been found
        if (documentStarted())
        {
            // did this para have any content ? if so, write it out
            if (paragraphStarted())     // para.size() > 0)
            {
                paragraph += '\n'
                writer.write(paragraph)
                paragraph = ""
                //if (!(pageEnd(txt))) 
                writer.write('\n')
            } // end of if
            
            
        } // end of if
        
    } // end of else

} // end of doLine()




// logic to get the RFC number
def getRFC()
{
    String input
    def number = 0
    def flag = false
    while(!flag)
    {
        input = JOptionPane.showInputDialog("Enter RFC Number :");
        if (input==null)
        { 
            println "cancel session"
            number = 0
            flag = true
        }
        else
        {
            println "Is this a number ? "+input.isInteger()
            flag = input.isInteger()
            print("User said : {"+input+"}");
            if (flag)
            {
                number = input as int
            }
            println "; which is the number $number"
        } // end of else 
               
    } // end of while
    
    return number;
} // end of method


// ==========================================================================
// got rfc number, go get dcoument and create a local .txt version of it
rfc = getRFC()
if (rfc > -1)
{
    // with this logic, we could do more than a single RFC into it's own output file 
  [rfc].each
  { 
	def num = (it>0) ? it : "-index"
    def ad = addr(num)
    print ad 
    fn = "/Users/jim/Desktop/rfc${num}.txt";
    println " and the output fn = "+fn    
    
    file3 = new File(fn)
    
    //localAddress: new InetSocketAddress(ad, 80)
    def url = ad.toURL() 
    
    // this version does not need parameters
    //def content = url.getText()
    
    // Get content of URL with parameters.
    payload = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: ['User-Agent': 'Groovy Sample Script'])
                          
                          
    println "\n======================================================================="
    
    
    file3.withWriter('UTF-8'){ writer ->    

        payload.eachLine{tx ->
            i=0;
           
            if (pageEnd(tx)) print "END OF PAGE "
            
            doLine(tx, writer);
            
            if (pageEnd(tx))
            {
                paragraph += '\n'
            }
    
            // print out RFC file stats
            if (linecount < 10)
            {
                println "-> leading blanks=${i} sz=${tx.size()} paragraph=${paragraphStarted()} started=${documentStarted()} endofpage=${pageEnd(tx)} lines=${lines}\n"+tx;
            }
        } // end of each

    
        // dump any trailing text
        if (paragraphStarted())
        {
            writer.write(paragraph)
        } // end of if

    } // end of withWriter for local output text

  } // end of each RFC file get
                             
} // end of if RFC


    
if (payload!=null && payload.size()>0)
{
    println "\n===================================================================\nthere are ${payload.size()} bytes available"
}


println "------- the end -----------"