package com.VipulMittal.expensemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class TransactionFragment extends Fragment {

	public TransactionFragment(int amount, String note, String description, Calendar calendar, int account, int cat, int subCat, int request, int type) {
		this.account=account;
		this.amount=amount;
		this.note=note;
		this.description=description;
		this.calendar=calendar;
		this.cat=cat;
		this.subCat=subCat;
		this.type=type;
		this.request=request;
	}


	private static final String TAG = "Vipul_tag";
	public TextView TVDate, TVTime, TVAccount, TVCategory;
	RadioGroup radioGroup;
	RadioButton RBIncome, RBExpense, RBTransfer;
	Toast toast;
	public int account,cat, subCat, type, amount, request;
	String note, description;
	Calendar calendar;
	EditText ETNote, ETDes, ETAmt;
	boolean BNote, BDes, BAmt, BAcc, BCat;
	Button save;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_transaction, container, false);

		TVDate=view.findViewById(R.id.TVDate);
		TVTime=view.findViewById(R.id.TVTime);
		TVAccount =view.findViewById(R.id.TVAccount);
		TVCategory=view.findViewById(R.id.TVCategory);
//		ActionBar actionBar=getSupportActionBar();
		radioGroup=view.findViewById(R.id.RadioGroupType);
		RBExpense=view.findViewById(R.id.radioCatExpense);
		RBIncome=view.findViewById(R.id.radioCatIncome);
		RBTransfer=view.findViewById(R.id.radioCatTransfer);
		ETNote =view.findViewById(R.id.ETNote);
		ETDes=view.findViewById(R.id.ETDes);
		ETAmt=view.findViewById(R.id.ETAmount);
		save=view.findViewById(R.id.transaction_save_button);

		ETAmt.setText(""+amount);
		ETNote.setText(note);
		ETDes.setText(description);

		enableDisableSaveButton();



		radioGroupSetListener();
		setDateAndTime(calendar);
		setRadioButton(type);

		MainActivity mainActivity=(MainActivity)getActivity();

		if(request == 1)
			mainActivity.setActionBarTitle("Add Transaction");
		else if(request == 2)
			mainActivity.setActionBarTitle("Edit Transaction");

		TVAccount.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(account, this);
			bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts");
		});

		TVCategory.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdCatFragment(cat, subCat, type, this);
			bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Category");
		});


		return view;
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void enableDisableSaveButton() {
		ETNote.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s=charSequence.toString().trim();
				BNote= s.length() != 0;
				save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		ETDes.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				String s=charSequence.toString().trim();
				BDes=s.length()!=0;
				save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
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
				if(s.length()==0) {
					ETAmt.setText("0");
				}
				else
				{
					if(s.indexOf('.')==-1)
					{
						long amt=Long.parseLong(s);
						ETAmt.setText(""+amt);
					}
					else
					{
						double amt=Double.parseDouble(s);
						ETAmt.setText(""+amt);
					}
				}

				BAmt=!s.equals("0");
				save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

	}

	public void saveSelectedAccount(int selectedAccount, String name)
	{
		account=selectedAccount;
		TVAccount.setText(name);
		BAcc=true;

		save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
	}

	public void saveSelectedCategoryWithName(int selectedCat, String name)
	{
		cat=selectedCat;
		TVCategory.setText(name);
		BCat=true;
		save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
		subCat=-1;
	}

	public void saveSelectedCategoryWithoutName(int selectedCat)
	{
		cat=selectedCat;
	}

	public void saveSelectedSubCategory(int selectedCat, int selectedSubCat, String name)
	{
		cat=selectedCat;
		subCat=selectedSubCat;
		TVCategory.setText(name);
		BCat=true;
		save.setEnabled(BNote && BDes && BAmt && BAcc && BCat);
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
				cat=-1;
				subCat=-1;
			}
			else if(i==R.id.radioCatExpense)
			{
				TVCategory.setText("Category");
				rSelected(RBExpense);
				rNotSelected(RBIncome);
				rNotSelected(RBTransfer);
				type=2;
				cat=-1;
				subCat=-1;
			}
			else if(i==R.id.radioCatTransfer)
			{
				TVCategory.setText("Account");
				rSelected(RBTransfer);
				rNotSelected(RBExpense);
				rNotSelected(RBIncome);
				type=3;
				cat=-1;
				subCat=-1;
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

	private void setDateAndTime(Calendar calendar) {
		final int[] date = {calendar.get(Calendar.DATE)};
		final int[] month = {calendar.get(Calendar.MONTH) + 1};
		final int[] year = {calendar.get(Calendar.YEAR)};
		final int[] hourOfDay = {calendar.get(Calendar.HOUR_OF_DAY)};
		final int[] minute = {calendar.get(Calendar.MINUTE)};
		TVDate.setText(datePrint(date[0], month[0], year[0]));
		TVTime.setText(timePrint(hourOfDay[0], minute[0]));

		TVDate.setOnClickListener(v->{
			DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),
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
			TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
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
