
package gameserver;

import boxes.ConnectionPool;
import gameobjects.GameSession;
import message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentLinkedQueue;


public class Replicator {
    private static final Logger log = LoggerFactory.getLogger(Replicator.class);

    public void writeReplica(GameSession gameSession) {


        String[] playerList = null;
        ConcurrentLinkedQueue<String> connectionPool = ConnectionPool.getInstance().getPlayersWithGameId((int) gameSession.getId());
        /*for (int i = 0; i < ConnectionPool.getInstance().getPlayersWithGameId(gameSession.getId()).size(); i++)*/
        //String player = playerList[i];
        while (!connectionPool.isEmpty()) {

            WebSocketSession session = ConnectionPool.getInstance().getSession(connectionPool.poll());
            if (gameSession.jsonStringBombs() == null) {
                if (gameSession.jsonStringExplosions() == null) {
                    if (gameSession.jsonStringBonus() == null) {
                        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                                "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonBomberGirl());
                    } else {
                        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                                "," + gameSession.jsonStringBonus() + "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonBomberGirl());
                    }
                } else {
                    if (gameSession.jsonStringBonus() == null) {
                        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                                "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
                    } else {
                        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                                "," + gameSession.jsonStringBonus() + "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
                    }
                }
            } else if (gameSession.jsonStringExplosions() == null) {
                if (gameSession.jsonStringBonus() == null) {
                    Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonBomberGirl());
                } else {
                    Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBonus() + "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonBomberGirl());
                }
            } else {
                if (gameSession.jsonStringBonus() == null) {
                    Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
                } else {
                    Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() + "," + gameSession.jsonStringBonus() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
                }
            }

        }
/*        if (gameSession.jsonStringBombs() == null) {
            if (gameSession.jsonStringExplosions() == null) {
                Broker.getInstance().broadcast(Topic.REPLICA, *//*gameSession.jsonStringGround() + "," +*//* gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBonus() +  "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonBomberGirl());
                *//*log.info(gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonBomberGirl());*//*
            } else {
                Broker.getInstance().broadcast(Topic.REPLICA, *//*gameSession.jsonStringGround() + "," + *//*gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBonus() + "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
            }
        } else if (gameSession.jsonStringExplosions() == null) {
            Broker.getInstance().broadcast(Topic.REPLICA, *//*gameSession.jsonStringGround() + "," + *//*gameSession.jsonStringWalls() +
                    "," + gameSession.jsonStringBonus() + "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonBomberGirl());
        } else {
            Broker.getInstance().broadcast(Topic.REPLICA, *//*gameSession.jsonStringGround() + "," + *//*gameSession.jsonStringWalls() +
                    "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
        }*/
    }
}