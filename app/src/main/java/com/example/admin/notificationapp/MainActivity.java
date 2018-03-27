package com.example.admin.notificationapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static TextView showDate;
    private static TextView showTime;
    private EditText heading;
    private int notificationId = 1;

    private static int selectedYear,selectedMonth,selectedDay,selectedHour,selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showDate = findViewById(R.id.showDate);
        showTime = findViewById(R.id.showTime);
        heading = findViewById(R.id.heading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.savebtn:
                if(heading.getText().length()==0)
                    Toast.makeText(this, "Enter a Task Heading", Toast.LENGTH_SHORT).show();
                else if(showDate.getText().length()==0)
                    Toast.makeText(this, "Select a Date", Toast.LENGTH_SHORT).show();
                else if(showTime.getText().length()==0)
                    Toast.makeText(this, "Select a Time", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                    intent.putExtra("NotificationId",notificationId);
                    intent.putExtra("Heading",heading.getText().toString());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,
                            intent,PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                    Calendar cal = Calendar.getInstance();
                    cal.set(selectedYear,selectedMonth,selectedDay,selectedHour,selectedMinute,0);
                    long notificationTime = cal.getTimeInMillis();

                    alarmManager.set(AlarmManager.RTC_WAKEUP,notificationTime,pendingIntent);
                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        Calendar c = Calendar.getInstance();
        int year,month,day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //To set the current date in the calendar
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            selectedYear = i;
            selectedMonth = i1;
            selectedDay = i2;
            c.set(i,i1,i2);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String dateString = sdf.format(c.getTime());
            showDate.setText(dateString);
        }

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        Calendar c = Calendar.getInstance();
        int hour,minute;

        //To set the current time in the time picker
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            selectedHour = i;
            selectedMinute = i1;
            c.set(Calendar.HOUR_OF_DAY,i);
            c.set(Calendar.MINUTE,i1);
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            String timeString = sdf.format(c.getTime());
            showTime.setText(timeString);
        }
    }

    public void pickDate(View view){
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(),"datepicker");
    }

    public void pickTime(View view){
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getFragmentManager(),"timepicker");
    }


}
