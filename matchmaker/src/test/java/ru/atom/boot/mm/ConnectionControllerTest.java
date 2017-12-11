package ru.atom.boot.mm;

import org.junit.Ignore;
import org.junit.Test;
import ru.atom.thread.mm.GameIdQueue;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ConnectionControllerTest {

    @Test
    @Ignore
    public void connect() throws Exception {
        MatchMakerController connectionHandler = new MatchMakerController();
        assertThat(GameIdQueue.getInstance().isEmpty());

        connectionHandler.join( "a");
        connectionHandler.connect(1);

        assertThat(!GameIdQueue.getInstance().isEmpty());

    }

}