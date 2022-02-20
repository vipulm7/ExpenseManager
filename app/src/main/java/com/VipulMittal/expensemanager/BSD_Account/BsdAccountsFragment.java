package com.VipulMittal.expensemanager.BSD_Account;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.VipulMittal.expensemanager.IconsAdapter;
import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BsdAccountsFragment extends BottomSheetDialogFragment {

	//constructor
	public BsdAccountsFragment(int aID, int other, int aType, int type, TransactionFragment transactionFragment, List<Transaction> transactionsToBeModified) {
		this.aID = aID;
		this.transactionFragment=transactionFragment;
		this.aType = aType;
		this.other=other;
		this.type=type;
		this.transactionsToBeModified = transactionsToBeModified;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVAccounts;
	Button addNew;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	int aID, aType, other, type;
	TransactionFragment transactionFragment;
	List<Account> accounts;
	MainActivity mainActivity;
	View accView;
	boolean b1=false, b2=false;
	TransactionViewModel transactionViewModel;
	public List<Transaction> transactionsToBeModified;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_accounts, container, false);
		RVAccounts =view.findViewById(R.id.bsd_rv_accounts);
		addNew=view.findViewById(R.id.BSD_BaddAccount);

		mainActivity=(MainActivity)getActivity();
		transactionViewModel = mainActivity.transactionViewModel;
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


				if(type == 4)
				{
					if(aID == other) {
						if (mainActivity.toast != null)
							mainActivity.toast.cancel();

						mainActivity.toast = Toast.makeText(getContext(), "Can't select this account", Toast.LENGTH_SHORT);
						mainActivity.toast.show();
					}
					else
					{
						Account accountToBeDeleted = accountViewModel.getAcc(transactionsToBeModified.get(0).accountID);

						for(int i=-1;++i< transactionsToBeModified.size();)
						{
							transactionsToBeModified.get(i).accountID = aID;
							transactionViewModel.Update(transactionsToBeModified.get(i));
							accountViewModel.UpdateAmt(transactionsToBeModified.get(i).amount, aID);
						}

						accountViewModel.Delete(accountToBeDeleted);
						dismiss();
					}
				}
				else if(type == 3 && aID == other)
				{
					if(mainActivity.toast!=null)
						mainActivity.toast.cancel();

					mainActivity.toast = Toast.makeText(getContext(), "Can't select same accounts", Toast.LENGTH_SHORT);
					mainActivity.toast.show();
				}
				else {
					if (aType == 1)
						transactionFragment.saveSelectedAccount(aID, accounts.get(pos)); //bsdAccountsFragment.selected can also be used
					else
						transactionFragment.saveSelectedCategoryWithName(aID, accounts.get(pos).name, accounts.get(pos).imageId);
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

		TextView accTitle = new TextView(getContext());
		accTitle.setText("Add New Account");
		accTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		accTitle.setPadding(2,16,2,10);
		accTitle.setTextSize(22);
		accTitle.setTypeface(null, Typeface.BOLD);
		IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_account);


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
				.setPositiveButton("Add", (dialog, which) -> {
					accountViewModel.Insert(new Account(ETForAccN.getText().toString().trim(),0, Integer.parseInt(ETForAccIB.getText().toString().trim()), mainActivity.icon_account[iconsAdapter.selected]));
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

		addNew.setOnClickListener(v->{
			Log.d(TAG, "onCreateView: builder = "+builder);
//			builder.create();

			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			ETForAccN.setText("");
			ETForAccIB.setText("");
			ETForAccN.requestFocus();

			RecyclerView recyclerView = accView.findViewById(R.id.rv_icons_account);

			IconsAdapter.ClickListener listener = viewHolder1 -> {
				int pos=viewHolder1.getAdapterPosition();

				int a=iconsAdapter.selected;
				iconsAdapter.selected=pos;

				iconsAdapter.notifyItemChanged(a);
				iconsAdapter.notifyItemChanged(pos);
			};
			iconsAdapter.listener = listener;
			recyclerView.setAdapter(iconsAdapter);
			recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));

			Log.d(TAG, "onCreateView: dialog created");

		});
	}


}