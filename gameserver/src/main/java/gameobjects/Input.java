package gameobjects;

import message.Message;
import org.springframework.web.socket.WebSocketSession;

public class Input {
    private Message message;
    private WebSocketSession session;

    public Input (WebSocketSession session, Message message) {
        this.message = message;
        this.session = session;
    }
    public Message getMessage() {
        return message;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
