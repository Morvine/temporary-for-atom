package threads;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import boxes.GameCreatorQueue;

import java.io.IOException;


public class GameServer implements Runnable {
    private static final Logger log = LogManager.getLogger(GameServer.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "192.168.99.100";
    private static final String PORT = ":8080";
    private int playerCount;

    @Override
    public void run() {
        log.info("Started");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (GameCreatorQueue.getInstance().isEmpty()) {
                    //Thread.sleep(100);
                }
                playerCount = GameCreatorQueue.getInstance().poll();
                GameMechanics GameMechanics = new GameMechanics(playerCount);
                Thread game = new Thread(GameMechanics);
                game.start();
                log.info("GameMechanics with id: " + GameMechanics.getId() + " was created");
                sendGameId(GameMechanics.getId());
            } catch (Exception e) {
                log.info("No response");
            }
        }
    }

    public static Response sendGameId(long gameId) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        log.info("Id prepared to sent");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "gameId=" + gameId))
                .url(PROTOCOL + HOST + PORT + "/matchmaker/gameId")
                .build();
        log.info("Id was send");
        return client.newCall(request).execute();
    }
}
