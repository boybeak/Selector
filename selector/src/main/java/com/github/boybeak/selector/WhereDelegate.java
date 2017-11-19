package com.github.boybeak.selector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 */
public class WhereDelegate<T> {

    private List<Where> whereList = null;
    private Selector<T> mSelector;

    WhereDelegate (Selector<T> selector) {
        whereList = new ArrayList<>();
        mSelector = selector;
    }

    protected List<Where> getWhereList () {
        return whereList;
    }

    protected boolean isT (Object object) {
        return mSelector.getTargetClass().isInstance(object);
    }

    public <V> WhereDelegate<T> and (Path<T, V> path, Operator operator, V ... value) {
        return and(new Where<T, V>(Where.AND, path, operator, value));
    }

    public <V> WhereDelegate<T> and (Where<T, V> where) {
        where.setConnector(Where.AND);
        addWithCheck(where);
        return this;
    }

    public <V> WhereDelegate<T> or (Path<T, V> path, Operator operator, V ... value) {
        return or(new Where<T, V>(Where.OR, path, operator, value));
    }

    public <V> WhereDelegate<T> or (Where<T, V> where) {
        where.setConnector(Where.OR);
        addWithCheck (where);
        return this;
    }

    <V> void addWithCheck (Where<T, V> where) {
        if (!whereList.contains(where)) {
            whereList.add(where);
        }
    }

    boolean acceptAnd (boolean a, boolean b) {
        return a && b;
    }

    boolean acceptOr (boolean a, boolean b) {
        return a || b;
    }

    boolean acceptXor (boolean a, boolean b) {
        return a != b;
    }

    <V> boolean accept (T t) {
        if (whereList.isEmpty()) {
            return true;
        }
        final int length = whereList.size();
        boolean result = whereList.get(0).accept(t);
//            Log.v(TAG, "accept before loop=" + result);
        for (int i = 1; i < length; i++) {
            Where<T, V> where = whereList.get(i);
//                Log.v(TAG, "accept " + where.getConnector() + " " + where.getKey());
            switch (where.getConnector()) {
                case Where.AND:
                    result = acceptAnd(result, where.accept(t));
                    break;
                case Where.OR:
                    result = acceptOr(result, where.accept(t));
                    break;
                case Where.XOR:
                    result = acceptXor(result, where.accept(t));
                    break;
                case Where.NONE:
                    continue;
            }
//                Log.v(TAG, "accept result=" + result + " after=" + where.getKey());
        }
        return result;
    }

