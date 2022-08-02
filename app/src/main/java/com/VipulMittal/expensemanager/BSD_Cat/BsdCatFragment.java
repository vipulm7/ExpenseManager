package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.transactionRoom.Transaction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class BsdCatFragment extends BottomSheetDialogFragment {

	public BsdCatFragment(int cID, int sID, int type, TransactionActivity transactionActivity, List<Transaction> transactionsToBeModified, CategoryAdapter.BSDCatViewHolder viewHolder, MainActivity mainActivity) {
		this.cID = cID;
		this.sID = sID;
		this.type=type;
		this.transactionActivity = transactionActivity;
		this.transactionsToBeModified=transactionsToBeModified;
		this.viewHolder=viewHolder;
		this.mainActivity=mainActivity;
	}

	int cID, sID, type;
	FragmentTransaction fragmentTransaction;
	TransactionActivity transactionActivity;
	List<Transaction> transactionsToBeModified;
	CategoryAdapter.BSDCatViewHolder viewHolder;
	MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bsd_cat, container, false);

		BsdCategoryFragment bsdCategoryFragment=new BsdCategoryFragment(cID, sID,type, transactionsToBeModified, viewHolder, mainActivity);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.FragmentForCategory, bsdCategoryFragment).commit();
		return view;
	}

	public void showSubCatFragment(int cID, int sID, int type, Category category) {
		BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(cID, this.cID, sID, type, category, transactionsToBeModified, viewHolder, mainActivity);
		fragmentTransaction=getChildFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
		fragmentTransaction.replace(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
	}
}
