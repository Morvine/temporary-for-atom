package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Bomb extends Field implements Positionable, Tickable, Comparable {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private GameSession gameSession;
    private long time;
    private boolean alive;
    private int x;
    private int y;


    public Bomb(int x, int y, GameSession gameSession) {
        super((x / 32) * 32, (y / 32) * 32);
        this.x = (x / 32) * 32;
        this.y = (y / 32) * 32;
        this.id = getId();
        this.alive = true;
        this.gameSession = gameSession;
        this.time = 2000;
        log.info("Bombid = " + id + "; " + "Bomb place = (" + this.x + "," +
                this.y + ")" + "; " + "Bomb timer = " + time);
    }

    public void bang() {
        log.info("KaBoom!");
        if (!this.gameSession.getCellFromGameArea(this.x, this.y + 32)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x, y + 32, State.BOX);
            this.gameSession.removeStateFromCell(x, y + 32, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x, y + 32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32,
                    (y * 32) / 32 + 32, this.gameSession));
        }

        if (!this.gameSession.getCellFromGameArea(this.x, this.y - 32)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x, y - 32, State.BOX);
            this.gameSession.removeStateFromCell(x, y - 32, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x, y - 32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32,
                    (y * 32) / 32 - 32, this.gameSession));
        }
        if (!this.gameSession.getCellFromGameArea(this.x + 32, this.y)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x + 32, y, State.BOX);
            this.gameSession.removeStateFromCell(x + 32, y, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x + 32, y, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32 + 32,
                    (y * 32) / 32, this.gameSession));
        }
        if (!this.gameSession.getCellFromGameArea(this.x - 32, this.y)
                .getState().contains(State.WALL)) {
            this.gameSession.removeStateFromCell(x - 32, y, State.BOX);
            this.gameSession.removeStateFromCell(x - 32, y, State.BOMBERGIRL);
            this.gameSession.addStateToCell(x - 32, y, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((x * 32) / 32 - 32,
                    (y * 32) / 32, this.gameSession));
        }

        this.gameSession.addStateToCell(this.x, this.y, State.EXPLOSION);
        this.gameSession.addGameObject(new Explosion((x * 32) / 32,
                (y * 32) / 32, this.gameSession));
    }

    @Override
    public void tick(long elapsed) {
        log.info("bomb {} tick", id);
        if (alive) {
            if (time < elapsed) {
                time = 0;
                alive = false;
                bang();
            } else {
                time -= elapsed;
            }
        } else gameSession.removeGameObject(this);
    }


    public String toJson() {
        String json = "{\"type\":\"Bomb\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + (this.x + 7) + ",\"y\":" + (this.y - 7) + "}}";
        return json;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
