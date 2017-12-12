package message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import gameobjects.Movable;

public class DirectionMessage {
    private final Movable.Direction direction;

    @JsonCreator
    public DirectionMessage(@JsonProperty("direction") Movable.Direction direction) {
        this.direction = direction;
    }

    public Movable.Direction getDirection() {
        return direction;
    }

}
