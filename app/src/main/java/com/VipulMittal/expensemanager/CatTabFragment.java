package com.VipulMittal.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;

public class CatTabFragment extends Fragment {

	public CatTabFragment(CategoryAdapter adapter) {
		this.adapter=adapter;
	}

	RecyclerView rv_cat;
	CategoryAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_cat_tab, container, false);

		rv_cat =view.findViewById(R.id.RV_cat);

		rv_cat.setAdapter(adapter);
		rv_cat.setLayoutManager(new LinearLayoutManager(getContext()));
		rv_cat.setNestedScrollingEnabled(false);

		return view;
	}
}
