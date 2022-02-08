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
import android.widget.Toast;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BsdAccountsFragment extends BottomSheetDialogFragment {

	//constructor
	public BsdAccountsFragment(int aID, int other, int aType, int type, TransactionFragment transactionFragment) {
		this.aID = aID;
		this.transactionFragment=transactionFragment;
		this.aType = aType;
		this.other=other;
		this.type=type;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVAccounts;
	Button addNew;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	int aID, aType, other, type;
	TransactionFragment transactionFragment;
	List<Account> accounts;
	View accView;
	boolean b1=false, b2=false;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_accounts, container, false);
		RVAccounts =view.findViewById(R.id.bsd_rv_accounts);
		addNew=view.findViewById(R.id.BSD_BaddAccount);

		MainActivity mainActivity=(MainActivity)getActivity();
		accountAdapter=mainActivity.accountAdapter;
		accounts=accountAdapter.accounts;
		accountAdapter.aID = aID;
		accountAdapter.who=1;

		accountViewModel=mainActivity.accountViewModel;


		AccountAdapter.ClickListener listener=new AccountAdapter.ClickListener() {
			@Override
			public void onItemClick(AccountAdapter.AccViewHolder viewHolder) {
				int pos=viewHolder.getAdapterPosition();
				Log.d(TAG, "onItemClick: "+pos);
				aID =accounts.get(pos).id;
				mainActivity.getSupportFragmentManager();


				if(type == 3 && aID == other)
				{
					if(mainActivity.toast!=null)
						mainActivity.toast.cancel();

					mainActivity.toast = Toast.makeText(getContext(), "Can't select same accounts", Toast.LENGTH_SHORT);
					mainActivity.toast.show();
				}
				else {
					if (aType == 1)
						transactionFragment.saveSelectedAccount(aID, accounts.get(pos).name); //bsdAccountsFragment.selected can also be used
					else
						transactionFragment.saveSelectedCategoryWithName(aID, accounts.get(pos).name);
					dismiss();
				}
			}
		};

		accountAdapter.listener=listener;

		RVAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
		Log.d(TAG, "onCreate: transaction done");
		RVAccounts.setNestedScrollingEnabled(false);
		RVAccounts.setAdapter(accountAdapter);

//		setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog);

		alertDialogForAddAcc();
		return view;
	}

	private void alertDialogForAddAcc() {
		LayoutInflater layoutInflater=LayoutInflater.from(getContext());
		accView=layoutInflater.inflate(R.layout.account_dialog, null);

		EditText ETForAccN=accView.findViewById(R.id.ETDialogAccName);
		EditText ETForAccIB=accView.findViewById(R.id.ETDialogAccBalance);

		AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
		builder.setTitle("Add New Account")
				.setNegativeButton("Cancel", (dialog, which) -> {

				})
				.setView(accView)
				.setPositiveButton("Add", (dialog, which) -> {
					accountViewModel.Insert(new Account(ETForAccN.getText().toString().trim(),0, Integer.parseInt(ETForAccIB.getText().toString().trim())));
				});
		AlertDialog dialog = builder.create();

		ETForAccN.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b1=charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		ETForAccIB.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b2=charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		addNew.setOnClickListener(v->{
			Log.d(TAG, "onCreateView: builder = "+builder);
//			builder.create();

			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			ETForAccN.setText("");
			ETForAccIB.setText("");
			ETForAccN.requestFocus();
			Log.d(TAG, "onCreateView: dialog created");
		});
	}
}