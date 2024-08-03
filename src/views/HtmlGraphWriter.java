package views;

import test.Graph;
import test.Node;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class HtmlGraphWriter {
    public static String getGraphHTML(Graph graph) {
        List<String> html = new ArrayList<>();
        html.add(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Directed Graph</title>
                            <script src="https://d3js.org/d3.v7.min.js"></script>
                            <style>
                                .link {
                                    fill: none;
                                    stroke: #666;
                                    stroke-width: 4.5px;
                                    marker-end: url(#arrow); /* Add arrow markers */
                                }
                                .node text {
                                    color: black;
                                    font: 12px sans-serif;
                                    pointer-events: none;
                                }
                            </style>
                        </head>
                        <body>
                        <svg width="500" height="500"></svg>
                        <script>
                        """
        );

        ArrayList<Map<String, String>> nodes = new ArrayList<>();
        ArrayList<Map<String, String>> edges = new ArrayList<>();

        for (Node node : graph) {
            Map<String, String> vertex = new HashMap<>();
            vertex.put("id", node.getName());
            vertex.put("name", node.getName().substring(1));
            if (node.getName().startsWith("A")) {
                vertex.put("type", "Agent");
            }
            else if (node.getName().startsWith("T")){
                vertex.put("type", "Topic");
            }
            for (Node neigh : node.getEdges()) {
                Map<String, String> edge = new HashMap<>();
                edge.put("source", node.getName());
                edge.put("target", neigh.getName());
                edges.add(edge);
            }
            nodes.add(vertex);
        }

        html.add("var nodes=" + nodes.toString()
                .replace("}", "\"}")
                .replace("=", ":\"")
                .replace(",", "\",")
                .replace("}\"", "}") + ";");

        html.add("var links=" + edges.toString()
                .replace("}", "\"}")
                .replace("=", ":\"")
                .replace(",", "\",")
                .replace("}\"", "}") + ";");



        html.add("""
                
                var svg = d3.select("svg"),
                    width = +svg.attr("width"),
                    height = +svg.attr("height");
                // Add marker for arrows
                svg.append('defs').append('marker')
                    .attr('id', 'arrow')
                    .attr('viewBox', [0, 0, 5, 5])
                    .attr('refX', 5)
                    .attr('refY', 3)
                    .attr('markerWidth', 3)
                    .attr('markerHeight', 3)
                    .attr('orient', 'auto-start-reverse')
                    .append('path')
                    .attr('d', 'M 0 0 L 10 5 L 0 10 z');
                var simulation = d3.forceSimulation(nodes)
                    .force("link", d3.forceLink(links).id(function(d) { return d.id; }))
                    .force("charge", d3.forceManyBody().strength(-1000))
                    .force("center", d3.forceCenter(width / 2, height / 2));
                var link = svg.append("g")
                    .attr("class", "links")
                    .selectAll("line")
                    .data(links)
                    .enter().append("line")
                    .attr("class", "link");
                var node = svg.append("g")
                    .attr("class", "nodes")
                    .selectAll("g")
                    .data(nodes)
                    .enter().append("g")
                    .attr("fill", function(v) { return v.color });
                node.each(function(d) {
                    if (d.type === 'Agent') {
                        d3.select(this).append("circle")
                            .attr("r", 10)
                            .attr("fill", 'blue');
                    } else if (d.type === 'Topic') {
                        d3.select(this).append("rect")
                            .attr("width", 20)
                            .attr("height", 20)
                            .attr("x", -10)
                            .attr("y", -10)
                            .attr("fill", 'green');
                    }
                });

                node.append("text")
                    .attr("dy", -3)
                    .attr("x", 12)
                    .text(function(d) { return d.name; })
                    .attr("fill", 'black');
                simulation
                    .nodes(nodes)
                    .on("tick", ticked);

                simulation.force("link")
                    .links(links);

                function ticked() {
                    link
                        .attr("x1", function(d) { return d.source.x; })
                        .attr("y1", function(d) { return d.source.y; })
                        .attr("x2", function(d) { return d.target.x; })
                        .attr("y2", function(d) { return d.target.y; });

                    node
                        .attr("transform", function(d) {
                            return "translate(" + d.x + "," + d.y + ")";
                        });
                }
                </script>
                </body>
                </html>""");

        return String.join("", html);
    }
}