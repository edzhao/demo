package com.sdlh.demo.functional;

@FunctionalInterface
public interface IAdder {
    int add();

    static void print() {}

    default void getCurrentValue() {}

    default void putValue() {}
}
