package gameserver;

import boxes.ConnectionPool;
import gameobjects.GameSession;
import gameserver.Broker;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class ConnectionHandler extends TextWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String[] name = session.getUri().getQuery().split("=");
        ConnectionPool.getInstance().add(session, name[2]);
        System.out.println("Socket Connected: " + session);
        //GameSession.addPlayer(name[2]);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Broker.getInstance().receive(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
        ConnectionPool.getInstance().remove(session);
    }
}

/*package gameserver;

//import com.sun.corba.se.pept.transport.ConnectionCache;

import boxes.ConnectionPool;
import boxes.InputQueue;
import util.JsonHelper;

import message.Input;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.*;

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

        *//**
         * taking player`s information from url query
         *//*
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
        //log.info(Broker.getInstance().receive(session,message.getPayload()).getTopic());
        //log.info(Broker.getInstance().receiveDirection(session, Broker.getInstance().receive(session, message.getPayload()).getData()).getDirection());
        InputQueue.getInstance().add(new Input(session, Broker.getInstance().receive(session, message.getPayload())));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }

}*/
