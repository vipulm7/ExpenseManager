package com.VipulMittal.expensemanager.categoryRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.BSDCatViewHolder> {

	ClickListener listener;
	public List<Category> categories;
	int selected;
	String TAG="Vipul_tag";

	public CategoryAdapter(int selected, ClickListener listener) {
		this.listener = listener;
		categories=new ArrayList<>();
		this.selected=selected;
	}

	@NonNull
	@Override
	public BSDCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_cat_layout_per_item, parent, false);
		return new BSDCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDCatViewHolder holder, int position) {
		holder.name.setText(categories.get(position).name);
		Log.d(TAG, "onBindViewHolder: "+position);
		if(position==selected)
			holder.name.setBackgroundColor(Color.CYAN);
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: size = "+categories.size());
		Log.d(TAG, "getItemCount: categories = "+categories);
		return categories.size();
	}


	public class BSDCatViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		public BSDCatViewHolder(@NonNull View itemView) {
			super(itemView);
			name=itemView.findViewById(R.id.BSD_Cat);

			itemView.setOnClickListener(view -> {
				int position=getAdapterPosition();
				if(listener!=null && position!=-1)
					listener.onItemClick(this);
			});
		}
	}

	public interface ClickListener
	{
		void onItemClick(BSDCatViewHolder viewHolder);
	}

	public void setCategories(List<Category> categories)
	{
		this.categories=categories;
	}
}
