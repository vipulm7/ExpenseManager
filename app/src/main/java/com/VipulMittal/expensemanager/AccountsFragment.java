package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;

public class AccountsFragment extends Fragment {

	public AccountsFragment() {
		// Required empty public constructor
	}

	RecyclerView RVAccount;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_accounts, container, false);

		RVAccount=view.findViewById(R.id.RVAccount);

		MainActivity mainActivity=(MainActivity) getActivity();
		accountAdapter=mainActivity.accountAdapter;
		accountAdapter.who=2;
		accountAdapter.aID =-1;
		accountAdapter.listener=null;
//		AccountAdapter.ClickListener listener=new AccountAdapter.ClickListener() {
//			@Override
//			public void onItemClick(AccountAdapter.AccViewHolder viewHolder) {
//				int position=viewHolder.getAdapterPosition();
//
//			}
//		};

		RVAccount.setLayoutManager(new LinearLayoutManager(getContext()));
		RVAccount.setAdapter(accountAdapter);
		RVAccount.setNestedScrollingEnabled(false);




		return view;
	}
}