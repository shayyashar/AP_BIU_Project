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

        PrintWriter out = new PrintWriter(new OutputStreamWriter(toClient));
        out.println("<html><body><table border='1'>");
        out.println("<tr><th>Topic</th><th>Value</th></tr>");
        for (Topic t : tm.getTopics()) {
            out.println("<tr><td>" + t.name + "</td><td>" + t.last_message + "</td></tr>");
        }
        out.println("</table></body></html>");
        out.flush();
    }

    @Override
    public void close() throws IOException {
        // Cleanup code if needed
    }
}