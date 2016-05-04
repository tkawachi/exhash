package com.github.tkawachi.exhash.core;

class TestClass {

    void foo(int i) {
        bar(i);
    }

    void bar(int i) {
        baz(i);
    }

    void baz(int i) {
        if (i == 1) {
            throw new RuntimeException("i == 1");
        }
        if (i == 2) {
            throw new RuntimeException("i == 2");
        }
    }
}
