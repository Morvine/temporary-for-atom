package gameobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession implements Tickable {
    private static final Logger log = LogManager.getLogger(GameSession.class);
    private List<GameObject> gameObjects = new ArrayList<>();
    private static AtomicInteger idGenerator = new AtomicInteger();
    private Cell[][] gameArea = new Cell[12][16];

    public List<GameObject> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void initGameArea() {

        for (int x = 0; x < 17; x++) {
            for (int y = 0; y < 13; y++) {
                if ((x == 0) || (x == 16)) {
                    addGameObject(new Wall(x * 32, y * 32));
                    gameArea[x][y] = new Cell(x * 32, y * 32, State.WALL);
                } else if (x % 2 != 0) {
                    if ((y == 0) || (y == 12)) {
                        addGameObject(new Wall(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x * 32, y * 32, State.WALL);
                    }
                } else {
                    if (y % 2 == 0) {
                        addGameObject(new Wall(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x * 32, y * 32, State.WALL);
                    }
                }
            }
        }
        for (int x = 1; x < 16; x++) {
            for (int y = 1; y < 12; y++) {
                if ((x % 2) != 0) {
                    addGameObject(new Ground(x * 32, y * 32));
                    gameArea[x][y] = new Cell(x * 32, y * 32, State.GROUND);
                } else {
                    if (y % 2 != 0) {
                        addGameObject(new Ground(x * 32, y * 32));
                        gameArea[x][y] = new Cell(x * 32, y * 32, State.GROUND);
                    }
                }
            }
        }
        for (int x = 1; x < 16; x++) {
            for (int y = 1; y < 12; y++) {
                if ((x == 1) || (x == 15)) {
                    if ((y > 2) && (y < 10)) {
                        addGameObject(new Box(x * 32, y * 32));
                        gameArea[x][y].addState(State.BOX);
                    }
                } else if ((x == 2) || (x == 14)) {
                    if ((y % 2 != 0) && (y != 1) && (y != 11)) {
                        addGameObject(new Box(x * 32, y * 32));
                        gameArea[x][y].addState(State.BOX);
                    }
                } else {
                    if (y % 2 != 0) {
                        addGameObject(new Box(x * 32, y * 32));
                        gameArea[x][y].addState(State.BOX);
                    }
                }

            }
        }
        addGameObject(new BomberGirl(32, 32));
        addGameObject(new BomberGirl(480, 32));
        addGameObject(new BomberGirl(32, 352));
        addGameObject(new BomberGirl(480, 352));
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

    public void addStateToCell(int x, int y, State state){
        gameArea[x][y].addState(state);
    }

    public void removeStateFromCell (int x, int y, State state) {
        if (gameArea[x][y].getState().contains(state))
            gameArea[x][y].getState().remove(state);
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
