package com.VipulMittal.expensemanager.subCategoryRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.BSDSubCatViewHolder> {

	public ClickListener listener;
	public int subCatSelected;
	public Category catSelected;
	String TAG="Vipul_tag";
	public List<SubCategory> subCats;
	public List<SubCategory> allSubCats;

	public SubCategoryAdapter() {
		subCats =new ArrayList<>();
	}

	public void setAllSubCats(List<SubCategory> allSubCats)
	{
		this.allSubCats=allSubCats;
	}

	@NonNull
	@Override
	public BSDSubCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_sub_cat_layout_per_item, parent, false);
		return new BSDSubCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDSubCatViewHolder holder, int position) {
//		holder.name.setText(subCats.get(position).name);
		Log.d(TAG, "onBindViewHolder: subCat name = "+subCats.get(position).name+" id = "+subCats.get(position).id);
		holder.name.setText(subCats.get(position).name);
		if(position== subCatSelected)
			holder.name.setBackgroundColor(Color.CYAN);
		else
			holder.name.setBackgroundColor(Color.WHITE);
	}

	@Override
	public int getItemCount() {
//		Log.d(TAG, "getItemCount: subCategoriesToPrint.size() = "+subCategoriesToPrint.size());
//		Log.d(TAG, "getItemCount: subCategoriesToPrint = "+subCategoriesToPrint);

		return subCats.size();
	}


	public class BSDSubCatViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		public BSDSubCatViewHolder(@NonNull View itemView) {
			super(itemView);
			name=itemView.findViewById(R.id.BSD_SubCat);

			itemView.setOnClickListener(view -> {
//				int position=getAdapterPosition();
//				if(listener!=null && position!=-1)
				if(listener!=null)
					listener.onItemClick(this);
			});
		}
	}

	public interface ClickListener
	{
		void onItemClick(BSDSubCatViewHolder viewHolder);
	}

//	public void setSubCategories(Map<Category, List<SubCategory>> subCategories)
//	{
//		this.subCategories=subCategories;
//	}
//	public void setSubCategoriesToPrint(List<SubCategory> subCategoriesToPrint)
//	{
//		this.subCategoriesToPrint=subCategoriesToPrint;
//	}


	public void setSubCats(List<SubCategory> subCats)
	{
		this.subCats=subCats;
	}
}
