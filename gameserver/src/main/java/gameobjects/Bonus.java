package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Bonus extends Field implements Positionable, Tickable, Comparable{

    private static final Logger log = LogManager.getLogger(Bonus.class);
    private final int id;
    private final int type;
    private GameSession gameSession;
    private boolean alive;
    private int x;
    private int y;

    public Bonus(int x, int y, GameSession gameSession, int type) {
        super((x / 32) * 32, (y / 32) * 32);
        this.x = (x / 32) * 32;
        this.y = (y / 32) * 32;
        this.id = getId();
        this.type = type;
        this.alive = true;
        this.gameSession = gameSession;
        log.info("Bombid = " + id + "; " + "Bomb place = (" + this.x + "," +
                this.y + ")" + "; ");
    }
    @Override
    public void tick(long elapsed) {
        if (alive) {
            if (!gameSession.getCellFromGameArea(getPosition().getX(), getPosition().getY())
                    .getState().contains(State.BONUS))
                alive = true;
        } else gameSession.removeGameObject(this);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    public String toJson() {
        String json = "{\"type\":\"BFire\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + this.x + ",\"y\":" + this.y + "}}";
        return json;
    }

}
