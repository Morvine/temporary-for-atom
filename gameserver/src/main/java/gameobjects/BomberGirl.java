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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class BomberGirl extends Field implements Tickable, Movable, Comparable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private int x;
    private int y;
    private int id;
    private double velocity;
    private boolean alive = true;
    private int maxBombs;
    private int bombPower;
    private double speedModifier = 1.0;
    private WebSocketSession session;
    private GameSession gameSession;
    protected List<Boolean> bombStatus = new ArrayList<>();

    public BomberGirl(int x, int y, WebSocketSession session, GameSession gameSession) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.id = getId();
        this.session = session;
        this.gameSession = gameSession;
        this.velocity = 0.2;
        this.bombPower = 1;
        this.maxBombs = 3;
        log.info("New BomberGirl with id {}", id);
        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.POSSESS, id);
    }

    public void tick(long elapsed) {
        //log.info("tick");
        if (gameSession.getCellFromGameArea(this.x + 12, this.y + 12)
                .getState().contains(State.EXPLOSION)) {
            alive = false;
        }
        if (alive) {
            Input input;
            //log.info("producing action");
            if (Input.hasInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (input.getMessage().getTopic() == Topic.MOVE) {
                    log.warn(receiveDirection(session, input.getMessage().getData()).getDirection());
                    move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
                    InputQueue.getInstance().remove(input);
                } else {
                    gameSession.addGameObject(new Bomb(this.x + 12, this.y + 12, gameSession, bombPower));
                    gameSession.getCellFromGameArea(this.x, this.y)
                            .addState(State.BOMB);

                }
                InputQueue.getInstance().remove(input);
            }

        } else gameSession.removeGameObject(this);
    }

    public Point move(Movable.Direction direction, long time) {
        int shift = (int) (time * velocity);
        if (direction == Movable.Direction.UP) {
            if (!isCellSolid(x, y + 23 + shift) && !isCellSolid(x + 23, y + 23 + shift)) {
                y = y + (int) (velocity * time);
            } else if (!isCellSolid(x + 26, y + 23 + shift)) {
                x = x + shift;
            } else if (!isCellSolid(x - 2, y + 23 + shift)) {
                x = x - shift;
            }
            //log.info(this.y);
        }
        if (direction == Movable.Direction.DOWN) {
            if (!isCellSolid(x, y - shift) && !isCellSolid(x + 23, y - shift)) {
                y = y - shift;
            } else if (!isCellSolid(x + 26, y - shift)) {
                x = x + shift;
            } else if (!isCellSolid(x - 2, y - shift)) {
                x = x - shift;
            }
        }
        if (direction == Movable.Direction.LEFT) {
            if (!isCellSolid(x - shift, y) && !isCellSolid(x - shift, y + 23)) {
                x = x - shift;
            } else if (!isCellSolid(x - shift, y + 26)) {
                y = y + shift;
            } else if (!isCellSolid(x - shift, y - 2)) {
                y = y - shift;
            }
        }
        if (direction == Movable.Direction.RIGHT) {
            if (!isCellSolid(x + 23 + shift, y) && !isCellSolid(x + 23 + shift, y + 23)) {
                x = x + shift;
            } else if (!isCellSolid(x + 23 + shift, y + 26)) {
                y = y + shift;
            } else if (!isCellSolid(x + 23 + shift, y - 2)) {
                y = y - shift;
            }
        }
        return new Point(x, y);
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

    public boolean isCellSolid(int x, int y) {
        return (this.gameSession.getCellFromGameArea(x,
                y).getState().contains(State.WALL)) || (this.gameSession.getCellFromGameArea(x, y)
                .getState().contains(State.BOX))
                /*|| (this.gameSession.getCellFromGameArea(coordX,
                    coordY)
                    .getState().contains(State.BOMB))*/;

    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

}
