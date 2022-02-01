package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;

import java.util.List;

public class BsdSubCategoryFragment extends Fragment {

	public BsdSubCategoryFragment(int cID, int sID, int type, Category categorySelected) {
		this.sID = sID;
		this.type=type;
		this.categorySelected = categorySelected;
		this.cID = cID;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category categorySelected;
	int sID, type, cID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		BsdCatFragment bsdCatFragment = (BsdCatFragment) getParentFragment();
		MainActivity mainActivity=(MainActivity)getActivity();
		TransactionFragment transactionFragment= bsdCatFragment.transactionFragment;

		Log.d(TAG, "onCreateView: cID in BsdSubCategoryFragment = "+cID);

		subCategoryAdapter= mainActivity.subCategoryAdapter;
//		mainActivity.subCategoryROOM(cID);

		Log.d(TAG, "onCreateView: subCategories.size() = "+subCategoryAdapter.subCategories.size());

		SubCategoryAdapter.ClickListener listener=new SubCategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(SubCategoryAdapter.BSDSubCatViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();
				sID = subCategoryAdapter.subCategories.get(position).id;
				transactionFragment.saveSelectedSubCategory(cID, sID, categorySelected.catName + " / " + subCategoryAdapter.subCategories.get(position).name);

				bsdCatFragment.dismiss();
			}
		};

		subCategoryAdapter.listener=listener;
		subCategoryAdapter.sID = sID;
		subCategoryAdapter.catSelected = categorySelected;

		RVSubCategories.setLayoutManager(new LinearLayoutManager((getContext())));
		RVSubCategories.setAdapter(subCategoryAdapter);
		RVSubCategories.setNestedScrollingEnabled(false);

		return view;
	}
}