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
        this.id = getId();
        this.session = session;
        this.gameSession = gameSession;
        this.velocity = 0.05;
        this.gameId = gameId;
        log.info("New BomberGirl with id {}", id);
        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.POSSESS, id);
    }

    public void tick(long elapsed) {
        log.info("tick");
        if (gameSession.getCellFromGameArea(getPosition().getX() / 32, getPosition().getY() / 32)
                .getState().contains(State.EXPLOSION)) {
            alive = false;
        }
        if (alive) {
            Input input;
            log.info("producing action");
            if (Input.hasInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (input.getMessage().getTopic() == Topic.MOVE) {
                    move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
                    InputQueue.getInstance().remove(input);
                } else {
                    gameSession.addGameObject(new Bomb((getPosition()
                            .getX() / 32) * 32, (getPosition().getY() / 32) * 32, gameSession));
                    gameSession.getCellFromGameArea(getPosition().getX() / 32, getPosition().getY() / 32)
                            .addState(State.BOMB);
                    InputQueue.getInstance().remove(input);
                }
            }
        } else gameSession.removeGameObject(this);
    }

    public Point move(Direction direction, long time) {
        if (direction == direction.UP) {
            if (!((this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() + 24 + (int) (time * velocity)) / 32)
                    .getState().contains(State.BOX)))) {
                y = y + (int) (velocity * time);
            }
        }
        if (direction == direction.DOWN) {
            if ((this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() - (int) (time * velocity)) / 32)
                    .getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() - (int) (time * velocity)) / 32)
                    .getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea(getPosition().getX() / 32,
                    (getPosition().getY() - (int) (time * velocity)) / 32)
                    .getState().contains(State.BOX))) {
            } else y = y - (int) (velocity * time);
        }
        if (direction == direction.LEFT) {
            if ((this.gameSession.getCellFromGameArea((getPosition().getX() - (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea((getPosition().getX() - (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea((getPosition().getX() - (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.BOX))) {
            } else x = x - (int) (velocity * time);
        }
        if (direction == direction.RIGHT) {
            if ((this.gameSession.getCellFromGameArea((getPosition().getX() + 24 + (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.WALL))
                    || (this.gameSession.getCellFromGameArea((getPosition().getX() + 24 + (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.BOMB))
                    || (this.gameSession.getCellFromGameArea((getPosition().getX() + 24 + (int) (time * velocity)) / 32,
                    getPosition().getY() / 32).getState().contains(State.BOX))) {
            } else x = x + (int) (velocity * time);
        }
        return getPosition();
    }

    public String toJson() {
        String json = "{\"position\":{\"x\":" + getPosition().getX() + ",\"y\":" + getPosition().getY() + "},\"id\":" +
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
