package gameobjects;

import geometry.Bar;
import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Box extends Field implements Positionable, Tickable {
    private final static Logger log = LogManager.getLogger(Box.class);
    private int x;
    private int y;
    private int id;
    private boolean alive;

    public Box(int x, int y) {
        super(x, y);
        this.id = getId();
        this.alive = true;
        log.info("New box with id {}", id);
    }

    public Bar getBar() {
        return new Bar(x,y,x+28,y+28);
    }

    public void tick(long elapsed) {

    }

}
