import javax.swing.JOptionPane;
class MetaLine
{
    int leadingSpaces
    boolean emptyLine
    int pageAt
}

// for the local output file which is a copy of the online version
def file3
def fn

// set when a line of text is not blank
boolean hasContent = false

// how many leading blanks on this line ?
def i=0;

def para = ""

def addr(at) { return "http://www.ietf.org/rfc/rfc${at}.txt"  }

Integer.metaClass.getSeconds = { ->
    delegate * 1000
}

// logic to get the RFC number
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
    }
} // end of while


// ==========================================================================
// got rfc number, go get dcoument and create a local .txt version of it
if (number > 0)
{
    // with this logic, we could do more than a single RFC into it's own output file 
  [number].each
  { 
    def ad = addr(it)
    print ad 
    fn = "./rfc${it}.txt";
    println " and the output fn = "+fn    
    
    file3 = new File(fn)
    
    //localAddress: new InetSocketAddress(ad, 80)
    def url = ad.toURL() 
    
    // this version does not need parameters
    //def content = url.getText()
    
    // Get content of URL with parameters.
    def content = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: ['User-Agent': 'Groovy Sample Script'])
                          
                          
    //println "content="+content
    println "\n\n======================================================================="
    
    
 file3.withWriter('UTF-8') { writer ->    
    content.eachLine{
    i=0;
    
    // count leading blanks on each line 
    if (it.size()>0)
    {
        if (hasContent)
        {
            para += it
        }
        else
        {
            para = it
            hasContent = true
        } // end of else
        
        para += '\n'
        flag = true
        i = 0
        
        while(flag)
        {
            // turn off flag once a non-blank char. is found on this line
            if (!(it[i]==' '))
            { 
                flag = false; 
            } // end of if
            
            // if our pointer is after the end of this line, fall out of loop
            if ( i > it.size() ) 
            {
                flag = false
            } // end of if
            // since this char. was a blank, bump our local pointer, which is alo a count of blanks at the start of this line
            if (flag) i+=1;
        } // end of while
    }
    
    // handle case when line has no content
    else
    {
        // did this para have any content ? if so, write it out
        if (para.size() > 0)
        {
            writer.write(para)
            para = ""
        } // end of if
        
        writer.write("\n")
        hasContent = false
    } // end of else

    // print out RFC file stats
    println "-> leading blanks=${i} sz=${it.size()} hasContent=${hasContent} "+it}

  } // end of each

    if (para.size() > 0)
    {
        writer.write(para)
        writer.write("\n")
    }
                      
 } // end of withWriter

    
    if (content!=null && content.size()>0)
    {
        println "\n\n===================================================================\nthere are ${content.size()} bytes available"
    }
    
} // end of if RFC




println "------- the end -----------"
