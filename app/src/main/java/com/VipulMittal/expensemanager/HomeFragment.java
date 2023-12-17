package com.VipulMittal.expensemanager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.accountRoom.AccountViewModel;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionAdapter;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;

import java.util.Arrays;
import java.util.Calendar;

public class HomeFragment extends Fragment {

	public static final String TAG = "Vipul_tag";
	public ActionMode actionMode;
	TextView TVMainExpense, TVMainIncome, TVMainTotal, TVBefore, TVAfter, TVPeriodShown, TVFilter, TVNoTransFound;
	RecyclerView RVTransactions;
	RadioGroup rg_Filter;

	MainActivity mainActivity;
	TransactionAdapter transactionAdapter;
	AccountViewModel accountViewModel;
	TransactionViewModel transactionViewModel;
	CategoryViewModel categoryViewModel;
	SubCategoryViewModel subCategoryViewModel;

	public HomeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		TVMainIncome = view.findViewById(R.id.TVIncomeAmt);
		TVMainExpense = view.findViewById(R.id.TVExpenseAmt);
		TVMainTotal = view.findViewById(R.id.TVTotalAmt);
		RVTransactions = view.findViewById(R.id.RecyclerViewID);
		TVAfter = view.findViewById(R.id.TVafter);
		TVBefore = view.findViewById(R.id.TVbefore);
		TVPeriodShown = view.findViewById(R.id.TVDateChange);
		TVNoTransFound = view.findViewById(R.id.TVNoTransactionsFound);
		TVFilter = view.findViewById(R.id.TVFilter);
		mainActivity = (MainActivity) getActivity();
		transactionAdapter = mainActivity.transactionAdapter;
		accountViewModel = mainActivity.accountViewModel;
		transactionViewModel = mainActivity.transactionViewModel;
		categoryViewModel = mainActivity.categoryViewModel;
		subCategoryViewModel = mainActivity.subCategoryViewModel;
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

		SharedPreferences.Editor editor = sharedPreferences.edit();

		mainActivity.transactionROOM();

		setDate();

		TVAfter.setText(">");
		TVBefore.setText("<");


