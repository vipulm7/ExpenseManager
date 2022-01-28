package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;

import java.util.Calendar;

public class HomeFragment extends Fragment {

	public HomeFragment() {
		// Required empty public constructor
	}

	TextView TVMainExpense,TVMainIncome,TVMainTotal;
	RecyclerView RVTransactions;
	TransactionAdapter transactionAdapter;

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
		mainActivity=(MainActivity)getActivity();
		transactionAdapter= mainActivity.transactionAdapter;

		Calendar calendar=Calendar.getInstance();
		mainActivity.transactionROOM(calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
		RVTransactions.setNestedScrollingEnabled(false);
		return view;
	}

	public void indexToID()
	{

	}
}
