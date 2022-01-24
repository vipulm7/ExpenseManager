package com.VipulMittal.expensemanager.BSD_Cat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

	public BsdCategoryFragment(int catSelected, int type) {
		this.catSelected = catSelected;
		this.type=type;
	}

	private static final String TAG = "Vipul_tag";
	RecyclerView RVCategories;
	CategoryAdapter categoryAdapter;
	CategoryViewModel categoryViewModel;
	int catSelected, type;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bsd_category, container, false);

		RVCategories=view.findViewById(R.id.bsd_rv_categories);
		categoryAdapter=new CategoryAdapter(catSelected, viewHolder -> {
			int position=viewHolder.getAdapterPosition();

			catSelected =position;
			TransactionActivity transactionActivity=(TransactionActivity) getActivity();
			if(transactionActivity!=null)
				transactionActivity.saveSelectedCategoryWithoutName(catSelected);
			else
				Toast.makeText(getContext(),"Error in selecting category",Toast.LENGTH_SHORT).show();

			BsdCatFragment bsdCatFragment= (BsdCatFragment) getParentFragment();
			bsdCatFragment.dismiss();
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

		return view;
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