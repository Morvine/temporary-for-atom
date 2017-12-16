package ru.atom.thread.mm;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerAmountQueue {
    private static BlockingQueue<Integer> instance = new LinkedBlockingQueue<>();

    public static BlockingQueue<Integer> getInstance() {
        return instance;
    }
}