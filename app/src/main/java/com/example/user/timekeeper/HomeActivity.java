package com.example.user.timekeeper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static String TAG ="Timer";

    Handler handler = new Handler();
    String startTime=null;
    SimpleDateFormat simpleDateFormat;
    TextView TV_timer,TV_homeCheckIn;
    long difference = 0L;
    int sec=0;
    int min=0;
    int hour =0;
    Button btn_checkIn;
    Button btn_checkOut;
    TextView btn_status;
    DBHelper dbHelper;
    private String mPath = null;
    private static final int CAMERA_REQ =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_checkIn = (Button) findViewById(R.id.UI_BTN_checkIn);
        btn_checkOut = (Button) findViewById(R.id.UI_BTN_checkOut);
        btn_status = (TextView) findViewById(R.id.UI_BTN_status);
        TV_timer = (TextView) findViewById(R.id.UI_TV_timer);
        TV_homeCheckIn = (TextView) findViewById(R.id.UI_TV_HomeCheckIn);
        dbHelper = new DBHelper(this);

        if (getLastEvent().equals("CheckIn")) {
            startTime = getStartTime();
            TV_homeCheckIn.setText(startTime);
            btn_checkIn.setVisibility(View.GONE);
            btn_checkOut.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable,0);
        }
        btn_checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gpsTracker = new GPSTracker(getApplication());
                Location location = gpsTracker.getLocation();
                if(location == null){
                    Toast.makeText(getApplicationContext(),"Turn-On location to Check-In",Toast.LENGTH_SHORT).show();
                }
                else{
                    captureCamera();
                }

            }
        });

        btn_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLastEvent().equals("CheckOut")){
                    Toast.makeText(HomeActivity.this, "Please check in first", Toast.LENGTH_SHORT).show();
                }
                else{
                    String hoursWorked = hour +" Hours "+ min +" Minutes "+ sec +" Seconds";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                    String checkOutTime = dateFormat.format(calendar.getTime());

                    GPSTracker gpsTracker = new GPSTracker(getApplication());
                    Location location = gpsTracker.getLocation();

                    if(location!=null){

                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();

                        try {
                            Cursor cursor = dbHelper.getAttendanceCursor();
                            cursor.moveToLast();
                            Integer id = cursor.getColumnIndex("primaryId");
                            Log.d(TAG,Long.toString(cursor.getLong(id)));
                            long isInserted = dbHelper.insertCheckOut(checkOutTime,getAddress(latitude,longitude),hoursWorked);
                            if(isInserted != -1) {
                                btn_checkIn.setVisibility(View.VISIBLE);
                                btn_checkOut.setVisibility(View.GONE);
                                handler.removeCallbacks(runnable);
                                Toast.makeText(HomeActivity.this, "Checked-Out", Toast.LENGTH_SHORT).show();
                            }
                            else if(isInserted == -1){
                                Toast.makeText(getApplicationContext(),"Please try after sometimes",Toast.LENGTH_SHORT).show();
                            }
                            } catch (IOException e) {

                            Toast.makeText(getApplicationContext(),"Turn On the internet connection",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            }

                    }
                    if(location==null){
                        Toast.makeText(getApplicationContext(),"Turn-On location to Check-Out",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

       btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,StatusActivity.class);
                startActivity(intent);

            }
        });


    }

    public void captureCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(cameraIntent,CAMERA_REQ);
    }
    public void checkIn(){
        Cursor cursor = dbHelper.getAttendanceCursor();
        if(getLastEvent().equals("CheckOut")||cursor.getCount()==0){

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String checkInTime = dateFormat.format(calendar.getTime());
            startTime = checkInTime;
            GPSTracker gpsTracker = new GPSTracker(getApplication());
            Location location = gpsTracker.getLocation();

            if(location!=null){

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                try {

                    long isInserted = dbHelper.insertCheckIn(startTime,getAddress(latitude,longitude),mPath);
                    if(isInserted != -1) {
                        btn_checkIn.setVisibility(View.GONE);
                        btn_checkOut.setVisibility(View.VISIBLE);
                        TV_homeCheckIn.setText(startTime);
                        handler.postDelayed(runnable,0);
                        Toast.makeText(HomeActivity.this,"Checked-In", Toast.LENGTH_SHORT).show();
                    }
                    else if(isInserted == -1){
                        Toast.makeText(getApplicationContext(),"Try after sometimes",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Turn On the internet connection",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
            if(location==null){
                Toast.makeText(getApplicationContext(),"Turn-On location to Check-In",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(getApplication(), "You have already Checked in!", Toast.LENGTH_SHORT).show();
        }
    }
    public File getFile(){
        File folder = new File("sdcard/TimeKeeper");
        if(!folder.exists()){
            folder.mkdir();
        }
        File file = new File(folder,"image.jpg");
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            mPath = "sdcard/TimeKeeper/image.jpg";
            checkIn();
        }
        else {
            Toast.makeText(getApplicationContext(),"Please Take Picture to Check In",Toast.LENGTH_SHORT).show();
        }
    }

    public String getAddress(Double latitude, Double longitude) throws IOException {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geoCoder.getFromLocation(latitude,longitude,1);
        String addressLine1 = addresses.get(0).getAddressLine(0);
        String addressLine2 = addresses.get(0).getAddressLine(1);
        String addressLine3 = addresses.get(0).getAddressLine(2);


        String address;
        if(addressLine2==null && addressLine3==null)address =addressLine1;
        else if(addressLine3==null) address = addressLine1 +" "+addressLine2;
        else address = addressLine1 +" "+addressLine2 +"\n"+addressLine3;
        return address;

    }
    public String getStartTime() {

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAttendanceCursor();
        cursor.moveToLast();
        return cursor.getString(cursor.getColumnIndex("time"));

    }

    public String getLastEvent() {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAttendanceCursor();

        if(cursor.getCount()>0){
            cursor.moveToLast();
            return cursor.getString(cursor.getColumnIndex("event"));}
        else return "null";


    }
    Runnable runnable = new Runnable() {
        public long getDifferenceInMilli(String startTm, String currentTime) throws ParseException {
            long difference = 0;
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Date dateStart = simpleDateFormat.parse(startTm);
            Date dateCurrent = simpleDateFormat.parse(currentTime);
            difference = dateCurrent.getTime() - dateStart.getTime();
            return difference;
        }

        public long getSecDifference(long differenceinMilli) {
            return differenceinMilli / 1000 % 60;
        }

        public long getMinDifference(long differenceInMilli) {
            return differenceInMilli / (1000 * 60) % 60;
        }

        public long getHourDifference(long differenceInMilli) {
            return differenceInMilli / (1000 * 60 * 60) % 24;
        }


        @Override
        public void run() {
            simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String currentTime = simpleDateFormat.format(calendar.getTime());
            try {
                difference = getDifferenceInMilli(startTime, currentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sec = (int) getSecDifference(difference);
            min = (int) getMinDifference(difference);
            hour = (int) getHourDifference(difference);

            TV_timer.setText(" "+String.format("%2d",hour)
                    +":"+String.format("%02d",min)
                    +":"+String.format("%02d",sec));
            handler.postDelayed(this,0);
        }

    };
}
