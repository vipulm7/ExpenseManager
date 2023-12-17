package com.VipulMittal.expensemanager.BSD_Cat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.IconsAdapter;
import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.VipulMittal.expensemanager.transactionRoom.TransactionViewModel;

import java.util.List;

public class BsdCategoryFragment extends Fragment {

	public static final String TAG = "Vipul_tag";
	RecyclerView RVCategories;
	Button BAddNewCat;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	BsdCatFragment bsdCatFragment;
	MainActivity mainActivity;
	int cID, sID, type;
	TransactionActivity transactionActivity;
	List<Transaction> transactionsToBeModified;
	Category categorySelected, categoryToBeDeleted;
	CategoryAdapter.BSDCatViewHolder viewHolderCat;
	View catView;
	TransactionViewModel transactionViewModel;
	SubCategoryViewModel subCategoryViewModel;
	boolean b1 = false, b2 = false;
	Toast toast;
	int cIDCame;

	public BsdCategoryFragment(int cID, int sID, int type, List<Transaction> transactionsToBeModified, CategoryAdapter.BSDCatViewHolder viewHolder, MainActivity mainActivity) {
		this.cID = cID;
		this.sID = sID;
		this.type = type;
		cIDCame = cID;
		this.transactionsToBeModified = transactionsToBeModified;
		this.viewHolderCat = viewHolder;
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories = view.findViewById(R.id.bsd_rv_categories);
		BAddNewCat = view.findViewById(R.id.BAddNewCat);
		bsdCatFragment = (BsdCatFragment) getParentFragment();
		transactionViewModel = mainActivity.transactionViewModel;
		subCategoryViewModel = mainActivity.subCategoryViewModel;

		Log.d(TAG, "onCreateView: subCatSelectedID = " + sID);
//		Log.d(TAG, "onCreateView: adapter before = "+categoryAdapter);

		if (transactionActivity != null) {
			if (type == 1) {
				categoryAdapter = mainActivity.categoryAdapter;
				categoryViewModel = mainActivity.categoryViewModel;
			} else {
				categoryAdapter = mainActivity.categoryAdapter2;
				categoryViewModel = mainActivity.categoryViewModel2;
			}
		} else {
			categoryAdapter = new CategoryAdapter(mainActivity);
			if (type == 1) {
				categoryAdapter.categories = mainActivity.categoryAdapter.categories;
				categoryViewModel = mainActivity.categoryViewModel;
			} else {
				categoryAdapter.categories = mainActivity.categoryAdapter2.categories;
				categoryViewModel = mainActivity.categoryViewModel2;
			}
		}

		categoryAdapter.who = 1;
		transactionActivity = bsdCatFragment.transactionActivity;
		if (cID != -1)
			categorySelected = categoryViewModel.getCat(cID);
		categoryToBeDeleted = categorySelected;

		CategoryAdapter.ClickListener listener = new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				if (cID != categoryAdapter.categories.get(position).catId) {
					categorySelected = categoryAdapter.categories.get(position);
					cID = categoryAdapter.categories.get(position).catId;
					if (transactionsToBeModified == null || cIDCame != cID)
						selectSubCat(-1);
					else {
						toast = mainActivity.toast;
						if (toast != null)
							toast.cancel();
						toast = Toast.makeText(mainActivity, "Can't select same Category!", Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					if (transactionsToBeModified == null) {
						transactionActivity.saveSelectedCategoryWithName(cID, categorySelected.catName, categorySelected.catImageID);
						bsdCatFragment.dismiss();
					} else if (cID == cIDCame && viewHolderCat != null) {
						toast = mainActivity.toast;
						if (toast != null)
							toast.cancel();
						toast = Toast.makeText(mainActivity, "Can't select same Category!", Toast.LENGTH_SHORT);
						toast.show();
					} else if (viewHolderCat != null) {
						categorySelected = categoryAdapter.categories.get(position);
						cID = categorySelected.catId;
						Log.d(TAG, "onItemClick: adapter = " + viewHolderCat.subCategoryAdapter);
						Log.d(TAG, "onItemClick: subCategories = " + viewHolderCat.subCategoryAdapter.subCategories);

						for (int i = -1; ++i < transactionsToBeModified.size(); ) {
							Transaction transaction = transactionsToBeModified.get(i);
							if (transaction.type == 3)
								continue;

							transaction.catID = cID;
							transaction.subCatID = -1;

							transactionViewModel.Update(transaction);
							categoryViewModel.UpdateAmt(transaction.amount, cID);
						}
						List<SubCategory> subCats = subCategoryViewModel.getSubcats(categoryToBeDeleted.catId);
						for (int i = -1; ++i < subCats.size(); )
							subCategoryViewModel.Delete(subCats.get(i));
						categoryViewModel.Delete(categoryToBeDeleted);

						Log.d(TAG, "onItemClick: viewHolderCat = " + viewHolderCat);
						Log.d(TAG, "onItemClick: subCategories = " + viewHolderCat.subCategoryAdapter);
						if (viewHolderCat.subCategoryAdapter.subCategories != null) {
							int x = viewHolderCat.subCategoryAdapter.subCategories.size();
							viewHolderCat.subCategoryAdapter.subCategories.clear();
							viewHolderCat.subCategoryAdapter.notifyItemRangeRemoved(0, x);
						}
						bsdCatFragment.dismiss();
					} else {

					}
				}
			}
		};
		if (transactionsToBeModified == null)
			categoryAdapter.cID = cID;
		categoryAdapter.cardListener = listener;

		RVCategories.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCategories.setAdapter(categoryAdapter);
		RVCategories.setNestedScrollingEnabled(false);

		if (transactionsToBeModified == null) {
			if (sID != -1) {
				Log.d(TAG, "onCreateView: categoryAdapter.categories.size() = " + categoryAdapter.categories.size());
				selectSubCat(sID);
			} else if (cID != -1 && categorySelected.noOfSubCat != 0)
				selectSubCat(-1);
		}

		addNewCat();

		return view;
	}

