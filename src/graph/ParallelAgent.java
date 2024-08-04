package graph;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {

    // inner class for get object of message and topic together
    public static class MessageAndTopic {
        public Message message;
        public String topic;

        public MessageAndTopic (String topic, Message msg) {
            this.topic = topic;
            this.message = msg;
        }
    }

    protected Agent agent;
    BlockingQueue<MessageAndTopic> messagesQueue;
    protected Thread thread;
    volatile boolean stop;

    public ParallelAgent(Agent agent, int capacity) { // constructor
        this.messagesQueue = new ArrayBlockingQueue<>(capacity);
        this.agent = agent;
        this.thread = new Thread(this::take);
        this.thread.start();
    }

    public void put(String topic, Message msg) {
        try { // put in the queue the MessageAndTopic
            this.messagesQueue.put(new MessageAndTopic(topic, msg));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void take() {
        while (!this.stop) { // take from the queue the MessageAndTopic
            MessageAndTopic mt;
            try {
                mt = this.messagesQueue.take();
                // splitting the message and topic
                this.agent.callback(mt.topic, mt.message);
            } catch (InterruptedException e) {
                if (this.stop) {
                    // If interrupted and stop is true, exit the loop
                    break;
                } else {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    @Override
    public String getName() {
        return this.agent.getName();
    }

    @Override
    public void reset() {
        this.agent.reset();
    }

    @Override
    public void callback(String topic, Message msg) {
        put(topic, msg);
    }

    @Override
    public void close() {
        this.stop = true; // to stop the thread
        this.thread.interrupt();

    }
}