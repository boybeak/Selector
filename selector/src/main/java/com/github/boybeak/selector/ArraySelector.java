package com.github.boybeak.selector;


/**
 * Created by gaoyunfei on 2017/6/21.
 */

public class ArraySelector<T> extends Selector<T> {

    private Object[] mTs;
    private WhereDelegate<T> mWhereDelegate;

    public ArraySelector (Class<T> tClass, Object[] ts) {
        super(tClass);
        mTs = ts;
        mWhereDelegate = new WhereDelegate<>(this);
    }

    @Override
    public int getSize() {
        return mTs.length;
    }

    @Override
    public Object get(int index) {
        return mTs[index];
    }

    @Override
    WhereDelegate<T> getWhereDelegate() {
        return mWhereDelegate;
    }
}
