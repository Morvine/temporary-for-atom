package threads;

import boxes.ConnectionPool;
import boxes.GameSessionMap;
import gameserver.Broker;
import gameserver.Replicator;
import gameserver.Ticker;
import geometry.Point;
import message.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import boxes.GameStarterQueue;

import gameobjects.*;
import org.springframework.web.socket.WebSocketSession;


public class GameMechanics implements Runnable {
    private static final Logger log = LogManager.getLogger(GameMechanics.class);
    private static AtomicLong idGenerator = new AtomicLong();
    private int playerCount;


    private long id = idGenerator.getAndIncrement() + 1;

    @Override
    public void run() {

        while (!GameStarterQueue.getInstance().contains((int) id)) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        GameStarterQueue.getInstance().poll();
        log.info("Game {} started", id);
        log.info("Players in this session:");
        ConcurrentLinkedQueue playerQueue = ConnectionPool.getInstance().getPlayersWithGameId((int) id);
        while (!playerQueue.isEmpty()) {
            log.info(playerQueue.poll());
        }
        GameSession gameSession = new GameSession((int) id, playerCount);
        gameSession.initGameArea();
        GameSessionMap.getInstance().add((int) id, gameSession);
        String string = "{\n" +
                "   \"topic\": \"REPLICA\",\n" +
                "   \"data\":\n" +
                "   {\n" +
                "       \"objects\":[{\"position\":{\"x\":16.0,\"y\":12.0},\"id\":16,\"type\":\"Wall\"},{\"position\":{\"x\":32.0,\"y\":32.0},\"id\":213,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"},{\"position\":{\"x\":32.0,\"y\":352.0},\"id\":214,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"}],\n" +
                "       \"gameOver\":false\n" +
                "   }\n" +
                "}";
        while (!Thread.currentThread().isInterrupted()) {
            Broker.getInstance().send("Kek", Topic.REPLICA, string);
        }
        /*for (int y = 0; y < 13; y++) {
            for (int x = 0; x < 17; x++)
                System.out.print(gameSession.getCellFromGameArea(x, y).getState());
            System.out.print("\n");}*/  //Proverka zapolnenia pol`a

        Ticker ticker = new Ticker();
        ticker.gameLoop(gameSession);
        log.info("Game #{} over", id);

    }

    public GameMechanics(int playerCount) {
        this.playerCount = playerCount;

    }


    public long getId() {
        return id;
    }



    public int getGameSessionPlayerCount() {
        return playerCount;
    }






 /*   public BomberGirl getBomberGirl() {
        WebSocketSession session = null;
        BomberGirl bomberGirl = new BomberGirl(1, 2, session, 32332);
        return bomberGirl;
    }*/

    /*private GameSession getGameSession(int id) {

    }*/
}
