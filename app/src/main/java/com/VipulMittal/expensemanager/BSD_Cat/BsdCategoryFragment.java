package com.VipulMittal.expensemanager.BSD_Cat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;

import java.util.List;

public class BsdCategoryFragment extends Fragment {

	public BsdCategoryFragment(int cID, int sID, int type) {
		this.cID = cID;
		this.sID = sID;
		this.type=type;
	}

	public static final String TAG = "Vipul_tag";
	RecyclerView RVCategories;
	Button BAddNewCat;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	BsdCatFragment bsdCatFragment;
	MainActivity mainActivity;
	int cID, sID, type;
	TransactionFragment transactionFragment;
	Category categorySelected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories=view.findViewById(R.id.bsd_rv_categories);
		BAddNewCat=view.findViewById(R.id.BAddNewCat);
		bsdCatFragment= (BsdCatFragment) getParentFragment();
		mainActivity=(MainActivity)getActivity();
//		mainActivity.categoryROOM(type);

		Log.d(TAG, "onCreateView: subCatSelectedID = "+ sID);
//		Log.d(TAG, "onCreateView: adapter before = "+categoryAdapter);

		if(type==1)
			categoryAdapter= mainActivity.categoryAdapter;
		else
			categoryAdapter= mainActivity.categoryAdapter2;
		categoryAdapter.who=1;
		categoryViewModel=mainActivity.categoryViewModel;
		transactionFragment= bsdCatFragment.transactionFragment;
		if(cID!=-1)
			categorySelected=categoryViewModel.getCat(cID);

		CategoryAdapter.ClickListener listener=new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				int position = viewHolder.getAdapterPosition();
				if(cID != categoryAdapter.categories.get(position).catId) {
					categorySelected=categoryAdapter.categories.get(position);
					cID = categoryAdapter.categories.get(position).catId;
					selectSubCat(-1);
				}
				else
				{
					transactionFragment.saveSelectedCategoryWithName(cID, categorySelected.catName);
					bsdCatFragment.dismiss();
				}
			}
		};
		categoryAdapter.cID = cID;
		categoryAdapter.listener=listener;

//		Log.d(TAG, "onCreateView: adapter after = "+categoryAdapter);

		RVCategories.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCategories.setAdapter(categoryAdapter);
		RVCategories.setNestedScrollingEnabled(false);

//		if(cID !=-1)
//			Log.d(TAG, "onCreateView: category.noOfSubCat"+categoryAdapter.categories.get(cID).noOfSubCat);

		if(sID != -1) {
			Log.d(TAG, "onCreateView: categoryAdapter.categories.size() = "+categoryAdapter.categories.size());
			selectSubCat(sID);
		}
		else if(cID !=-1 && categorySelected.noOfSubCat!=0)
			selectSubCat(-1);

		BAddNewCat.setOnClickListener(v->{

		});

		return view;
	}


	private void selectSubCat(int sID) {

		int a=categoryAdapter.cID;
		categoryAdapter.cID = cID;

		notify2ItemsChanged(a);

		Log.d(TAG, "selectSubCat: catSelectedID = "+ cID);
		transactionFragment = bsdCatFragment.transactionFragment;
		if(categorySelected.noOfSubCat!=0) {
//			mainActivity.subCategoryROOM(cID);

			if(transactionFragment.TVCategory.getText().toString().equals("Category"))
			{
				transactionFragment.saveSelectedCategoryWithoutName(cID);
				Log.d(TAG, "selectSubCat: saved cat");
			}
			Log.d(TAG, "category sent = "+categorySelected.catName);
			mainActivity.subCategoryViewModel.getSubcategories(cID).observe(getViewLifecycleOwner(), new Observer<List<SubCategory>>() {
				@Override
				public void onChanged(List<SubCategory> subCategories) {
					mainActivity.subCategoryAdapter.setSubCategories(subCategories);
					mainActivity.subCategoryAdapter.notifyDataSetChanged();
					bsdCatFragment.showSubCatFragment(cID,sID, type, categorySelected);
				}
			});
		}
		else
		{
			transactionFragment.saveSelectedCategoryWithName(cID, categorySelected.catName);
			bsdCatFragment.dismiss();
		}
	}

	private void notify2ItemsChanged(int oldCID) {
		///////////////////////////////////////////////////////////////////////////////
		//may cause dikkt
		int p=categoryAdapter.categories.indexOf(categorySelected);
		for(int i=-1;++i<categoryAdapter.categories.size();)
		{
			if(categoryAdapter.categories.get(i).catId==cID)
			{
				p=i;
				break;
			}
		}

		int p1=-1;
		if(oldCID!=-1)
		{
			for(int i=-1;++i<categoryAdapter.categories.size();)
			{
				if(categoryAdapter.categories.get(i).catId==oldCID)
				{
					p1=i;
					break;
				}
			}
		}

		categoryAdapter.notifyItemChanged(p);
		categoryAdapter.notifyItemChanged(p1);

	}
}