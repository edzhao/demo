package com.sdlh.demo.stream;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        String[] employees = {"zak", "fenix", "jude", "wason", "bond", "howard", "sky", "frank"};
        Spliterator<Object> spliterator1 = Spliterators.spliterator(employees, 0);
//        Spliterator<Object> spliterator2 = spliterator1.trySplit();
//        spliterator1.forEachRemaining(o -> System.out.print(o + ","));
//        System.out.println("------------------------------------------------");
//        spliterator2.forEachRemaining(o -> System.out.print(o + ","));
        spliterator1.tryAdvance(i -> System.out.print(i+",")); //取出a
        spliterator1.tryAdvance(i -> System.out.print(i+",")); //取出b
        spliterator1.tryAdvance(i -> System.out.print(i+",")); //取出c
        spliterator1.tryAdvance(i -> System.out.print(i+",")); //取出d
        Stream.of(employees).forEach(s -> System.out.println(s));
    }
}
