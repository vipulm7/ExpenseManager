package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdCatFragment extends BottomSheetDialogFragment {

	public BsdCatFragment(int catSelected, int subCatSelected, int type) {
		this.catSelected = catSelected;
		this.subCatSelected = subCatSelected;
		this.type=type;
	}

//	ConstraintLayout CLCategory, CLSubCategory;
	int catSelected, subCatSelected, type;
	FragmentTransaction fragmentTransaction;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_cat, container, false);

//		CLCategory=view.findViewById(R.id.FragmentForCategory);
//		CLSubCategory=view.findViewById(R.id.FragmentForSubCategory);

		BsdCategoryFragment bsdCategoryFragment=new BsdCategoryFragment(catSelected, subCatSelected,type, R.id.FragmentForSubCategory);
//		BsdSubCategoryFragment bsdSubCategoryFragment=new BsdSubCategoryFragment();
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.FragmentForCategory, bsdCategoryFragment).commit();
//		fragmentTransaction.add(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
		return view;
	}

	public void showSubCatFragment(int subCatSelected, int type, Category category) {
		BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(subCatSelected, type, category);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
	}
}