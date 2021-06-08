package ua.app.classroom.websocket;

import ua.app.classroom.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class WebSocketForAdmin {

    @Inject
    @Push
    private PushContext adminChannel;

    public void userDeleted(String username) {
        send(new Message("User delete", username));
    }

    public void send(Message message) {
        adminChannel.send(message);
        adminChannel.send("updateUserTable");
    }

}
