package clients;

import gameserver.ConnectionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.TextMessage;
import gameserver.Broker;

import java.io.IOException;

public class EvClient {
    public static void main(String[] args) {

        String uri = "ws://localhost:8080/game/connect?gameId=1&name=lol";
        Boolean stop = false;
        //Broker broker = Broker.getInstance();

        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketSession session = null;
        try {

            ConnectionHandler socket = new ConnectionHandler();

            ListenableFuture<WebSocketSession> fut = client.doHandshake(socket, uri);

            session = fut.get();

            String kek = "kek";

            //session.sendMessage(new TextMessage(kek));
            while (!stop) {
                Thread.sleep(100000);
                stop=true;
            }

            session.close();

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}

