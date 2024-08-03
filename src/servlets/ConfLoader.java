package servlets;

import test.GenericConfig;
import test.Graph;
import test.Servlet;
import test.RequestParser;
import views.HtmlGraphWriter;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfLoader implements Servlet {
    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {

        Map<String, String> params = ri.getParameters();

        String filename = "";
        for(String param: params.keySet()) {
            if (param.equals("filename")) {
                filename = params.get("filename").replace("\"", "");
            }
        }

        String fullPath = "config_files/" + filename;

        GenericConfig genConf = new GenericConfig();
        genConf.setConfFile(fullPath);

        genConf.create();

        Graph graph = new Graph();

        graph.createFromTopics();

        String htmlGraph = String.valueOf(HtmlGraphWriter.getGraphHTML(graph));

        PrintWriter writer = new PrintWriter(toClient, true);


        writer.println(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n"));

        writer.println(htmlGraph);

        System.out.println("here");
    }

    @Override
    public void close() throws IOException {

    }
}