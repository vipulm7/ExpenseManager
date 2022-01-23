package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdAccountsFragment extends BottomSheetDialogFragment {

	//constructor
	public BsdAccountsFragment(int selected) {
		this.selected=selected;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVAccounts;
	Button addNew;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	int selected;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_accounts, container, false);
		RVAccounts =view.findViewById(R.id.bsd_rv_accounts);
		addNew=view.findViewById(R.id.BSD_BaddAccount);

		accountAdapter=new AccountAdapter(selected, new AccountAdapter.ClickListener() {
			@Override
			public void onItemClick(AccountAdapter.AccViewHolder viewHolder) {
				int pos=viewHolder.getAdapterPosition();
				Log.d(TAG, "onItemClick: "+pos);
				selected=pos;
				TransactionActivity transactionActivity=(TransactionActivity)getActivity();

				if (transactionActivity != null) {
					transactionActivity.saveSelectedAccount(pos,accountAdapter.accounts.get(pos).name); //bsdAccountsFragment.selected can also be used
				}
				else
					Toast.makeText(getContext(),"Error in selecting account",Toast.LENGTH_SHORT).show();

				dismiss();
			}
		});


		accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
		accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
			accountAdapter.setAccounts(accounts);
			accountAdapter.notifyDataSetChanged();
		});

		RVAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
		Log.d(TAG, "onCreate: transaction done");
		RVAccounts.setNestedScrollingEnabled(false);
		RVAccounts.setAdapter(accountAdapter);

		addNew.setOnClickListener(v->{

		});

		return view;
	}
}