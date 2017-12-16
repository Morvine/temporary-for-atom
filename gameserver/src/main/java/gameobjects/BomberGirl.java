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

import java.util.concurrent.ConcurrentLinkedQueue;


public class BomberGirl extends Field implements Tickable, Movable, Comparable {
    private static final Logger log = LogManager.getLogger(BomberGirl.class);
    private float x;
    private float y;
    private int id;
    private double velocity;
    private boolean alive = true;
    private int maxBombs;
    private int bombPower;
    private double speedModifier = 1.0;
    private WebSocketSession session;
    private GameSession gameSession;
    private ConcurrentLinkedQueue<Boolean> bombStatus = new ConcurrentLinkedQueue<>();

    public BomberGirl(float x, float y, WebSocketSession session, GameSession gameSession) {
        super((int)x,(int) y);
        this.x = x;
        this.y = y;
        this.id = getId();
        this.session = session;
        this.gameSession = gameSession;
        this.velocity = 0.2;
        this.bombPower = 1;
        this.maxBombs = 1;
        log.info("New BomberGirl with id {}", id);
        Broker.getInstance().send(ConnectionPool.getInstance().getPlayer(session), Topic.POSSESS, id);
    }

    public void tick(long elapsed) {
        //log.info("tick");
        if (gameSession.getCellFromGameArea((int)this.x + 12, (int)this.y + 12)
                .getState().contains(State.EXPLOSION)) {
            //alive = false;
        }
        if (alive) {
            if (gameSession.getCellFromGameArea((int)x + 12, (int)y + 12).getState().contains(State.BONUSBOMB)) {
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUS);
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUSBOMB);
                this.maxBombs++;
            }
            if (gameSession.getCellFromGameArea((int)x + 12, (int)y + 12).getState().contains(State.BONUSFIRE)) {
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUS);
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUSFIRE);
                this.bombPower++;
                log.info("Stop");
            }
            if (gameSession.getCellFromGameArea((int)x + 12, (int)y + 12).getState().contains(State.BONUSSPEED)) {
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUS);
                gameSession.removeStateFromCell((int)x + 12, (int)y + 12, State.BONUSSPEED);
                this.speedModifier++;
            }
            Input input;
            //log.info("producing action");
            if (Input.hasMoveInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                log.warn(receiveDirection(session, input.getMessage().getData()).getDirection());
                move(receiveDirection(session, input.getMessage().getData()).getDirection(), elapsed);
                InputQueue.getInstance().remove(input);
            }
            if (Input.hasBombInputForPlayer(session)) {
                input = Input.getInputForPlayer(session);
                if (maxBombs > bombPlantedCount()) {
                    gameSession.addGameObject(new Bomb((int)this.x + 12, (int)this.y + 12, gameSession, bombPower, this));
                    gameSession.getCellFromGameArea((int)this.x, (int)this.y)
                            .addState(State.BOMB);
                    bombStatus.offer(true);
                    InputQueue.getInstance().remove(input);
                }
            }
        } else gameSession.removeGameObject(this);
    }

    public Point move(Movable.Direction direction, long time) {
        int shift = (int) (time * velocity * speedModifier);
        if (direction == Movable.Direction.UP) {
            if (!isCellSolid((int)x, (int)y + 23 + shift) && !isCellSolid((int)x + 23, (int)y + 23 + shift)) {
                y = y + shift;
            } else if (!isCellSolid((int)x + 26, (int)y + 23 + shift)) {
                if (!isCellSolid((int)x + 23 + shift, (int)y) && !isCellSolid((int)x + 23 + shift, (int)y + 23))
                    x = x + shift;
            } else if (!isCellSolid((int)x - 2, (int)y + 23 + shift)) {
                if (!isCellSolid((int)x - shift, (int)y) && !isCellSolid((int)x - shift, (int)y + 23))
                    x = x - shift;
            }
            //log.info(this.y);
        }
        if (direction == Movable.Direction.DOWN) {
            if (!isCellSolid((int)x, (int)y - shift) && !isCellSolid((int)x + 23, (int)y - shift)) {
                y = y - shift;
            } else if (!isCellSolid((int)x + 26, (int)y - shift)) {
                if (!isCellSolid((int)x + 23 + shift, (int)y) && !isCellSolid((int)x + 23 + shift, (int)y + 23))
                    x = x + shift;
            } else if (!isCellSolid((int)x - 2, (int)y - shift)) {
                if (!isCellSolid((int)x - shift, (int)y) && !isCellSolid((int)x - shift, (int)y + 23))
                    x = x - shift;
            }
        }
        if (direction == Movable.Direction.LEFT) {
            if (!isCellSolid((int)x - shift, (int)y) && !isCellSolid((int)x - shift, (int)y + 23)) {
                x = x - shift;
            } else if (!isCellSolid((int)x - shift, (int)y + 26)) {
                if (!isCellSolid((int)x, (int)y + 23 + shift) && !isCellSolid((int)x + 23, (int)y + 23 + shift))
                    y = y + shift;
            } else if (!isCellSolid((int)x - shift, (int)y - 2)) {
                if (!isCellSolid((int)x, (int)y - shift) && !isCellSolid((int)x + 23, (int)y - shift))
                    y = y - shift;
            }
        }
        if (direction == Movable.Direction.RIGHT) {
            if (!isCellSolid((int)x + 23 + shift, (int)y) && !isCellSolid((int)x + 23 + shift, (int)y + 23)) {
                x = x + shift;
            } else if (!isCellSolid((int)x + 23 + shift, (int)y + 26)) {
                if (!isCellSolid((int)x, (int)y + 23 + shift) && !isCellSolid((int)x + 23, (int)y + 23 + shift))
                    y = y + shift;
            } else if (!isCellSolid((int)x + 23 + shift, (int)y - 2)) {
                if (!isCellSolid((int)x, (int)y - shift) && !isCellSolid((int)x + 23, (int)y - shift))
                    y = y - shift;
            }
        }
        return new Point((int)x, (int)y);
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

    public int bombPlantedCount() {
        int count = 0;
        for (boolean s : bombStatus) {
            if (s)
                count++;
        }
        return count;
    }

    public void changeBombStatus() {
        for (boolean b : bombStatus)
            if (b)
                bombStatus.poll();
    }


}
