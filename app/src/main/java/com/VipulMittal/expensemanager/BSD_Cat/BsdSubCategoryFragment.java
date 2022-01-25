package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;

import java.util.List;
import java.util.Map;

public class BsdSubCategoryFragment extends Fragment {

	public BsdSubCategoryFragment(int subCatSelected, int type, Category category) {
		this.subCatSelected = subCatSelected;
		this.type=type;
		this.category=category;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category category;
	int subCatSelected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		subCategoryAdapter=new SubCategoryAdapter(subCatSelected, viewHolder -> {
			int position=viewHolder.getAdapterPosition();

			subCatSelected = position;
			BsdCategoryFragment bsdCategoryFragment = (BsdCategoryFragment) getParentFragment();
			BsdCatFragment bsdCatFragment = (BsdCatFragment) bsdCategoryFragment.getParentFragment();
			TransactionActivity transactionActivity = (TransactionActivity) bsdCatFragment.getActivity();
			if(transactionActivity!=null)
			{
				transactionActivity.saveSelectedSubCategory(subCatSelected, category.name + " / " + subCategoryAdapter.subCategoriesToPrint.get(subCatSelected).name);
				bsdCatFragment.dismiss();
			}
			else {
				Toast.makeText(getContext(), "Error in selecting subCategory", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "selectSubCat: transactionActivity of BsdSubCategoryFragment is null");
			}
		}, category);

		Log.d(TAG, "onCreateView: map = "+subCategoryAdapter.subCategories.get(category));

		subCategoryViewModel=new ViewModelProvider(this).get(SubCategoryViewModel.class);
		subCategoryViewModel.getAllSubCategories(type).observe(getViewLifecycleOwner(), new Observer<Map<Category, List<SubCategory>>>() {
			@Override
			public void onChanged(Map<Category, List<SubCategory>> subCategories) {
				int pos=pos(subCategories.get(category), subCategoryAdapter.subCategoriesToPrint);
				subCategoryAdapter.setSubCategories(subCategories);
				subCategoryAdapter.notifyItemInserted(pos);
			}
		});

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