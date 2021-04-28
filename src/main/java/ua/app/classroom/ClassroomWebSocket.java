package ua.app.classroom;

import com.google.gson.Gson;
import ua.app.classroom.Model.Message;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
public class ClassroomWebSocket {

    private static final Set<ClassroomWebSocket> connections = new CopyOnWriteArraySet<>();
    private static final Gson GSON = new Gson();
    private Session session;

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
    }

    @OnClose
    public void end() {
        connections.remove(this);
    }

    @OnMessage
    public void incoming(String message) {
        Message msg = GSON.fromJson(message, Message.class);
        broadcast(msg.getTopic(), msg.getUsername());
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
    }

    public static void userConnected(String username) {
        broadcast("user_connected", username);
    }

    public static void userDisconnected(String username) {
        broadcast("user_disconnected", username);
    }

    public static void userHandUp(String username) {
        broadcast("user_hand_up", username);
    }

    public static void userHandDown(String username) {
        broadcast("user_hand_down", username);
    }

    private static void broadcast(String topic, String username) {
        for (ClassroomWebSocket client : connections) {
            try {
                client.session.getBasicRemote().sendText(GSON.toJson(new Message(topic, username)));
            } catch (IOException e) {
                e.printStackTrace();
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}