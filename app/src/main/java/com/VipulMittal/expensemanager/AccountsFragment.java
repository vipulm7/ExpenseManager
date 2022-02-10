package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountDAO;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;

public class AccountsFragment extends Fragment {

	public AccountsFragment() {
		// Required empty public constructor
	}

	RecyclerView RVAccount;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	View accView;
	boolean b1=false, b2=false;

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
		accountViewModel= mainActivity.accountViewModel;



		AccountAdapter.ClickListener listener=new AccountAdapter.ClickListener() {
			@Override
			public void onItemClick(AccountAdapter.AccViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();
				Account accountSelected=accountAdapter.accounts.get(position);


				LayoutInflater layoutInflater=LayoutInflater.from(getContext());
				accView=layoutInflater.inflate(R.layout.account_dialog, null);

				EditText ETForAccN=accView.findViewById(R.id.ETDialogAccName);
				EditText ETForAccIB=accView.findViewById(R.id.ETDialogAccBalance);

				TextView accTitle = new TextView(getContext());
				accTitle.setText("Update Account");
				accTitle.setGravity(Gravity.CENTER_HORIZONTAL);
				accTitle.setPadding(2,16,2,10);
				accTitle.setTextSize(22);
				accTitle.setTypeface(null, Typeface.BOLD);

				AlertDialog.Builder builder;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
					builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
				}
				else
					builder = new AlertDialog.Builder(getContext());
				builder.setCustomTitle(accTitle)
						.setNegativeButton("Cancel", (dialog, which) -> {

						})
						.setView(accView)
						.setPositiveButton("Update", (dialog, which) -> {
							Account account=new Account(ETForAccN.getText().toString().trim(),accountSelected.amount, Integer.parseInt(ETForAccIB.getText().toString().trim()));
							account.id=accountSelected.id;
							accountViewModel.Update(account);
						});
				AlertDialog dialog = builder.create();
				dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

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


				dialog.show();
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				ETForAccN.setText(accountSelected.name);
				ETForAccIB.setText(""+accountSelected.initialBalance);
				ETForAccN.requestFocus();
			}
		};


		accountAdapter.listener=listener;

		RVAccount.setLayoutManager(new LinearLayoutManager(getContext()));
		RVAccount.setAdapter(accountAdapter);
		RVAccount.setNestedScrollingEnabled(false);

		return view;
	}
}