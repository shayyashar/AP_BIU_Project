import servlets.ConfLoader;
import servlets.HtmlLoader;
import servlets.TopicDisplayer;
import test.HTTPServer;
import test.MyHTTPServer;

public class Main {
    public static void main(String[] args) throws Exception{
        HTTPServer server=new MyHTTPServer(8080,1);
        server.addServlet("GET", "/publish", new TopicDisplayer());
//        server.addServlet("POST", "/upload", new ConfLoader());
        server.addServlet("GET", "/app/", new HtmlLoader("html_files"));
        server.start();
        System.in.read();
        server.close();
        System.out.println("done");
    }
}