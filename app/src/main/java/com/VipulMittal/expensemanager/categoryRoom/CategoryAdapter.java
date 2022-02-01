package com.VipulMittal.expensemanager.categoryRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.BSDCatViewHolder> {

	public ClickListener listener;
	public List<Category> categories;
	public int cID;
	String TAG="Vipul_tag";
	public int who;

	public CategoryAdapter() {
		categories=new ArrayList<>();
	}

	@NonNull
	@Override
	public BSDCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		if(who==1)
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_cat_layout_per_item, parent, false);
		else
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout_per_item, parent, false);
		return new BSDCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDCatViewHolder holder, int position) {
		Category category = categories.get(position);
		if(who==1)
		{
			holder.name.setText(category.catName);
			Log.d(TAG, "onBindViewHolder: ofCat position=" + position);
			Log.d(TAG, "onBindViewHolder: ofCat name=" + category.catName + " noOfSubCat=" + category.noOfSubCat + " id=" + category.catId);
			if (category.catId == cID)
				holder.name.setBackgroundColor(Color.CYAN);
			else
				holder.name.setBackgroundColor(Color.WHITE);

			if (category.noOfSubCat == 0)
				holder.arrow.setVisibility(View.INVISIBLE);
			else
				holder.arrow.setVisibility(View.VISIBLE);
		}
		else if(who==2)
		{
			holder.name.setText(category.catName);
			if(category.catAmount>=0)
				holder.amt.setText(""+category.catAmount);
			else
				holder.amt.setText(""+(-category.catAmount));

			if(category.type==1)
				holder.amt.setTextColor(Color.GREEN);
			else
				holder.amt.setTextColor(Color.RED);


			holder.bgt.setText(""+category.catBudget);
			if(category.noOfSubCat>0)
				holder.arrow.setVisibility(View.VISIBLE);
			else
				holder.arrow.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: categories.size() = "+categories.size());
		Log.d(TAG, "getItemCount: categories = "+categories);
		return categories.size();
	}


	public class BSDCatViewHolder extends RecyclerView.ViewHolder
	{
		TextView name,arrow, bgt,amt;
		ImageView imageView;
		public BSDCatViewHolder(@NonNull View itemView) {
			super(itemView);
			if(who==1)
			{
				name = itemView.findViewById(R.id.BSD_Cat);
				arrow = itemView.findViewById(R.id.BSD_Cat_Arrow);

				itemView.setOnClickListener(view -> {
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who1 = " + position);
//				if(listener!=null && position!=-1)
					if (listener != null)
						listener.onItemClick(this);
				});
			}
			else if(who==2)
			{
				imageView=itemView.findViewById(R.id.IVCat_image);
				name=itemView.findViewById(R.id.TVCat_name);
				arrow=itemView.findViewById(R.id.TVCat_ARROW);
				bgt=itemView.findViewById(R.id.TVCat_budget);
				amt=itemView.findViewById(R.id.TVCat_amt);

				itemView.setOnClickListener(v->{
					int position = getAdapterPosition();
					Log.d(TAG, "BSDCatViewHolder: pos for who2 = " + position);
					if(listener!=null)
						listener.onItemClick(this);
				});
			}
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
