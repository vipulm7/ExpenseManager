package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdCatFragment extends BottomSheetDialogFragment {

	public BsdCatFragment(int catSelected, int subCatSelected, int type, TransactionFragment transactionFragment) {
		this.catSelected = catSelected;
		this.subCatSelected = subCatSelected;
		this.type=type;
		this.transactionFragment=transactionFragment;
	}

//	ConstraintLayout CLCategory, CLSubCategory;
	int catSelected, subCatSelected, type;
	FragmentTransaction fragmentTransaction;
	TransactionFragment transactionFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_cat, container, false);

		BsdCategoryFragment bsdCategoryFragment=new BsdCategoryFragment(catSelected, subCatSelected,type);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.FragmentForCategory, bsdCategoryFragment).commit();
		return view;
	}

	public void showSubCatFragment(int catSelected, int subCatSelected, int type, Category category) {
		BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(catSelected, subCatSelected, type, category);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
	}
}