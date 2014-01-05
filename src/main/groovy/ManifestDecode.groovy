public class Result
{
    int leadingChars =0;
    String line="";
    String fullLine="";
    int wordCount = 0;
    boolean comment = false;
} // end of class


def count(ln)
{
    def r = new Result();
    r.line = ln;
    print  "["+ln+"]"

    ln = ln.replace('\t','    ')
    r.fullLine = ln;
    
    int i = 0;
    def charFlag=false
    def stopFlag=false

    ln.each
    { x ->
        charFlag = (( x== ' ' || x== '-' ) && !stopFlag ) ? true : false;
        if (charFlag) {i++};
        if (!charFlag) stopFlag = true;
    } // end of while
        
    def tx = ln.substring(i).trim();
    def words = tx.split(':')
    print "\t\t[words=${words.size()}]"
    return i;
} // end of count


def showIt(ln) 
{
    def lns = ln.trim()
    def flag = (lns.startsWith('---') || (lns.startsWith('#')) )
    if (!flag) println "\t[leading chars:"+count(ln)+']';
} // end of def

new File('.').eachFile{ fn ->

def ab = fn.canonicalFile.toString()

    if (fn.toString().toLowerCase().startsWith("./manifest") )
    {
        println fn.toString()+" :"+ab;
        //def tx = fn.readLines()
        fn.eachLine{ln ->  
            showIt(ln)
        } // end of each        
    }    
}