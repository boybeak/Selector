package com.github.boybeak.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.boybeak.selector.Operator;
import com.github.boybeak.selector.Path;
import com.github.boybeak.selector.Selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void show (View view) {
//        User user = new User();
        /*try {
            Bundle bundle = new Bundle();
            bundle.putInt("abc", 123);
            Method method = bundle.getClass().getMethod("getInt", String.class, int.class);
            Object obj = method.invoke(bundle, "abc", 2);
            Log.v(TAG, "show obj=" + obj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            users.add(user);
            Log.v(TAG, i + " " + user.type());
        }
        int count = Selector.selector(User.class, users).where(
                Path.with(User.class, Integer.class).methodWith("type"), Operator.OPERATOR_EQUAL, 1
        ).count();
        Toast.makeText(this, "count=" + count, Toast.LENGTH_SHORT).show();
    }
}
