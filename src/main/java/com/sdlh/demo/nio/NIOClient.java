package com.sdlh.demo.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class NIOClient implements Closeable {
    private static Selector selector;
    private static SocketChannel socketChannel;

    public static void main(String[] args) {
        try(NIOClient client = new NIOClient()){
            client.init();
            client.listen();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void init() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",7321));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    private void listen() throws IOException {
        while (!Thread.currentThread().isInterrupted()) {
            int select = selector.select();
            log.info("当前有" + select + "个通道可进行IO操作");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                process(key);
            }
        }
    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private void process(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel)key.channel();

            if (key.isConnectable()) {
                log.info("收到connectable事件");
                boolean finishConnect = channel.finishConnect();
                if (finishConnect) {
                    channel.register(selector, SelectionKey.OP_READ);
                    writeToServer(channel, "hello server, I'm client");
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                log.info("收到readable事件");
                buffer.clear();
                int length = channel.read(buffer);
                String response = new String(buffer.array(), 0, length);
                log.info("从服务器端接收到回复:\n" + response);
            }
            if (key.isWritable()) {
                log.info("收到writable事件");
                doWrite(key);
            }
        } else {
            log.info("-----------key is invalid!-----------");
        }
    }

    private void doWrite(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel clientChannel = (SocketChannel) key.channel();
        if (buffer.hasRemaining()) {
            int write = clientChannel.write(buffer);
            if (write == 0) {
                log.info("客户端网络拥塞，重新注册写事件");
                int interestOps = key.interestOps();
                if ((interestOps & SelectionKey.OP_WRITE) == 0) {
                    key.interestOps(interestOps | SelectionKey.OP_WRITE);
                    log.info("发送失败，重新注册OP_WRITE事件");
                }
            }
        } else {
            //发送完了就取消写事件，否则下次还会进入该分支
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    private void writeToServer(SocketChannel channel, String request) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(request.getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    @Override
    public void close() throws IOException {
        selector.close();
        socketChannel.close();
        log.info("NIO客户端已关闭");
    }
}
