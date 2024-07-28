package servlets;

import test.Graph;
import test.Servlet;
import test.RequestParser;
import views.HtmlGraphWriter;
import java.io.*;

public class ConfLoader implements Servlet {
    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
//        String boundary = "--" + ri.getHeaders().get("Content-Type").split("boundary=")[1];
//        InputStream input = ri.getContentStream();

        File tempFile = File.createTempFile("upload", ".tmp");
//        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = input.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        }

        Graph graph = new Graph();
        String htmlGraph = String.valueOf(HtmlGraphWriter.getGraphHTML(graph));

        PrintWriter out = new PrintWriter(new OutputStreamWriter(toClient));
        out.println(htmlGraph);
        out.flush();

        tempFile.delete();
    }

    @Override
    public void close() throws IOException {

    }
}