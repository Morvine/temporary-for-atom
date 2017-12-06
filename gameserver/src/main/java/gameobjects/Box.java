package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Box implements Positionable, Tickable {
    private int x;
    private int y;
    private int id;
    private boolean breakable;

    public Box(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.breakable = true;
        log.info("new unbreakable wall");
    }

    private final static Logger log = LogManager.getLogger(Box.class);

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

    public void tick(long elapsed) {

    }

}
