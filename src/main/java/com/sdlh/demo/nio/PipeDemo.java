package com.sdlh.demo.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;

@Slf4j
public class PipeDemo {
    public static final String LOG = "D:\\workspace\\projects\\demo\\src\\main\\resources\\ocean-server.log";
    public static final String NEW_LOG = "D:\\workspace\\projects\\demo\\src\\main\\resources\\ocean-server-new.log";
    public static final int capacity = 2097152;
    public static ByteBuffer sinkBuffer = ByteBuffer.allocate(capacity);
    public static ByteBuffer sourceBuffer = ByteBuffer.allocate(capacity);

    public static void main(String[] args) throws IOException {
        long t1 = System.currentTimeMillis();
        final Pipe pipe = Pipe.open();

        Thread sinkThread = new Thread(() -> sink(pipe));
        sinkThread.setName("sinkThread");
        sinkThread.start();

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread sourceThread = new Thread(() -> source(pipe));
        sourceThread.setName("sourceThread");
        sourceThread.start();

        log.info("本次程序共执行" + (System.currentTimeMillis() - t1) + "ms");
    }

    private static void sink(Pipe pipe) {
        try(Pipe.SinkChannel sink = pipe.sink()) {
//            sink.configureBlocking(false);
            long t2 = System.currentTimeMillis();

            try(RandomAccessFile file = new RandomAccessFile(LOG, "r");
                FileChannel fileChannel = file.getChannel()) {
                int len = 0, temp = 0, times = 0, count = 0;
                while ((temp = fileChannel.read(sinkBuffer)) > 0) {
                    sinkBuffer.flip();
                    count++;
                    while (sinkBuffer.hasRemaining()) {
                        int write = sink.write(sinkBuffer);
                        log.info("第" + (++times) + "次共写入" + write + "个字节到sink管道");
                    }
                    sinkBuffer.clear();
                    len += temp;
                }
                log.info("分" + count + "批，总共读取" + len + "个字节到sink管道，花费时间" + (System.currentTimeMillis() - t2) + "ms");
                log.info("sink线程退出");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void source(Pipe pipe) {
        try(Pipe.SourceChannel source = pipe.source()) {
            long t3 = System.currentTimeMillis();

            try(RandomAccessFile file = new RandomAccessFile(NEW_LOG, "rw");
                FileChannel fileChannel = file.getChannel()) {
                int len = 0, temp = 0, count = 0;
                while ((temp = source.read(sourceBuffer)) > 0) {
                    sourceBuffer.flip();
                    log.info("第" + (++count) + "次从source管道读取" + temp + "个字节写入文件");
                    while (sourceBuffer.hasRemaining()) {
                        fileChannel.write(sourceBuffer);
                    }
                    sourceBuffer.clear();
                    len += temp;
                }
                log.info("分" + count + "批，总共写入" + len + "个字节，花费时间" + (System.currentTimeMillis() - t3)+ "ms");
                log.info("source线程退出");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
