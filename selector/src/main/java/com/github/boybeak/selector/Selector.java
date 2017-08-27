package com.github.boybeak.selector;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/21.
 */

public abstract class Selector<T> {

    public static <T> ListSelector<T> selector (Class<T> tClass, List list) {
        return new ListSelector<>(tClass, list);
    }

    public static <T> ArraySelector<T> selector (Class<T> tClass, Object[] ts) {
        return new ArraySelector<>(tClass, ts);
    }

    private Class<T> mTClass;

    Selector (Class<T> tClass) {
        mTClass = tClass;
    }

    public abstract int getSize ();
    public abstract Object get (int index);
    abstract WhereDelegate<T> getWhereDelegate ();
    public boolean isEmpty () {
        return getSize() == 0;
    }

    public Class<T> getTargetClass () {
        return mTClass;
    }

    public <V> WhereDelegate<T> where (Path<T, V> path, Operator operator, V ... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    public <V> WhereDelegate<T> where (Where<T, V> where) {
        getWhereDelegate().addWithCheck(where);
        return getWhereDelegate();
    }

    public void map (Action<T> action) {
        getWhereDelegate().map(action);
    }

    public List<T> findAll () {
        return getWhereDelegate().findAll();
    }

    public T findFirst () {
        return getWhereDelegate().findFirst();
    }

    public T findLast () {
        return getWhereDelegate().findLast();
    }

    public int count () {
        return getWhereDelegate().count();
    }

    public <V> List<V> extractAll(Path<T, V> path) {
        return getWhereDelegate().extractAll(path);
    }

    public <V> V extractFirst (Path<T, V> path) {
        return getWhereDelegate().extractFirst(path);
    }

    public <V> V extractLast (Path<T, V> path) {
        return getWhereDelegate().extractLast(path);
    }

    public <V> T max (Path<T, V> path) {
        return getWhereDelegate().max(path);
    }

    public <V> T min (Path<T, V> path) {
        return getWhereDelegate().min(path);
    }

    public <V> V avg (Path<T, V> path) {
        return getWhereDelegate().avg(path);
    }

    public <V> double sum (Path<T, V> path) {
        return getWhereDelegate().sum(path);
    }

}
