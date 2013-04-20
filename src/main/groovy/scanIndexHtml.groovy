import javax.swing.*;  
// after running this code once the index.html file is renamed as index.html.bak and used as input
// this code will ask user for a link or html file name for each <a href="#"> entry then build a series of html pages, one for each
def say(t) {println t;}
say "---- starting ----"

def path = "/Volumes/Media/webdevelopment/"

def fc = new JFileChooser(path);
fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
int option = fc.showOpenDialog(null);
if (option == JFileChooser.APPROVE_OPTION) 
{
    path = fc.getSelectedFile().getAbsolutePath()+"/"
    def  pwd = fc.getCurrentDirectory() 
    println "${path} pwd=${pwd} \n"
}


def fn = path+"index.html"
def bak = path+"index.html.bak"
if (new File(bak).exists())
{   
    say "index.html.bak already exists, so you may have run this code before"; 
    JOptionPane.showMessageDialog(null, "index.html.bak already exists,\nso you may have run this code before.");
    System.exit(0);
}

new File(path).eachFileMatch(~/.*.html/) 
{ 
    if (!it.name.equals("index.html")) 
    {
        def fd = new File(path+it.name);
        fd.delete();
        say "deleted "+it.name;
    } // end of if 
} // end of each

//JOptionPane.showMessageDialog(null, "Ok to continue");

new File(fn).renameTo(new File(bak))  


def fo = path+"index.html"
def fi = new File(bak);
def file3 = new File(fo);
def sb = new StringBuilder()

if (file3.exists()) file3.delete()

int r = 1;
def links =[]
def tx
def link2

file3.withWriter('UTF-8') { writer ->

fi.eachLine{ln ->
    tx = ln
    int i = tx.indexOf("href=\"")

    while(i>-1)
    {
        int j = i + 6
        def txt = tx.substring(0,j)
        def tx2 = tx.substring(j)
        int k = tx2.indexOf("\"");
        int l = j + k   
        def xx = tx.substring(j,l)
        tx2 = tx.substring(l)
        def tag = "link"
        def link = tag
        
        
        if (xx.equals("#"))
        {
            def tl = tx2
            if (tx2.size()>50) tl = tx2.substring(0,50)
            def inString = JOptionPane.showInputDialog(null,tl,"${tag}${r}",JOptionPane.PLAIN_MESSAGE);
            link = (inString?.length()>0) ? inString.toLowerCase() : tag + r++;
            link2 = (link.indexOf(".")< 0 ) ? link + ".html" : link;
        } // end of if
        else
        {
            link2 = xx;
        }
        
        println "${ln}\ni=$i j=${j} k=${k} l=${l} \n${txt}|${xx}|${tx2}| link=|${link}| "
        
        links << link2;
        tx = txt + link2 + tx2
        println tx;
        i = tx.indexOf("href=\"#")
    }

    // write new index html
    writer.writeLine(tx)
    sb += tx;
    sb += '\n'
  } // end of each

  //writer.write('================')
} // end of withWriter


// show links
println "\nLink Pages are :"
links.sort().unique().each{ 

    if (!it.equals("index.html"))
    {
        println "   "+it
        fo = path + it.trim()  //+".html"
        file3 = new File(fo);
        if (file3.exists()) file3.delete()
        file3.withWriter('UTF-8') 
        {   
            writer ->
                writer.writeLine(sb)
        } // end of withWriter
    } // end of if
} // end of withWriter

println "========================="