package graph;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManagerSingleton {

    private static TopicManager instance;

    public static class TopicManager {

        public ConcurrentHashMap<String, Topic> topicsHashMaps;

        TopicManager() {
            this.topicsHashMaps = new ConcurrentHashMap<String, Topic>();
        }

        public Topic getTopic(String topicName) {
            Topic topicValue = this.topicsHashMaps.get(topicName);
            if (topicValue == null) {
                topicValue = new Topic(topicName);
                this.topicsHashMaps.put(topicName, topicValue);
            }
            return topicValue;
        }

        public Collection<Topic> getTopics() {
            return this.topicsHashMaps.values();
        }

        public void clear() {
            this.topicsHashMaps.clear();
        }


    }

    public static TopicManager get(){
        if (instance == null) {
            instance = new TopicManager();
        }
        return instance;
    }



}
