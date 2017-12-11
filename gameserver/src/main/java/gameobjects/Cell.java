package gameobjects;

import geometry.Bar;
import java.util.ArrayList;

public class Cell extends Bar {
    private int x;
    private int y;
    private ArrayList<State> states = new ArrayList<>();

    public Cell(int x, int y, State state) {
        super(x, y, x + 32, y + 32);
        this.states.add(state);
    }

    public void addState(State state) {
        this.states.add(state);
    }

    public ArrayList<State> getState() {
        return states;
    }
}
