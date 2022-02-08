package com.VipulMittal.expensemanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

	public TransactionFragment(int amount, String note, String description, Calendar calendar, int aID, int cID, int sID, int request, int type, int id) {
		this.id=id;
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
		dateArray[5]=calendar.get(Calendar.SECOND);
		dateArray[6]=calendar.get(Calendar.MILLISECOND);
		amountCame=amount;
		Log.d(TAG, "TransactionFragment: aid="+aID+" cid="+cID+" sid="+sID);
	}


	private static final String TAG = "Vipul_tag";
	public TextView TVDate, TVTime, TVAccount, TVCategory;
	RadioGroup radioGroup;
	RadioButton RBIncome, RBExpense, RBTransfer;
	Toast toast;
	public int type, amount, request, cID, sID, aID, amountCame, id;
	String note, description;
	Calendar calendar;
	EditText ETNote, ETDes, ETAmt;
	boolean BNote, BAmt, BAcc, BCat;
	Button save, repeat;
	int dateArray[]=new int[7];
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
		save.setEnabled(request != 1);
		repeat.setEnabled(request != 1);
		mainActivity=(MainActivity) getActivity();


		setRadioButton(type);
		doColoring();
		radioGroupSetListener();
		setDateAndTime(calendar);

		if(aID!=-1)
		{
			Account account=mainActivity.accountViewModel.getAcc(aID);
			Log.d(TAG, "onCreateView: account = "+account);
			Log.d(TAG, "onCreateView: account name = "+account.name);
			TVAccount.setText(account.name);
			BAcc=true;
		}

		if(cID!=-1)
		{
			Category category=mainActivity.categoryViewModel.getCat(cID);
			Log.d(TAG, "onCreateView: category = "+category);
			Log.d(TAG, "onCreateView: category name = "+category.catName);
			if(sID!=-1)
			{
				SubCategory subCategory=mainActivity.subCategoryViewModel.getSubCat(sID);
				Log.d(TAG, "onCreateView: subCategory = "+subCategory);
				Log.d(TAG, "onCreateView: subCategory name = "+subCategory.name);
				TVCategory.setText(category.catName + " / " + subCategory.name);
			}
			else
				TVCategory.setText(category.catName);
			BCat=true;
		}

		enableDisableSaveButton();

		if(amount!=0) {
			if (amount > 0)
				ETAmt.setText("" + amount);
			else
				ETAmt.setText("" + (-amount));
			ETNote.setText(note);
			ETDes.setText(description);
		}


		if(request == 1)
			mainActivity.setActionBarTitle("Add Transaction");
		else if(request == 2)
			mainActivity.setActionBarTitle("Edit Transaction");

		TVAccount.setOnClickListener(v->{
			BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(aID, cID, 1, type, this);
			bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts");
		});

		TVCategory.setOnClickListener(v->{
			if(type!=3) {
				BottomSheetDialogFragment bottomSheetDialogFragment = new BsdCatFragment(cID, sID, type, this);
				bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Category");
			}
			else
			{
				BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(cID, aID, 2, type, this);
				bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts2");
			}
		});
		return view;
	}


	private void enableDisableSaveButton() {

		save.setOnClickListener(v->{
			Log.d(TAG, "enableDisableSaveButton: string before = "+ETAmt.getText().toString().trim());
			String s = amt(ETAmt.getText().toString().trim());
			Log.d(TAG, "enableDisableSaveButton: string after = "+s);
			int a=Integer.parseInt(s);
			if(type!=1)
				a=-a;
			calendar.set(dateArray[0],dateArray[1],dateArray[2],dateArray[3],dateArray[4]);
			calendar.set(Calendar.SECOND, dateArray[5]);
			calendar.set(Calendar.MILLISECOND, dateArray[6]);
			Log.d(TAG, "enableDisableSaveButton: month = "+(dateArray[1]-1)+" y= "+dateArray[0]);
			Log.d(TAG, "enableDisableSaveButton: starting");
			if(request==1)
				mainActivity.transactionViewModel.Insert(new Transaction(ETNote.getText().toString().trim(), a, "", aID,cID,sID,ETDes.getText().toString().trim(),type,calendar.getTimeInMillis()-dateArray[3]*3600000L-dateArray[4]*60000L-dateArray[5]*1000L-dateArray[6], calendar.getTimeInMillis()));
			else {
				Transaction transaction=new Transaction(ETNote.getText().toString().trim(), a, "", aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3]*3600000L - dateArray[4]*60000L -dateArray[5]*1000L - dateArray[6], calendar.getTimeInMillis());
				transaction.id=id;
				mainActivity.transactionViewModel.Update(transaction);
			}
			mainActivity.onBackPressed();
			Log.d(TAG, "enableDisableSaveButton: ended");
			mainActivity.accountViewModel.UpdateAmt(a-amountCame, aID);
			if(type!=3)
			{
				mainActivity.categoryViewModel.UpdateAmt(a - amountCame, cID);
				if (sID != -1)
					mainActivity.subCategoryViewModel.UpdateAmt(a - amountCame, sID);
			}
			else
				mainActivity.accountViewModel.UpdateAmt(amountCame-a, cID);//cid has aid2 data
		});

		repeat.setOnClickListener(v->{
			String s = amt(ETAmt.getText().toString().trim());
			int a=Integer.parseInt(s);
			if(type==2)
				a=-a;
			calendar.set(dateArray[0],dateArray[1],dateArray[2],dateArray[3],dateArray[4]);
			calendar.set(Calendar.SECOND, dateArray[5]);
			calendar.set(Calendar.MILLISECOND, dateArray[6]);

			if(request==1)
				mainActivity.transactionViewModel.Insert(new Transaction(ETNote.getText().toString().trim(), a, "", aID,cID,sID,ETDes.getText().toString().trim(),type,calendar.getTimeInMillis()-dateArray[3]*3600000L-dateArray[4]*60000L -dateArray[5]*1000L - dateArray[6], calendar.getTimeInMillis()));
			else {
				Transaction transaction=new Transaction(ETNote.getText().toString().trim(), a, "", aID, cID, sID, ETDes.getText().toString().trim(), type, calendar.getTimeInMillis() - dateArray[3] * 3600000L - dateArray[4] * 60000L -dateArray[5]*1000L - dateArray[6], calendar.getTimeInMillis());
				transaction.id=id;
				mainActivity.transactionViewModel.Update(transaction);
			}

			mainActivity.accountViewModel.UpdateAmt(a-amountCame, aID);
			if(type!=3)
			{
				mainActivity.categoryViewModel.UpdateAmt(a - amountCame, cID);
				if (sID != -1)
					mainActivity.subCategoryViewModel.UpdateAmt(a - amountCame, sID);
			}
			else
				mainActivity.accountViewModel.UpdateAmt(amountCame-a, cID);//cid has aid2 data

//			mainActivity.getSupportFragmentManager().
			TransactionFragment transactionFragment=new TransactionFragment(0, "", "", Calendar.getInstance(), aID, cID, sID, 1, type, -1);
			FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
			fragmentTransaction.replace(R.id.layoutForFragment, transactionFragment, "repeat");
			fragmentTransaction.commit();
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
				repeat.setEnabled(BNote && BAmt && BAcc && BCat);
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
				repeat.setEnabled(BNote && BAmt && BAcc && BCat);
				Log.d(TAG, "onTextChanged: note="+BNote);
				Log.d(TAG, "onTextChanged: amt="+BAmt);
				Log.d(TAG, "onTextChanged: acc="+BAcc);
				Log.d(TAG, "onTextChanged: cat="+BCat);
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
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
	}

	public void saveSelectedCategoryWithName(int cID, String name)
	{
		this.cID=cID;
		TVCategory.setText(name);
		BCat = true;
		this.sID = -1;
		save.setEnabled(BNote && BAmt && BAcc && BCat);
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
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
		repeat.setEnabled(BNote && BAmt && BAcc && BCat);
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

	private void doColoring() {
		int i=radioGroup.getCheckedRadioButtonId();
		if(i==R.id.radioCatIncome)
		{
			rSelected(RBIncome);
			rNotSelected(RBExpense);
			rNotSelected(RBTransfer);
		}
		else if(i==R.id.radioCatExpense)
		{
			rSelected(RBExpense);
			rNotSelected(RBIncome);
			rNotSelected(RBTransfer);
		}
		else if(i==R.id.radioCatTransfer)
		{
			rSelected(RBTransfer);
			rNotSelected(RBExpense);
			rNotSelected(RBIncome);
		}
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

	private void setDateAndTime(Calendar calendar)
	{
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
						public void onTimeSet(TimePicker timePicker, int h, int m) {
							TVTime.setText(timePrint(h,m));
							dateArray[3] =h;
							dateArray[4] =m;
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
