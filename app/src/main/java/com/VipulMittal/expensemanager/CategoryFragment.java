package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.categoryRoom.Category;
import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {

	public CategoryFragment() {
		// Required empty public constructor
	}

	RecyclerView RVIncomeCats, RVExpenseCats;
	CategoryAdapter categoryAdapter;
	MainActivity mainActivity;

	CategoryAdapter categoryAdapter2;
	CategoryViewModel categoryViewModel2;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	List<SubCategory> subCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_category, container, false);

		RVExpenseCats=view.findViewById(R.id.RVCat_expense);
		RVIncomeCats=view.findViewById(R.id.RVCat_income);


		categoryAdapter=mainActivity.categoryAdapter;
//		mainActivity.categoryROOM(1);
		categoryAdapter.listener=null;
		categoryAdapter.who=2;
		categoryAdapter.cID=-1;
		RVIncomeCats.setAdapter(categoryAdapter);
		RVIncomeCats.setLayoutManager(new LinearLayoutManager(getContext()));
		RVIncomeCats.setNestedScrollingEnabled(false);

		categoryAdapter2= mainActivity.categoryAdapter2;
//		mainActivity.categoryROOM2(2);
		categoryAdapter2.listener=null;
		categoryAdapter2.who=2;
		categoryAdapter2.cID=-1;

		RVExpenseCats.setAdapter(categoryAdapter2);
		RVExpenseCats.setLayoutManager(new LinearLayoutManager(getContext()));
		RVExpenseCats.setNestedScrollingEnabled(false);

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity=(MainActivity) getActivity();
//		mainActivity.categoryROOM(1);
//		mainActivity.categoryROOM2(2);

	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
}