package com.VipulMittal.expensemanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.BSD_Categrory.BsdCategoryFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

   private static final String TAG = "Vipul_tag";
   TextView TVDate, TVTime, TVAccount, TVCategory;
   RadioGroup radioGroup;
   RadioButton RBIncome, RBExpense, RBTransfer;
   Toast toast;
   int account,cat, subCat;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		TVDate=findViewById(R.id.TVDate);
		TVTime=findViewById(R.id.TVTime);
		TVAccount =findViewById(R.id.TVAccount);
		TVCategory=findViewById(R.id.TVCategory);
		ActionBar actionBar=getSupportActionBar();
		radioGroup=findViewById(R.id.RadioGroupType);
		RBExpense=findViewById(R.id.radioCatExpense);
		RBIncome=findViewById(R.id.radioCatIncome);
		RBTransfer=findViewById(R.id.radioCatTransfer);


		radioGroupSetListener();


		Intent intent=getIntent();
		Log.d(TAG, "onCreate: intent received");
		int amount = intent.getIntExtra("amount",0);
		String note = intent.getStringExtra("note");
		String description = intent.getStringExtra("description");
		Bundle bundle=intent.getBundleExtra("bundle");
		Calendar calendar = (Calendar) bundle.getSerializable("date");
		Log.d(TAG, "onCreate: Adapter just to receive");
		Log.d(TAG, "onCreate: Adapter received");
		account = intent.getIntExtra("account",-1);
		cat = intent.getIntExtra("cat",-1);
		subCat = intent.getIntExtra("subCat",-1);
		int request = intent.getIntExtra("request",-1);
		int type = intent.getIntExtra("type",2);



		setDateAndTime(calendar);

	   	if(request == 1)
	   		actionBar.setTitle("Add Transaction");
	   	else if(request == 2)
			actionBar.setTitle("Edit Transaction");

	   	setRadioButton(type); //sets which button is ticked in radio group based on int type



		TVAccount.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(account);
			bottomSheetDialogFragment.show(getSupportFragmentManager(), "BSD_Accounts");
		});

		TVCategory.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdCategoryFragment(cat, type);
			bottomSheetDialogFragment.show(getSupportFragmentManager(), "BSD_Category");
		});

//		TVCategory.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View view, MotionEvent motionEvent) {
//				TVCategory.setBackgroundColor(Color.CYAN);
//				return false;
//			}
//
//
//		});







	}

	public void saveSelectedAccount(int selected, String name)
	{
		account=selected;
		TVAccount.setText(name);
	}

	public void saveSelectedCategoryWithName(int selected, String name)
	{
		cat=selected;
		TVCategory.setText(name);
	}

	public void saveSelectedCategoryWithoutName(int selected)
	{
		cat=selected;
	}


	private void radioGroupSetListener() {
		radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
			if(i==R.id.radioCatIncome)
			{
				TVCategory.setText("Category");
				rSelected(RBIncome);
				rNotSelected(RBExpense);
				rNotSelected(RBTransfer);

			}
			else if(i==R.id.radioCatExpense)
			{
				TVCategory.setText("Category");
				rSelected(RBExpense);
				rNotSelected(RBIncome);
				rNotSelected(RBTransfer);


			}
			else if(i==R.id.radioCatTransfer)
			{
				TVCategory.setText("Account");
				rSelected(RBTransfer);
				rNotSelected(RBExpense);
				rNotSelected(RBIncome);

			}
		});
	}

	private void rSelected(RadioButton selected) {
		selected.setTextSize(27);
		selected.setTextColor(Color.parseColor("#a912db"));
	}

	private void rNotSelected(RadioButton notSelected) {
		notSelected.setTextSize(20);
		notSelected.setTextColor(Color.parseColor("#db4002"));
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
		final int[] date = {calendar.get(Calendar.DATE)};
		final int[] month = {calendar.get(Calendar.MONTH) + 1};
		final int[] year = {calendar.get(Calendar.YEAR)};
		final int[] hourOfDay = {calendar.get(Calendar.HOUR_OF_DAY)};
		final int[] minute = {calendar.get(Calendar.MINUTE)};
		TVDate.setText(datePrint(date[0], month[0], year[0]));
		TVTime.setText(timePrint(hourOfDay[0], minute[0]));

		TVDate.setOnClickListener(v->{
			DatePickerDialog datePickerDialog=new DatePickerDialog(TransactionActivity.this,
					  (datePicker, i, i1, i2) -> {
						  i1++;
						  year[0] =i;
						  month[0] =i1;
						  date[0] =i2;
						  TVDate.setText(datePrint(i2,i1,i));
					  }, year[0], month[0] -1, date[0]);
			datePickerDialog.show();
		});

		TVTime.setOnClickListener(v->{
			TimePickerDialog timePickerDialog=new TimePickerDialog(TransactionActivity.this,
					  new TimePickerDialog.OnTimeSetListener() {
						  @Override
						  public void onTimeSet(TimePicker timePicker, int i, int i1) {
								  TVTime.setText(timePrint(i,i1));
								  hourOfDay[0] =i;
								  minute[0] =i1;
						  }
					  }, hourOfDay[0], minute[0],false);
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