package gameserver;

import boxes.ConnectionPool;
import message.Topic;
import org.springframework.web.socket.WebSocketSession;
import threads.GameMechanics;


public class Replicator {

    public void writeReplica(GameMechanics gameMechanics, ConnectionPool connectionPool) {
        String[] playerList = null;
        ConnectionPool.getInstance().getPlayersWithGameId((int) gameMechanics.getId()).toArray(playerList);
        for (int i = 0; i < gameMechanics.getGameSessionPlayerCount(); i++) {
            String player = playerList[i];
            WebSocketSession session = ConnectionPool.getInstance().getSession(player);
            if (gameMechanics.jsonStringBombs() == null) {
                if (gameMechanics.jsonStringExplosions() == null) {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameMechanics.jsonStringWalls() +
                            "," + gameMechanics.getBomberGirl().toJson());
                } else {
                    Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameMechanics.jsonStringWalls() +
                            "," + gameMechanics.jsonStringExplosions() + "," + gameMechanics.getBomberGirl().toJson());
                }
            } else if (gameMechanics.jsonStringExplosions() == null) {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameMechanics.jsonStringWalls() +
                        "," + gameMechanics.jsonStringBombs() + "," + gameMechanics.getBomberGirl().toJson());
            } else {
                Broker.getInstance().send(connectionPool.getPlayer(session), Topic.REPLICA, gameMechanics.jsonStringWalls() +
                        "," + gameMechanics.jsonStringBombs() + "," + gameMechanics.jsonStringExplosions() + "," + gameMechanics.getBomberGirl().toJson());
            }
        }
    }
}
