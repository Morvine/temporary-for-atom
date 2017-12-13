package gameobjects;

import boxes.ConnectionPool;
import gameserver.Broker;
import gameserver.Ticker;
import geometry.Point;
import message.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession implements Tickable {
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private Ticker ticker;

    private ConcurrentHashMap<Integer, BomberGirl> inGameBomberGirls = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Wall> inGameWalls = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Box> inGameBoxes = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Bomb> inGameBombs = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Ground> inGameGround = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Explosion> inGameExplosions = new ConcurrentHashMap<>();

    private List<GameObject> gameObjects = new ArrayList<>();
    private Cell[][] gameArea = new Cell[17][13];
    private long id;

    public GameSession(long id, Ticker ticker) {
        this.id = id;
        this.ticker = ticker;
    }

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);

        if (gameObject.getClass().getSimpleName().equals("Bomb")) {
            inGameBombs.put(gameObject.getId(),(Bomb) gameObject);
            ticker.registerTickable((Tickable) gameObject);
        }
        if (gameObject.getClass().getSimpleName().equals("Box")) {
            inGameBoxes.put(gameObject.getId(),(Box) gameObject);
            ticker.registerTickable((Tickable)gameObject);
        }
        if (gameObject.getClass().getSimpleName().equals("BomberGirl")) {
            inGameBomberGirls.put(gameObject.getId(),(BomberGirl) gameObject);
            ticker.registerTickable((Tickable) gameObject);
        }
        if (gameObject.getClass().getSimpleName().equals("Explosion")) {
            inGameExplosions.put(gameObject.getId(),(Explosion) gameObject);
            ticker.registerTickable((Tickable) gameObject);
        }
        if (gameObject.getClass().getSimpleName().equals("Ground")) {
            inGameGround.put(gameObject.getId(),(Ground) gameObject);
        }
        if (gameObject.getClass().getSimpleName().equals("Wall")) {
            inGameWalls.put(gameObject.getId(),(Wall) gameObject);
        }
    }

    public void removeGameObject(GameObject gameObject) {
        log.info("{} was deleted", gameObject.getClass().getSimpleName());
        gameObjects.remove(gameObject);
        ticker.unregisterTickable((Tickable) gameObject);
    }

    public void initGameArea() {

        for (int x = 0; x < 17; x++) {
            for (int y = 0; y < 13; y++) {
                if ((x == 0) || (x == 16)) {
                    addGameObject(new Wall(x * 32, y * 32));
                    gameArea[x][y] = new Cell(x , y , State.WALL);
                } else if (x % 2 != 0) {
                    if ((y == 0) || (y == 12)) {
                        addGameObject(new Wall(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x , y , State.WALL);
                    }
                } else {
                    if (y % 2 == 0) {
                        addGameObject(new Wall(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x , y , State.WALL);
                    }
                }
            }
        }
        for (int x = 1; x < 16; x++) {
            for (int y = 1; y < 12; y++) {
                if ((x % 2) != 0) {
                    addGameObject(new Ground(x * 32, y * 32));
                    gameArea[x][y] = new Cell(x , y , State.GROUND);
                } else {
                    if (y % 2 != 0) {
                        addGameObject(new Ground(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x , y , State.GROUND);
                    }
                }
            }
        }
        for (int x = 1; x < 16; x++) {
            for (int y = 1; y < 12; y++) {
                if ((x == 1) || (x == 15)) {
                    if ((y > 2) && (y < 10)) {
                        addGameObject(new Box(x * 32, y * 32, this));
                        gameArea[x][y].addState(State.BOX);
                    }
                } else if ((x == 2) || (x == 14)) {
                    if ((y % 2 != 0) && (y != 1) && (y != 11)) {
                        addGameObject(new Box(x * 32, y * 32, this));
                        gameArea[x][y].addState(State.BOX);
                    }
                } else {
                    if (y % 2 != 0) {
                        addGameObject(new Box(x * 32, y * 32, this));
                        gameArea[x][y].addState(State.BOX);
                    } else if (x % 2 != 0) {
                        addGameObject(new Box(x * 32, y * 32, this));
                        gameArea[x][y].addState(State.BOX);
                    }
                }
            }
        }
        ConcurrentLinkedQueue<WebSocketSession> playerQueue = ConnectionPool.getInstance().getSessionsWithGameId((int) id);
        addGameObject(new BomberGirl(32, 32, playerQueue.poll(), this));
        addGameObject(new BomberGirl(480, 32, playerQueue.poll(), this));
        addGameObject(new BomberGirl(32, 352, playerQueue.poll(), this));
        addGameObject(new BomberGirl(480, 352, playerQueue.poll(), this));
        gameArea[1][1].addState(State.BOMBERGIRL);
        gameArea[15][1].addState(State.BOMBERGIRL);
        gameArea[1][11].addState(State.BOMBERGIRL);
        gameArea[15][11].addState(State.BOMBERGIRL);
    }

    public Cell[][] getGameArea() {
        return gameArea;
    }

    public Cell getCellFromGameArea(int x, int y) {
        return gameArea[x][y];
    }

    public void addStateToCell(int x, int y, State state) {
        gameArea[x][y].addState(state);
    }

    public boolean removeStateFromCell(int x, int y, State state) {
        return (gameArea[x][y].getState().remove(state));
    }

    public int getId() {
        return (int) this.id;
    }

    public String jsonStringGround() {
        String objjson = "";
        for (Integer p : inGameGround.keySet()) {
            Ground obj = inGameGround.get(p);
            objjson = objjson + obj.toJson() + ",";
        }
        String result = objjson.substring(0, (objjson.length() - 1));
        return result;
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

    public String jsonStringBoxes() {
        String objjson = "";
        for (Integer p : inGameBoxes.keySet()) {
            Box obj = inGameBoxes.get(p);
            objjson = objjson + obj.toJson() + ",";
        }
        String result = objjson.substring(0, (objjson.length() - 1));
        return result;
    }

    public String jsonStringWalls() {
        String objjson = "";
        for (Integer p : inGameWalls.keySet()) {
            Wall obj = inGameWalls.get(p);
            objjson = objjson + obj.toJson() + ",";
        }
        String result = objjson.substring(0, (objjson.length() - 1));
        return result;
    }

    public String jsonBomberGirl() {
        String objjson = "";
        for (Integer p : inGameBomberGirls.keySet()) {
            BomberGirl obj = inGameBomberGirls.get(p);
            objjson = objjson + obj.toJson() + ",";
        }
        String result = objjson.substring(0, (objjson.length() - 1));
        return result;
    }


    @Override
    public void tick(long elapsed) {
        log.info("tick");
        /*for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Tickable) {
                ((Tickable) gameObject).tick(elapsed);
            }
        }*/
    }
}