	private void addNewCat() {
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		catView = layoutInflater.inflate(R.layout.category_dialog, null);

		EditText ETForCatN = catView.findViewById(R.id.ETDialogCatName);
		EditText ETForCatIB = catView.findViewById(R.id.ETDialogCatBudget);
		IconsAdapter iconsAdapter = new IconsAdapter(mainActivity.icon_category_income);

		TextView catTitle = catView.findViewById(R.id.TVDialogCT);
		if (type == 1)
			catTitle.setText("Add new Income Category");
		else
			catTitle.setText("Add new Expense Category");

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			builder = new AlertDialog.Builder(getContext(), android.R.style.ThemeOverlay_Material_Dialog);
		} else
			builder = new AlertDialog.Builder(getContext());
		builder
				.setNegativeButton("Cancel", (dialog, which) -> {

				})
				.setView(catView)
				.setPositiveButton("Add", null);
		AlertDialog dialog = builder.create();
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_25);

		ETForCatN.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b1 = charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		ETForCatIB.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				b2 = charSequence.toString().trim().length() != 0;
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b1 && b2);
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		BAddNewCat.setOnClickListener(v -> {
			Log.d(TAG, "onCreateView: builder = " + builder);
//			builder.create();

			dialog.show();
			Button del1 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
			del1.setOnClickListener(view -> {
				if (possible(ETForCatIB.getText().toString().trim())) {
					categoryViewModel.Insert(new Category(ETForCatN.getText().toString().trim(), 0, Integer.parseInt(ETForCatIB.getText().toString().trim()), 0, type, mainActivity.icon_category_income[iconsAdapter.selected]));
					dialog.dismiss();
				} else {
					if (toast != null)
						toast.cancel();

					toast = Toast.makeText(mainActivity, "Only 0-9 characters allowed", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			ETForCatN.setText("");
			ETForCatIB.setText("");
			ETForCatN.requestFocus();

			RecyclerView recyclerView = catView.findViewById(R.id.rv_icons_category);

			IconsAdapter.ClickListener listener = viewHolder2 -> {
				int pos2 = viewHolder2.getAdapterPosition();

				int a = iconsAdapter.selected;
				iconsAdapter.selected = pos2;

				iconsAdapter.notifyItemChanged(a);
				iconsAdapter.notifyItemChanged(pos2);
			};
			iconsAdapter.listener = listener;
			recyclerView.setAdapter(iconsAdapter);
			recyclerView.setHasFixedSize(true);
			recyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 2, GridLayoutManager.HORIZONTAL, false));

			Log.d(TAG, "onCreateView: dialog created");
		});
	}


	private void selectSubCat(int sID) {

		int a = categoryAdapter.cID;
		categoryAdapter.cID = cID;

		notify2ItemsChanged(a);

		Log.d(TAG, "selectSubCat: catSelectedID = " + cID);
		transactionActivity = bsdCatFragment.transactionActivity;
		if (categorySelected.noOfSubCat != 0) {

			TextView x = (TextView) (transactionActivity.TVCategory.getCurrentView());
			if (transactionActivity != null && x.getText().toString().equals("Category")) {
				transactionActivity.saveSelectedCategoryWithoutName(cID);
				Log.d(TAG, "selectSubCat: saved cat");
			}
			Log.d(TAG, "category sent = " + categorySelected.catName);
			mainActivity.subCategoryViewModel.getSubcategories(cID).observe(getViewLifecycleOwner(), new Observer<List<SubCategory>>() {
				@Override
				public void onChanged(List<SubCategory> subCategories) {
					mainActivity.subCategoryAdapter.setSubCategories(subCategories);
					mainActivity.subCategoryAdapter.who = 1;
					mainActivity.subCategoryAdapter.notifyDataSetChanged();
					bsdCatFragment.showSubCatFragment(cID, sID, type, categorySelected);
				}
			});
		} else {
			if (transactionActivity != null) {
				transactionActivity.saveSelectedCategoryWithName(cID, categorySelected.catName, categorySelected.catImageID);
				bsdCatFragment.dismiss();
			} else if (cID == cIDCame) {
				toast = mainActivity.toast;
				if (toast != null)
					toast.cancel();
				toast = Toast.makeText(mainActivity, "Can't select same Category!", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				categorySelected = categoryViewModel.getCat(cID);

				for (int i = -1; ++i < transactionsToBeModified.size(); ) {
					Transaction transaction = transactionsToBeModified.get(i);
					if (transaction.type == 3)
						continue;

					transaction.catID = cID;
					transaction.subCatID = -1;

					transactionViewModel.Update(transaction);
					categoryViewModel.UpdateAmt(transaction.amount, cID);
				}
				List<SubCategory> subCats = subCategoryViewModel.getSubcats(categoryToBeDeleted.catId);
				for (int i = -1; ++i < subCats.size(); )
					subCategoryViewModel.Delete(subCats.get(i));
				categoryViewModel.Delete(categoryToBeDeleted);

				int x = viewHolderCat.subCategoryAdapter.subCategories.size();
				viewHolderCat.subCategoryAdapter.subCategories.clear();
				viewHolderCat.subCategoryAdapter.notifyItemRangeRemoved(0, x);
				bsdCatFragment.dismiss();
			}
		}
	}

	private void notify2ItemsChanged(int oldCID) {
		///////////////////////////////////////////////////////////////////////////////
		//may cause dikkt
		int p = categoryAdapter.categories.indexOf(categorySelected);
		for (int i = -1; ++i < categoryAdapter.categories.size(); ) {
			if (categoryAdapter.categories.get(i).catId == cID) {
				p = i;
				break;
			}
		}

		int p1 = -1;
		if (oldCID != -1) {
			for (int i = -1; ++i < categoryAdapter.categories.size(); ) {
				if (categoryAdapter.categories.get(i).catId == oldCID) {
					p1 = i;
					break;
				}
			}
		}

		categoryAdapter.notifyItemChanged(p);
		categoryAdapter.notifyItemChanged(p1);
	}

	private boolean possible(String trim) {
		int n = trim.length();
		for (int i = -1; ++i < n; ) {
			if (trim.charAt(i) >= '0' && trim.charAt(i) <= '9')
				continue;
			return false;
		}
		return true;
	}
}