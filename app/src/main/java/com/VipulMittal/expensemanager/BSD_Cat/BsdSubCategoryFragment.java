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

import com.VipulMittal.expensemanager.MainActivity;
import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.TransactionActivity;
import com.VipulMittal.expensemanager.TransactionFragment;
import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.mix;

import java.util.List;
import java.util.Map;

public class BsdSubCategoryFragment extends Fragment {

	public BsdSubCategoryFragment(int subCatSelected, int type, Category catSelected) {
		this.subCatSelected = subCatSelected;
		this.type=type;
		this.catSelected = catSelected;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVSubCategories;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	Category catSelected;
	int subCatSelected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_bsd_sub_category, container, false);

		RVSubCategories=view.findViewById(R.id.bsd_rv_subcategories);
		BsdCatFragment bsdCatFragment = (BsdCatFragment) getParentFragment();
		MainActivity mainActivity=(MainActivity)getActivity();
		TransactionFragment transactionFragment= bsdCatFragment.transactionFragment;

//		subCategoryAdapter=new SubCategoryAdapter(subCatSelected, viewHolder -> {
//			int position=viewHolder.getAdapterPosition();
//			subCatSelected = position;
//
//			transactionFragment.saveSelectedSubCategory(subCatSelected, catSelected.catName + " / " + subCategoryAdapter.subCats.get(subCatSelected).name);
//
////			Log.d(TAG, "onCreateView: hasActiveObservers = "+categoryViewModel.getAllCategories(type).hasActiveObservers());
////			Log.d(TAG, "onCreateView: hasObservers = "+categoryViewModel.getAllCategories(type).hasObservers());
//			bsdCatFragment.dismiss();
//		}, catSelected);



		mainActivity.subCategoryROOM(catSelected.catId);

//		subCategoryAdapter=new SubCategoryAdapter();
//		subCategoryAdapter.subCats=mainActivity.subCategoryAdapter.subCats;
		subCategoryAdapter= mainActivity.subCategoryAdapter;

		SubCategoryAdapter.ClickListener listener=new SubCategoryAdapter.ClickListener() {
			@Override
			public void onItemClick(SubCategoryAdapter.BSDSubCatViewHolder viewHolder) {
				int position=viewHolder.getAdapterPosition();
				subCatSelected = position;
				transactionFragment.saveSelectedSubCategory(subCatSelected, catSelected.catName + " / " + subCategoryAdapter.subCats.get(subCatSelected).name);

//				Log.d(TAG, "onCreateView: hasActiveObservers = "+categoryViewModel.getAllCategories(type).hasActiveObservers());
//				Log.d(TAG, "onCreateView: hasObservers = "+categoryViewModel.getAllCategories(type).hasObservers());
				bsdCatFragment.dismiss();
			}
		};

//		subCategoryAdapter= mainActivity.subCategoryAdapter;
		subCategoryAdapter.listener=listener;
		subCategoryAdapter.subCatSelected=subCatSelected;
		subCategoryAdapter.catSelected=catSelected;




//		Log.d(TAG, "onCreateView: map = "+subCategoryAdapter.subCategories.get(catSelected));

//		subCategoryViewModel=new ViewModelProvider(this).get(SubCategoryViewModel.class);
//		subCategoryViewModel=mainActivity.subCategoryViewModel;
//		subCategoryViewModel.getSubs(catSelected.catId).observe(getViewLifecycleOwner(), new Observer<List<SubCategory>>() {
//			@Override
//			public void onChanged(List<SubCategory> subCats) {
//				Log.d(TAG, "onChanged: "+subCats);
//				subCategoryAdapter.setSubCats(subCats);
//				subCategoryAdapter.notifyDataSetChanged();
//			}
//		});

//		subCategoryViewModel.getAllSubCategories(type).observe(getViewLifecycleOwner(), new Observer<Map<Category, List<SubCategory>>>() {
//			@Override
//			public void onChanged(Map<Category, List<SubCategory>> subCategories) {
//				int pos=pos(subCategories.get(catSelected), subCategoryAdapter.subCategoriesToPrint);
//				subCategoryAdapter.setSubCategories(subCategories);
//				subCategoryAdapter.setSubCategoriesToPrint(subCategories.get(catSelected));
////				subCategoryAdapter.notifyItemInserted(pos);
//				subCategoryAdapter.notifyDataSetChanged();
//
//				Log.d(TAG, "onChanged: subCategories="+subCategories);
////				Log.d(TAG, "onChanged: subCategories.get(catSelected)="+subCategories.get(catSelected));
////				Log.d(TAG, "onChanged: subCategories.get(catSelected).get(0)="+subCategories.get(catSelected).get(0));
//			}
//		});


//		subCategoryViewModel.getSubs(catSelected.catId).observe(getViewLifecycleOwner());



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