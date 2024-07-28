package test;

import java.util.ArrayList;

public class Topic {
    public final String name;
    public ArrayList<Agent> subs;
    public ArrayList<Agent> pubs;
    public Message last_message;

    Topic(String name) {
        this.name = name;
        this.subs = new ArrayList<Agent>();
        this.pubs = new ArrayList<Agent>();
        this.last_message = new Message(0.0);
    }


    public void subscribe(Agent agent){
        this.subs.add(agent);
    }

    public void unsubscribe(Agent agent){
        this.subs.remove(agent);
    }

    public void publish(Message message){
        this.last_message = message;
        for (Agent agent : this.subs){
            agent.callback(this.name, message);
        }
    }

    public void addPublisher(Agent agent){
        this.pubs.add(agent);
    }

    public void removePublisher(Agent agent){
        this.pubs.remove(agent);
    }
}
