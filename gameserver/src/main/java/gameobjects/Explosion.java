package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Explosion extends Field implements Positionable, Tickable, Comparable {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private Point point;
    private long time;
    private GameSession gameSession;
    private boolean alive;


    public Explosion(int x, int y, GameSession gameSession) {
        super(x, y);
        this.id = getId();
        this.alive = true;
        this.gameSession = gameSession;
        this.point = new Point(x,y);;
        this.time = 400;
       log.info("Explosionid = " + id + "; " + "Explosion place = (" + point.getX() + "," +
                point.getY() + ")" + "; " + "Explosion timer = " + time);
    }

    @Override
    public void tick(long elapsed) {
        if (alive) {
            if (time < elapsed) {
                time = 0;
                alive = false;
            } else {
                time -= elapsed;
            }
        } else gameSession.removeGameObject(this);
    }

    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"Fire\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }



    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}