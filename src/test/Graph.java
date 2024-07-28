package test;

import java.util.ArrayList;
import java.util.Collection;

import test.TopicManagerSingleton.TopicManager;

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
        String vertexName = nodeType + nodeName;
        for (Node vertex : this) {
            if (vertex.getName().equals(vertexName)) {
                return vertex;
            }
        }
        Node vertex = new Node(vertexName);
        this.add(vertex);
        return vertex;
    }

    public void createFromTopics() {
        TopicManager tm = TopicManagerSingleton.get();

        Collection<Topic> collTopics =  tm.getTopics();

        for (Topic t : collTopics) {
            String topicName = t.name;
            Node topicNode = getOrCreateNode(topicName, 'T');

            for (Agent a : t.subs) {
                Node agentNode = getOrCreateNode(a.getName(), 'A');
                topicNode.addEdge(agentNode);
            }

            for (Agent a: t.pubs) {
                Node agentNode = getOrCreateNode(a.getName(), 'A');
                agentNode.addEdge(topicNode);
            }
        }
    }    

    
}
