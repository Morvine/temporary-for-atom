package boxes;

import gameobjects.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;

public class GameSessionMap {

    private static final Logger log = LoggerFactory.getLogger(GameSessionMap.class);
    private static final GameSessionMap instance = new GameSessionMap();

    private final ConcurrentHashMap<Integer, GameSession> map;

    public static GameSessionMap getInstance() {
        return instance;
    }

    private GameSessionMap() {
        map = new ConcurrentHashMap<>();
    }

    public GameSession getGameSession(int id) {
        return map.get(id);
    }

    public void add(int id, GameSession gameSession) {
        if (map.putIfAbsent(id, gameSession) == null) {
            log.info("gamesessin {} created", id);
        }
    }
}
