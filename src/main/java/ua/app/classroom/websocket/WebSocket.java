package ua.app.classroom.websocket;

import ua.app.classroom.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class WebSocket {

    @Inject
    @Push
    private PushContext classroom;

    public void userConnected(String username) {
        send(new Message("User connected", username));
    }

    public void userDisconnected(String username) {
        send(new Message("User disconnected", username));
    }

    public void userHandUp(String username) {
        send(new Message("User raised hand up", username));
    }

    public void userHandDown(String username) {
        send(new Message("User raised hand down", username));
    }

    public void send(Message message) {
        classroom.send(message);
        classroom.send("updateTable");
    }

}
