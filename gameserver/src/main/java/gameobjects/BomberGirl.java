package gameobjects;

import geometry.Bar;
import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BomberGirl extends Field implements Tickable, Movable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private int x;
    private int y;
    private int id;
    private double velocity;
    private int maxBombs;
    private double speedModifier;
    private boolean alive = true;

    public BomberGirl(int x, int y) {
        super(x, y);
        this.id = getId();
        this.velocity = 0.05;
        this.maxBombs = 1;
        this.speedModifier = 1;
        log.info("New BomberGirl with id {}", id);
    }



    public void tick(long elapsed) {
        log.info("tick");
    }
    //Point firstPosition = ((Movable) gameObject).getPosition();
    // Point currentPosition = ((Movable) gameObject).move(Movable.Direction.UP, 1000);

    public Point move(Direction direction, long time) {
        if (direction == direction.UP) {
            y = y + (int)(velocity * time);
        }
        if (direction == direction.DOWN) {
            y = y - (int)(velocity * time);
        }
        if (direction == direction.LEFT) {
            x = x - (int)(velocity * time);
        }
        if (direction == direction.RIGHT) {
            x = x + (int)(velocity * time);
        }
        if (direction == direction.IDLE) {
        }
        return getPosition();
    }


    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"" + this.getClass().getSimpleName() + "\",\"id\":" +
                this.getId() + ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }

}
