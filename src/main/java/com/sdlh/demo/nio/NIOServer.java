package com.sdlh.demo.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

@Slf4j
public class NIOServer implements Closeable {
    private static Selector selector;
    private static ServerSocketChannel serverSocketChannel;

    public static void main(String[] args) {
        try(NIOServer server = new NIOServer()) {
            server.init();
            server.listen();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        selector.close();
        serverSocketChannel.close();
        log.info("NIO服务器端已关闭");
    }

    private void init() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost",7321));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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

    private void process(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                log.info("收到acceptable事件");
                ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
                SocketChannel clientChannel = serverChannel.accept();
                clientChannel.configureBlocking(false);
                clientChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                log.info("收到readable事件");
                SocketChannel clientChannel = (SocketChannel) key.channel();
                // 在通道中读取客户端的数据
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = clientChannel.read(buffer);
                if (read < 0) {
                    //当读取的长度小于0说明连接断开，必须要把客户端通道关闭，否则会不停的进入readable分支
                    clientChannel.close();
                } else {
                    // trim方法用于删除字符串两边的空格字符串
                    String request = new String(buffer.array(), 0, read).trim();
                    log.info("收到客户端请求:" + request);
                    //不采用OP_WRITE事件在OP_READ事件里直接往channel里写，这种方式在需要多次写的情况下会造成阻塞，极端情况网络拥塞下还会造成死循环
//                    String response = "Ok, I heard you client.";
//                    buffer.flip();
//                    buffer.clear();
//                    writeToClient(clientChannel, response, buffer);

                    //采用OP_WRITE事件方式，这种方式可以避免以上问题，但是需要注意每写一次都要注册OP_WRITE事件写完后要取消注册OP_WRITE事件，否则也会死循环
                    int interestOps = key.interestOps();
                    if ((interestOps & SelectionKey.OP_WRITE) == 0) {
                        try(FileChannel fileChannel = FileChannel.open(
                                Paths.get("D:\\workspace\\projects\\demo\\src\\main\\resources\\logback.xml"),
                                StandardOpenOption.READ)) {
                            long size = fileChannel.size();
                            ByteBuffer fileBuffer = ByteBuffer.allocate((int) size);
                            log.info("已读取" + fileChannel.read(fileBuffer) + "个字节到缓冲区");
                            fileBuffer.flip();
                            key.attach(fileBuffer);
                            key.interestOps(interestOps | SelectionKey.OP_WRITE);
                        }
                    }
                }
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
                log.info("服务器端网络拥塞，重新注册写事件");
                int interestOps = key.interestOps();
                if ((interestOps & SelectionKey.OP_WRITE) == 0) {
                    key.interestOps(interestOps | SelectionKey.OP_WRITE);
                    log.info("发送失败，重新注册OP_WRITE事件");
                }
            }
        } else {
            //发送完了就取消写事件，否则下次还会进入该分支
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            log.info("发送完毕，取消OP_WRITE事件注册");
        }
    }

    private void writeToClient(SocketChannel clientChannel, String response, ByteBuffer buffer) throws IOException {
        buffer.put(response.getBytes());
        buffer.flip();
        // ByteBuffer包装数据再返回给通道
        clientChannel.write(buffer);
    }
}
