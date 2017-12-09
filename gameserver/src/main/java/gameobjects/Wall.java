package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Wall implements Positionable {
    private int x;
    private int y;
    private int id;
    private boolean breakable;

    public Wall(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.breakable = false;
        log.info("new wall");
    }

    private final static Logger log = LogManager.getLogger(Wall.class);

    public int getId() {
        return id;
    }

    public boolean durability() {
        return breakable;
    }

    public Point getPosition() {
        Point point = new Point(x, y);
        return point;

    }

    public String toJson() {
        Point pos = getPosition();
        String json = "{\"id\":" + this.getId() +
                ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }


}
