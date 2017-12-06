package gameserver;

import boxes.ConnectionPool;
import message.Topic;
import org.springframework.web.socket.WebSocketSession;
import threads.GameSession;

public class Replicator {

    public void writeReplica(GameSession gameSession, ConnectionPool connectionPool) {
        String[] playerList = null;
        ConnectionPool.getInstance().getPlayerQueueWithGameId((int) gameSession.getId()).toArray(playerList);
        for (int i = 0; i < gameSession.gameSessionPlayerCount(); i++) {
            String player = playerList[i];
            WebSocketSession session = ConnectionPool.getInstance().getSession(player);
            if (gameSession.jsonStringBombs() == null) {
                if (gameSession.jsonStringExplosions() == null) {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                            "," + gameSession.getBomberGirl().toJson());
                } else {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringExplosions() + "," + gameSession.getBomberGirl().toJson());
                }
            } else if (gameSession.jsonStringExplosions() == null) {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBombs() + "," + gameSession.getBomberGirl().toJson());
            } else {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.getBomberGirl().toJson());
            }
        }
    }
}
