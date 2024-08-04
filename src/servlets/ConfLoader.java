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

        // extract the filename value
        String filename = "";
        for(String param: params.keySet()) {
            if (param.equals("filename")) {
                filename = params.get("filename").replace("\"", "");
            }
        }

        // clear all topics
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.clear();

        // create config from file
        String fullPath = "config_files/" + filename;
        GenericConfig genConf = new GenericConfig();
        genConf.setConfFile(fullPath);
        genConf.create();

        // create graph from the topics
        Graph graph = new Graph();
        graph.createFromTopics();

        // call to the HtmlGraphWriter to get this in html file
        String htmlGraph = String.valueOf(HtmlGraphWriter.getGraphHTML(graph));
        // return to client the output
        PrintWriter writer = new PrintWriter(toClient, true);
        writer.println(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n"));
        writer.println(htmlGraph);

    }

    @Override
    public void close() throws IOException {

    }
}