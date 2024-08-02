package servlets;

import test.GenericConfig;
import test.Graph;
import test.Servlet;
import test.RequestParser;
import views.HtmlGraphWriter;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfLoader implements Servlet {
    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
//        String boundary = "--" + ri.getHeaders().get("Content-Type").split("boundary=")[1];
//        InputStream input = ri.getContentStream();

        String content = new String(ri.getContent());

        // Define a regex pattern to extract the filename
        String filenamePattern = "filename=\"([^\"]+)\"";

        // Compile the pattern
        Pattern pattern = Pattern.compile(filenamePattern);
        Matcher matcher = pattern.matcher(content);

        String filename = "";
        if (matcher.find()) {
            filename = matcher.group(1);
        }

        String fullPath = "config_files/" + filename;

        GenericConfig genConf = new GenericConfig();
        genConf.setConfFile(fullPath);

        genConf.create();

        Graph graph = new Graph();

        graph.createFromTopics();

        String htmlGraph = String.valueOf(HtmlGraphWriter.getGraphHTML(graph));

        toClient.write("hiiiii".getBytes());
//
//        PrintWriter out = new PrintWriter(new OutputStreamWriter(toClient));
//        out.println(htmlGraph);
//        out.flush();

//        tempFile.delete();
        System.out.println("here");
        toClient.close();
    }

    @Override
    public void close() throws IOException {

    }
}