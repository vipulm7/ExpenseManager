package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;

import java.util.Calendar;

public class HomeFragment extends Fragment {

	public HomeFragment() {
		// Required empty public constructor
	}

	TextView TVMainExpense,TVMainIncome,TVMainTotal, TVBefore, TVAfter, TVMonthShown, TVFilter;
	RecyclerView RVTransactions;
	TransactionAdapter transactionAdapter;
	RadioGroup RGFilter;

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
		TVMonthShown =view.findViewById(R.id.TVDateChange);
		TVFilter=view.findViewById(R.id.TVFilter);
		mainActivity=(MainActivity)getActivity();
		transactionAdapter= mainActivity.transactionAdapter;

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
			mainActivity.toShow.add(Calendar.MONTH, -1);
			mainActivity.transactionROOM();
			setDate();
		});

		TVAfter.setOnClickListener(v->{
			mainActivity.toShow.add(Calendar.MONTH, 1);
			mainActivity.transactionROOM();
			setDate();
		});

		View filterView = inflater.inflate(R.layout.filter_dialog, null);
		RGFilter=filterView.findViewById(R.id.RGFilter);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("View Mode")
				.setView(filterView);
		AlertDialog dialog = builder.create();

		TVFilter.setOnClickListener(v->{
			dialog.show();
		});

		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
		RVTransactions.setNestedScrollingEnabled(false);
		return view;
	}

	private void setDate() {
		int month=mainActivity.toShow.get(Calendar.MONTH);
		int year=mainActivity.toShow.get(Calendar.YEAR);

		TVMonthShown.setText(getM(month)+", "+year);
	}

	private String getM(int m) {
		String a[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return a[m];
	}
}
