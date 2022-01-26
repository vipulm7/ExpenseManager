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
import android.widget.Button;
import android.widget.Toast;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;

import java.util.List;

public class BsdCategoryFragment extends Fragment {

	public BsdCategoryFragment(int catSelected, int subCatSelected, int type) {
		this.catSelected = catSelected;
		this.subCatSelected = subCatSelected;
		this.type=type;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVCategories;
	Button BAddNewCat;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	TransactionActivity transactionActivity;
	BsdCatFragment bsdCatFragment;
	int catSelected, subCatSelected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories=view.findViewById(R.id.bsd_rv_categories);
		BAddNewCat=view.findViewById(R.id.BAddNewCat);
		bsdCatFragment= (BsdCatFragment) getParentFragment();
		transactionActivity=(TransactionActivity) bsdCatFragment.getActivity();

		Log.d(TAG, "onCreateView: subCatSelected = "+subCatSelected);

		Log.d(TAG, "onCreateView: adapter before = "+categoryAdapter);

		Observer observer=new Observer<List<Category>>() {
			@Override
			public void onChanged(List<Category> categories) {
				int pos1=pos(categories, categoryAdapter.categories);
				Log.d(TAG, "onCreateView: view model called");
				Log.d(TAG, "onCreateView: type = "+type);
				categoryAdapter.setCategories(categories);
				categoryAdapter.notifyItemInserted(pos1);
			}
		};

		categoryAdapter=new CategoryAdapter(catSelected, viewHolder -> {
			int position=viewHolder.getAdapterPosition();
			catSelected = position;

			selectSubCat(-1, observer);
		});

		Log.d(TAG, "onCreateView: adapter after = "+categoryAdapter);

		categoryViewModel= new ViewModelProvider(this).get(CategoryViewModel.class);
//		categoryAdapter.setCategories(categoryViewModel.getAllCategories(type).getValue());
//		categoryAdapter.notifyDataSetChanged();


		categoryViewModel.getAllCategories(type).observe(getViewLifecycleOwner(), observer);

		RVCategories.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCategories.setAdapter(categoryAdapter);
		RVCategories.setNestedScrollingEnabled(false);

		if(subCatSelected!=-1) {
			Log.d(TAG, "onCreateView: categoryAdapter.categories.size() = "+categoryAdapter.categories.size());
			selectSubCat(subCatSelected, observer);
		}

		BAddNewCat.setOnClickListener(v->{

		});
		return view;
	}

	private void selectSubCat(int subCatSelected, Observer observer) {
		if(transactionActivity!=null)
		{
			int a=categoryAdapter.catSelected;
			categoryAdapter.catSelected=catSelected;
			categoryAdapter.notifyItemChanged(a);
			categoryAdapter.notifyItemChanged(catSelected);

			Log.d(TAG, "selectSubCat: catSelected = "+catSelected);
			if(categoryAdapter.categories.get(catSelected).noOfSubCat!=0) {
				transactionActivity.saveSelectedCategoryWithoutName(catSelected);
				Log.d(TAG, "category sent = "+categoryAdapter.categories.get(catSelected).catName);
				bsdCatFragment.showSubCatFragment(subCatSelected, type, categoryAdapter.categories.get(catSelected), categoryViewModel, observer);

//				BsdSubCategoryFragment bsdSubCategoryFragment = new BsdSubCategoryFragment(subCatSelected, type, categoryAdapter.categories.get(catSelected));
//				bsdCatFragment.fragmentTransaction=getChildFragmentManager().beginTransaction();
//				bsdCatFragment.fragmentTransaction.replace(R.id.FragmentForSubCategory, bsdSubCategoryFragment).commit();
			}
			else
			{
				transactionActivity.saveSelectedCategoryWithName(catSelected, categoryAdapter.categories.get(catSelected).catName);
				categoryAdapter.notifyItemRangeRemoved(0,categoryAdapter.categories.size());
				categoryViewModel.getAllCategories(type).removeObserver(observer);
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