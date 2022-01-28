package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

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

		TransactionAdapter.CLickListener listener=new TransactionAdapter.CLickListener() {
			@Override
			public void onItemClick(TransactionAdapter.TransViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();

				Transaction transaction=transactionAdapter.transactions.get(position);
				calendar.setTimeInMillis(transaction.dateTime);

				int send[]=new int[3];
				getSend(send);

				TransactionFragment transactionFragment=new TransactionFragment(transaction.amount,transaction.note,transaction.description,calendar, send[0], send[1], send[2], 2,transaction.type);
				FragmentTransaction fragmentTransaction=mainActivity.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.layoutForFragment, transactionFragment);
				fragmentTransaction.addToBackStack("home_page");
				fragmentTransaction.commit();

				mainActivity.FABAdd.hide();
			}
		};

		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
		RVTransactions.setNestedScrollingEnabled(false);
		return view;
	}

	private void getSend(int[] send) {
		Map<Integer, String> acc;
		Map<Integer, String> cat;
		Map<Integer, String> subcat;

		acc=mainActivity.transactionAdapter.acc;
		cat=mainActivity.transactionAdapter.cat;
		subcat=mainActivity.transactionAdapter.subcat;

		for(int i=-1;++i<acc.size();)
		{
//			if(acc.keySet)
		}
		Set<Integer> keySet=acc.keySet();
//		keySet.
	}

	public void indexToID()
	{

	}
}
