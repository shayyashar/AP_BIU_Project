package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

import java.util.Objects;

public class PlusAgent implements Agent {

    public String topicName1;
    public double x;
    public String topicName2;
    public double y;
    public String outputTopicName;

    public PlusAgent(String[] subs, String[] pubs) {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        this.topicName1 = subs[0];
        tm.getTopic(this.topicName1).subscribe(this);
        this.topicName2 = subs[1];
        tm.getTopic(this.topicName2).subscribe(this);
        this.outputTopicName = pubs[0];
        tm.getTopic(this.outputTopicName).addPublisher(this);

        this.x = 0;
        this.y = 0;
    }


    @Override
    public String getName() {
        return "Plus Agent";
    }

    @Override
    public void reset() {

    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            if (Objects.equals(this.topicName1, topic)) {
                this.x = msg.asDouble;
            } else if (Objects.equals(this.topicName2, topic)) {
                this.y = msg.asDouble;
            }
            else {
                return;
            }
            double res = this.x + this.y;
            TopicManager tm = TopicManagerSingleton.get();
            tm.getTopic(this.outputTopicName).publish(new Message(res));
        }
    }

    @Override
    public void close() {

    }
}
