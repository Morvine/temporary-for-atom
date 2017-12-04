package threads;

import boxes.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import boxes.GameStarterQueue;


public class GameSession implements Runnable {
    private static final Logger log = LogManager.getLogger(GameServer.class);
    private static AtomicLong idGenerator = new AtomicLong();

    private int playerCount;

    private long id = idGenerator.getAndIncrement() + 1;

    public GameSession(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            while (!GameStarterQueue.getInstance().contains((int) id)) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            GameStarterQueue.getInstance().poll();
            log.info("Game {} started", id);
            log.info("Players in this session:");

            ConcurrentLinkedQueue playerQueue = ConnectionPool.getInstance().getPlayerQueueWithGameId((int) id);
            while (!playerQueue.isEmpty()) {
                log.info(playerQueue.poll());
            }

            try {
                Thread.sleep(5000);
            } catch (Exception e) {

            }
            log.info("Game #{} over", id);
            Thread.currentThread().interrupt();
        }
    }


    public long getId() {
        return id;
    }
}
