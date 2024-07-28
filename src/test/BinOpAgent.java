package test;

import java.util.Objects;
import java.util.function.BinaryOperator;

import test.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {

    public String name;
    public String topicName1;
    public double inputFromTopic1;
    public String topicName2;
    public double inputFromTopic2;
    public String outputTopicName;
    public BinaryOperator<Double> lambdaFunc;

    BinOpAgent (String name, String topicName1, String topicName2, String outputTopicName,
                BinaryOperator<Double> lambdaFunc) {
        this.name = name;

        this.inputFromTopic1 = 0;
        this.inputFromTopic2 = 0;

        this.topicName1 = topicName1;
        this.topicName2 = topicName2;
        this.outputTopicName = outputTopicName;

        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(topicName1).subscribe(this);
        tm.getTopic(topicName2).subscribe(this);
        tm.getTopic(outputTopicName).addPublisher(this);

        this.lambdaFunc = lambdaFunc;
    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void reset() {
        this.inputFromTopic1 = 0;
        this.inputFromTopic2 = 0;
    }

    @Override
    public void callback(String topic, Message msg) {

        if (!Double.isNaN(msg.asDouble)) {
            if (Objects.equals(this.topicName1, topic)) {
                this.inputFromTopic1 = msg.asDouble;
            } else if (Objects.equals(this.topicName2, topic)) {
                this.inputFromTopic2 = msg.asDouble;
            }
            else {
                return;
            }
            double res = lambdaFunc.apply(this.inputFromTopic1, this.inputFromTopic2);
            TopicManager tm = TopicManagerSingleton.get();
            tm.getTopic(outputTopicName).publish(new Message(res));
        }
    }

    @Override
    public void close() {

    }
}
