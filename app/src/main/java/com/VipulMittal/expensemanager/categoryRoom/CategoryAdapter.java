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
	public int catSelected;
	String TAG="Vipul_tag";

	public CategoryAdapter(int catSelected, ClickListener listener) {
		this.listener = listener;
		categories=new ArrayList<>();
		this.catSelected =catSelected;
	}

	@NonNull
	@Override
	public BSDCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_cat_layout_per_item, parent, false);
		return new BSDCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDCatViewHolder holder, int position) {
		holder.name.setText(categories.get(position).catName);
		Log.d(TAG, "onBindViewHolder: ofCat position="+position);
		Log.d(TAG, "onBindViewHolder: ofCat name="+categories.get(position).catName +" noOfSubCat="+categories.get(position).noOfSubCat+" id="+categories.get(position).catId);
		if(position== catSelected)
			holder.name.setBackgroundColor(Color.CYAN);
		else
			holder.name.setBackgroundColor(Color.WHITE);
		if(categories.get(position).noOfSubCat==0)
			holder.arrow.setVisibility(View.INVISIBLE);
	}

	@Override
	public int getItemCount() {
//		Log.d(TAG, "getItemCount: size = "+categories.size());
//		Log.d(TAG, "getItemCount: categories = "+categories);
		return categories.size();
	}


	public class BSDCatViewHolder extends RecyclerView.ViewHolder
	{
		TextView name,arrow;
		public BSDCatViewHolder(@NonNull View itemView) {
			super(itemView);
			name=itemView.findViewById(R.id.BSD_Cat);
			arrow=itemView.findViewById(R.id.BSD_Cat_Arrow);

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
