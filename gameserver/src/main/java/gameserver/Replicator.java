package gameserver;

import boxes.ConnectionPool;
import gameobjects.GameSession;
import message.Topic;
import org.springframework.web.socket.WebSocketSession;
import threads.GameMechanics;


public class Replicator {

    public void writeReplica(GameSession gameSession, ConnectionPool connectionPool) {
        String[] playerList = null;
        ConnectionPool.getInstance().getPlayersWithGameId((int) gameSession.getId()).toArray(playerList);
        for (int i = 0; i < gameMechanics.getGameSessionPlayerCount(); i++) {
            String player = playerList[i];
            WebSocketSession session = ConnectionPool.getInstance().getSession(player);
            if (gameSession.jsonStringBombs() == null) {
                if (gameSession.jsonStringExplosions() == null) {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.getBomberGirl().toJson());
                } else {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                            "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringExplosions() + "," + gameSession.getBomberGirl().toJson());
                }
            } else if (gameSession.jsonStringExplosions() == null) {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.getBomberGirl().toJson());
            } else {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameSession.jsonStringGround() + "," + gameSession.jsonStringWalls() +
                        "," + gameSession.jsonStringBoxes() + "," + gameSession.jsonStringBombs() + "," + gameSession.jsonStringExplosions() + "," + gameSession.getBomberGirl().toJson());
            }
        }
    }
}
