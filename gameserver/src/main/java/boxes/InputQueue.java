package boxes;

import gameobjects.Input;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InputQueue
{private static BlockingQueue<Input> instance = new LinkedBlockingQueue<>();

    public static BlockingQueue<Input> getInstance() {
        return instance;
    }

}
