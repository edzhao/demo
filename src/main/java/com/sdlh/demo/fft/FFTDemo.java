package com.sdlh.demo.fft;


import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FFTDemo {
    public static void main(String[] args) {
        double[] array = new double[]{1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d};
        Double[] sub = sub(ArrayUtils.toObject(array), 0, array.length, 4);
        for (Double value: sub) {
            System.out.println(value);
        }
    }

    @SneakyThrows(RuntimeException.class)
    public static <T> T[] sub(T[] array, int start, int end, int step) {
        int length = array == null? 0: Array.getLength(array);
        Class<?> type = array.getClass().getComponentType();

        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return (T[]) Array.newInstance(type, 0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return (T[]) Array.newInstance(type, 0);
            }
            end = length;
        }

        if (step <= 1) {
            step = 1;
        }

        final ArrayList<T> list = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            list.add(array[i]);
        }
        T[] objects = (T[]) Array.newInstance(type, list.size());

        return list.toArray(objects);
    }
}
