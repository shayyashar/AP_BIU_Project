package views;

import test.Graph;

import java.util.List;
import java.util.ArrayList;

public class HtmlGraphWriter {
    public static List<String> getGraphHTML(Graph graph) {
        List<String> html = new ArrayList<>();
        html.add("<html>");
        html.add("<body>");
        html.add("<canvas id='graphCanvas'></canvas>");
        html.add("<script>");
        html.add("var canvas = document.getElementById('graphCanvas');");
        html.add("var ctx = canvas.getContext('2d');");

        // Example to draw the graph, replace with actual logic
        html.add("ctx.fillStyle = 'red';");
        html.add("ctx.fillRect(10, 10, 50, 50);");

        html.add("</script>");
        html.add("</body>");
        html.add("</html>");
        return html;
    }
}