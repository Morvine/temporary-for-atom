package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ground extends Field implements Positionable {
    private int x;
    private int y;
    private int id;

    public Ground(int x, int y) {
        super(x, y);
        this.id = getId();
        log.info("New ground with id {}", id);
    }

    private final static Logger log = LogManager.getLogger(Ground.class);

    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"id\":" +
                this.getId() + ",\"material\":\"grass\",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }


}

