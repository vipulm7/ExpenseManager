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

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.TransactionFragment;
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
	BsdCatFragment bsdCatFragment;
	MainActivity mainActivity;
	int catSelected, subCatSelected, type;
	TransactionFragment transactionFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories=view.findViewById(R.id.bsd_rv_categories);
		BAddNewCat=view.findViewById(R.id.BAddNewCat);
		bsdCatFragment= (BsdCatFragment) getParentFragment();
		mainActivity=(MainActivity)getActivity();

		Log.d(TAG, "onCreateView: subCatSelected = "+subCatSelected);
		Log.d(TAG, "onCreateView: adapter before = "+categoryAdapter);


//

		mainActivity.categoryROOM(type);

//		categoryAdapter=new CategoryAdapter();
//		categoryAdapter.categories=mainActivity.categoryAdapter.categories;
		categoryAdapter= mainActivity.categoryAdapter;

		CategoryAdapter.ClickListener listener=new CategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder) {
				final int[] position = {viewHolder.getAdapterPosition()};
				catSelected = position[0];

				selectSubCat(-1);

//				CategoryAdapter.ClickListener listener1=new CategoryAdapter.ClickListener() {
//					@Override
//					public void onItemClick(CategoryAdapter.BSDCatViewHolder viewHolder1) {
//						int position1=viewHolder1.getAdapterPosition();
//						if(position[0] == position1)
//						{
//							transactionFragment.saveSelectedCategoryWithName(catSelected, categoryAdapter.categories.get(catSelected).catName, true);
//							bsdCatFragment.dismiss();
//						}
//						else
//							listener.onItemClick(viewHolder1);
//					}
//				};
			}
		};
		categoryAdapter.catSelected=catSelected;
		categoryAdapter.listener=listener;

//		categoryViewModel= mainActivity.categoryViewModel;
//		categoryViewModel.getAllCategories(type).observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
//			@Override
//			public void onChanged(List<Category> categories) {
////				int pos1= posCat(categories, categoryAdapter.categories);
////				Log.d(TAG, "onCreateView: view model called");
//				Log.d(TAG, "onCreateView: type = "+type);
//				categoryAdapter.setCategories(categories);
////				categoryAdapter.notifyItemInserted(pos1);
//				categoryAdapter.notifyDataSetChanged();
//			}
//		});





		Log.d(TAG, "onCreateView: adapter after = "+categoryAdapter);

		RVCategories.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCategories.setAdapter(categoryAdapter);
		RVCategories.setNestedScrollingEnabled(false);

		if(subCatSelected != -1) {
			Log.d(TAG, "onCreateView: categoryAdapter.categories.size() = "+categoryAdapter.categories.size());
			selectSubCat(subCatSelected);
		}
		else if(catSelected!=-1)
			selectSubCat(-1);

		BAddNewCat.setOnClickListener(v->{

		});
		return view;
	}

	private void selectSubCat(int subCatSelected) {
		int a=categoryAdapter.catSelected;
		categoryAdapter.catSelected=catSelected;
		categoryAdapter.notifyItemChanged(a);
		categoryAdapter.notifyItemChanged(catSelected);

		Log.d(TAG, "selectSubCat: catSelected = "+catSelected);
		transactionFragment = bsdCatFragment.transactionFragment;
		if(categoryAdapter.categories.get(catSelected).noOfSubCat!=0) {
			transactionFragment.saveSelectedCategoryWithoutName(catSelected);
			Log.d(TAG, "category sent = "+categoryAdapter.categories.get(catSelected).catName);
			bsdCatFragment.showSubCatFragment(subCatSelected, type, categoryAdapter.categories.get(catSelected));
		}
		else
		{
			transactionFragment.saveSelectedCategoryWithName(catSelected, categoryAdapter.categories.get(catSelected).catName);
			bsdCatFragment.dismiss();
		}
	}

	private int posCat(List<Category> newCategories, List<Category> oldCategories) {

		int i=-1;
		for(;++i<oldCategories.size();)
		{
			if(newCategories.get(i) != oldCategories.get(i))
				return i;
		}
		return i;
	}
}