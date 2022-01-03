package com.sdlh.demo.algorithm.dsp;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class DSPMain {
    public static void main(String[] args) {
        testMovmean();
//        testArraySortIndexNoChange();
    }

    public static void testArraySortIndexNoChange() {
        double[] data = {8.2, 4.5, 3.7, 9.1, 0.35, 2.67, 1.56, 5.37};
        Map<Integer, Double> map = arraySortIndexNoChange(data, true);
        map.entrySet()
           .stream()
           .forEach(System.out::println);
    }

    public static Map<Integer, Double> arraySortIndexNoChange(double[] a, boolean isDesc) {
        Map<Integer, Double> result = new LinkedHashMap<>();
        Map<Integer, Double> map = new HashMap<>();

        for (int i = 0; i < a.length; i++) {
            map.put(i, a[i]);
        }

        Comparator<Map.Entry<Integer, Double>> comparator = Comparator.comparing(Map.Entry::getValue);
        map.entrySet()
           .stream()
           .sorted(!isDesc ? comparator : comparator.reversed())
           .forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    public static void testMovmean() {
        int num = 100000;
        double[] array = IntStream.range(0, num).asDoubleStream().toArray();
        double[] data = new double[]{0,2,4,1,3,5,7};

        long start1 = System.currentTimeMillis();
        double[] movmean1 = movmean(array, 2, 3);
        System.out.println("单线程执行" + num + "个元素的数组花费时间" + (System.currentTimeMillis() - start1) + "ms");

        long start2 = System.currentTimeMillis();
        double[] movmean2 = movmeanInParalle(array, 2, 3);
        System.out.println("多线程执行" + num + "个元素的数组花费时间" + (System.currentTimeMillis() - start2) + "ms");
    }

    private static double[] movmean(double[] data, int left, int right) {
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            if (left >= 0) {
                if (i - left >= 0) {
                    if (i + right > data.length - 1) {
                        result[i] = DoubleStream.of(ArrayUtils.subarray(data, i - left, data.length))
                                                .average()
                                                .orElse(0);
                    } else {
                        result[i] = DoubleStream.of(ArrayUtils.subarray(data, i - left, i + right + 1))
                                                .average()
                                                .orElse(0);
                    }
                } else {
                    if (i + right > data.length - 1) {
                        result[i] = DoubleStream.of(ArrayUtils.subarray(data, 0, data.length))
                                                .average()
                                                .orElse(0);
                    } else {
                        result[i] = DoubleStream.of(ArrayUtils.subarray(data, 0, i + right + 1))
                                                .average()
                                                .orElse(0);
                    }
                }
            }
        }
        return result;
    }

    private static double[] movmeanInParalle(double[] data, int left, int right) {
        double[] result = new double[data.length];
        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (left >= 0) {
                if (i - left >= 0) {
                    if (i + right > data.length - 1) {
                        final int index = i;
                        futures.add(CompletableFuture.runAsync(() -> {
                            result[index] = DoubleStream.of(ArrayUtils.subarray(data, index - left, data.length))
                                                        .average()
                                                        .orElse(0);
                        }));
                    } else {
                        final int index = i;
                        futures.add(CompletableFuture.runAsync(() -> {
                            result[index] = DoubleStream.of(ArrayUtils.subarray(data, index - left, index + right + 1))
                                                        .average()
                                                        .orElse(0);
                        }));
                    }
                } else {
                    if (i + right > data.length - 1) {
                        final int index = i;
                        futures.add(CompletableFuture.runAsync(() -> {
                            result[index] = DoubleStream.of(ArrayUtils.subarray(data, 0, data.length))
                                                        .average()
                                                        .orElse(0);
                        }));
                    } else {
                        final int index = i;
                        futures.add(CompletableFuture.runAsync(() -> {
                            result[index] = DoubleStream.of(ArrayUtils.subarray(data, 0, index + right + 1))
                                                        .average()
                                                        .orElse(0);
                        }));
                    }
                }
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                         .join();
        return result;
    }

}
