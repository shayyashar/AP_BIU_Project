package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class SquareAgent  implements Agent {

    public String inputTopicName;
    public String outputTopicName;

    public SquareAgent(String[] subs, String[] pubs) {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        this.inputTopicName = subs[0];
        tm.getTopic(this.inputTopicName).subscribe(this);
        this.outputTopicName = pubs[0];
        tm.getTopic(this.outputTopicName).addPublisher(this);
    }


    @Override
    public String getName() {
        return "Square Agent";
    }

    @Override
    public void reset() {

    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            TopicManager tm = TopicManagerSingleton.get();
            tm.getTopic(this.outputTopicName).publish(new Message(msg.asDouble * msg.asDouble));
        }
    }

    @Override
    public void close() {

    }
}
