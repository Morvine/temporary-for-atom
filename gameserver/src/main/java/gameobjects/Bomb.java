package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Bomb extends Field implements Positionable, Tickable, Comparable {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private Point position;
    private GameSession gameSession;
    private long time;
    private boolean alive;
    private int x;
    private int y;


    public Bomb(int x, int y, GameSession gameSession) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.id = getId();
        this.alive = true;
        this.gameSession = gameSession;
        this.position = new Point(x, y);
        this.time = 2000;
        log.info("Bombid = " + id + "; " + "Bomb place = (" + position.getX() + "," +
                position.getY() + ")" + "; " + "Bomb timer = " + time);
    }

    public void bang(int x, int y) {
        log.info("KaBoom!");
        if (!this.gameSession.getCellFromGameArea(this.x / 32, this.y / 32 + 1)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x / 32, y / 32 + 1, State.BOX);
            this.gameSession.removeStateFromCell(x / 32, y / 32 + 1, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x / 32, y / 32 + 1, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32,
                    (y * 32) / 32 + 32, this.gameSession));
        }

        if (!this.gameSession.getCellFromGameArea(this.x / 32, this.y / 32 - 1)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x / 32, (y - 1) / 32, State.BOX);
            this.gameSession.removeStateFromCell(x / 32, (y - 1) / 32, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x / 32, (y - 1) / 32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32,
                    (y * 32) - 1, this.gameSession));
        }
        if (!this.gameSession.getCellFromGameArea(this.x / 32 + 1, this.y / 32)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell((x + 32) / 32, (y) / 32, State.BOX);
            this.gameSession.removeStateFromCell((x + 32) / 32, (y) / 32, State.BOMBERGIRL);
            this.gameSession.addStateToCell((x + 32) / 32, (y) / 32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32 + 1,
                    (y * 32), this.gameSession));
        }
        if (!this.gameSession.getCellFromGameArea(this.x / 32 - 1, this.y / 32)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell((x - 1) / 32, (y) / 32, State.BOX);
            this.gameSession.removeStateFromCell((x - 1) / 32, (y) / 32, State.BOMBERGIRL);
            this.gameSession.addStateToCell((x - 1) / 32, (y) / 32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32 - 1,
                    (y * 32), this.gameSession));
        }

        this.gameSession.addStateToCell(x / 32, y / 32, State.EXPLOSION);
        this.gameSession.addGameObject(new Explosion((x * 32) / 32,
                (x * 32) / 32, this.gameSession));
    }

    @Override
    public void tick(long elapsed) {
        log.info("bomb {} tick", id);
        if (alive) {
            if (time < elapsed) {
                time = 0;
                alive = false;
                bang(this.x, this.y);
            } else {
                time -= elapsed;
            }
        } else gameSession.removeGameObject(this);
    }


    public String toJson() {
        String json = "{\"type\":\"Bomb\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + getPosition().getX() + ",\"y\":" + getPosition().getY() + "}}";
        return json;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
