package com.example.a2fevents;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.chaquo.python.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Python py = Python.getInstance();
        PyObject eventGetter = py.getModule("getUpcomingEvents");
        PyObject returned = eventGetter.callAttr("main");
        String test = returned.toString();
        ((TextView)findViewById(R.id.helloWorld)).setText(test);
    }
}
