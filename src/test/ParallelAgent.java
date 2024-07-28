package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ParallelAgent implements Agent {

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

    public ParallelAgent(Agent agent, int capacity) {
        this.messagesQueue = new ArrayBlockingQueue<>(capacity);
        this.agent = agent;
        this.thread = new Thread(this::take);
        this.thread.start();
    }

    public void put(String topic, Message msg) {
        try {
            this.messagesQueue.put(new MessageAndTopic(topic, msg));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void take() {
        while (!this.stop) {
            MessageAndTopic mt;
            try {
                mt = this.messagesQueue.take();
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
        this.stop = true;
        this.thread.interrupt();

    }
}