package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BomberGirl implements Tickable, Movable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private int x;
    private int y;
    private int id;
    private int velocity;
    private int maxBombs;
    private int speedModifier;

    public BomberGirl(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.velocity = 10;
        this.maxBombs = 1;
        this.speedModifier = 1;
        log.info("BomberGirl initialized");
    }

    public void tick(long elapsed) {
        log.info("tick");
    }
    //Point firstPosition = ((Movable) gameObject).getPosition();
    // Point currentPosition = ((Movable) gameObject).move(Movable.Direction.UP, 1000);

    public Point move(Direction direction, long time) {
        if (direction == direction.UP) {
            y = y + velocity * (int) time;
        }
        if (direction == direction.DOWN) {
            y = y - velocity * (int) time;
        }
        if (direction == direction.LEFT) {
            x = x - velocity * (int) time;
        }
        if (direction == direction.RIGHT) {
            x = x + velocity * (int) time;
        }
        if (direction == direction.IDLE) {
            x = x;
            y = y;
        }
        return getPosition();
    }


    public int getId() {
        return id;
    }

    public Point getPosition() {
        Point point = new Point(x, y);
        return point;

    }

    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }

}
