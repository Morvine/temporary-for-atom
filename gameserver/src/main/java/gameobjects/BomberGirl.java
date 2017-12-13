package gameobjects;

import boxes.ConnectionPool;
import boxes.InputQueue;
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


public class BomberGirl extends Field implements Tickable, Movable, Comparable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private int x;
    private int y;
    private int id;
    private double velocity;
    private boolean alive = true;
    private int maxBombs = 1;
    private int bombPower = 1;
    private double speedModifier = 1.0;
    private int gameId;
    private WebSocketSession session;
    private GameSession gameSession;

    public BomberGirl(int x, int y, WebSocketSession session, GameSession gameSession) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.id = getId();
        this.session = session;
        this.gameSession = gameSession;
        this.velocity = 0.2;
        this.gameId = gameId;
        log.info("New BomberGirl with id {}", id);
        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.POSSESS, id);
    }

    public void tick(long elapsed) {
        log.info("tick");
        if (gameSession.getCellFromGameArea(this.x / 32, this.y / 32)
                .getState().contains(State.EXPLOSION)) {
            alive = false;
        }
        if (alive) {
            Input input;
            log.info("producing action");
            if (Input.hasInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (input.getMessage().getTopic() == Topic.MOVE) {
                    log.warn(receiveDirection(session, input.getMessage().getData()).getDirection());
                    move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
                    InputQueue.getInstance().remove(input);
                } else {
                    gameSession.addGameObject(new Bomb((this.x / 32) * 32, (this.y / 32) * 32, gameSession));
                    gameSession.getCellFromGameArea(this.x / 32, this.y / 32)
                            .addState(State.BOMB);
                    InputQueue.getInstance().remove(input);
                }
            }
        } else gameSession.removeGameObject(this);
    }

    public Point move(Movable.Direction direction, long time) {
        if (direction == Movable.Direction.UP) {
            if (!((this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.BOX)))) {
                this.y = this.y + (int) (velocity * time);
            }
            log.info(this.y);
        }
        if (direction == Movable.Direction.DOWN) {
            if ((this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y - (int) (time * velocity)) / 32)
                    .getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y - (int) (time * velocity)) / 32)
                    .getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea(this.x / 32,
                    (this.y - (int) (time * velocity)) / 32)
                    .getState().contains(State.BOX))) {
            } else y = y - (int) (velocity * time);
        }
        if (direction == Movable.Direction.LEFT) {
            if ((this.gameSession.getCellFromGameArea((this.x - (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea((this.x - (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea((this.x - (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.BOX))) {
            } else x = x - (int) (velocity * time);
        }
        if (direction == Movable.Direction.RIGHT) {
            if ((this.gameSession.getCellFromGameArea((this.x + 24 + (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea((this.x + 24 + (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea((this.x + 24 + (int) (time * velocity)) / 32,
                    this.y / 32).getState().contains(State.BOX))) {
            } else x = x + (int) (velocity * time);
        }
        return new Point(this.x, this.y);
    }

    public String toJson() {
        String json = "{\"position\":{\"x\":" + this.x + ",\"y\":" + this.y + "},\"id\":" +
                this.getId() + ",\"velocity\":" +
                this.velocity + ",\"maxBombs\":" +
                this.maxBombs + ",\"speedModifier\":" +
                this.speedModifier + ",\"type\":\"Pawn\"}";
        return json;
    }

    public DirectionMessage receiveDirection(@NotNull WebSocketSession session, @NotNull String msg) {
        DirectionMessage message = JsonHelper.fromJson(msg, DirectionMessage.class);
        return message;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

}
