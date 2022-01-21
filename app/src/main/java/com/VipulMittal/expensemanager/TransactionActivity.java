package com.VipulMittal.expensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

   private static final String TAG = "Vipul_tag";
   TextView TVDate, TVTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		TVDate=findViewById(R.id.TVDate);
		TVTime=findViewById(R.id.TVTime);




		Intent intent=getIntent();
	   Log.d(TAG, "onCreate: intent received");
		int amount = intent.getIntExtra("amount",0);
	    String note = intent.getStringExtra("note");
	    String description = intent.getStringExtra("description");
	    Bundle bundle=intent.getBundleExtra("bundle");
	   Calendar calendar = (Calendar) bundle.getSerializable("date");
//	    Calendar calendar = (Calendar) intent.getSerializableExtra("bundle");
	    int account = intent.getIntExtra("account",-1);
	   	int cat = intent.getIntExtra("cat",-1);
	   	int subCat = intent.getIntExtra("subCat",-1);
	   	int IET = intent.getIntExtra("IET",2);

	   setDateAndTime(calendar);

	}

	private void setDateAndTime(Calendar calendar) {
		int date=calendar.get(Calendar.DATE);
		int month=calendar.get(Calendar.MONTH)+1;
		int year=calendar.get(Calendar.YEAR);
		int hour=calendar.get(Calendar.HOUR);
		int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		TVDate.setText(datePrint(date,month,year));
		TVTime.setText(timePrint(hourOfDay,minute));

		TVDate.setOnClickListener(v->{
			DatePickerDialog datePickerDialog=new DatePickerDialog(TransactionActivity.this,
					  (datePicker, i, i1, i2) -> {
						  i1++;
						  TVDate.setText(datePrint(i2,i1,i));
					  },year,month-1,date);
			datePickerDialog.show();
		});

		TVTime.setOnClickListener(v->{
			TimePickerDialog timePickerDialog=new TimePickerDialog(TransactionActivity.this,
					  new TimePickerDialog.OnTimeSetListener() {
						  @Override
						  public void onTimeSet(TimePicker timePicker, int i, int i1) {
								  TVTime.setText(timePrint(i,i1));
						  }
					  },hourOfDay,minute,false);
			timePickerDialog.show();
		});
	}

	private String timePrint(int hour, int minute) {
		String amOrPm;
		if(hour<12) {
			amOrPm="AM";
		} else {
			hour -= 12;
			amOrPm="PM";
		}
		if(hour==0)
			hour=12;

		String minut=""+minute;
		if(minute<10)
			minut="0"+minut;
		return ""+hour+":"+minut+" "+amOrPm;
	}

	private String datePrint(int date, int month, int year) {
		String monthName=getMonth(month);
		return ""+date+" "+monthName+", "+year;
	}

	private String getMonth(int month) {
		String name[]={"Jan","Feb","Mar","Apr","May","Jun",
		"Jul","Aug","Sep","Oct","Nov","Dec"};
		return name[month-1];
	}
}