
package servlets;

import test.RequestParser.RequestInfo;
import test.Servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlLoader implements Servlet {
    private final String htmlDirectory;

    public HtmlLoader(String htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
    }

    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws IOException {
        String filePath = htmlDirectory + "/" + String.join("/", ri.getUriSegments()[ri.getUriSegments().length - 1]);
        if (Files.exists(Paths.get(filePath))) {
            byte[] content = Files.readAllBytes(Paths.get(filePath));
            toClient.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n").getBytes());
            toClient.write(content);
        } else {
            toClient.write("HTTP/1.1 Status Code 404 Not Found\r\n\r\n".getBytes());
        }
        toClient.close();
    }

    @Override
    public void close() throws IOException {
        // Close resources if any
    }
}