package threads;

import boxes.ConnectionPool;
import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import boxes.GameStarterQueue;

import gameobjects.*;


public class GameMechanics implements Runnable {
    private ConcurrentHashMap<Point, Wall> inGameWalls = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Bomb> inGameBombs = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Explosion> inGameExplosions = new ConcurrentHashMap<>();
    private static final Logger log = LogManager.getLogger(GameServer.class);
    private static AtomicLong idGenerator = new AtomicLong();


    private int playerCount;

    private long id = idGenerator.getAndIncrement() + 1;

    private ConcurrentLinkedQueue<String> playerList = ConnectionPool.getInstance().getPlayerQueueWithGameId((int)id);

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

            ConcurrentLinkedQueue playerQueue = ConnectionPool.getInstance().getPlayerQueueWithGameId((int) id);
            while (!playerQueue.isEmpty()) {
                log.info(playerQueue.poll());
            }
            GameSession gameSession = new GameSession();
            Ticker ticker = new Ticker();
            ticker.gameLoop();

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
    public int gameSessionPlayerCount (){
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
        BomberGirl bomberGirl = new BomberGirl(1,2,3);
        return bomberGirl;
    }
}
