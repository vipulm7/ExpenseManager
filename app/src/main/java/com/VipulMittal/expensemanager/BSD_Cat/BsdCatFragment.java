package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdCatFragment extends BottomSheetDialogFragment {

	public BsdCatFragment(int selected, int type) {
		this.selected=selected;
		this.type=type;
	}

//	ConstraintLayout CLCategory, CLSubCategory;
	int selected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_cat, container, false);

//		CLCategory=view.findViewById(R.id.FragmentForCategory);
//		CLSubCategory=view.findViewById(R.id.FragmentForSubCategory);

		BsdCategoryFragment bsdCategoryFragment=new BsdCategoryFragment(selected,type);
//		BsdSubCategoryFragment bsdSubCategoryFragment=new BsdSubCategoryFragment();
		FragmentTransaction fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.FragmentForCategory, bsdCategoryFragment).commit();
//		fragmentTransaction.add(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();

		return view;
	}
}