package servlets;

import test.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class TopicDisplayer implements Servlet {
    @Override
    public void handle(RequestParser.RequestInfo ri, OutputStream toClient) throws IOException {
        String topic = ri.getParameters().get("topic");
        String message = ri.getParameters().get("message");

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(topic).publish(new Message(message));

        PrintWriter writer = new PrintWriter(toClient, true);
        StringBuilder toWrite = new StringBuilder();
        writer.println("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n");
        toWrite.append("<!DOCTYPE html><html lang=\"en\"><body><table border='1' style='margin:auto;margin-top:100px'>");
        toWrite.append("<tr><th>Topic</th><th>Value</th></tr>");
        for (Topic t : tm.getTopics()) {
            toWrite.append("<tr><td>" + t.name + "</td><td>" + t.last_message.asDouble + "</td></tr>");
        }
        toWrite.append("</table></body></html>");
        writer.println(toWrite);
    }

    @Override
    public void close() throws IOException {
        // Cleanup code if needed
    }
}