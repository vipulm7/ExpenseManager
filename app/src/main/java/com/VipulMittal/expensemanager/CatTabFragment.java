package com.VipulMittal.expensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.categoryRoom.CategoryAdapter;

public class CatTabFragment extends Fragment {

	public NestedScrollView nestedScrollView;
	RecyclerView rv_cat;
	CategoryAdapter adapter;
	MainActivity mainActivity;

	public CatTabFragment(CategoryAdapter adapter, MainActivity mainActivity) {
		this.adapter = adapter;
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_cat_tab, container, false);

		rv_cat = view.findViewById(R.id.RV_cat);
		nestedScrollView = view.findViewById(R.id.NestedScrollView);

		rv_cat.setAdapter(adapter);
		rv_cat.setLayoutManager(new LinearLayoutManager(requireContext()));
		rv_cat.setNestedScrollingEnabled(false);

		rv_cat.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

				if (dy > 0)
					mainActivity.FABAdd.hide();
				else
					mainActivity.FABAdd.show();
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		return view;
	}
}
