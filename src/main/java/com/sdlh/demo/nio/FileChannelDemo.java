package com.sdlh.demo.nio;

import com.sdlh.demo.utils.BytesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class MyThreadFactory implements ThreadFactory {
    private AtomicInteger counter = new AtomicInteger(0);
    private String prefix;

    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        int index = counter.incrementAndGet();
        Thread t = new Thread();
        t.setDaemon(false);
        t.setName(prefix + index);
        return t;
    }

    public int getIndex() {
        return counter.get();
    }
}

@Slf4j
public class FileChannelDemo {
    private static final String LOG = "D:\\workspace\\projects\\demo\\src\\main\\resources\\ocean-server.log";
    private static final String NEW_LOG = "D:\\workspace\\projects\\demo\\src\\main\\resources\\ocean-server-new.log";
    private static final String DATA_ROOT = "D:\\workspace\\测试数据\\0.5\\07051641-07051655-DAT";
    private static final String A_TXT = "D:\\workspace\\projects\\demo\\src\\main\\resources\\a.txt";
    private static ExecutorService pool = new ThreadPoolExecutor(4,
                                                                8,
                                                                10,
                                                                TimeUnit.SECONDS,
                                                                new LinkedBlockingQueue<>(),
                                                                new MyThreadFactory("MyThreadFactory-"),
                                                                new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
//        testAsynchronousFileChannel();
        testScatterChannel();
        log.info("程序退出......");
    }

    private static void testAsynchronousFileChannel() {
        boolean isPosix = FileSystems.getDefault()
                                     .supportedFileAttributeViews()
                                     .contains("posix");
        if (isPosix) {
            log.info("windows支持Posix");


        } else {
            log.info("windows不支持Posix");
        }

        Path path = Paths.get(DATA_ROOT);
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            Iterator<Path> pathIterator = directoryStream.iterator();
            List<Path> fileList = StreamSupport.stream(
                                                Spliterators.spliteratorUnknownSize(
                                                    pathIterator,
                                                    Spliterator.ORDERED),
                                            false)
                                               .filter(pa -> {
                                                   String name = pa.toFile().getName();
                                                   if (name.endsWith(".DAT")) {
                                                       return true;
                                                   } else {
                                                       return false;
                                                   }
                                               })
                                               .sorted(Comparator.comparing(pa -> Integer.valueOf(pa.toFile()
                                                                                                    .getName()
                                                                                                    .replace(".DAT", ""))))
                                               .collect(Collectors.toList());

            long t = System.currentTimeMillis();
//            List<Future<Integer>> futureList = new ArrayList<>();
//            for (Path p: fileList) {
//                futureList.add(testAsyncReadByFuture(p));
//            }
//            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
//            log.info("异步FileChannel采用Future方式共执行" + (System.currentTimeMillis() - t) + "ms");

            for (Path p: fileList) {
                testAsyncReadByCompletionHandler(p);
            }
            log.info("异步FileChannel采用CompletionHandler方式共执行" + (System.currentTimeMillis() - t) + "ms");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static Future testAsyncReadByFuture(Path path) {
        Future future = null;

        try {
            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
                                                                                           StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            Future<Integer> readFuture = asynchronousFileChannel.read(buffer, 0);
            future = CompletableFuture.runAsync(() -> {
                try {
                    Integer result = readFuture.get();
                    log.info("异步FileChannel采用Future方式读取到result=" + result + "个字节");
                    buffer.flip();
                    log.info(BytesUtil.bytesToHexString(buffer.array()));
                    buffer.clear();
                    log.info("---------------------------------------");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    try {
                        asynchronousFileChannel.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return future;
    }

    private static void testAsyncReadByCompletionHandler(Path path) {
        try {
            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//            Set<OpenOption> optionSet = new HashSet<>();
//            optionSet.add(StandardOpenOption.READ);
//            FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(
//                    PosixFilePermissions.fromString("rwxrwxrwx"));
//            AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path,
//                                                                                           optionSet,
//                                                                                  null,
//                                                                                            new FileAttribute<?>[0]);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    try {
                        log.info("异步FileChannel采用CompletionHandler方式读取到result=" + result + "个字节");
                        attachment.flip();
                        log.info(BytesUtil.bytesToHexString(attachment.array()));
                        attachment.clear();
                        log.info("---------------------------------------");
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 读取到多个缓冲区
     */
    private static void testScatterChannel() {
        try(ScatteringByteChannel channel = FileChannel.open(Paths.get(A_TXT), StandardOpenOption.READ)) {
            ByteBuffer key = ByteBuffer.allocate(5), value = ByteBuffer.allocate(5);
            ByteBuffer[] buffers = new ByteBuffer[]{key, value};
            while(channel.read(buffers)!=-1){
                key.flip();
                value.flip();
                log.info(BytesUtil.bytesToHexString(key.array()));
                log.info(BytesUtil.bytesToHexString(value.array()));
                key.clear();
                value.clear();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 从多个缓冲区写入
     */
    private static void testGatherChannel() {
        try(FileChannel channel = FileChannel.open(Paths.get(A_TXT), StandardOpenOption.WRITE)) {
            ByteBuffer key = ByteBuffer.allocate(10), value = ByteBuffer.allocate(10);
            byte[] data = "017 Robothy".getBytes();
            key.put(data, 0, 3);
            value.put(data, 4, data.length-4);
            ByteBuffer[] buffers = new ByteBuffer[]{key, value};
            key.flip();
            value.flip();
            channel.write(buffers);
            channel.force(false); // 将数据刷出到磁盘
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void testSeekableByteChannel() {
    }

    private static void transeferTo() {}
    private static void transeferFrom() {}
    private static void truncate() {}
    private static void mmap() {}
    private static void fileLock() {}
}
