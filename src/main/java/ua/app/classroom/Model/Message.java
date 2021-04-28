package ua.app.classroom.Model;

public class Message {

    private String topic;
    private String username;

    public Message() {
    }

    public Message(String topic, String username) {
        this.topic = topic;
        this.username = username;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
