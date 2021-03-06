package com.github.boybeak.selector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class Where<T, V> {

    private static final String TAG = Where.class.getSimpleName();

    static final int NONE = 0, AND = 1, OR = 2, XOR = 3;

    /*public static final String  = "<=",  = "in",
             = "not in",  = "between",  = "is null",
            OPERATOR_IS = "is", OPERATOR_IS_NOT = "is not";*/

    @Retention(RetentionPolicy.SOURCE)
    public @interface Connector{}

    private Path<T, V> path;
    private Operator operator;
    private V[] value;

    private @Connector int connector = NONE;

    public Where (Path<T, V> path, Operator operator, V ... value) {
        this (NONE, path, operator, value);
    }

    Where (@Connector int connector, Path<T, V> path, Operator operator, V ... value) {
        this.path = path;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
    }

    public int getConnector() {
        return connector;
    }

    void setConnector(int connector) {
        this.connector = connector;
    }

    public Path<T, V> getPath() {
        return path;
    }

    public V[] getValue() {
        return value;
    }

    boolean accept (T t) {

        Object obj = path.extract(t);
        return operator.accept(obj, value);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Where) {
            Where ow = (Where)obj;
            return path != null && path.equals(ow.path)
                && operator != null && operator.equals(ow.operator)
                && value != null && Arrays.equals(value, ow.value);
        }
        return false;
    }
}
