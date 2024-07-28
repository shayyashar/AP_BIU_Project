package servlets;

import test.RequestParser;
import test.Servlet;
import java.io.*;

public class HtmlLoader implements Servlet {
    private String baseDirectory;

    public HtmlLoader(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
        String filePath = baseDirectory + "/" + String.join("/", ri.getUriSegments());
        File file = new File(filePath);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(toClient));
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                }
            }
        } else {
            out.println("<html><body><h1>404 Not Found</h1></body></html>");
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {

    }
}