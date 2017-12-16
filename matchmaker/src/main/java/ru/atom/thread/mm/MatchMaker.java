package ru.atom.thread.mm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.bd.Gamesession;
import ru.atom.bd.UserDao;
import ru.atom.boot.mm.MatchMakerClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


public class MatchMaker implements Runnable {
    private static final Logger log = LogManager.getLogger(MatchMaker.class);


    @Override
    public void run() {
        log.info("Started");
        boolean create = false;
        UserDao userDao = new UserDao();
        List<GameSession> candidates = new ArrayList<>();
        MatchMakerMonitoring monitor = new MatchMakerMonitoring();
        JsonWork jsonWork = new JsonWork();
        int playerInGame = GameSession.PLAYERS_IN_GAME;
        while (!Thread.currentThread().isInterrupted()) {

            while (!ConnectionQueue.getInstance().isEmpty())
            {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            }




            //Создание сессии

            if (candidates.size() == 1) {
                if(!PlayerAmountQueue.getInstance().isEmpty()){
                    playerInGame = PlayerAmountQueue.getInstance().peek();
                } else
                    playerInGame = GameSession.PLAYERS_IN_GAME;
                try {
                    MatchMakerClient.create(playerInGame);
                } catch (Exception e) {
                    log.warn("Bad request to GameServer in create request");
                }
            }

            //Старт сессии

            if (candidates.size() == playerInGame) {
                try {
                   Gamesession newUser = new Gamesession(GameIdQueue.getInstance().peek().getGameId(),
                            candidates.get(0).toString(), candidates.get(1).toString());
                    userDao.insert(newUser);

                    MatchMakerClient.start(GameIdQueue.getInstance().poll().getGameId());
                    monitor.decrimentQueue(playerInGame);
                } catch (Exception e) {
                    log.warn("Bad request to GameServer in start request");
                    GameIdQueue.getInstance().poll();

                }
                log.info("Session created");
                candidates.clear();
            }

        }
    }
}

