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

	public BsdSubCategoryFragment(int catSelected, int subCatSelected, int type, Category categorySelected) {
		this.subCatSelected = subCatSelected;
		this.type=type;
		this.categorySelected = categorySelected;
		this.catSelected=catSelected;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category categorySelected;
	int subCatSelected, type, catSelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		BsdCatFragment bsdCatFragment = (BsdCatFragment) getParentFragment();
		MainActivity mainActivity=(MainActivity)getActivity();
		TransactionFragment transactionFragment= bsdCatFragment.transactionFragment;

		mainActivity.subCategoryROOM(categorySelected.catId);

		subCategoryAdapter= mainActivity.subCategoryAdapter;

		SubCategoryAdapter.ClickListener listener=new SubCategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(SubCategoryAdapter.BSDSubCatViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();
				subCatSelected = position;
				transactionFragment.saveSelectedSubCategory(catSelected, subCatSelected, categorySelected.catId, subCategoryAdapter.subCats.get(subCatSelected).id, categorySelected.catName + " / " + subCategoryAdapter.subCats.get(subCatSelected).name);

				bsdCatFragment.dismiss();
			}
		};

		subCategoryAdapter.listener=listener;
		subCategoryAdapter.subCatSelected=subCatSelected;
		subCategoryAdapter.catSelected=categorySelected;

		RVSubCategories.setLayoutManager(new LinearLayoutManager((getContext())));
		RVSubCategories.setAdapter(subCategoryAdapter);
		RVSubCategories.setNestedScrollingEnabled(false);

		return view;
	}

	private int pos(List<SubCategory> newCategories, List<SubCategory> oldCategories) {

		int i=-1;
		Log.d(TAG, "newCategories = "+newCategories);
		if(oldCategories==null)
			return 0;
		for(;++i<oldCategories.size();)
		{
			if(newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}
}