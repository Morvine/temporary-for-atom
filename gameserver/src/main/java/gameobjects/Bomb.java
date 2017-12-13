package gameobjects;

import geometry.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Bomb extends Field implements Positionable, Tickable, Comparable {
    private static final Logger log = LogManager.getLogger(Bomb.class);
    private final int id;
    private Point position;
    private GameSession gameSession;
    private long time;
    private boolean alive;

    public Bomb(int x, int y, GameSession gameSession) {
        super(x,y);
        this.id = getId();
        this.alive = true;
        this.gameSession = gameSession;
        this.position = new Point(x,y);
        this.time= 2000;
        log.info("Bombid = " + id + "; " + "Bomb place = (" + position.getX() + "," +
                position.getY() + ")" + "; " + "Bomb timer = " + time);
    }

    public void bang(int x,int y)
    {
        if (this.gameSession.getCellFromGameArea(getPosition().getX()/32, getPosition().getY()/32+1)
                .getState().contains(State.WALL))
        {
            this.gameSession.removeStateFromCell( x/32,(y+32)/32, State.BOX);
            this.gameSession.removeStateFromCell( x/32,(y+32)/32, State.BOMBERGIRL);
            this.gameSession.addStateToCell( x/32,(y+32)/32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((getPosition().getX()*32)/32,
                    (getPosition().getY()*32)/32+1,this.gameSession));
        }

        if (this.gameSession.getCellFromGameArea(getPosition().getX()/32, getPosition().getY()/32-1)
                .getState().contains(State.WALL))
        {
            this.gameSession.removeStateFromCell( x/32,(y-1)/32, State.BOX);
            this.gameSession.removeStateFromCell( x/32,(y-1)/32, State.BOMBERGIRL);
            this.gameSession.addStateToCell( x/32,(y-1)/32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((getPosition().getX()*32)/32,
                    (getPosition().getY()*32)/32-1,this.gameSession));
        }
        if (this.gameSession.getCellFromGameArea(getPosition().getX()/32+1, getPosition().getY()/32)
                .getState().contains(State.WALL))
        {
            this.gameSession.removeStateFromCell( (x+32)/32,(y)/32, State.BOX);
            this.gameSession.removeStateFromCell( (x+32)/32,(y)/32, State.BOMBERGIRL);
            this.gameSession.addStateToCell( (x+32)/32,(y)/32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((getPosition().getX()*32)/32+1,
                    (getPosition().getY()*32)/32,this.gameSession));
        }
        if (this.gameSession.getCellFromGameArea(getPosition().getX()/32-1, getPosition().getY()/32)
                .getState().contains(State.WALL))
        {
            this.gameSession.removeStateFromCell( (x-1)/32,(y)/32, State.BOX);
            this.gameSession.removeStateFromCell( (x-1)/32,(y)/32, State.BOMBERGIRL);
            this.gameSession.addStateToCell( (x-1)/32,(y)/32, State.EXPLOSION);
            gameSession.addGameObject(new Explosion((getPosition().getX()*32)/32-1,
                    (getPosition().getY()*32)/32,this.gameSession));
        }

        this.gameSession.addStateToCell( x/32,(y)/32, State.EXPLOSION);
        this.gameSession.addGameObject(new Explosion((getPosition().getX()*32)/31,
                (getPosition().getY()*32)/32,this.gameSession));
    }

    @Override
    public void tick(long elapsed) {
        if (alive) {
            if (time < elapsed) {
                time = 0;
                alive=false;
                bang(getPosition().getX(),getPosition().getY());
            } else {
                time -= elapsed;
            }
        } else gameSession.removeGameObject(this);
    }


    public String toJson() {
        Point pos = getPosition();
        String json = "{\"type\":\"" + this.getClass().getSimpleName() + "\",id\":" +
                this.getId() + ",\"position\":{\"x\":" + pos.getX() + ",\"y\":" + pos.getY() + "}}";
        return json;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
