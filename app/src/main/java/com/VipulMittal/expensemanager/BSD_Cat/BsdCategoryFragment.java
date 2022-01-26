package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;

import java.util.List;

public class BsdCategoryFragment extends Fragment {

	public BsdCategoryFragment(int catSelected, int subCatSelected, int type, int fr) {
		this.catSelected = catSelected;
		this.subCatSelected = subCatSelected;
		this.type=type;
		this.fr=fr;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVCategories;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	TransactionActivity transactionActivity;
	BsdCatFragment bsdCatFragment;
	int catSelected, subCatSelected, type, fr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories=view.findViewById(R.id.bsd_rv_categories);
		bsdCatFragment= (BsdCatFragment) getParentFragment();
		transactionActivity=(TransactionActivity) bsdCatFragment.getActivity();

		Log.d(TAG, "onCreateView: subCatSelected = "+subCatSelected);

		categoryAdapter=new CategoryAdapter(catSelected, viewHolder -> {
			int position=viewHolder.getAdapterPosition();
			catSelected = position;

			selectCat(-1);
		});

		categoryViewModel= new ViewModelProvider(this).get(CategoryViewModel.class);
		categoryViewModel.getAllCategories(type).observe(getViewLifecycleOwner(), categories -> {
			int pos=pos(categories, categoryAdapter.categories);
			Log.d(TAG, "onCreateView: view model called");
			Log.d(TAG, "onCreateView: type = "+type);
			categoryAdapter.setCategories(categories);
			categoryAdapter.notifyItemInserted(pos);
		});

		RVCategories.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCategories.setAdapter(categoryAdapter);
		RVCategories.setNestedScrollingEnabled(false);

		if(subCatSelected!=-1)
			selectCat(subCatSelected);

		return view;
	}

	private void selectCat(int subCatSelected) {
		if(transactionActivity!=null)
		{
			transactionActivity.saveSelectedCategoryWithoutName(catSelected);
			int a=categoryAdapter.catSelected;
			categoryAdapter.catSelected=catSelected;
			if(a != -1)
				categoryAdapter.notifyItemChanged(a);
			categoryAdapter.notifyItemChanged(catSelected);

			if(categoryAdapter.categories.get(catSelected).noOfSubCat!=0) {
				Log.d(TAG, "category sent = "+categoryAdapter.categories.get(catSelected));
				bsdCatFragment.showSubCatFragment(subCatSelected, type, categoryAdapter.categories.get(catSelected));

//				BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(subCatSelected, type, categoryAdapter.categories.get(catSelected));
//				bsdCatFragment.fragmentTransaction=getChildFragmentManager().beginTransaction();
//				bsdCatFragment.fragmentTransaction.replace(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
			}
			else
			{
				transactionActivity.saveSelectedCategoryWithName(catSelected, categoryAdapter.categories.get(catSelected).name);
				bsdCatFragment.dismiss();
			}

		}
		else {
			Toast.makeText(getContext(), "Error in selecting category", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "selectCat: transactionActivity of BsdCategoryFragment is null");
		}
	}

	private int pos(List<Category> newCategories, List<Category> oldCategories) {

		int i=-1;
		for(;++i<oldCategories.size();)
		{
			if(newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}
}