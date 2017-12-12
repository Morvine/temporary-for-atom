package gameobjects;

import boxes.ConnectionPool;
import boxes.GameSessionMap;
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
    private int gameId;
    private WebSocketSession session;
    private GameSession gameSession;

    public BomberGirl(int x, int y, WebSocketSession session, int gameId) {
        super(x, y);
        this.id = getId();
        this.session = session;
        this.velocity = 0.05;
        this.gameId = gameId;
        this.gameSession = GameSessionMap.getInstance().getGameSession(gameId);
        //this.maxBombs = 1;
        //this.speedModifier = 1;
        log.info("New BomberGirl with id {}", id);
    }

    public void tick(long elapsed) {
        //log.info("tick");
        if (alive) {
            Input input;
            if (Input.hasInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (input.getMessage().getTopic() == Topic.MOVE)
                    move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
            } else move(Direction.IDLE, elapsed);
        }
    }


    public Point move(Direction direction, long time) {
        if (direction == direction.UP) {
            if (this.gameSession.getCellFromGameArea(getPosition().getX(), getPosition().getY() + 24)
                    .getState().contains(State.WALL)) //obrazetc obrashenia k gameArea
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