    public void map (Action<T> tAction) {
        if (!mSelector.isEmpty()) {
            final int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        tAction.action(i, (T)object);
                    }
                }
            }
        }
    }

    public ArrayList<T> findAll () {
        ArrayList<T> tList = new ArrayList<>();
        if (!mSelector.isEmpty()) {

            int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        tList.add(t);
                    }
                }
            }
            return tList;
        }
        return null;
    }

    public int firstIndex () {
        if (!mSelector.isEmpty()) {
            int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public T findFirst () {
        int index = firstIndex();
        if (index >= 0) {
            return (T)mSelector.get(index);
        }
        return null;
        /*if (!mSelector.isEmpty()) {
            int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        return t;
                    }
                }
            }
        }
        return null;*/
    }

    public int lastIndex () {
        if (!mSelector.isEmpty()) {
            int size = mSelector.getSize();
            for (int i = size - 1; i >= 0; i--) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public T findLast () {
        int index = lastIndex();
        if (index >= 0) {
            return (T)mSelector.get(index);
        }
        return null;
        /*if (!mSelector.isEmpty()) {
            int size = mSelector.getSize();
            for (int i = size - 1; i >= 0; i--) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        return t;
                    }
                }
            }
        }
        return null;*/
    }

    public int count () {
        if (!mSelector.isEmpty()) {
            int count = 0;
            int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (whereList.isEmpty() || accept(t)) {
                        count++;
                    }
                }
            }
            return count;
        }
        return 0;
    }

    public <V> ArrayList<V> extractAll (Path<T, V> path) {
        return extractAll(path, false);
    }

    public <V> ArrayList<V> extractAll (Path<T, V> path, final boolean ignoreRepeat) {

        if (!mSelector.isEmpty()) {
            ArrayList<V> vList = new ArrayList<>();
            final int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        V v = path.extract(t);
                        if (ignoreRepeat && vList.contains(v)) {
                            continue;
                        }
                        vList.add(v);
                    }
                }
            }
            return vList;
        }
        return null;

    }

    public <V> V extractFirst (Path<T, V> path) {

        if (!mSelector.isEmpty()) {
            final int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        return path.extract(t);
                    }
                }
            }
        }
        return null;

    }

    public <V> V extractLast (Path<T, V> path) {

        if (!mSelector.isEmpty()) {
            final int size = mSelector.getSize();
            for (int i = size - 1; i >= 0; i--) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        return path.extract(t);
                    }
                }
            }
        }
        return null;

    }

    public <V> T min (Path<T, V> path) {
        if (!mSelector.isEmpty()) {
            final int size = mSelector.getSize();
            T minT = null;
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        if (minT == null) {
                            minT = t;
                        } else {
                            minT = minT(minT, t, path);
                        }
                    }
                }
            }
            return minT;
        }
        return null;
    }

    public <V> T max (Path<T, V> path) {
        if (!mSelector.isEmpty()) {
            final int size = mSelector.getSize();
            T maxT = null;
            for (int i = 0; i < size; i++) {
                Object object = mSelector.get(i);
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {

                        if (maxT == null) {
                            maxT = t;
                        } else {
                            maxT = maxT(maxT, t, path);
                        }
                    }
                }
            }
            return maxT;
        }
        return null;
    }

    private <V> T minT (T t1, T t2, Path<T, V> path) {
        V v1 = path.extract(t1);
        V v2 = path.extract(t2);
        if (v1 instanceof Comparable && v2 instanceof Comparable) {
            if (((Comparable) v1).compareTo(v2) <= 0) {
                return t1;
            } else {
                return t2;
            }
        }
        throw new UnsupportedOperationException(t1.getClass().getName() + " doesn't implement Comparable");
    }

    private <V> T maxT (T t1, T t2, Path<T, V> path) {
        V v1 = path.extract(t1);
        V v2 = path.extract(t2);
        if (v1 instanceof Comparable && v2 instanceof Comparable) {
            if (((Comparable) v1).compareTo(v2) >= 0) {
                return t1;
            } else {
                return t2;
            }
        }

        throw new UnsupportedOperationException(t1.getClass().getName() + " doesn't implement Comparable");
    }

    public <V> V avg (Path<T, V> path) {
        double[] sumAndCount = sumAndCount(path);
        return (V)(Double)(sumAndCount[0] / sumAndCount[1]);
    }

    public <V> double sum (Path<T, V> path) {
        return sumAndCount(path)[0];
    }

    private <V> double[] sumAndCount (Path<T, V> path) {
        double[] sumAndCount = new double[2];
        if (!mSelector.isEmpty()) {
            int size = mSelector.getSize();
            for (int i = 0; i < size; i++) {
                Object obj = mSelector.get(i);
                if (isT(obj)) {
                    T t = (T)obj;
                    V v = path.extract(t);
                    if (v instanceof Integer || v instanceof Long || v instanceof Byte || v instanceof Short) {
                        sumAndCount[0] += (Long)v;
                    } else if (v instanceof Float || v instanceof Double) {
                        sumAndCount[0] += (Double) v;
                    } else {
                        throw new UnsupportedClassVersionError("can not calculate avg for this type " + v.getClass().getName());
                    }
                    sumAndCount[1]++;
                }
            }
        }
        return sumAndCount;
    }

}