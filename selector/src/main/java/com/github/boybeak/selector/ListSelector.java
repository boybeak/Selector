package com.github.boybeak.selector;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class ListSelector<T> extends Selector<T>{

    private List mList;

    private ListWhereDelegate<T> mWhereDelegate;

    ListSelector(Class<T> tClass, List list) {
        super(tClass);
        mList = list;

        mWhereDelegate = new ListWhereDelegate<T>(this);
    }

    @Override
    public <V> ListWhereDelegate<T> where(Path<T, V> path, Operator operator, V... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    @Override
    public <V> ListWhereDelegate<T> where(Where<T, V> where) {
        getWhereDelegate().addWithCheck(where);
        return getWhereDelegate();
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public Object get(int index) {
        return mList.get(index);
    }

    public void remove () {
        remove(null);
    }

    public void remove (OnRemoveCallback<T> callback) {
        mWhereDelegate.remove(callback);
    }

    @Override
    ListWhereDelegate<T> getWhereDelegate() {
        return mWhereDelegate;
    }

    public class ListWhereDelegate<T> extends WhereDelegate<T> {

        ListWhereDelegate(Selector<T> selector) {
            super(selector);
        }

        public void remove () {
            remove(null);
        }

        public void remove (OnRemoveCallback<T> callback) {
            int count = 0;
            if (!isEmpty()) {
                Iterator iterator = mList.iterator();
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    if (isT(obj)) {
                        T t = (T)obj;
                        if (getWhereList().isEmpty() || accept(t)) {
                            iterator.remove();
                            if (callback != null) {
                                callback.onRemoved(count, t);
                            }
                            count++;
                        }
                    }
                }
            }
            if (callback != null) {
                callback.onRemoveComplete(count);
            }
        }
    }

    public interface OnRemoveCallback<T> {
        void onRemoved (int n, T t);
        void onRemoveComplete (int count);
    }
}
