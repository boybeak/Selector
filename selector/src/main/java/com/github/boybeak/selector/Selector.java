package com.github.boybeak.selector;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/21.
 */

public abstract class Selector<T> {

    /**
     *
     * @param tClass The element contains in list
     * @param list Target data list
     * @param <T>
     * @return A ListSelector instance
     */
    public static <T> ListSelector<T> selector (Class<T> tClass, List list) {
        return new ListSelector<>(tClass, list);
    }

    /**
     *
     * @param tClass The element contains in ts
     * @param ts Target data array
     * @param <T>
     * @return An ArraySelector instance
     */
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

    /**
     * Add query conditions.
     * @param path  The invoke path of methods or fields start with the target class.
     * @param operator {@link Operator}
     * @param value The target value
     * @param <V>
     * @return
     */
    public <V> WhereDelegate<T> where (Path<T, V> path, Operator operator, V ... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    /**
     * Add query conditions.
     * @param where A where condition
     * @param <V>
     * @return
     */
    public <V> WhereDelegate<T> where (Where<T, V> where) {
        getWhereDelegate().addWithCheck(where);
        return getWhereDelegate();
    }

    /**
     * @param action
     */
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

    /**
     * Extract the first value find by path
     * @param path
     * @param <V>
     * @return
     */
    public <V> V extractFirst (Path<T, V> path) {
        return getWhereDelegate().extractFirst(path);
    }

    /**
     * Extract the last value find by path
     * @param path
     * @param <V>
     * @return
     */
    public <V> V extractLast (Path<T, V> path) {
        return getWhereDelegate().extractLast(path);
    }

    /**
     * Only work for {@link Comparable} values
     * @param path
     * @param <V>   A value that implements {@link Comparable}
     * @return
     */
    public <V> T max (Path<T, V> path) {
        return getWhereDelegate().max(path);
    }

    /**
     * Only work for {@link Comparable} values
     * @param path  A value that implements {@link Comparable}
     * @param <V>
     * @return
     */
    public <V> T min (Path<T, V> path) {
        return getWhereDelegate().min(path);
    }

    /**
     * Only work for some basic data types. Such as int, float etc.
     * @param path
     * @param <V>
     * @return
     */
    public <V> V avg (Path<T, V> path) {
        return getWhereDelegate().avg(path);
    }

    /**
     * Only work for some basic data types. Such as int, float etc.
     * @param path
     * @param <V>
     * @return
     */
    public <V> double sum (Path<T, V> path) {
        return getWhereDelegate().sum(path);
    }

}
