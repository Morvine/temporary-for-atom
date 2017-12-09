package gameserver;

//import com.sun.corba.se.pept.transport.ConnectionCache;
import boxes.ConnectionPool;
import boxes.InputQueue;

import gameobjects.Input;
import message.Message;
import org.springframework.stereotype.Component;
        import org.springframework.web.socket.*;
        import org.springframework.web.socket.handler.TextWebSocketHandler;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;
        import java.lang.*;

        import java.net.URI;

@Component
public class ConnectionHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static final Logger log = LogManager.getLogger(ConnectionHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String name;
        String temp;
        try {
            super.afterConnectionEstablished(session);
        } catch (Exception e) {

        }
        log.info("Hello!");

        /**
         * taking player`s information from url query
         */
        try {
            temp = session.getUri().getQuery();

            String[] tempStr = temp.split("&");
            String[] nameStr = tempStr[1].split("=");
            name = nameStr[1];

            ConnectionPool.getInstance().add(session, name);
            log.info("Socket Connected: " + session.getUri());
            log.info("Player with name: " + ConnectionPool.getInstance().getPlayer(session)
                    + " is trying to connect to game with id: " + ConnectionPool.getInstance().getGameId(name));
        } catch (NullPointerException e) {
        }
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info(Broker.getInstance().receive(session,message.getPayload()).getTopic());
        log.info(Broker.getInstance().receive(session,message.getPayload()).getData());
        InputQueue.getInstance().add(new Input(session,Broker.getInstance().receive(session,message.getPayload())));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }

}
