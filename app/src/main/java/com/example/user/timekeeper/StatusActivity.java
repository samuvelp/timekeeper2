package com.example.user.timekeeper;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StatusActivity extends AppCompatActivity {
    private static String TAG ="Timer";

     public StatusActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);


        ListView LV_status = (ListView) findViewById(R.id.UI_LV_status);
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAttendanceCursor();
        ArrayList<HashMap<String,String>> attendanceList = dbHelper.getAttendance();
        Collections.reverse(attendanceList);
        String[] from ={"event","time","location"};
        int[] to ={R.id.UI_TV_Event,R.id.UI_TV_Time,R.id.UI_TV_Location};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplication(),attendanceList,R.layout.singlerow_attendance,from,to);

        LV_status.setAdapter(simpleAdapter);

    }
}
