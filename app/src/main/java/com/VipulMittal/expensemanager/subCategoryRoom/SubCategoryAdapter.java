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

	ClickListener listener;
//	public Map<Category, List<SubCategory>> subCategories;
	int subCatSelected;
	Category catSelected;
	String TAG="Vipul_tag";
//	public List<SubCategory> subCategoriesToPrint;
	public List<SubCategory> subCats;

	public SubCategoryAdapter(int subCatSelected, ClickListener listener, Category catSelected) {
		this.listener = listener;
//		subCategories= new HashMap<>();
		subCats =new ArrayList<>();
//		Category a=new Category("A",0,0,0,2);
//		subCategories.put(a,new ArrayList<>());
		this.subCatSelected = subCatSelected;
		this.catSelected=catSelected;
//		subCategoriesToPrint=subCategories.get(catSelected);
//		Log.d(TAG, "SubCategoryAdapter: subCategoriesToPrint = "+subCategoriesToPrint);
//		Log.d(TAG, "SubCategoryAdapter: subCategories = "+subCategories);
	}

	@NonNull
	@Override
	public BSDSubCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_sub_cat_layout_per_item, parent, false);
		return new BSDSubCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDSubCatViewHolder holder, int position) {
//		holder.name.setText(subCategoriesToPrint.get(position).name);
		Log.d(TAG, "onBindViewHolder: subCat pos = "+position);
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
				int position=getAdapterPosition();
				if(listener!=null && position!=-1)
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
