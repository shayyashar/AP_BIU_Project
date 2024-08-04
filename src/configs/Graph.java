package configs;

import java.util.ArrayList;
import java.util.Collection;

import graph.Agent;
import graph.Topic;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node>{

    public boolean hasCycles() {
        for (Node vertex : this) {
            if (vertex.hasCycles()) {
                return true;
            }
        }
        return false;
    }

    private Node getOrCreateNode (String nodeName, char nodeType) {
        // check if the node exists - like singleton
        String vertexName = nodeType + nodeName; // the new name of vertex
        for (Node vertex : this) {
            if (vertex.getName().equals(vertexName)) {
                return vertex; // exists and return it
            }
        }
        // not exists and create and return it
        Node vertex = new Node(vertexName);
        this.add(vertex);
        return vertex;
    }

    public void createFromTopics() {
        TopicManager tm = TopicManagerSingleton.get();

        Collection<Topic> collTopics =  tm.getTopics();

        // for loop on topics
        for (Topic t : collTopics) {
            // create/get topic
            String topicName = t.name;
            Node topicNode = getOrCreateNode(topicName, 'T');

            // all edges from the topic node to agents
            for (Agent a : t.subs) {
                Node agentNode = getOrCreateNode(a.getName(), 'A');
                topicNode.addEdge(agentNode);
            }

            // all edges to the topic node from agents
            for (Agent a: t.pubs) {
                Node agentNode = getOrCreateNode(a.getName(), 'A');
                agentNode.addEdge(topicNode);
            }
        }
    }
}
