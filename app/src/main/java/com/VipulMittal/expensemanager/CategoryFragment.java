package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

	RecyclerView RVCats;
	CategoryAdapter categoryAdapter;
	MainActivity mainActivity;
	TextView TVIncome, TVExpense;

	CategoryAdapter categoryAdapter2;
	CategoryViewModel categoryViewModel2;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	List<SubCategory> subCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_category, container, false);

		RVCats =view.findViewById(R.id.RVCat_income);
		TVIncome = view.findViewById(R.id.TVCat_income);
		TVExpense = view.findViewById(R.id.TVCat_expense);


		categoryAdapter=mainActivity.categoryAdapter;
		categoryAdapter.listener=null;
		categoryAdapter.who=2;
		categoryAdapter.cID=-1;
		RVCats.setAdapter(categoryAdapter);
		RVCats.setLayoutManager(new LinearLayoutManager(getContext()));
		RVCats.setNestedScrollingEnabled(false);

		categoryAdapter2= mainActivity.categoryAdapter2;
		categoryAdapter2.listener=null;
		categoryAdapter2.who=2;
		categoryAdapter2.cID=-1;

		TVIncome.setOnClickListener(v->{
			RVCats.setAdapter(categoryAdapter);
		});

		TVExpense.setOnClickListener(v->{
			RVCats.setAdapter(categoryAdapter2);
		});

		return view;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity=(MainActivity) getActivity();
	}
}