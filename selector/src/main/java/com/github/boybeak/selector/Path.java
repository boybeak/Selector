package com.github.boybeak.selector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/16.
 */

public class Path<T, V> {

    /*public static <T, V> Path<T, V> newPathFieldWith (String name, Class<T> tClass) {
        return new Path<T, V>().fieldWith(name);
    }

    public static <T, V> Path<T, V> newPathMethodWith (String name, Class<T> tClass, Object ... params) {
        return new Path<T, V>().methodWith(name, params);
    }*/

    public static <T, V> Path<T, V> with (Class<T> tClass, Class<V> vClass) {
        return new Path<T, V>();
    }

    /*public static <T> Path with (T t) {
        Path path = new Path(t);

        return path;
    }*/

    private List<Item> mItems = new ArrayList<>();

    public Path<T, V> fieldWith (String name) {
        mItems.add(new Item<V>(name, Item.TYPE_FIELD));
        return this;
    }

    public Path<T, V> methodWith (String name, Object ... params) {
        mItems.add(new Item<V>(name, Item.TYPE_METHOD, params));
        return this;
    }

    public Path<T, V> methodWithPairs (String name, Pair<Class, ?> ... typeValues) {
        mItems.add(new Item<V>(name, Item.TYPE_METHOD, typeValues));
        return this;
    }

    public V extract (T t) {
        Object obj = t;
        for (Item item : mItems) {
            obj = item.getValue(obj);
        }
        return (V) obj;
    }

    public static class Item<M> {

        public static final int TYPE_FIELD = 1, TYPE_METHOD = 2;

        @Retention(RetentionPolicy.SOURCE)
        @interface Type{}

        public String name;
        @Type int type;

        Class[] classes;
        Object[] params;

        Item (String name, @Type int type) {
            this.name = name;
            this.type = type;
        }

        Item (String name, @Type int type, Object ... params) {
            this.name = name;
            this.type = type;
            this.params = params;
            if (params != null && params.length > 0) {
                classes = new Class[params.length];
                for (int i = 0; i < classes.length; i++) {
                    if (params[i] != null) {
                        classes[i] = params[i].getClass();
                    }
                }
            }
        }

        Item (String name, @Type int type, Pair<Class, ?> ... typeValueList) {
            this.name = name;
            this.type = type;

            if (typeValueList != null && typeValueList.length > 0) {
                classes = new Class[typeValueList.length];
                params = new Object[typeValueList.length];
                for (int i = 0; i < typeValueList.length; i++) {
                    Pair<Class, ?> pair = typeValueList[i];
                    classes[i] = pair.fst;
                    params[i] = pair.snd;
                }
            }
        }

        public Object getValue (Object object) {
            Class clz = object.getClass();
            switch (this.type) {
                case TYPE_FIELD:
                    Field field = null;
                    try {
                        field = clz.getField(name);
                        try {
                            return field.get(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                    break;
                case TYPE_METHOD:
                    try {
                        Method method = clz.getMethod(name, classes);
                        return method.invoke(object, params);
                    } catch (NoSuchMethodException e1) {
                        System.err.println("getValue " + clz.getName());
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
            return null;
        }
    }

    public static class Pair<FST, SND> {
        public FST fst;
        public SND snd;

        public Pair(FST fst, SND snd) {
            this.fst = fst;
            this.snd = snd;
        }
    }
}
