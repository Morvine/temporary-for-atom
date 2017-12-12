package gameobjects;

import gameserver.Broker;
import geometry.Point;
import message.DirectionMessage;
import message.Input;
import message.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.WebSocketSession;
import util.JsonHelper;


public class BomberGirl extends Field implements Tickable, Movable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private int x;
    private int y;
    private int id;
    private double velocity;
    //private int maxBombs;
    // private double speedModifier;
    private boolean alive = true;
    private WebSocketSession session;

    public BomberGirl(int x, int y, WebSocketSession session) {
        super(x, y);
        this.id = getId();
        this.session = session;
        this.velocity = 0.05;
        //this.maxBombs = 1;
        //this.speedModifier = 1;
        log.info("New BomberGirl with id {}", id);
    }

    public String directionToString(Direction direction) {
        return direction.toString();
    }

    public void tick(long elapsed) {
        //log.info("tick");
        if (alive) {
            Input input;
            if (Input.hasInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (input.getMessage().getTopic() == Topic.MOVE)
                    move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
            }
        }
    }


    public Point move(Direction direction, long time) {
        if (direction == direction.UP) {
            y = y + (int) (velocity * time);
        }
        if (direction == direction.DOWN) {
            y = y - (int) (velocity * time);
        }
        if (direction == direction.LEFT) {
            x = x - (int) (velocity * time);
        }
        if (direction == direction.RIGHT) {
            x = x + (int) (velocity * time);
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

    public DirectionMessage receiveDirection(@NotNull WebSocketSession session, @NotNull String msg) {
        DirectionMessage message = JsonHelper.fromJson(msg, DirectionMessage.class);
        return message;
    }

}
