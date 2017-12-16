package ru.atom.thread.mm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by sergey on 3/14/17.
 */
public class GameSession {
    private static final Logger log = LogManager.getLogger(MatchMaker.class);

    public static final int PLAYERS_IN_GAME = 4;

    private final long gameId;
    private final String name;

    public GameSession(long id, String name) {
        this.gameId = id;
        this.name = name;
    }



    public long getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
