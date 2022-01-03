package com.sdlh.demo;

import com.sdlh.demo.utils.BytesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Main {
    private static final String ocean_server = "D:\\workspace\\projects\\ocean-server\\src\\main";
    private static final String ocean_web = "D:\\workspace\\projects\\ocean-web\\src";
    private static final String seahorse_server = "D:\\workspace\\projects\\seahorse-server\\src\\main";
    private static final String seahorse_web = "D:\\workspace\\projects\\seahorse-web\\src";
    private static final String sealand_server = "D:\\workspace\\projects\\sealand-server\\src\\main";
    private static final String sealand_web = "D:\\workspace\\projects\\sealand-web\\src";
    private static final String dolphin_server = "D:\\workspace\\projects\\dolphin-server\\src\\main";
    private static final String dolphin_web = "D:\\workspace\\projects\\dolphin-web\\src";
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String NANO_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";

    private static boolean exited = false;

    public static void main(String[] args) {
//        statistics();
//        testBigDecimal();
//        testStringToTimeStamp();
//        byte[] bytes = BytesUtil.hexStringToByteArray("55");
//        for (byte b: bytes) {
//            System.out.println(b + ",");
//        }
        testMicroSecond();
    }

    private static void testMicroSecond() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(NANO_PATTERN);

        LocalTime localTime = LocalTime.now();
        System.out.println(localTime.getHour() + "时" + localTime.getMinute() + "分" + localTime.getSecond() + "秒"
                + localTime.get(ChronoField.MILLI_OF_SECOND) + "毫秒"
                + localTime.get(ChronoField.MICRO_OF_SECOND) + "微秒"
                + localTime.get(ChronoField.NANO_OF_SECOND) + "纳秒");

        Instant instant = Instant.now();
        System.out.println(dtf.format(LocalDateTime.ofEpochSecond(instant.getEpochSecond(), instant.getNano(), ZoneOffset.of("+8"))));

        System.out.println(ZonedDateTime.now().format(dtf));
    }

    private static void testStringToTimeStamp() {
        LocalDateTime localDateTime = LocalDateTime.parse("2021-06-30 11:19:55.041", DateTimeFormatter.ofPattern(PATTERN));
        System.out.println(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private static void testTimeStampToString() {
        long timestamp = 1564502400000L;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        DateTimeFormatter df = DateTimeFormatter.ofPattern(PATTERN);
        System.out.println(localDateTime.format(df));
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        System.out.println(sdf.format(date));
    }

    private static void testBigDecimal() {
        BigDecimal decimal = BigDecimal.valueOf(9.135).setScale(2, RoundingMode.HALF_UP);
        System.out.println(decimal.toPlainString());
        double d = 9.135;
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println(df.format(d));
    }

    private static void statistics() {
        long numOfOceanServer = loopAllFiles(Paths.get(ocean_server), new LongAdder());
        long numOfOceanWeb = loopAllFiles(Paths.get(ocean_web), new LongAdder());
        log.info("ocean_server总共有" + numOfOceanServer + "行代码, ocean_web总共有" + numOfOceanWeb
                         + "行代码, 现场数据中心项目总共有" + (numOfOceanServer + numOfOceanWeb) + "行代码");
        long numOfSeahorseServer = loopAllFiles(Paths.get(seahorse_server), new LongAdder());
        long numOfSeahorseWeb = loopAllFiles(Paths.get(seahorse_web), new LongAdder());
        log.info("seahorse_server总共有" + numOfSeahorseServer + "行代码, seahorse_web总共有" + numOfSeahorseWeb
                         + "行代码, 研发测试中心项目总共有" + (numOfSeahorseServer + numOfSeahorseWeb) + "行代码");
        long numOfSealandServer = loopAllFiles(Paths.get(sealand_server), new LongAdder());
        long numOfSealandWeb = loopAllFiles(Paths.get(sealand_web), new LongAdder());
        log.info("sealand_server总共有" + numOfSealandServer + "行代码, sealand_web总共有" + numOfSealandWeb
                         + "行代码, 总后台项目总共有" + (numOfSealandServer + numOfSealandWeb) + "行代码");
        long numOfDolphinServer = loopAllFiles(Paths.get(dolphin_server), new LongAdder());
        long numOfDolphinWeb = loopAllFiles(Paths.get(dolphin_web), new LongAdder());
        log.info("dolphin_server总共有" + numOfDolphinServer + "行代码, dolphin_web总共有" + numOfDolphinWeb
                         + "行代码, 全生命周期跟踪项目总共有" + (numOfDolphinServer + numOfDolphinWeb) + "行代码");
    }

    private static long loopAllFiles(Path root, LongAdder counter) {
        try(Stream<Path> stream = Files.list(root)) {
            stream.parallel()
                  .forEach(path -> {
                      File file = path.toFile();
                      String name = file.getName();
                      if (file.isDirectory()) {
                          if (file.getAbsolutePath().contains("-web")) {
                              if (!name.equals(".umi") && !name.equals("e2e")) {
                                  loopAllFiles(path, counter);
                              }
                          }
                          if (file.getAbsolutePath().contains("-server")) {
                              loopAllFiles(path, counter);
                          }
                      } else {
                          if (file.getAbsolutePath().contains("-server")) {
                              if (name.endsWith(".java")) {
                                  counter.add(getLinesNum(path));
                              }
                          }
                          if (file.getAbsolutePath().contains("-web")) {
                              if (name.endsWith(".js") || name.endsWith(".jsx") || name.endsWith(".ts")
                                      || name.endsWith(".tsx") || name.endsWith(".less")) {
                                  counter.add(getLinesNum(path));
                              }
                          }
                      }
                  });
            return counter.longValue();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return 0;
        }
    }

    private static long getLinesNum(Path path) {
        long result = 0;
        try {
            result = Files.lines(path).count();
            return result;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return result;
        }
    }
}
