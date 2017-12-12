package gameserver;

import boxes.ConnectionPool;
import gameobjects.GameSession;
import message.Topic;
import org.springframework.web.socket.WebSocketSession;


public class Replicator {

    public void writeReplica(GameSession gameSession, ConnectionPool connectionPool) {
        String[] playerList = null;
        ConnectionPool.getInstance().getPlayersWithGameId((int) gameSession.getId()).toArray(playerList);
        for (int i = 0; i < ConnectionPool.getInstance().getPlayersWithGameId(gameSession.getId()).size(); i++) {
            String player = playerList[i];
            WebSocketSession session = ConnectionPool.getInstance().getSession(player);
            if (gameSession.jsonStringBombs() == null) {
                if (gameSession.jsonStringExplosions() == null) {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonBomberGirl());
                } else {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
                }
            } else if (gameSession.jsonStringExplosions() == null) {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonBomberGirl());
            } else {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.jsonBomberGirl());
            }
        }
    }
}
