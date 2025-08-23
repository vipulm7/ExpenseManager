package com.VipulMittal.expensemanager.subCategoryRoom;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.VipulMittal.expensemanager.R;
import com.VipulMittal.expensemanager.categoryRoom.Category;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.BSDSubCatViewHolder> {

	public ClickListener listener, deleteListener;
	public int sID, cID;
	public Category catSelected;
	public List<SubCategory> subCategories;
	public List<SubCategory> allSubCats;
	public int who;
	String TAG = "Vipul_tag";

	public SubCategoryAdapter() {
		subCategories = new ArrayList<>();
	}

	public void setAllSubCats(List<SubCategory> allSubCats) {
		this.allSubCats = allSubCats;
	}

	@NonNull
	@Override
	public BSDSubCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view;
		if (who == 1)
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bsd_sub_cat_layout_per_item, parent, false);
		else
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_layout_per_item, parent, false);
		return new BSDSubCatViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull BSDSubCatViewHolder holder, int position) {
//		holder.name.setText(subCats.get(position).name);
		SubCategory subCategory = subCategories.get(position);
		Log.d(TAG, "onBindViewHolder: subCategoryv = " + subCategory.name + " subCategory.id" + subCategory.id);

		if (who == 1) {
			Log.d(TAG, "onBindViewHolder: subCat name = " + subCategories.get(position).name + " id = " + subCategories.get(position).id);
			holder.name.setText(subCategories.get(position).name);
			if (subCategories.get(position).id == sID)
				holder.name.setBackgroundColor(Color.parseColor("#C6E6E8"));//cyan
			else
				holder.name.setBackgroundResource(R.drawable.bsd);
		} else if (who == 2) {
			holder.name.setText(subCategory.name);
			if (subCategory.subCatAmount >= 0)
				holder.amt.setText("\u20b9" + moneyToString(subCategory.subCatAmount));
			else
				holder.amt.setText("\u20b9" + moneyToString(-subCategory.subCatAmount));

			if (subCategory.type == 1)
				holder.amt.setTextColor(Color.parseColor("#38a169"));//green
			else
				holder.amt.setTextColor(Color.parseColor("#e53e3e"));//red

			holder.bgt.setText("\u20b9" + moneyToString(subCategory.subCatBudget));

			if (subCategory.type == 1) {
				holder.bgt.setVisibility(View.GONE);
				holder.bgt2.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.GONE);
			}

			Log.d(TAG, "onBindViewHolder: subCat.name = " + subCategory.name);
			Log.d(TAG, "onBindViewHolder: subCatAmount = " + subCategory.subCatAmount);
			Log.d(TAG, "onBindViewHolder: subCatBudget = " + subCategory.subCatBudget);

			if (subCategory.subCatBudget != 0) {
				int progress = (int) ((-100L * subCategory.subCatAmount) / subCategory.subCatBudget);
				Log.d(TAG, "onBindViewHolder: progress = " + progress);
				if (progress > 100)
					progress = 100;

				holder.progressBar.setProgress(progress);
			} else
				holder.progressBar.setProgress(0);

			holder.imageView.setImageResource(subCategory.subCatImageID);
		}
	}

	@Override
	public int getItemCount() {
//		Log.d(TAG, "getItemCount: subCategoriesToPrint.size() = "+subCategoriesToPrint.size());
//		Log.d(TAG, "getItemCount: subCategoriesToPrint = "+subCategoriesToPrint);

		return subCategories.size();
	}

	public String moneyToString(long money) {
		int a = countDigits(money);
		if (a < 4)
			return String.valueOf(money);
		else {
			char[] c = new char[27];
			for (int i = -1; ++i < 3; ) {
				c[i] = (char) (money % 10 + 48);
				money /= 10;
			}
			a -= 3;

			int b = 0;
			int index = 3;
			while (a > 0) {
				if (b == 0)
					c[index++] = ',';
				c[index++] = (char) (money % 10 + 48);
				money /= 10;
				b ^= 1;
				a--;
			}
			String s = "";
			for (int i = -1; ++i < index; )
				s = c[i] + s;
			return s;
		}
	}

	int countDigits(long l) {
		if (l >= 1000000000000000000L) return 19;
		if (l >= 100000000000000000L) return 18;
		if (l >= 10000000000000000L) return 17;
		if (l >= 1000000000000000L) return 16;
		if (l >= 100000000000000L) return 15;
		if (l >= 10000000000000L) return 14;
		if (l >= 1000000000000L) return 13;
		if (l >= 100000000000L) return 12;
		if (l >= 10000000000L) return 11;
		if (l >= 1000000000L) return 10;
		if (l >= 100000000L) return 9;
		if (l >= 10000000L) return 8;
		if (l >= 1000000L) return 7;
		if (l >= 100000L) return 6;
		if (l >= 10000L) return 5;
		if (l >= 1000L) return 4;
		if (l >= 100L) return 3;
		if (l >= 10L) return 2;
		return 1;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public interface ClickListener {
		void onItemClick(BSDSubCatViewHolder viewHolder);
	}

	public class BSDSubCatViewHolder extends RecyclerView.ViewHolder {
		TextView name, bgt, amt, amt2, bgt2, del;
		ImageView imageView;
		ProgressBar progressBar;

		public BSDSubCatViewHolder(@NonNull View itemView) {
			super(itemView);
			if (who == 1) {
				name = itemView.findViewById(R.id.BSD_SubCat);

				itemView.setOnClickListener(view -> {
//				int position=getAdapterPosition();
//				if(listener!=null && position!=-1)
					if (listener != null)
						listener.onItemClick(this);
				});
			} else if (who == 2) {
				imageView = itemView.findViewById(R.id.IVCat_image);
				name = itemView.findViewById(R.id.TVCat_name);
				bgt = itemView.findViewById(R.id.TVCat_budget);
				amt = itemView.findViewById(R.id.TVCat_amt);
				bgt2 = itemView.findViewById(R.id.TVCat_BUDGET);
				amt2 = itemView.findViewById(R.id.TVCat_AMOUNT);
				progressBar = itemView.findViewById(R.id.progressBar);
				del = itemView.findViewById(R.id.BSubCatDel);


				itemView.setOnClickListener(v -> {
					if (listener != null)
						listener.onItemClick(this);
				});

				del.setOnClickListener(v -> {
					if (deleteListener != null)
						deleteListener.onItemClick(this);
				});

			}
		}
	}
}
