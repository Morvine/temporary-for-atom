package ru.atom.boot.mm;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.thread.mm.*;


import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;



@Controller
@CrossOrigin
@RequestMapping("/matchmaker")
public class MatchMakerController {
    private static final Logger log = LogManager.getLogger(MatchMakerController.class);


    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> join(@RequestParam("name") String name) {
        log.info("New connection  name=" + name);
        ConnectionQueue.getInstance().offer(new GameSession(0, name));
        while (GameIdQueue.getInstance().isEmpty()) {
        }
        try {
            return new ResponseEntity<>(
                    GameIdQueue.getInstance().peek().toString(),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    "-1",
                    HttpStatus.OK);

        }

    }

    @RequestMapping(
            path = "gameId",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void connect(@RequestParam("gameId") long id) {

        log.info("New gameId id=" + id);

        GameIdQueue.getInstance().offer(new GameId(id));
    }


    @RequestMapping(
            path = "gameId",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void playerInGame(@RequestParam("playerAmount") int amount) {
        PlayerAmountQueue.getInstance().clear();
        log.info("New player amount in game=" + amount);
        PlayerAmountQueue.getInstance().offer(amount);
    }
    @RequestMapping(
            path = "gameId",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> gameId() {
        try {
            return new ResponseEntity<>(
                    GameIdQueue.getInstance().peek().toString(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    "-1",
                    HttpStatus.OK);

        }
    }


    //Тестирование
    @RequestMapping(
            path = "create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void chek0(@RequestParam("playerCount") long id) {

        log.info("Create: " + id);
    }

    @RequestMapping(
            path = "start",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void chek1(@RequestParam("gameId") long id) {

        log.info("Start: " + id);
    }


}
