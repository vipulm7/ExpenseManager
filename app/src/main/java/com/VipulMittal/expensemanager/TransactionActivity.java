package com.VipulMittal.expensemanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

   private static final String TAG = "Vipul_tag";
   TextView TVDate, TVTime,TVAccountName, TVCategoryName, TVCategory;
   RadioGroup radioGroup;
   RadioButton RBIncome, RBExpense, RBTransfer;
   Toast toast;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		TVDate=findViewById(R.id.TVDate);
		TVTime=findViewById(R.id.TVTime);
		TVAccountName=findViewById(R.id.TVAccountName);
		TVCategoryName=findViewById(R.id.TVCategoryName);
		TVCategory=findViewById(R.id.TVCategory);
		ActionBar actionBar=getSupportActionBar();
		radioGroup=findViewById(R.id.RadioGroupType);

		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if(i==R.id.radioCatIncome)
				{
					TVCategory.setText("Category");
					TVCategoryName.setHint("Category Name");

				}
				else if(i==R.id.radioCatExpense)
				{
					TVCategory.setText("Category");
					TVCategoryName.setHint("Category Name");

				}
				else if(i==R.id.radioCatTransfer)
				{
					TVCategory.setText("Account");
					TVCategoryName.setHint("Account Name");

				}
			}
		});








		Intent intent=getIntent();
		Log.d(TAG, "onCreate: intent received");
		int amount = intent.getIntExtra("amount",0);
		String note = intent.getStringExtra("note");
		String description = intent.getStringExtra("description");
		Bundle bundle=intent.getBundleExtra("bundle");
		Calendar calendar = (Calendar) bundle.getSerializable("date");
		int account = intent.getIntExtra("account",-1);
		int cat = intent.getIntExtra("cat",-1);
		int subCat = intent.getIntExtra("subCat",-1);
		int request = intent.getIntExtra("request",-1);
		int type = intent.getIntExtra("type",-1);

		setDateAndTime(calendar);

	   	if(request == 1)
	   		actionBar.setTitle("Add Transaction");
	   	else if(request == 2)
			actionBar.setTitle("Edit Transaction");

	   	setRadioButton(type);






	}

	private void setRadioButton(int type) {
		int id=-1;
		if(type==1)
			id=R.id.radioCatIncome;
		else if(type==2)
			id=R.id.radioCatExpense;
		else if(type==3)
			id=R.id.radioCatTransfer;
		if(id!=-1)
			radioGroup.check(id);
		else
		{
			if(toast!=null)
				toast.cancel();
			toast=Toast.makeText(TransactionActivity.this, "Error", Toast.LENGTH_SHORT);
			toast.show();
		}

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