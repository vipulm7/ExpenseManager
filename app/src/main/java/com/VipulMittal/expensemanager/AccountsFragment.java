package com.VipulMittal.expensemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.VipulMittal.expensemanager.BSD_Account.BsdAccountsFragment;
import com.VipulMittal.expensemanager.accountRoom.Account;
import com.VipulMittal.expensemanager.accountRoom.AccountAdapter;
import com.VipulMittal.expensemanager.accountRoom.AccountDAO;
import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AccountsFragment extends Fragment {

	public AccountsFragment() {
		// Required empty public constructor
	}

	RecyclerView RVAccount;
	AccountAdapter accountAdapter;
	AccountViewModel accountViewModel;
	TransactionViewModel transactionViewModel;
	CategoryViewModel categoryViewModel;
	SubCategoryViewModel subCategoryViewModel;
	View accView;
	Toast toast;
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
		transactionViewModel = mainActivity.transactionViewModel;
		categoryViewModel = mainActivity.categoryViewModel;
		subCategoryViewModel = mainActivity.subCategoryViewModel;
		toast=mainActivity.toast;



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
						.setPositiveButton("Update", (dialog, which) -> {

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
				Button del1=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				del1.setOnClickListener(view->{
					if(possible(ETForAccIB.getText().toString().trim())) {
						Account account=new Account(ETForAccN.getText().toString().trim(),accountSelected.amount, Integer.parseInt(ETForAccIB.getText().toString().trim()), mainActivity.icon_account[iconsAdapter.selected]);
						account.id=accountSelected.id;
						accountViewModel.Update(account);
						dialog.dismiss();
					}
					else
					{
						if(toast!=null)
							toast.cancel();

						toast= Toast.makeText(mainActivity, "Only 0-9 characters allowed", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				ETForAccN.setText(accountSelected.name);
				ETForAccIB.setText(""+accountSelected.initialBalance);
				ETForAccN.requestFocus();

				RecyclerView recyclerView = accView.findViewById(R.id.rv_icons_account);

				IconsAdapter.ClickListener listener = viewHolder1 -> {
					int pos=viewHolder1.getAdapterPosition();

					int a=iconsAdapter.selected;
					iconsAdapter.selected=pos;

					iconsAdapter.notifyItemChanged(a);
					iconsAdapter.notifyItemChanged(pos);
				};

				iconsAdapter.selected = findIndex(mainActivity.icon_account, accountSelected.imageId);
				iconsAdapter.listener = listener;
				recyclerView.setAdapter(iconsAdapter);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
			}
		};

		AccountAdapter.ClickListener deleteListener = viewHolder -> {
			int position = viewHolder.getAdapterPosition();
			Account account = accountAdapter.accounts.get(position);

			List<Transaction>transactionsToBeDeleted = transactionViewModel.getAllTransactionsAcc(account.id);

			AlertDialog.Builder builder;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
				builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
			}
			else
				builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Delete Account")
					.setMessage("Are you sure want to delete this account!")
					.setNegativeButton("Cancel", (dialog, which) -> {

					})
					.setPositiveButton("Delete", null);
			final AlertDialog[] dialog = {builder.create()};
			dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);
//				dialog.getWindow().getAttributes().windowAnimations = R.style.

			dialog[0].show();
			Dialog dialog1 = dialog[0];

			Button del = dialog[0].getButton(AlertDialog.BUTTON_POSITIVE);
			del.setOnClickListener(v->{
				if(transactionsToBeDeleted.size()==0)
					accountViewModel.Delete(account);
				else
				{
					builder.setTitle("")
							.setMessage("There are "+transactionsToBeDeleted.size()+" transactions with done using this account. What to do with them")
							.setPositiveButton("Delete those transaction", (dialog2, which2) -> {
								for(int i=-1;++i<transactionsToBeDeleted.size();)
								{
									Transaction transaction = transactionsToBeDeleted.get(i);

									transactionViewModel.Delete(transaction);
									accountViewModel.UpdateAmt(-transaction.amount, transaction.accountID);
									if(transaction.type == 3)
										accountViewModel.UpdateAmt(transaction.amount, transaction.catID);
									else {
										categoryViewModel.UpdateAmt(-transaction.amount, transaction.catID);
										if(transaction.subCatID!=-1)
											subCategoryViewModel.UpdateAmt(-transaction.amount, transaction.subCatID);
									}
								}
								mainActivity.transactionAdapter.notifyDataSetChanged();
								accountViewModel.Delete(account);
							})
							.setNegativeButton("Choose New Account", (dialog2, which2) -> {

								BottomSheetDialogFragment bottomSheetDialogFragment=new BsdAccountsFragment(account.id, account.id, 1, 4, null, transactionsToBeDeleted, mainActivity);
								bottomSheetDialogFragment.show(mainActivity.getSupportFragmentManager(), "BSD_Accounts");
							});
					dialog[0] = builder.create();
					dialog[0].getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);
//				dialog.getWindow().getAttributes().windowAnimations = R.style.

					dialog[0].show();
				}
				dialog1.dismiss();
			});
		};


		accountAdapter.listener=listener;
		accountAdapter.deleteListener = deleteListener;

		RVAccount.setLayoutManager(new LinearLayoutManager(getContext()));
		RVAccount.setAdapter(accountAdapter);
		RVAccount.setNestedScrollingEnabled(false);

		return view;
	}

	private int findIndex(int[] icon_account, int imageId) {
		for(int i=-1;++i<icon_account.length;)
			if(icon_account[i] == imageId)
				return i;

		return -1;
	}

	private boolean possible(String trim) {
		int n=trim.length();
		for(int i=-1;++i<n;)
		{
			if(trim.charAt(i)>='0' && trim.charAt(i)<='9')
				continue;
			return false;
		}
		return true;
	}
}