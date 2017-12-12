package gameobjects;

import geometry.Bar;
import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bomb extends Field implements Positionable, Tickable {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private Point position;
    private long time;
    private boolean alive;
    private final long lifeTime = 2;

    public Bomb(int x, int y, long time) {
        super(x,y);
        this.id = getId();
        this.position = new Point(x,y);
        this.time= time;
        log.info("Bombid = " + id + "; " + "Bomb place = (" + position.getX() + "," +
                position.getY() + ")" + "; " + "Bomb timer = " + time);
    }

    @Override
    public void tick(long elapsed) {
        if (time < elapsed) {
            time = 0;
        } else {
            time -= elapsed;
        }
    }


    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }
}
