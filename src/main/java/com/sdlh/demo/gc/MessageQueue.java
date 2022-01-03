package com.sdlh.demo.gc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
    public static BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
}
