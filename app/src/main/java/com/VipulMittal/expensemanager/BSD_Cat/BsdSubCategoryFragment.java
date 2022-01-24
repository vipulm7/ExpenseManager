package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Map;

public class BsdSubCategoryFragment extends Fragment {

	public BsdSubCategoryFragment(int selected, int type, Category category) {
		this.selected=selected;
		this.type=type;
		this.category=category;
	}

	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category category;
	int selected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		subCategoryAdapter=new SubCategoryAdapter(selected, viewHolder -> {
			int position=viewHolder.getAdapterPosition();

			selected=position;
			TransactionActivity transactionActivity=(TransactionActivity) getActivity();
			transactionActivity.saveSelectedSubCategory(selected, "Subway");

			BsdCatFragment bsdCatFragment= (BsdCatFragment) getParentFragment();
			bsdCatFragment.dismiss();
		}, category);

		subCategoryViewModel=new ViewModelProvider(this).get(SubCategoryViewModel.class);
		subCategoryViewModel.getAllSubCategories(type).observe(getViewLifecycleOwner(), new Observer<Map<Category, List<SubCategory>>>() {
			@Override
			public void onChanged(Map<Category, List<SubCategory>> subCategories) {
				int pos=pos(subCategories.get(category), subCategoryAdapter.subCategoriesToPrint);
				subCategoryAdapter.setSubCategories(subCategories);
				subCategoryAdapter.notifyItemInserted(pos);
			}
		});

		return view;
	}

	private int pos(List<SubCategory> newCategories, List<SubCategory> oldCategories) {

		int i=-1;
		for(;++i<oldCategories.size();)
		{
			if(newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}
}