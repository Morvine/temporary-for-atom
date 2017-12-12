package message;

import boxes.InputQueue;
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

    public static boolean hasInputForPlayer(WebSocketSession session) {
        Input input;
        while (InputQueue.getInstance().iterator().hasNext()) {
            input = InputQueue.getInstance().iterator().next();
            if (input.getSession()==session)
                return true;
        }
        return false;
    }

    public static Input getInputForPlayer(WebSocketSession session) {
        Input input;
        while (InputQueue.getInstance().iterator().hasNext()) {
            input = InputQueue.getInstance().iterator().next();
            if (input.getSession()==session)
                return input;
        }
        return null;
    }
}