		TransactionAdapter.CLickListener listener = (viewHolder, view1) -> {
			int position = viewHolder.getAdapterPosition();

			if (!transactionAdapter.selectionModeOn) {
				Transaction transaction = transactionAdapter.transactions.get(position);

				Intent intent = new Intent(mainActivity, TransactionActivity.class);
				intent.putExtra("EXTRA_DURATION", 500L);
				intent.putExtra("calendar", transaction.dateTime);
				intent.putExtra("amount", transaction.amount);
				intent.putExtra("note", transaction.note);
				intent.putExtra("description", transaction.description);
				intent.putExtra("aID", transaction.accountID);
				intent.putExtra("cID", transaction.catID);
				intent.putExtra("sID", transaction.subCatID);
				intent.putExtra("request", 2);
				intent.putExtra("type", transaction.type);
				intent.putExtra("id", transaction.id);

				Bundle bundle = new Bundle();
				bundle.putBinder("bind", new ObjectWrapper(mainActivity));
				intent.putExtras(bundle);

				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mainActivity, view1, "EXTRA_VIEW_LIST");
				startActivity(intent, options.toBundle());


//				mainActivity.FABAdd.hide();
				mainActivity.systemTimeInMillies = 0;
				mainActivity.hideMenu();
			} else {
				viewHolder.changeSelection();
			}
		};


		TransactionAdapter.CLickListener longListener = (viewHolder, view1) -> {
			if (!transactionAdapter.selectionModeOn) {
				transactionAdapter.selectionModeOn = true;
				transactionAdapter.transactionsToBeDeleted.clear();
				boolean[] select = new boolean[transactionAdapter.transactions.size()];
				transactionAdapter.select = select;
				mainActivity.FABAdd.hide();
				mainActivity.navigationBarView.setVisibility(View.INVISIBLE);


				ActionMode.Callback callback = new ActionMode.Callback() {
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						MenuInflater inflater1 = mode.getMenuInflater();
						inflater1.inflate(R.menu.selection_menu, menu);
						actionMode = mode;

						return true;
					}

					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

						return true;
					}

					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						int id = item.getItemId();
						switch (id) {
							case R.id.menuSelectDelete:

								if (transactionAdapter.transactionsToBeDeleted.size() == 0) {
									if (mainActivity.toast != null)
										mainActivity.toast.cancel();

									mainActivity.toast = Toast.makeText(mainActivity, "Select at least 1 transaction", Toast.LENGTH_LONG);
									mainActivity.toast.show();
								} else {
									TextView delTitle = new TextView(getContext());
									delTitle.setText("Delete Transaction(s)");
									delTitle.setGravity(Gravity.CENTER_HORIZONTAL);
									delTitle.setPadding(2, 16, 2, 10);
									delTitle.setTextSize(22);
									delTitle.setTypeface(null, Typeface.BOLD);

									AlertDialog.Builder builder;
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
										builder = new AlertDialog.Builder(mainActivity, android.R.style.ThemeOverlay_Material_Dialog);
									else
										builder = new AlertDialog.Builder(mainActivity);
									builder.setCustomTitle(delTitle)
											.setMessage("Are you sure to delete " + transactionAdapter.transactionsToBeDeleted.size() + " transaction(s)")
											.setNegativeButton("Cancel", (dialog, which) -> {

											})
											.setPositiveButton("Delete", (dialog, which) -> {
												for (int i = -1; ++i < transactionAdapter.transactionsToBeDeleted.size(); ) {
													Transaction transaction = transactionAdapter.transactionsToBeDeleted.get(i);

													transactionViewModel.Delete(transaction);
													accountViewModel.UpdateAmt(-transaction.amount, transaction.accountID);
													if (transaction.type == 3)
														accountViewModel.UpdateAmt(transaction.amount, transaction.catID);
													else {
														categoryViewModel.UpdateAmt(-transaction.amount, transaction.catID);
														if (transaction.subCatID != -1)
															subCategoryViewModel.UpdateAmt(-transaction.amount, transaction.subCatID);
													}
												}
												transactionAdapter.notifyDataSetChanged();
												mode.finish();
											});
									AlertDialog dialog = builder.create();
									dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);
									builder.show();
								}
								break;
							case R.id.menuSelectAll:
								transactionAdapter.transactionsToBeDeleted.clear();
								if (!allSelected()) {
									for (int i = -1; ++i < transactionAdapter.transactions.size(); ) {
										if (transactionAdapter.transactions.get(i).id == -1)
											continue;
										transactionAdapter.transactionsToBeDeleted.add(transactionAdapter.transactions.get(i));
									}
								}
								Arrays.fill(transactionAdapter.select, !allSelected());
								transactionAdapter.notifyDataSetChanged();
								mode.setTitle(String.valueOf(transactionAdapter.transactionsToBeDeleted.size()));
						}
						return true;
					}

					@Override
					public void onDestroyActionMode(ActionMode mode) {
						transactionAdapter.selectionModeOn = false;
						transactionAdapter.transactionsToBeDeleted.clear();
//						transactionAdapter.selectAll();
						transactionAdapter.notifyDataSetChanged();
						mainActivity.FABAdd.show();
						mainActivity.navigationBarView.setVisibility(View.VISIBLE);
						actionMode = null;
						transactionAdapter.select = null;
					}
				};

				((AppCompatActivity) view.getContext()).startActionMode(callback);
			}
			viewHolder.changeSelection();

		};


		transactionAdapter.listener = listener;
		transactionAdapter.longListener = longListener;

		TVBefore.setOnClickListener(v -> {
			if (mainActivity.viewMode == R.id.RBM)
				mainActivity.toShow.add(Calendar.MONTH, -1);
			else if (mainActivity.viewMode == R.id.RBD)
				mainActivity.toShow.add(Calendar.DATE, -1);
			else if (mainActivity.viewMode == R.id.RBW)
				mainActivity.toShow.add(Calendar.WEEK_OF_YEAR, -1);
			mainActivity.transactionROOM();
			setDate();
		});

		TVAfter.setOnClickListener(v -> {
			if (mainActivity.viewMode == R.id.RBM)
				mainActivity.toShow.add(Calendar.MONTH, 1);
			else if (mainActivity.viewMode == R.id.RBD)
				mainActivity.toShow.add(Calendar.DATE, 1);
			else if (mainActivity.viewMode == R.id.RBW)
				mainActivity.toShow.add(Calendar.WEEK_OF_YEAR, 1);
			mainActivity.transactionROOM();
			setDate();
		});

		View filterView = inflater.inflate(R.layout.filter_dialog, null);
		rg_Filter = filterView.findViewById(R.id.RGFilter);

		TextView viewTitle = new TextView(getContext());
		viewTitle.setText("View Mode");
		viewTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		viewTitle.setPadding(2, 16, 2, 10);
		viewTitle.setTextSize(22);
		viewTitle.setTypeface(null, Typeface.BOLD);

		AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
		else
			builder = new AlertDialog.Builder(getContext());
		builder.setCustomTitle(viewTitle)
				.setView(filterView);
		AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

		RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
				editor.putInt("view", checkedID);
				editor.apply();
				mainActivity.viewMode = checkedID;
				Log.d(TAG, "onCheckedChanged: mainActivity.viewMode" + mainActivity.viewMode);
				Log.d(TAG, "onCheckedChanged: shared = " + sharedPreferences.getInt("view", -1));
				dialog.dismiss();

				mainActivity.toShow.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
				mainActivity.tellDate();
				mainActivity.transactionROOM();
				setDate();
				rg_Filter.setOnCheckedChangeListener(null);
			}
		};


		TVFilter.setOnClickListener(v -> {
			dialog.show();
			rg_Filter.check(mainActivity.viewMode);
			rg_Filter.setOnCheckedChangeListener(listener1);
//			rg_Filter.check(mainActivity.viewMode);
		});

		RVTransactions.setAdapter(transactionAdapter);
		RVTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
		RVTransactions.setNestedScrollingEnabled(false);

		RVTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

				if (dy > 0)
					mainActivity.FABAdd.hide();
				else
					mainActivity.FABAdd.show();
				super.onScrolled(recyclerView, dx, dy);
			}
		});
		return view;
	}

	private boolean allSelected() {
		for (int i = -1; ++i < transactionAdapter.select.length; ) {
			if (transactionAdapter.transactions.get(i).id == -1)
				continue;
			if (!transactionAdapter.select[i])
				return false;
		}
		return true;
	}

	private void setDate() {
		int date = mainActivity.toShow.get(Calendar.DATE);
		int week = mainActivity.toShow.get(Calendar.WEEK_OF_YEAR);
		int month = mainActivity.toShow.get(Calendar.MONTH);
		int year = mainActivity.toShow.get(Calendar.YEAR);

		if (mainActivity.viewMode == R.id.RBM)
			TVPeriodShown.setText(getM(month) + ", " + year);
		else if (mainActivity.viewMode == R.id.RBW) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(mainActivity.toShow.getTimeInMillis());
			calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK) + 1);
			String s = calendar.get(Calendar.DATE) + " " + getM(calendar.get(Calendar.MONTH)) + " - ";
			calendar.add(Calendar.DATE, 6);


			TVPeriodShown.setText(s + calendar.get(Calendar.DATE) + " " + getM(calendar.get(Calendar.MONTH)));
		} else if (mainActivity.viewMode == R.id.RBD)
			TVPeriodShown.setText(date + " " + getM(month) + ", " + year);
	}

	private String getM(int m) {
		String[] a = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		return a[m];
	}
}
