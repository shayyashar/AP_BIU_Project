package servlets;

import configs.GenericConfig;
import configs.Graph;
import graph.TopicManagerSingleton;
import server.RequestParser;
import views.HtmlGraphWriter;
import java.io.*;
import java.util.Map;

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

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.clear();

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