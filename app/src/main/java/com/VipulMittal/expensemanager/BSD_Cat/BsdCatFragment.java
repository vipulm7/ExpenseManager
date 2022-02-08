package com.VipulMittal.expensemanager.BSD_Cat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BsdCatFragment extends BottomSheetDialogFragment {

	public BsdCatFragment(int cID, int sID, int type, TransactionFragment transactionFragment) {
		this.cID = cID;
		this.sID = sID;
		this.type=type;
		this.transactionFragment=transactionFragment;
	}

	int cID, sID, type;
	FragmentTransaction fragmentTransaction;
	TransactionFragment transactionFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bsd_cat, container, false);

		BsdCategoryFragment bsdCategoryFragment=new BsdCategoryFragment(cID, sID,type);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.FragmentForCategory, bsdCategoryFragment).commit();
		return view;
	}

	public void showSubCatFragment(int cID, int sID, int type, Category category) {
		BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(cID, sID, type, category);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		fragmentTransaction.replace(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
	}
}
