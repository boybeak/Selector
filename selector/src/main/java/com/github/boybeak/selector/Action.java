package com.github.boybeak.selector;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public interface Action<T> {
    void action(int index, T t);
}
