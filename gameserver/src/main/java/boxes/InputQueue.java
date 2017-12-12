package boxes;

import gameobjects.Movable;
import message.Input;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InputQueue
{private static ConcurrentLinkedQueue<Input> instance = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<Input> getInstance() {
        return instance;
    }

    public String getDirection(WebSocketSession session) {
        Input input = null;
        while (input.getSession()!=session) {
            input = InputQueue.getInstance().iterator().next();
        }
        return input.getMessage().getData();
    }

}
