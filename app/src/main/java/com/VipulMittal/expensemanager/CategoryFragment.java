package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;
import com.VipulMittal.expensemanager.categoryRoom.CategoryViewModel;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategory;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryAdapter;
import com.VipulMittal.expensemanager.subCategoryRoom.SubCategoryViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

	public CategoryFragment() {
		// Required empty public constructor
	}


	CategoryAdapter categoryAdapter;
	MainActivity mainActivity;
	TabLayout catTabLayout;
	ViewPager2 viewPager;

	CategoryAdapter categoryAdapter2;
	CategoryViewModel categoryViewModel2;
	SubCategoryAdapter subCategoryAdapter;
	SubCategoryViewModel subCategoryViewModel;
	List<SubCategory> subCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_category, container, false);

		catTabLayout = view.findViewById(R.id.catTabLayout);
		viewPager = view.findViewById(R.id.catViewPager);
		mainActivity=(MainActivity) getActivity();

		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("Income");
		arrayList.add("Expense");

		MainAdapter adapter = new MainAdapter(this);
		viewPager.setAdapter(adapter);

		new TabLayoutMediator(catTabLayout, viewPager, ((tab, position) -> {
			if(position==0)
				tab.setText("Income");
			else
				tab.setText("Expense");
		})).attach();

		categoryAdapter=mainActivity.categoryAdapter;
		categoryAdapter.listener=null;
		categoryAdapter.who=2;
		categoryAdapter.cID=-1;

		categoryAdapter2= mainActivity.categoryAdapter2;
		categoryAdapter2.listener=null;
		categoryAdapter2.who=2;
		categoryAdapter2.cID=-1;

		return view;
	}


	private class MainAdapter extends FragmentStateAdapter
	{
		public MainAdapter(Fragment fragment)
		{
			super(fragment);
		}

		@NonNull
		@Override
		public Fragment createFragment(int position) {
			Fragment catIncomeFragment;
			if(position==0)
				catIncomeFragment = new CatTabFragment(categoryAdapter);
			else
				catIncomeFragment = new CatTabFragment(categoryAdapter2);

			return catIncomeFragment;
		}

		@Override
		public int getItemCount() {
			return 2;
		}
	}
}