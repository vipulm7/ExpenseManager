package com.VipulMittal.expensemanager.BSD_Account;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdAccountsFragment extends BottomSheetDialogFragment {

	//constructor
	public BsdAccountsFragment(int catSelected, TransactionFragment transactionFragment) {
		this.catSelected = catSelected;
		this.transactionFragment=transactionFragment;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVAccounts;
	Button addNew;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	int catSelected;
	TransactionFragment transactionFragment;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_accounts, container, false);
		RVAccounts =view.findViewById(R.id.bsd_rv_accounts);
		addNew=view.findViewById(R.id.BSD_BaddAccount);

		MainActivity mainActivity=(MainActivity)getActivity();
		accountAdapter=mainActivity.accountAdapter;
		accountAdapter.selected=catSelected;
		accountAdapter.who=1;

		accountViewModel=mainActivity.accountViewModel;


		AccountAdapter.ClickListener listener=new AccountAdapter.ClickListener() {
			@Override
			public void onItemClick(AccountAdapter.AccViewHolder viewHolder) {
				int pos=viewHolder.getAdapterPosition();
				Log.d(TAG, "onItemClick: "+pos);
				catSelected =pos;
				mainActivity.getSupportFragmentManager();

				transactionFragment.saveSelectedAccount(pos,accountAdapter.accounts.get(pos).name); //bsdAccountsFragment.selected can also be used

				dismiss();
			}
		};

		accountAdapter.listener=listener;


//		accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
//		accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
//			accountAdapter.setAccounts(accounts);
//			accountAdapter.notifyItemInserted(accounts.size()-1);
//		});

		RVAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
		Log.d(TAG, "onCreate: transaction done");
		RVAccounts.setNestedScrollingEnabled(false);
		RVAccounts.setAdapter(accountAdapter);


		EditText editText=new EditText(getContext());

		AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
		builder.setTitle("Add New Account")
				.setNegativeButton("Cancel", (dialog, which) -> {

				})
				.setView(editText)
				.setPositiveButton("Add", (dialog, which) -> {
					accountViewModel.Insert(new Account(editText.getText().toString()));
				});
		AlertDialog dialog = builder.create();

		addNew.setOnClickListener(v->{
			Log.d(TAG, "onCreateView: builder = "+builder);
//			builder.create();

			editText.setText("");
			editText.requestFocus();
			dialog.show();

			Log.d(TAG, "onCreateView: dialog created");

			((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

			editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(charSequence.toString().trim().length() != 0);
				}

				@Override
				public void afterTextChanged(Editable editable) {

				}
			});
		});

		return view;
	}
}