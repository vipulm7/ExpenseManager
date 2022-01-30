package com.VipulMittal.expensemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.BSD_Cat.BsdCatFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Map;

public class TransactionFragment extends Fragment {

	public TransactionFragment(int amount, String note, String description, Calendar calendar, int aID, int cID, int sID, int request, int type) {
		this.aID=aID;
		this.amount=amount;
		this.note=note;
		this.description=description;
		this.calendar=calendar;
		this.cID=cID;
		this.sID=sID;
		this.type=type;
		this.request=request;
		dateArray[0]=calendar.get(Calendar.YEAR);
		dateArray[1]=calendar.get(Calendar.MONTH);
		dateArray[2]=calendar.get(Calendar.DATE);
		dateArray[3]=calendar.get(Calendar.HOUR_OF_DAY);
		dateArray[4]=calendar.get(Calendar.MINUTE);
		Log.d(TAG, "TransactionFragment: aid="+aID+" cid="+cID+" sid="+sID);
	}


	private static final String TAG = "Vipul_tag";
	public TextView TVDate, TVTime, TVAccount, TVCategory;
	RadioGroup radioGroup;
	RadioButton RBIncome, RBExpense, RBTransfer;
	Toast toast;
	public int type, amount, request;
	String note, description;
	Calendar calendar;
	EditText ETNote, ETDes, ETAmt;
	boolean BNote, BAmt, BAcc, BCat;
	Button save, repeat;
	int dateArray[]=new int[5];
	public int cID, sID, aID;
	MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_transaction, container, false);

		TVDate=view.findViewById(R.id.TVDate);
		TVTime=view.findViewById(R.id.TVTime);
		TVAccount =view.findViewById(R.id.TVAccount);
		TVCategory=view.findViewById(R.id.TVCategory);
		radioGroup=view.findViewById(R.id.RadioGroupType);
		RBExpense=view.findViewById(R.id.radioCatExpense);
		RBIncome=view.findViewById(R.id.radioCatIncome);
		RBTransfer=view.findViewById(R.id.radioCatTransfer);
		ETNote =view.findViewById(R.id.ETNote);
		ETDes=view.findViewById(R.id.ETDes);
		ETAmt=view.findViewById(R.id.ETAmount);
		save=view.findViewById(R.id.transaction_save_button);
		repeat=view.findViewById(R.id.transaction_repeat_button);
		save.setEnabled(false);
		mainActivity=(MainActivity) getActivity();


		if(amount>0)
			ETAmt.setText(""+amount);
		else if(amount<0)
			ETAmt.setText(""+(-amount));
		ETNote.setText(note);
		ETDes.setText(description);

		enableDisableSaveButton();

		if(aID!=-1)
		{
			Account account=mainActivity.accountViewModel.getAcc(aID);
			TVAccount.setText(account.name);
		}
		if(cID!=-1)
		{
			Category category=mainActivity.categoryViewModel.getCat(cID);
			if(sID!=-1)
			{
				SubCategory subCategory=mainActivity.subCategoryViewModel.getSubCat(sID);
				TVCategory.setText(category.catName + " / " + subCategory.name);
			}
			else
				TVCategory.setText(category.catName);
		}

		radioGroupSetListener();
		setDateAndTime(calendar);
		setRadioButton(type);

		MainActivity mainActivity=(MainActivity)getActivity();

		if(request == 1)
			mainActivity.setActionBarTitle("Add Transaction");
		else if(request == 2)
			mainActivity.setActionBarTitle("Edit Transaction");

		TVAccount.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(aID, cID, 1, this);
			bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts");
		});

		TVCategory.setOnClickListener(v->{
			if(type!=3) {
				BottomSheetDialogFragment bottomSheetDialogFragment = new BsdCatFragment(cID, sID, type, this);
				bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Category");
			}
			else
			{
				BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(cID, aID, 2, this);
				bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts");
			}
		});
		return view;
	}


	private void enableDisableSaveButton() {

		save.setOnClickListener(v->{
			MainActivity mainActivity = (MainActivity) getActivity();
			Log.d(TAG, "enableDisableSaveButton: string before = "+ETAmt.getText().toString().trim());
			String s = amt(ETAmt.getText().toString().trim());
			Log.d(TAG, "enableDisableSaveButton: string after = "+s);
			int a=Integer.parseInt(s);
			calendar.set(dateArray[0],dateArray[1],dateArray[2],dateArray[3],dateArray[4]);
			Log.d(TAG, "enableDisableSaveButton: month = "+(dateArray[1]-1)+" y= "+dateArray[0]);
			Log.d(TAG, "enableDisableSaveButton: starting");
			mainActivity.transactionViewModel.Insert(new Transaction(ETNote.getText().toString().trim(), a, "", aID,cID,sID,ETDes.getText().toString().trim(),type,calendar.getTimeInMillis()-dateArray[3]*3600000-dateArray[4]*60000, calendar.getTimeInMillis()));
			Log.d(TAG, "enableDisableSaveButton: ended");
		});

		ETNote.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s=charSequence.toString().trim();
				BNote= s.length() != 0;
				save.setEnabled(BNote && BAmt && BAcc && BCat);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		ETAmt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s=charSequence.toString().trim();
				BAmt=s.length() != 0;
				save.setEnabled(BNote && BAmt && BAcc && BCat);
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

	}

	public void saveSelectedAccount(int aID, String name)
	{
		this.aID = aID;
		TVAccount.setText(name);
		BAcc = true;

		save.setEnabled(BNote && BAmt && BAcc && BCat);
	}

	public void saveSelectedCategoryWithName(int cID, String name)
	{
		this.cID=cID;
		TVCategory.setText(name);
		BCat = true;
		this.sID = -1;
		save.setEnabled(BNote && BAmt && BAcc && BCat);
	}

	public void saveSelectedCategoryWithoutName(int cID)
	{
		this.cID =cID;
	}

	public void saveSelectedSubCategory(int cID, int sID, String name)
	{
		TVCategory.setText(name);
		BCat=true;
		save.setEnabled(BNote && BAmt && BAcc && BCat);
		this.sID=sID;
		this.cID=cID;
	}

	private void radioGroupSetListener() {
		radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
			if(i==R.id.radioCatIncome)
			{
				TVCategory.setText("Category");
				rSelected(RBIncome);
				rNotSelected(RBExpense);
				rNotSelected(RBTransfer);
				type=1;
				cID=-1;
				sID=-1;
				BCat=false;
			}
			else if(i==R.id.radioCatExpense)
			{
				TVCategory.setText("Category");
				rSelected(RBExpense);
				rNotSelected(RBIncome);
				rNotSelected(RBTransfer);
				type=2;
				cID=-1;
				sID=-1;
				BCat=false;
			}
			else if(i==R.id.radioCatTransfer)
			{
				TVCategory.setText("Account");
				rSelected(RBTransfer);
				rNotSelected(RBExpense);
				rNotSelected(RBIncome);
				type=3;
				cID=-1;
				sID=-1;
				BCat=false;
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
			toast=Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public String amt(String s)
	{
		int i=-1;
		for(;++i<s.length() && s.indexOf(i)=='0';);
		i--;
		String s1="";
		for(;++i<s.length();)
			s1+=s.charAt(i);
		return s1;
	}

	private void setDateAndTime(Calendar calendar) {

//		final int[] date = {calendar.get(Calendar.DATE)};
//		final int[] month = {calendar.get(Calendar.MONTH) + 1};
//		final int[] year = {calendar.get(Calendar.YEAR)};
//		final int[] hourOfDay = {calendar.get(Calendar.HOUR_OF_DAY)};
//		final int[] minute = {calendar.get(Calendar.MINUTE)};

//		dateArray[0]=calendar.get(Calendar.YEAR);
//		dateArray[1]=calendar.get(Calendar.MONTH)+1;
//		dateArray[2]=calendar.get(Calendar.DATE);
//		dateArray[3]=calendar.get(Calendar.HOUR_OF_DAY);
//		dateArray[4]=calendar.get(Calendar.MINUTE);
		TVDate.setText(datePrint(dateArray[2], dateArray[1], dateArray[0]));
		TVTime.setText(timePrint(dateArray[3], dateArray[4]));

		TVDate.setOnClickListener(v->{
			DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),
					(datePicker, y, m, d) -> {
						dateArray[0] =y;
						dateArray[1] =m;
						dateArray[2] =d;
						TVDate.setText(datePrint(d,m,y));
					}, dateArray[0], dateArray[1], dateArray[2]);
			datePickerDialog.show();
		});

		TVTime.setOnClickListener(v->{
			TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker timePicker, int i, int i1) {
							TVTime.setText(timePrint(i,i1));
							dateArray[3] =i;
							dateArray[4] =i1;
						}
					}, dateArray[3], dateArray[4],false);
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
		return name[month];
	}
}
