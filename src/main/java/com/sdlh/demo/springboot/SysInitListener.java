package com.sdlh.demo.springboot;

import com.sdlh.demo.gc.NewMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@Order(value = 1)
public class SysInitListener implements ApplicationRunner {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String BATCH_KEY = "batch";
    private static final String PIPELINE_KEY = "pipeline";
    private static final String RAW_DATA = "raw_data";
    private static final int COUNT = 100000;
    private static final String time = "2021/06/30 11:19:55.041";

    @Override
    public void run(ApplicationArguments args) {
        log.info("应用程序已启动......");
        testStringSerializer();
//        testObjectSerializer();
    }

    private void testStringSerializer() {
        redisTemplate.delete(RAW_DATA);
        int size = 100000;
        String[] messages = new String[size];
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(time, df);
        long timestamp = localDateTime.atZone(ZoneId.systemDefault())
                                      .toInstant()
                                      .toEpochMilli();
        for (int i = 0; i < size; i++) {
            messages[i] = time + "," + 245.18664550781247 + "," + 16999;
        }
        long t1 = System.currentTimeMillis();
        redisTemplate.boundListOps(RAW_DATA).rightPushAll(messages);
        log.info("批量发送" + size + "个原始数据到redis，花费时间" + (System.currentTimeMillis() - t1) + "ms");
    }

    private void testObjectSerializer() {
        redisTemplate.delete(RAW_DATA);
        int size = 100000;
        NewMessage[] messages = new NewMessage[size];
        for (int i = 0; i < size; i++) {
            messages[i] = new NewMessage(time,
                                         232.51330566406247,
//                                         86.16312789092449,
//                                         0.0,
//                                         5.0,
//                                         "云南省丽江",
                                         30000);
        }
        long t1 = System.currentTimeMillis();
        redisTemplate.boundListOps(RAW_DATA).rightPushAll(messages);
        log.info("批量发送" + size + "个原始数据到redis，花费时间" + (System.currentTimeMillis() - t1) + "ms");
    }

    private void testBatchAndPipeline() {
        List<Integer> list = IntStream.range(0, COUNT)
                                      .boxed()
                                      .collect(Collectors.toList());
        long t1 = System.currentTimeMillis();
        redisTemplate.delete(BATCH_KEY);
        log.info("redis删除单个list，花费时间" + (System.currentTimeMillis() - t1) + "ms");
        Map<String, String> map = list.stream().collect(Collectors.toMap(o -> "key:" + o, o -> "value:" + o));
        long t2 = System.currentTimeMillis();
        redisTemplate.delete(PIPELINE_KEY);
        log.info("redis遍历删除所有key，花费时间" + (System.currentTimeMillis() - t2) + "ms");
//        testNoBatch(list);
        testBatch(list);
        log.info(BATCH_KEY + ".size=" + redisTemplate.boundListOps(BATCH_KEY).size());
        testPipeline(map);
        log.info(PIPELINE_KEY + ".size=" + redisTemplate.boundHashOps(PIPELINE_KEY).size());
    }

    private void testNoBatch(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        list.forEach(integer -> redisTemplate.boundListOps(BATCH_KEY).rightPush(integer));
        log.info("非批量发送" + COUNT + "个数据到redis，花费时间" + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void testBatch(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        redisTemplate.boundListOps(BATCH_KEY).rightPushAll(list.toArray(new Integer[0]));
        log.info("批量发送" + COUNT + "个数据到redis，花费时间" + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void testPipeline(List<Integer> list) {
        long startTime = System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            list.stream().forEach(integer -> connection.rPush(redisTemplate.getKeySerializer().serialize(PIPELINE_KEY),
                                                              redisTemplate.getValueSerializer().serialize(integer)));
            return null;
        });
        log.info("pipeline批量发送" + COUNT + "个数据到redis，花费时间" + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void testPipeline(Map<String, String> map) {
        long startTime = System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            map.entrySet().stream().forEach(entry -> connection.hSet(redisTemplate.getKeySerializer().serialize(PIPELINE_KEY),
                                                                     redisTemplate.getHashKeySerializer().serialize(entry.getKey()),
                                                                     redisTemplate.getHashValueSerializer().serialize(entry.getValue())));
            return null;
        });
//        redisTemplate.boundHashOps(PIPELINE_KEY).putAll(map);
        log.info("pipeline批量发送" + COUNT + "个数据到redis，花费时间" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
