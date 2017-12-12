package threads;

import boxes.ConnectionPool;
import dto.ReplicaDto;
import gameserver.Broker;
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
    private ConcurrentHashMap<Point, Wall> inGameWalls = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Bomb> inGameBombs = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Explosion> inGameExplosions = new ConcurrentHashMap<>();
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
        GameSession gameSession = new GameSession(id);
        gameSession.initGameArea();
        String replica =  "{\"objects\":[{\"position\":{\"x\":16.0,\"y\":12.0},\"id\":16,\"type\":\"Wall\"},{\"position\":{\"x\":32.0,\"y\":32.0},\"id\":213,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"},{\"position\":{\"x\":32.0,\"y\":352.0},\"id\":214,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"}] \"gameOver\":false }";
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
                Broker.getInstance().broadcast( new ReplicaDto(GameSession.getAllDto(), false));

            } catch (Exception e) {
            }
        }
        //Ticker ticker = new Ticker();
        //ticker.gameLoop(gameSession);
        log.info("Game #{} over", id);

    }


    public GameMechanics(int playerCount) {
        this.playerCount = playerCount;
    }


    public long getId() {
        return id;
    }

    public String jsonStringWalls() {
        String objjson = "";
        for (Point p : inGameWalls.keySet()) {
            Wall obj = inGameWalls.get(p);
            objjson = objjson + obj.toJson() + ",";
        }
        String result = objjson.substring(0, (objjson.length() - 1));
        return result;
    }

    public int getGameSessionPlayerCount() {
        return playerCount;
    }

    public String jsonStringExplosions() {
        if (inGameExplosions.size() == 0) {
            return null;
        } else {
            String objjson = "";
            for (Integer i : inGameExplosions.keySet()) {
                Explosion obj = inGameExplosions.get(i);
                objjson = objjson + obj.toJson() + ",";
            }
            String result = objjson.substring(0, (objjson.length() - 1));
            return result;
        }
    }

    public String jsonStringBombs() {
        if (inGameBombs.size() == 0) {
            return null;
        } else {
            String objjson = "";
            for (Integer i : inGameBombs.keySet()) {
                Bomb obj = inGameBombs.get(i);
                objjson = objjson + obj.toJson() + ",";
            }
            String result = objjson.substring(0, (objjson.length() - 1));
            return result;
        }
    }

    public BomberGirl getBomberGirl() {
        WebSocketSession session=null;
        BomberGirl bomberGirl = new BomberGirl(1, 2, session);
        return bomberGirl;
    }

    private void initGameField() {
    }
}
