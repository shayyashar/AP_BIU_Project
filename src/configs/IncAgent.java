package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class IncAgent  implements Agent {

    public String inputTopicName;
    public String outputTopicName;

    public IncAgent(String[] subs, String[] pubs) {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        this.inputTopicName = subs[0];
        tm.getTopic(this.inputTopicName).subscribe(this);
        this.outputTopicName = pubs[0];
        tm.getTopic(this.outputTopicName).addPublisher(this);
    }


    @Override
    public String getName() {
        return "Inc Agent";
    }

    @Override
    public void reset() {

    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            TopicManager tm = TopicManagerSingleton.get();
            tm.getTopic(this.outputTopicName).publish(new Message(msg.asDouble + 1));
        }
    }

    @Override
    public void close() {

    }
}
