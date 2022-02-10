package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;

import java.util.Calendar;

public class HomeFragment extends Fragment {

	public HomeFragment() {
		// Required empty public constructor
	}
	public static final String TAG="Vipul_tag";

	TextView TVMainExpense,TVMainIncome,TVMainTotal, TVBefore, TVAfter, TVPeriodShown, TVFilter;
	RecyclerView RVTransactions;
	TransactionAdapter transactionAdapter;
	RadioGroup rg_Filter;

	MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		TVMainIncome=view.findViewById(R.id.TVIncomeAmt);
		TVMainExpense=view.findViewById(R.id.TVExpenseAmt);
		TVMainTotal=view.findViewById(R.id.TVTotalAmt);
		RVTransactions=view.findViewById(R.id.RecyclerViewID);
		TVAfter=view.findViewById(R.id.TVafter);
		TVBefore=view.findViewById(R.id.TVbefore);
		TVPeriodShown =view.findViewById(R.id.TVDateChange);
		TVFilter=view.findViewById(R.id.TVFilter);
		mainActivity=(MainActivity) getActivity();
		transactionAdapter= mainActivity.transactionAdapter;
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

		SharedPreferences.Editor editor = sharedPreferences.edit();



		mainActivity.transactionROOM();
		setDate();

		TVAfter.setText(">");
		TVBefore.setText("<");


		TransactionAdapter.CLickListener listener=new TransactionAdapter.CLickListener() {
			@Override
			public void onItemClick(TransactionAdapter.TransViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();

				Calendar calendar=Calendar.getInstance();
				Transaction transaction=transactionAdapter.transactions.get(position);
				calendar.setTimeInMillis(transaction.dateTime);

				TransactionFragment transactionFragment=new TransactionFragment(transaction.amount,transaction.note,transaction.description,calendar, transaction.accountID, transaction.catID, transaction.subCatID, 2,transaction.type, transaction.id);
				FragmentTransaction fragmentTransaction=mainActivity.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
				fragmentTransaction.replace(R.id.layoutForFragment, transactionFragment);
				fragmentTransaction.addToBackStack("home_page");
				fragmentTransaction.commit();

				mainActivity.FABAdd.hide();
				mainActivity.systemTimeInMillies=0;
			}
		};


		transactionAdapter.listener=listener;

		TVBefore.setOnClickListener(v->{
			if(mainActivity.viewMode == R.id.RBM)
				mainActivity.toShow.add(Calendar.MONTH, -1);
			else if(mainActivity.viewMode == R.id.RBD)
				mainActivity.toShow.add(Calendar.DATE, -1);
			else if(mainActivity.viewMode == R.id.RBW)
				mainActivity.toShow.add(Calendar.WEEK_OF_YEAR, -1);
			mainActivity.transactionROOM();
			setDate();
		});

		TVAfter.setOnClickListener(v->{
			if(mainActivity.viewMode == R.id.RBM)
				mainActivity.toShow.add(Calendar.MONTH, 1);
			else if(mainActivity.viewMode == R.id.RBD)
				mainActivity.toShow.add(Calendar.DATE, 1);
			else if(mainActivity.viewMode == R.id.RBW)
				mainActivity.toShow.add(Calendar.WEEK_OF_YEAR, 1);
			mainActivity.transactionROOM();
			setDate();
		});

		View filterView = inflater.inflate(R.layout.filter_dialog, null);
		rg_Filter =filterView.findViewById(R.id.RGFilter);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("View Mode")
				.setView(filterView);
		AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

		RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
				editor.putInt("view", checkedID);
				editor.apply();
				mainActivity.viewMode=checkedID;
				Log.d(TAG, "onCheckedChanged: mainActivity.viewMode"+mainActivity.viewMode);
				Log.d(TAG, "onCheckedChanged: shared = "+sharedPreferences.getInt("view", -1));
				dialog.dismiss();

				mainActivity.toShow.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
				mainActivity.tellDate();
				mainActivity.transactionROOM();
				setDate();
				rg_Filter.setOnCheckedChangeListener(null);
			}
		};


		TVFilter.setOnClickListener(v->{
			dialog.show();
			rg_Filter.check(mainActivity.viewMode);
			rg_Filter.setOnCheckedChangeListener(listener1);
//			rg_Filter.check(mainActivity.viewMode);
		});

		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
		RVTransactions.setNestedScrollingEnabled(false);
		return view;
	}

	private void setDate() {
		int date=mainActivity.toShow.get(Calendar.DATE);
		int week = mainActivity.toShow.get(Calendar.WEEK_OF_YEAR);
		int month=mainActivity.toShow.get(Calendar.MONTH);
		int year=mainActivity.toShow.get(Calendar.YEAR);

		if(mainActivity.viewMode == R.id.RBM)
			TVPeriodShown.setText(getM(month)+", "+year);
		else if(mainActivity.viewMode == R.id.RBW) {
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(mainActivity.toShow.getTimeInMillis());
			calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK)+1);
			String s=calendar.get(Calendar.DATE)+" "+getM(calendar.get(Calendar.MONTH))+" - ";
			calendar.add(Calendar.DATE,6);


			TVPeriodShown.setText(s+calendar.get(Calendar.DATE)+" "+getM(calendar.get(Calendar.MONTH)));
		}
		else if(mainActivity.viewMode == R.id.RBD)
			TVPeriodShown.setText(date+" "+getM(month)+", "+year);
	}

	private String getM(int m) {
		String a[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return a[m];
	}
}
