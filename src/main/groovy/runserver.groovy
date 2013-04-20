import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.*
import groovy.servlet.*

@Grab(group='org.mortbay.jetty', module='jetty-embedded', version='6.1.14')
def startJetty() {
    def jetty = new Server(9090)
    
    def context = new Context(jetty, '/', Context.SESSIONS)  // Allow sessions.
    context.resourceBase = '.'  // Look in current dir for Groovy scripts.
    context.addServlet(GroovyServlet, '*.groovy')  // All files ending with .groovy will be served.
    context.addServlet(TemplateServlet, '*.gtpl')  // All files ending with .groovy will be served.    
    context.addServlet("servlet_info", '/view')
    context.setAttribute('version', '1.0')  // Set an context attribute.
    
    jetty.start()
}

println "Starting Jetty, press Ctrl+C to stop."
startJetty()